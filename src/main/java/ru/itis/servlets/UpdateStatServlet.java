package ru.itis.servlets;

import ru.itis.models.Account;
import ru.itis.models.MultiLink;
import ru.itis.services.AccountService;
import ru.itis.services.MultiLinkService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/update")
public class UpdateStatServlet extends HttpServlet {

    private ServletContext servletContext;
    private AccountService accountService;
    private MultiLinkService multiLinkService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        servletContext = config.getServletContext();
        accountService = (AccountService) servletContext.getAttribute("accountService");
        multiLinkService = (MultiLinkService) servletContext.getAttribute("multiLinkService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] multiLinkInfo = request.getParameter("multiLinkInfo").split("!");
        Optional<Account> optionalAccount = accountService.findByNickname(multiLinkInfo[0]);

        if(optionalAccount.isPresent()){
            Account account = optionalAccount.get();
            Optional<MultiLink> optionalMultiLink = multiLinkService.findByAccountAndName(account, multiLinkInfo[1]);
            if(optionalMultiLink.isPresent()){
                MultiLink multiLink = optionalMultiLink.get();
                multiLinkService.visit(multiLink);
                String link = multiLink.getLink().toString();
                response.sendRedirect((link.length() > link.replaceAll("//", "").length() ? "" :"//" ) + link);
                return;
            }
        }
        response.sendRedirect(multiLinkInfo[1]);
    }
}
