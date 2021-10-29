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

@WebServlet("/profile/*")
public class ProfileServlet extends HttpServlet {

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
        String nickname = request.getRequestURI().replaceAll("/profile/", "");
        Optional<Account> optionalAccount = accountService.findByNickname(nickname);
        if(!optionalAccount.isPresent()){
            request.getRequestDispatcher("/WEB-INF/jsp/profileNotFound.jsp").forward(request, response);
        } else {
            Account profile = optionalAccount.get();

            if (securityService.isAuth(request)){
                Account auth = (Account) request.getAttribute("authAccount");
                if(auth.getId().equals(profile.getId())){
                    response.sendRedirect(servletContext.getContextPath() + "/my");
                }
            }

            request.setAttribute("profile", profile);
            request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);
        }
    }
}
