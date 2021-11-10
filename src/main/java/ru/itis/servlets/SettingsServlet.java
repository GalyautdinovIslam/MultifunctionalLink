package ru.itis.servlets;

import ru.itis.exceptions.BadNewPasswordException;
import ru.itis.exceptions.BadOldPasswordException;
import ru.itis.exceptions.PasswordMismatchException;
import ru.itis.exceptions.SamePasswordException;
import ru.itis.forms.ChangePasswordForm;
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

@WebServlet("/settings")
public class SettingsServlet extends HttpServlet {

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
        if (securityService.isAuth(request)) {
            request.getRequestDispatcher("/WEB-INF/jsp/settings.jsp").forward(request, response);
        } else {
            noticeHelper.addMessage(request, Messages.NOT_AUTH.get(), false);
            response.sendRedirect(servletContext.getContextPath() + "/signIn");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ChangePasswordForm changePasswordForm = ChangePasswordForm.builder()
                .oldPassword(request.getParameter("oldPassword"))
                .newPassword(request.getParameter("newPassword"))
                .reNewPassword(request.getParameter("reNewPassword"))
                .build();

        Account account = securityService.getAuthAccount(request);

        try {
            accountService.changePassword(account, changePasswordForm);
            securityService.updateAuthAccount(request, account);
            noticeHelper.addMessage(request, Messages.SUCCESSFUL_CHANGE_PASSWORD.get(), true);
            response.sendRedirect(servletContext.getContextPath() + "/settings");
        } catch (BadOldPasswordException | BadNewPasswordException | PasswordMismatchException | SamePasswordException ex) {
            noticeHelper.addMessage(request, ex.getMessage(), false);
            request.getRequestDispatcher("/WEB-INF/jsp/settings.jsp").forward(request, response);
        }

    }
}
