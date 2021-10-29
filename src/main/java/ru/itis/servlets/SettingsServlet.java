package ru.itis.servlets;

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

    @Override
    public void init(ServletConfig config) throws ServletException {
        servletContext = config.getServletContext();
        securityService = (SecurityService) servletContext.getAttribute("securityService");
        accountService = (AccountService) servletContext.getAttribute("accountService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(securityService.isAuth(request)){
            request.getRequestDispatcher("/WEB-INF/jsp/settings.jsp").forward(request, response);
        } else {
            response.sendRedirect(servletContext.getContextPath() + "/signin");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String reNewPassword = request.getParameter("reNewPassword");

        Account account = securityService.getAuthAccount(request);
        if(account.getPassword().equals(oldPassword) && newPassword.equals(reNewPassword)){
            account.setPassword(newPassword);
            accountService.updatePassword(account);
            response.sendRedirect(servletContext.getContextPath() + "/settings");
        } else {
            request.setAttribute("message", "проблема");
            request.getRequestDispatcher("/WEB-INF/jsp/settings.jsp").forward(request, response);
        }
    }
}
