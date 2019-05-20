package com.nscharrenberg.kwetter.websocket;

import com.nscharrenberg.kwetter.domain.User;
import com.nscharrenberg.kwetter.responses.ObjectResponse;
import com.nscharrenberg.kwetter.service.TweetService;
import com.nscharrenberg.kwetter.service.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

@ApplicationScoped
@ServerEndpoint("/ws/newTweet/{username}")
public class NewTweetWebSocket {
    @Inject
    private UserService userService;

    @Inject
    private TweetService tweetService;

    private static final Map<String, Session> clients
            = Collections.synchronizedMap(new HashMap<String, Session>());

    private static final Set<Session> activeClients
            = Collections.synchronizedSet(new HashSet<Session>());

    private User userSession;

    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) {
        clients.put(username, session);
        activeClients.add(session);
        ObjectResponse<User> response = userService.getByUsername(username);

        if(response.getObject() == null) {
            return;
        }

        this.userSession = response.getObject();
    }

    @OnClose
    public void onClose(Session session) {
        activeClients.remove(session);
        clients.remove(this.userSession.getUsername());
    }

    @OnError
    public void onError(Throwable t) {
        System.out.println(t);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            synchronized (clients) {
                for (User u : this.userSession.getFollowing()) {
                    Session s = clients.get(u.getUsername());

                    if (s != null) {
                        if (!s.equals(session)) {
                            System.out.println("sending message to " + u.getUsername());
                            clients.get(u.getUsername()).getBasicRemote().sendText(message);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
