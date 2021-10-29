package ru.itis.servlets;

import ru.itis.services.SecurityService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/my")
public class MyProfileServlet extends HttpServlet {

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
            request.getRequestDispatcher("/WEB-INF/jsp/myProfile.jsp").forward(request, response);
        } else {
            response.sendRedirect(servletContext.getContextPath() + "/signin");
        }
    }
}
