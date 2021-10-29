package ru.itis.servlets;

import ru.itis.exceptions.*;
import ru.itis.forms.AccountRegisterForm;
import ru.itis.services.SecurityService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {

    private ServletContext servletContext;
    private SecurityService securityService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        servletContext = config.getServletContext();
        securityService = (SecurityService) servletContext.getAttribute("securityService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(securityService.isAuth(request)){
            response.sendRedirect(servletContext.getContextPath() + "/my");
        } else {
            request.getRequestDispatcher("/WEB-INF/jsp/signup.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountRegisterForm accountRegisterForm = AccountRegisterForm.builder()
                .email(request.getParameter("email"))
                .password(request.getParameter("password"))
                .rePassword(request.getParameter("rePassword"))
                .nickname(request.getParameter("nickname"))
                .build();

        try {
            securityService.signup(request, accountRegisterForm);
        } catch (BadNicknameException | BadPasswordException | NicknameAlreadyExistException | SignUpPasswordMismatchException | BadEmailAddressException ex) {
            request.setAttribute("email", accountRegisterForm.getEmail());
            request.setAttribute("nickname", accountRegisterForm.getNickname());
            request.setAttribute("message", ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/signup.jsp").forward(request, response);
        } catch (EmailAlreadyExistException ex) {
            // TODO: существующий имеил
            request.setAttribute("email", accountRegisterForm.getEmail());
            request.setAttribute("nickname", accountRegisterForm.getNickname());
            request.setAttribute("message", "почта");
            request.getRequestDispatcher("/WEB-INF/jsp/signup.jsp").forward(request, response);
        }
    }
}
