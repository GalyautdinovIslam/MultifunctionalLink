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
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@WebServlet("/deleteCut")
public class DeleteCutServlet extends HttpServlet {

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cut = request.getParameter("cut");
        Account account = securityService.getAuthAccount(request);
        Optional<CutLink> optionalCutLink = cutLinkService.findByCut(cut);
        if(optionalCutLink.isPresent()){
            CutLink cutLink = optionalCutLink.get();

            Set<CutLink> cutLinks = account.getCutLinks();
            for(CutLink c : cutLinks){
                if(c.getCut().equals(cutLink.getCut())){
                    cutLinkService.deleteCut(cutLink);
                    account.getCutLinks().remove(c);
                    break;
                }
            }
        }
        response.sendRedirect(servletContext.getContextPath() + "/stats/cut");
    }
}
