package ru.itis.servlets;

import ru.itis.exceptions.BadCutIdException;
import ru.itis.helpers.Messages;
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
import java.util.Optional;
import java.util.Set;

@WebServlet("/stats/cut")
public class CutStatsServlet extends HttpServlet {

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
            Set<CutLink> cutLinks = securityService.getAuthAccount(request).getCutLinks();
            String idString = request.getParameter("id");
            try {
                if (idString != null) {
                    long id = Long.parseLong(idString);

                    Optional<CutLink> optionalCutLink = cutLinkService.findById(id);
                    if (!optionalCutLink.isPresent()) {
                        throw new BadCutIdException();
                    }
                    CutLink cutLink = optionalCutLink.get();

                    if (!cutLinks.contains(cutLink)) {
                        throw new BadCutIdException();
                    }

                    request.setAttribute("cutLink", cutLink);
                    request.getRequestDispatcher("/WEB-INF/jsp/oneCutStats.jsp").forward(request, response);
                } else {
                    request.getRequestDispatcher("/WEB-INF/jsp/allCutStats.jsp").forward(request, response);
                }
            } catch (NumberFormatException | BadCutIdException ex) {
                securityService.addMessage(request, Messages.BAD_LINK_ID.get(), false);
                response.sendRedirect(servletContext.getContextPath() + "/stats/cut");
            }
        } else {
            securityService.addMessage(request, Messages.NOT_AUTH.get(), false);
            request.getRequestDispatcher("/WEB-INF/jsp/signIn.jsp").forward(request, response);
        }
    }
}
