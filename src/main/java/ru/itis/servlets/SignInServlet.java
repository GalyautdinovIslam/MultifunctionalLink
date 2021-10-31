package ru.itis.servlets;

import ru.itis.exceptions.IncorrectSignInDataException;
import ru.itis.forms.AccountSignInForm;
import ru.itis.helpers.Messages;
import ru.itis.services.SecurityService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/signIn")
public class SignInServlet extends HttpServlet {

    private ServletContext servletContext;
    private SecurityService securityService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        servletContext = config.getServletContext();
        securityService = (SecurityService) servletContext.getAttribute("securityService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (securityService.isAuth(request)) {
            securityService.addMessage(request, Messages.ALREADY_AUTH.get(), false);
            response.sendRedirect(servletContext.getContextPath() + "/my");
        } else {
            request.getRequestDispatcher("/WEB-INF/jsp/signIn.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountSignInForm accountSignInForm = AccountSignInForm.builder()
                .email(request.getParameter("email"))
                .password(request.getParameter("password"))
                .build();

        try {
            securityService.signIn(request, accountSignInForm);
            response.sendRedirect(servletContext.getContextPath() + "/my");
        } catch (IncorrectSignInDataException ex) {
            request.setAttribute("email", accountSignInForm.getEmail());
            request.setAttribute("message", ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/signIn.jsp").forward(request, response);
        }
    }
}
