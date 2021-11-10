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
import java.util.Optional;
import java.util.Set;

@WebServlet("/deleteMulti")
public class DeleteMultiServlet extends HttpServlet {

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        Account account = securityService.getAuthAccount(request);
        Optional<MultiLink> optionalMultiLink = multiLinkService.findByAccountAndName(account, name);
        if(optionalMultiLink.isPresent()){
            MultiLink multiLink = optionalMultiLink.get();
            Set<MultiLink> multiLinks = account.getMultiLinks();

            for(MultiLink m : multiLinks){
                if(m.getName().equals(multiLink.getName())){
                    multiLinkService.deleteMulti(multiLink);
                    account.getMultiLinks().remove(m);
                    break;
                }
            }
        }
        response.sendRedirect(servletContext.getContextPath() + "/stats/multi");
    }
}
