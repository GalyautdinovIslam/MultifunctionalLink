package ru.itis.servlets;

import ru.itis.models.Account;
import ru.itis.services.AccountService;
import ru.itis.services.MailService;
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

@WebServlet("/recovery")
public class RecoveryServlet extends HttpServlet {

    private ServletContext servletContext;
    private SecurityService securityService;
    private MailService mailService;
    private AccountService accountService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        servletContext = config.getServletContext();
        securityService = (SecurityService) servletContext.getAttribute("securityService");
        accountService = (AccountService) servletContext.getAttribute("accountService");
        mailService = (MailService) servletContext.getAttribute("mailService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        securityService.logout(request);
        request.getRequestDispatcher("WEB-INF/jsp/recovery.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String input = request.getParameter("emailOrNickname");
        boolean isEmail = input.contains("@");
        Optional<Account> optionalAccount;

        if(isEmail){
            optionalAccount = accountService.findByEmail(input);
        } else {
            optionalAccount = accountService.findByNickname(input);
        }

        if(optionalAccount.isPresent()){
            Account account = optionalAccount.get();
            String recoveryCode = securityService.generateRecoveryCode(account);
            String pathForMail = servletContext.getContextPath() + "/newpassword?r=" + recoveryCode;
            mailService.sendRecoveryMessage(account.getEmail(), pathForMail);
        }

        // TODO: проверьте свою почту
    }
}
