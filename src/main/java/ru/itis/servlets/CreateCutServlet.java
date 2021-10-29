package ru.itis.servlets;

import ru.itis.models.Account;
import ru.itis.models.CutLink;
import ru.itis.services.CutLinkService;
import ru.itis.services.SecurityService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/create/cut")
public class CreateCutServlet extends HttpServlet {

    private ServletContext servletContext;
    private SecurityService securityService;
    private CutLinkService cutLinkService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        servletContext = config.getServletContext();
        securityService = (SecurityService) servletContext.getAttribute("securityService");
        cutLinkService = (CutLinkService) servletContext.getAttribute("cutLinkService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(securityService.isAuth(request)){
            request.getRequestDispatcher("/WEB-INF/jsp/createCut.jsp").forward(request, response);
        } else {
            response.sendRedirect(servletContext.getContextPath() + "/signin");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String link = request.getParameter("link");
        Account owner = securityService.getAuthAccount(request);

        CutLink cutLink = CutLink.builder()
                .owner(owner)
                .cut(cutLinkService.generateCut(8))
                .link(link)
                .build();

        cutLinkService.createCut(cutLink);
        owner.getCutLinks().add(cutLink);

        request.getSession().setAttribute("authAccount", owner);
        response.sendRedirect(servletContext.getContextPath() + "/stats?link=cut&id=" + cutLink.getId());
    }
}
