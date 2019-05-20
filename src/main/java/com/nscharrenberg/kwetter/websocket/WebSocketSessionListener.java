package com.nscharrenberg.kwetter.websocket;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WebSocketSessionListener {
    private static WebSocketSessionListener sessionListener = null;
    private static Set<String> activeUsers;
    private static Map<String, Session> sessionMap;

    private WebSocketSessionListener() {
        activeUsers = new HashSet<>();
        sessionMap = new HashMap<>();
    }

    public static WebSocketSessionListener getInstance() {
        if(sessionListener == null) {
            sessionListener = new WebSocketSessionListener();
        }

        return sessionListener;
    }

    public Set<String> getActiveUsers() {
        return activeUsers;
    }

    public Map<String, Session> getSessionMap() {
        return sessionMap;
    }
}
