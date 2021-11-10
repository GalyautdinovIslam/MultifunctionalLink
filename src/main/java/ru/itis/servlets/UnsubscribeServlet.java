package ru.itis.servlets;

import ru.itis.exceptions.AlreadyUnsubscribedException;
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

@WebServlet("/unsubscribe")
public class UnsubscribeServlet extends HttpServlet {

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
        response.sendRedirect(servletContext.getContextPath());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (securityService.isAuth(request)) {
            Account account = securityService.getAuthAccount(request);
            String nickname = request.getParameter("profileToUnsub");
            Optional<Account> optionalAccount = accountService.findByNickname(nickname);
            if(optionalAccount.isPresent()) {
                Account profile = optionalAccount.get();
                try {
                    accountService.unsubscribe(account, profile);
                    response.sendRedirect(servletContext.getContextPath() + "/profile/" + nickname);
                } catch (AlreadyUnsubscribedException ex) {
                    request.setAttribute("message", ex.getMessage());
                    request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);
                }
            }
        }
    }
}
