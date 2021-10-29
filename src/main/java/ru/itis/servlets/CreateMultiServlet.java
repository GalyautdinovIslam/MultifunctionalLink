package ru.itis.servlets;

import ru.itis.models.Account;
import ru.itis.models.MultiLink;
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

@WebServlet("/create/multi")
public class CreateMultiServlet extends HttpServlet {

    private ServletContext servletContext;
    private SecurityService securityService;
    private MultiLinkService multiLinkService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        servletContext = config.getServletContext();
        securityService = (SecurityService) servletContext.getAttribute("securityService");
        multiLinkService = (MultiLinkService) servletContext.getAttribute("multiLinkService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (securityService.isAuth(request)) {
            request.getRequestDispatcher("/WEB-INF/jsp/createMulti.jsp").forward(request, response);
        } else {
            response.sendRedirect(servletContext.getContextPath() + "/signin");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String link = request.getParameter("link");
        Account owner = securityService.getAuthAccount(request);

        MultiLink multiLink = MultiLink.builder()
                .owner(owner)
                .link(link)
                .build();

        multiLinkService.createMulti(multiLink);
        owner.getMultiLinks().add(multiLink);

        request.getSession().setAttribute("authAccount", owner);
        response.sendRedirect(servletContext.getContextPath() + "/stats?link=multi&id=" + multiLink.getId());
    }
}
