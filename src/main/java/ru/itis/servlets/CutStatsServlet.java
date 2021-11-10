package ru.itis.servlets;

import ru.itis.exceptions.BadCutIdException;
import ru.itis.helpers.Messages;
import ru.itis.helpers.NoticeHelper;
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
import java.util.Optional;
import java.util.Set;

@WebServlet("/stats/cut")
public class CutStatsServlet extends HttpServlet {

    private ServletContext servletContext;
    private SecurityService securityService;
    private CutLinkService cutLinkService;
    private NoticeHelper noticeHelper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        servletContext = config.getServletContext();
        securityService = (SecurityService) servletContext.getAttribute("securityService");
        cutLinkService = (CutLinkService) servletContext.getAttribute("cutLinkService");
        noticeHelper = (NoticeHelper) servletContext.getAttribute("noticeHelper");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (securityService.isAuth(request)) {
            Account account = securityService.getAuthAccount(request);
            String cut = request.getParameter("cut");
            try {
                if (cut != null) {
                    Optional<CutLink> optionalCutLink = cutLinkService.findByCut(cut);

                    if (!optionalCutLink.isPresent()) {
                        throw new BadCutIdException();
                    }

                    CutLink cutLink = optionalCutLink.get();
                    System.out.println(cutLink.getOwner().getId());
                    System.out.println(account.getId());
                    if (!cutLink.getOwner().getId().equals(account.getId())) {
                        System.out.println("test");
                        throw new BadCutIdException();
                    }

                    String linkToCopy = request.getRequestURI().replaceAll("/stats.+", "/c/" + cutLink.getCut());
                    request.setAttribute("cutLink", cutLink);
                    request.setAttribute("linkToCopy", linkToCopy);
                    request.getRequestDispatcher("/WEB-INF/jsp/oneCutStats.jsp").forward(request, response);
                } else {
                    request.setAttribute("cutLinks", account.getCutLinks());
                    request.getRequestDispatcher("/WEB-INF/jsp/allCutStats.jsp").forward(request, response);
                }
            } catch (BadCutIdException ex) {
                noticeHelper.addMessage(request, Messages.BAD_LINK_ID.get(), false);
                response.sendRedirect(servletContext.getContextPath() + "/stats/cut");
            }
        } else {
            noticeHelper.addMessage(request, Messages.NOT_AUTH.get(), false);
            response.sendRedirect(servletContext.getContextPath() + "/signIn");
        }
    }
}
