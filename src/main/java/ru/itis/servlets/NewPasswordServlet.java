package ru.itis.servlets;

import ru.itis.exceptions.BadNewPasswordException;
import ru.itis.exceptions.PasswordMismatchException;
import ru.itis.forms.RecoveryPasswordForm;
import ru.itis.helpers.Messages;
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

    @Override
    public void init(ServletConfig config) throws ServletException {
        servletContext = config.getServletContext();
        securityService = (SecurityService) servletContext.getAttribute("securityService");
        accountService = (AccountService) servletContext.getAttribute("accountService");
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
            request.setAttribute("recoveryAccount", account);
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

        Account account = (Account) request.getAttribute("recoveryAccount");
        Optional<Account> optionalAccount = accountService.findById(account.getId());

        if (optionalAccount.isPresent()) {
            account = optionalAccount.get();
            try {
                accountService.recoveryPassword(account, recoveryPasswordForm);
                securityService.addMessage(request, Messages.SUCCESSFUL_CHANGE_PASSWORD.get(), true);
                response.sendRedirect(servletContext.getContextPath() + "/signIn");
            } catch (BadNewPasswordException | PasswordMismatchException ex) {
                request.setAttribute("message", ex.getMessage());
                request.getRequestDispatcher("WEB-INF/jsp/newPassword.jsp").forward(request, response);
            }
        } else {
            response.sendRedirect(servletContext.getContextPath());
        }
    }
}
