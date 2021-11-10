package ru.itis.servlets;

import ru.itis.exceptions.BadLinkException;
import ru.itis.exceptions.BadMultiNameException;
import ru.itis.helpers.Messages;
import ru.itis.helpers.NoticeHelper;
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
import java.net.URI;
import java.net.URISyntaxException;

@WebServlet("/create/multi")
public class CreateMultiServlet extends HttpServlet {

    private ServletContext servletContext;
    private SecurityService securityService;
    private MultiLinkService multiLinkService;
    private NoticeHelper noticeHelper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        servletContext = config.getServletContext();
        securityService = (SecurityService) servletContext.getAttribute("securityService");
        multiLinkService = (MultiLinkService) servletContext.getAttribute("multiLinkService");
        noticeHelper = (NoticeHelper) servletContext.getAttribute("noticeHelper");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (securityService.isAuth(request)) {
            request.getRequestDispatcher("/WEB-INF/jsp/createMulti.jsp").forward(request, response);
        } else {
            noticeHelper.addMessage(request, Messages.NOT_AUTH.get(), false);
            response.sendRedirect(servletContext.getContextPath() + "/signIn");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String name = request.getParameter("name").trim();
        String link = request.getParameter("link");
        try {
            multiLinkService.isValid(name, link);
            Account owner = securityService.getAuthAccount(request);

            try {
                URI uri = new URI(link);
                MultiLink multiLink = MultiLink.builder()
                        .owner(owner)
                        .name(name)
                        .link(uri)
                        .build();

                multiLink = multiLinkService.createMulti(multiLink);
                owner.getMultiLinks().add(multiLink);
                securityService.updateAuthAccount(request, owner);

                response.sendRedirect(servletContext.getContextPath() + "/stats/multi?name=" + multiLink.getName());
            } catch (URISyntaxException ex) {
                throw new BadLinkException();
            }
        } catch (BadLinkException | BadMultiNameException ex) {
            noticeHelper.addMessage(request, ex.getMessage(), false);
            request.getRequestDispatcher("/WEB-INF/jsp/createMulti.jsp").forward(request, response);
        }
    }
}
