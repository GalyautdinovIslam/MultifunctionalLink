package ru.itis.servlets;

import ru.itis.exceptions.BadAgeException;
import ru.itis.helpers.Messages;
import ru.itis.helpers.ValidateHelper;
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

@WebServlet("/edit")
public class EditProfileServlet extends HttpServlet {

    private ServletContext servletContext;
    private SecurityService securityService;
    private AccountService accountService;
    private ValidateHelper validator;

    @Override
    public void init(ServletConfig config) throws ServletException {
        servletContext = config.getServletContext();
        securityService = (SecurityService) servletContext.getAttribute("securityService");
        accountService = (AccountService) servletContext.getAttribute("accountService");
        validator = (ValidateHelper) servletContext.getAttribute("validator");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (securityService.isAuth(request)) {
            request.getRequestDispatcher("/WEB-INF/jsp/edit.jsp").forward(request, response);
        } else {
            securityService.addMessage(request, Messages.NOT_AUTH.get(), false);
            response.sendRedirect(servletContext.getContextPath() + "/signIn");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int age;

            try {
                age = Integer.parseInt(request.getParameter("age"));
            } catch (NumberFormatException ex) {
                throw new BadAgeException();
            }

            validator.checkAge(age);

            Account account = securityService.getAuthAccount(request);

            account.setAge(age);
            accountService.updateAge(account);

            securityService.addMessage(request, Messages.SUCCESSFUL_CHANGE_AGE.get(), true);
            response.sendRedirect(servletContext.getContextPath() + "/edit");
        } catch (BadAgeException ex) {
            request.setAttribute("age", request.getParameter("age"));
            request.setAttribute("message", ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/edit.jsp").forward(request, response);
        }
    }
}
