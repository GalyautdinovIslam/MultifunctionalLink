package ru.itis.helpers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class NoticeHelperImpl implements NoticeHelper {
    @Override
    public void addMessage(HttpServletRequest request, String message, boolean type) {
        HttpSession session = request.getSession();
        if (session.getAttribute("messageMap") == null) {
            session.setAttribute("messageMap", new HashMap<String, Boolean>());
        }
        Map<String, Boolean> map = (Map<String, Boolean>) session.getAttribute("messageMap");
        map.put(message, type);
    }
}
