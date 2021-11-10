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
        String nickname = request.getRequestURI().replaceAll(servletContext.getContextPath() + "/profile/", "");
        Optional<Account> optionalAccount = accountService.findByNickname(nickname);

        if (!optionalAccount.isPresent()) {
            response.setStatus(404);
            request.getRequestDispatcher("/WEB-INF/jsp/profileNotFound.jsp").forward(request, response);
        } else {
            Account profile = optionalAccount.get();

            if (securityService.isAuth(request)) {
                Account auth = securityService.getAuthAccount(request);

                if (auth.getId().equals(profile.getId())) {
                    response.sendRedirect(servletContext.getContextPath() + "/my");
                    return;
                }

                boolean isSub = false;
                for(Account sub : auth.getSubscriptions()){
                    if(sub.getId().equals(profile.getId())){
                        isSub = true;
                        break;
                    }
                }

                request.setAttribute("isSub", isSub);
            }

            request.setAttribute("profile", profile);
            request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = request.getParameter("search");
        response.sendRedirect(servletContext.getContextPath() + "/profile/" + search);
    }
}
