package com.nscharrenberg.kwetter.websocket;

import com.nscharrenberg.kwetter.service.TweetService;

import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/websocket/{username}")
public class WebSocket {

    @Inject
    private TweetService tweetService;

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        //TODO: Implement logic
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        //TODO: Implement Logic
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("username") String username) {
        //TODO: Implement logic
    }
}
