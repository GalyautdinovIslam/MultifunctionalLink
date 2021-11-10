package ru.itis.servlets;

import ru.itis.models.CutLink;
import ru.itis.services.CutLinkService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/c/*")
public class CutLinkServlet extends HttpServlet {

    private ServletContext servletContext;
    private CutLinkService cutLinkService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        servletContext = config.getServletContext();
        cutLinkService = (CutLinkService) servletContext.getAttribute("cutLinkService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cut = request.getRequestURI().replaceAll(servletContext.getContextPath() + "/c/", "");
        Optional<CutLink> optionalCutLink = cutLinkService.findByCut(cut);
        if (optionalCutLink.isPresent()) {
            CutLink cutLink = optionalCutLink.get();
            cutLinkService.visit(cutLink);
            request.setAttribute("cutLink", cutLink);
            request.getRequestDispatcher("/WEB-INF/jsp/cut.jsp").forward(request, response);
        } else {
            response.setStatus(404);
            request.getRequestDispatcher("/WEB-INF/jsp/cutNotFound.jsp").forward(request, response);
        }
    }
}
