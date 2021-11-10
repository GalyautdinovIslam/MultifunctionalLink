package ru.itis.servlets;

import ru.itis.helpers.Messages;
import ru.itis.helpers.NoticeHelper;
import ru.itis.models.Account;
import ru.itis.services.AccountService;
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

@WebServlet("/continueSignUp")
public class ContinueSignUpServlet extends HttpServlet {

    private ServletContext servletContext;
    private SecurityService securityService;
    private AccountService accountService;
    private NoticeHelper noticeHelper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        servletContext = config.getServletContext();
        securityService = (SecurityService) servletContext.getAttribute("securityService");
        accountService = (AccountService) servletContext.getAttribute("accountService");
        noticeHelper = (NoticeHelper) servletContext.getAttribute("noticeHelper");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("r");

        if (code != null) {
            Optional<Account> optionalAccount = accountService.checkSignUpCode(code);

            if (optionalAccount.isPresent()) {
                securityService.logout(request);
                optionalAccount = accountService.findById(optionalAccount.get().getId());
                if (optionalAccount.isPresent()) {
                    Account account = optionalAccount.get();
                    accountService.deleteSignUpCode(account.getId());
                    accountService.updateStatus(account.getId());
                    securityService.login(request, account);

                    noticeHelper.addMessage(request, Messages.SUCCESSFUL_SIGN_UP.get(), true);
                }
            }
        }
        response.sendRedirect(servletContext.getContextPath());
    }
}
