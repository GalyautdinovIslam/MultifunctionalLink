package ru.itis.servlets;

import ru.itis.exceptions.PasswordMismatchException;
import ru.itis.helpers.EncryptHelper;
import ru.itis.models.Account;
import ru.itis.services.AccountService;
import ru.itis.services.CutLinkService;
import ru.itis.services.MultiLinkService;
import ru.itis.services.SecurityService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/deleteAccount")
public class DeleteAccountServlet extends HttpServlet {

    private ServletContext servletContext;
    private SecurityService securityService;
    private AccountService accountService;
    private CutLinkService cutLinkService;
    private MultiLinkService multiLinkService;
    private EncryptHelper encryptHelper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        servletContext = config.getServletContext();
        securityService = (SecurityService) servletContext.getAttribute("securityService");
        accountService = (AccountService) servletContext.getAttribute("accountService");
        cutLinkService = (CutLinkService) servletContext.getAttribute("cutLinkService");
        multiLinkService = (MultiLinkService) servletContext.getAttribute("multiLinkService");
        encryptHelper = (EncryptHelper) servletContext.getAttribute("encryptHelper");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String password = encryptHelper.encryptPassword(request.getParameter("password"));
        Account account = securityService.getAuthAccount(request);
        if(account.getPassword().equals(password)) {
            cutLinkService.deleteAllCutsByAccount(account);
            multiLinkService.deleteALlMultiByAccount(account);
            accountService.deleteAccount(account);
            securityService.logout(request);
            response.sendRedirect(servletContext.getContextPath());
        } else {
            request.setAttribute("message", new PasswordMismatchException().getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/settings").forward(request, response);
        }
    }
}
