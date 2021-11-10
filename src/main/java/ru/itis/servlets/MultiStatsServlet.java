package ru.itis.servlets;

import ru.itis.exceptions.BadMultiIdException;
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
import java.util.Optional;
import java.util.Set;

@WebServlet("/stats/multi")
public class MultiStatsServlet extends HttpServlet {

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
            Account account = securityService.getAuthAccount(request);
            String name = request.getParameter("name");
            try {
                if (name != null) {
                    Optional<MultiLink> optionalMultiLink = multiLinkService.findByAccountAndName(account, name);

                    if (!optionalMultiLink.isPresent()) {
                        throw new BadMultiIdException();
                    }

                    MultiLink multiLink = optionalMultiLink.get();

                    request.setAttribute("multiLink", multiLink);
                    request.getRequestDispatcher("/WEB-INF/jsp/oneMultiStats.jsp").forward(request, response);
                } else {
                    request.setAttribute("multiLinks", account.getMultiLinks());
                    request.getRequestDispatcher("/WEB-INF/jsp/allMultiStats.jsp").forward(request, response);
                }
            } catch (BadMultiIdException ex) {
                noticeHelper.addMessage(request, Messages.BAD_LINK_ID.get(), false);
                response.sendRedirect(servletContext.getContextPath() + "/stats/multi");
            }
        } else {
            noticeHelper.addMessage(request, Messages.NOT_AUTH.get(), false);
            response.sendRedirect(servletContext.getContextPath() + "/signIn");
        }
    }
}
