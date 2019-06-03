package com.nscharrenberg.kwetter.websocket;

import com.nscharrenberg.kwetter.domain.User;
import com.nscharrenberg.kwetter.dtos.tweets.TweetDto;
import com.nscharrenberg.kwetter.responses.ObjectResponse;
import com.nscharrenberg.kwetter.service.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Set;

@ApplicationScoped
@ServerEndpoint(
        value = "/websocket/{username}",
        decoders = TweetMessageDecoder.class,
        encoders = TweetMessageEncoder.class
)
public class TweetWebsocket {
    @Inject
    private UserService userService;

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        WebSocketSessionListener.getInstance().getSessionMap().put(username, session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        WebSocketSessionListener.getInstance().getSessionMap().remove(username);
    }

    @OnMessage
    public void onMessage(TweetDto message, Session session, @PathParam("username") String username) {
        Set<String> users = WebSocketSessionListener.getInstance().getActiveUsers();

        ObjectResponse<User> userResponse = userService.getByUsername(username);
        if(userResponse.getObject() == null) {
            return;
        }

        if(users.contains(userResponse.getObject().getUsername())) {
            WebSocketSessionListener.getInstance().getSessionMap().get(userResponse.getObject().getUsername()).getAsyncRemote().sendObject(message);
        }

        message.getMentions().forEach(f -> {
            if(users.contains(f.getUsername())) {
                WebSocketSessionListener.getInstance().getSessionMap().get(f.getUsername()).getAsyncRemote().sendObject(message);
            }
        });

        userResponse.getObject().getFollowers().forEach(f -> {
            if(users.contains(f.getUsername())) {
                WebSocketSessionListener.getInstance().getSessionMap().get(f.getUsername()).getAsyncRemote().sendObject(message);
            }
        });
    }
}
