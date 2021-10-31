package ru.itis.servlets;

import ru.itis.exceptions.BadMultiIdException;
import ru.itis.helpers.Messages;
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
import java.util.Optional;
import java.util.Set;

@WebServlet("/stats/multi")
public class MultiStatsServlet extends HttpServlet {

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
            Set<MultiLink> multiLinks = securityService.getAuthAccount(request).getMultiLinks();
            String idString = request.getParameter("id");
            try {
                if (idString != null) {
                    long id = Long.parseLong(idString);

                    Optional<MultiLink> optionalMultiLink = multiLinkService.findById(id);

                    if (!optionalMultiLink.isPresent()) {
                        throw new BadMultiIdException();
                    }

                    MultiLink multiLink = optionalMultiLink.get();

                    if (!multiLinks.contains(multiLink)) {
                        throw new BadMultiIdException();
                    }

                    request.setAttribute("multiLink", multiLink);
                    request.getRequestDispatcher("/WEB-INF/jsp/oneMultiStats.jsp").forward(request, response);
                } else {
                    request.getRequestDispatcher("/WEB-INF/jsp/allMultiStats.jsp").forward(request, response);
                }
            } catch (NumberFormatException | BadMultiIdException ex) {
                securityService.addMessage(request, Messages.BAD_LINK_ID.get(), false);
                response.sendRedirect(servletContext.getContextPath() + "/stats/multi");
            }
        } else {
            securityService.addMessage(request, Messages.NOT_AUTH.get(), false);
            request.getRequestDispatcher("/WEB-INF/jsp/signIn.jsp").forward(request, response);
        }
    }
}
