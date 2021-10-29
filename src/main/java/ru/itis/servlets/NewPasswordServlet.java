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
import java.util.Optional;

@WebServlet("/newpassword")
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
        if (recoveryCode != null && !recoveryCode.equals("")) {
            Optional<Account> optionalAccount = securityService.checkRecoveryCode(recoveryCode);
            if (optionalAccount.isPresent()) {
                securityService.logout(request);
                Account account = optionalAccount.get();
                request.setAttribute("recoveryAccount", account);
                request.getRequestDispatcher("WEB-INF/jsp/newPassword.jsp").forward(request, response);
            }
        }
        response.sendRedirect(servletContext.getContextPath());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String recoveryCode = request.getParameter("r");
        String newPassword = request.getParameter("newPassword");
        String reNewPassword = request.getParameter("reNewPassword");

        Account account = (Account) request.getAttribute("recoveryAccount");
        account = accountService.findById(account.getId()).get();

        if (newPassword.equals(reNewPassword) && !account.getPassword().equals(newPassword)) {
            account.setPassword(newPassword);
            accountService.updatePassword(account);
            securityService.deleteRecoveryCode(recoveryCode);
            response.sendRedirect(servletContext.getContextPath() + "/signin");
        } else {
            request.setAttribute("message", "пароли не совпадают");
            request.getRequestDispatcher("WEB-INF/jsp/newPassword.jsp").forward(request, response);
        }
    }
}
