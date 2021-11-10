package ru.itis.helpers;

import javax.servlet.http.HttpServletRequest;

public interface NoticeHelper {
    void addMessage(HttpServletRequest request, String message, boolean type);
}
