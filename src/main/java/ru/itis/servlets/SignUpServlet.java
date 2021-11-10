package ru.itis.servlets;

import ru.itis.exceptions.EmailAlreadyExistException;
import ru.itis.exceptions.SignUpException;
import ru.itis.exceptions.marks.InterfaceSignUpException;
import ru.itis.forms.AccountSignUpForm;
import ru.itis.helpers.Messages;
import ru.itis.helpers.NoticeHelper;
import ru.itis.models.Account;
import ru.itis.services.AccountService;
import ru.itis.services.MailService;
import ru.itis.services.SecurityService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/signUp")
public class SignUpServlet extends HttpServlet {

    private ServletContext servletContext;
    private SecurityService securityService;
    private AccountService accountService;
    private MailService mailService;
    private NoticeHelper noticeHelper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        servletContext = config.getServletContext();
        securityService = (SecurityService) servletContext.getAttribute("securityService");
        accountService = (AccountService) servletContext.getAttribute("accountService");
        mailService = (MailService) servletContext.getAttribute("mailService");
        noticeHelper = (NoticeHelper) servletContext.getAttribute("noticeHelper");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (securityService.isAuth(request)) {
            noticeHelper.addMessage(request, Messages.ALREADY_AUTH.get(), false);
            response.sendRedirect(servletContext.getContextPath() + "/my");
        } else {
            request.getRequestDispatcher("/WEB-INF/jsp/signUp.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountSignUpForm accountSignUpForm = AccountSignUpForm.builder()
                .email(request.getParameter("email"))
                .password(request.getParameter("password"))
                .rePassword(request.getParameter("rePassword"))
                .nickname(request.getParameter("nickname"))
                .build();

        List<InterfaceSignUpException> exceptions = new ArrayList<>();

        try {
            securityService.signup(request, accountSignUpForm, exceptions);

            Account account = (Account) request.getAttribute("justSignUp");

            String code = accountService.generateSignUpCode(account);
            String pathForMail = request.getRequestURL().toString().replaceAll("/signU.+", "/continueSignUp?r=" + code);
            new Thread(() -> mailService.sendSuccessfulSignUpMessage(accountSignUpForm.getEmail(), pathForMail)).start();
            noticeHelper.addMessage(request, Messages.JUST_SIGN_UP.get(), true);
            response.sendRedirect(servletContext.getContextPath());

        } catch (EmailAlreadyExistException ex) {
            String recoveryCode = accountService.generateRecoveryCode(accountSignUpForm.getEmail());
            String pathForMail = request.getRequestURL().toString().replaceAll("/signU.+", "/newPassword?r=" + recoveryCode);
            new Thread(() -> mailService.sendUnsuccessfulSignUpMessage(accountSignUpForm.getEmail(), pathForMail)).start();
            noticeHelper.addMessage(request, Messages.JUST_SIGN_UP.get(), true);
            response.sendRedirect(servletContext.getContextPath());
        } catch (SignUpException ex) {
            request.setAttribute("email", accountSignUpForm.getEmail());
            request.setAttribute("nickname", accountSignUpForm.getNickname());
            request.setAttribute("exceptions", exceptions);
            request.getRequestDispatcher("/WEB-INF/jsp/signUp.jsp").forward(request, response);
        }
    }
}
