package ru.itis.servlets;

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
            Long id = -1L;
            try {
                if (idString != null) {
                    id = Long.parseLong(idString);

                    Optional<MultiLink> optionalMultiLink = multiLinkService.findById(id);
                    if (!optionalMultiLink.isPresent()) throw new Exception();
                    MultiLink multiLink = optionalMultiLink.get();

                    if (!multiLinks.contains(multiLink)) throw new Exception();

                    request.setAttribute("multiLink", multiLink);
                    request.getRequestDispatcher("/WEB-INF/jsp/oneMultiStats.jsp").forward(request, response);
                } else {
                    request.getRequestDispatcher("/WEB-INF/jsp/allMultiStats.jsp").forward(request, response);
                }
            } catch (Exception e) {
                response.sendRedirect(servletContext.getContextPath() + "/stats?link=multi");
            }
        } else {
            request.getRequestDispatcher("/WEB-INF/jsp/signin.jsp").forward(request, response);
        }
    }
}
