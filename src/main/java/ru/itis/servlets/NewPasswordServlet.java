package ru.itis.servlets;

import ru.itis.exceptions.BadNewPasswordException;
import ru.itis.exceptions.PasswordMismatchException;
import ru.itis.forms.RecoveryPasswordForm;
import ru.itis.helpers.Messages;
import ru.itis.helpers.NoticeHelper;
import ru.itis.models.Account;
import ru.itis.services.AccountService;
import ru.itis.services.SecurityService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/newPassword")
public class NewPasswordServlet extends HttpServlet {

    private ServletContext servletContext;
    private SecurityService securityService;
    private AccountService accountService;
    private NoticeHelper noticeHelper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        servletContext = config.getServletContext();
        securityService = (SecurityService) servletContext.getAttribute("securityService");
        accountService = (AccountService) servletContext.getAttribute("accountService");
        noticeHelper = (NoticeHelper) servletContext.getAttribute("noticeHelper");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String recoveryCode = request.getParameter("r");

        if (recoveryCode == null || recoveryCode.equals("")) {
            response.sendRedirect(servletContext.getContextPath());
        }

        Optional<Account> optionalAccount = accountService.checkRecoveryCode(recoveryCode);

        if (optionalAccount.isPresent()) {
            securityService.logout(request);
            Account account = optionalAccount.get();
            request.getSession().setAttribute("recoveryAccount", account);
            request.getRequestDispatcher("WEB-INF/jsp/newPassword.jsp").forward(request, response);
        } else {
            response.sendRedirect(servletContext.getContextPath());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RecoveryPasswordForm recoveryPasswordForm = RecoveryPasswordForm.builder()
                .newPassword(request.getParameter("newPassword"))
                .reNewPassword(request.getParameter("reNewPassword"))
                .build();

        Account account = (Account) request.getSession().getAttribute("recoveryAccount");
        request.getSession().removeAttribute("recoveryAccount");
        Optional<Account> optionalAccount = accountService.findById(account.getId());

        if (optionalAccount.isPresent()) {
            account = optionalAccount.get();
            try {
                accountService.recoveryPassword(account, recoveryPasswordForm);
                accountService.deleteSignUpCode(account.getId());
                accountService.updateStatus(account.getId());
                securityService.login(request, account);
                noticeHelper.addMessage(request, Messages.SUCCESSFUL_CHANGE_PASSWORD.get(), true);
                response.sendRedirect(servletContext.getContextPath() + "/my");
            } catch (BadNewPasswordException | PasswordMismatchException ex) {
                noticeHelper.addMessage(request, ex.getMessage(), false);
                request.getRequestDispatcher("WEB-INF/jsp/newPassword.jsp").forward(request, response);
            }
        } else {
            response.sendRedirect(servletContext.getContextPath());
        }
    }
}
