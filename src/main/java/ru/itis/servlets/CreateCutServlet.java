package ru.itis.servlets;

import ru.itis.exceptions.BadLinkException;
import ru.itis.helpers.Messages;
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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
        if (securityService.isAuth(request)) {
            request.getRequestDispatcher("/WEB-INF/jsp/createCut.jsp").forward(request, response);
        } else {
            securityService.addMessage(request, Messages.NOT_AUTH.get(), false);
            response.sendRedirect(servletContext.getContextPath() + "/signIn");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String link = request.getParameter("link");
        try {
            cutLinkService.isValid(link);
            Account owner = securityService.getAuthAccount(request);

            try {
                URI uri = new URI(link);
                CutLink cutLink = CutLink.builder()
                        .owner(owner)
                        .cut(cutLinkService.generateCut())
                        .link(uri)
                        .build();

                cutLinkService.createCut(cutLink);
                owner.getCutLinks().add(cutLink);
                securityService.updateAuthAccount(request, owner);

                response.sendRedirect(servletContext.getContextPath() + "/stats?link=cut&id=" + cutLink.getId());
            } catch (URISyntaxException ex) {
                throw new BadLinkException();
            }
        } catch (BadLinkException ex) {
            request.setAttribute("message", ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/createMulti.jsp").forward(request, response);
        }
    }
}
