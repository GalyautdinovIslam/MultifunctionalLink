package ru.itis.servlets;

import ru.itis.exceptions.BadAgeException;
import ru.itis.helpers.Messages;
import ru.itis.helpers.NoticeHelper;
import ru.itis.helpers.ValidateHelper;
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

@WebServlet("/edit")
public class EditProfileServlet extends HttpServlet {

    private ServletContext servletContext;
    private SecurityService securityService;
    private AccountService accountService;
    private ValidateHelper validator;
    private NoticeHelper noticeHelper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        servletContext = config.getServletContext();
        securityService = (SecurityService) servletContext.getAttribute("securityService");
        accountService = (AccountService) servletContext.getAttribute("accountService");
        validator = (ValidateHelper) servletContext.getAttribute("validator");
        noticeHelper = (NoticeHelper) servletContext.getAttribute("noticeHelper");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (securityService.isAuth(request)) {
            request.getRequestDispatcher("/WEB-INF/jsp/edit.jsp").forward(request, response);
        } else {
            noticeHelper.addMessage(request, Messages.NOT_AUTH.get(), false);
            response.sendRedirect(servletContext.getContextPath() + "/signIn");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Integer age = Integer.parseInt(request.getParameter("age"));

            validator.checkAge(age);

            Account account = securityService.getAuthAccount(request);

            account.setAge(age);
            accountService.updateAge(account);

            noticeHelper.addMessage(request, Messages.SUCCESSFUL_CHANGE_AGE.get(), true);
            response.sendRedirect(servletContext.getContextPath() + "/edit");

        } catch (NumberFormatException | BadAgeException ex) {
            request.setAttribute("age", request.getParameter("age"));
            noticeHelper.addMessage(request, ex.getMessage(), false);
            request.getRequestDispatcher("/WEB-INF/jsp/edit.jsp").forward(request, response);
        }
    }
}
