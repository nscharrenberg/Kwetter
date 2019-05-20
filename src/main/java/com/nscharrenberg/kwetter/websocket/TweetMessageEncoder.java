package com.nscharrenberg.kwetter.websocket;

import com.google.gson.Gson;
import com.nscharrenberg.kwetter.dtos.tweets.TweetDto;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class TweetMessageEncoder implements Encoder.Text<TweetDto> {
    private static Gson gson = new Gson();

    /**
     * Encode the given object into a String.
     *
     * @param object the object being encoded.
     * @return the encoded object as a string.
     */
    @Override
    public String encode(TweetDto object) throws EncodeException {
        return gson.toJson(object);
    }

    /**
     * This method is called with the endpoint configuration object of the
     * endpoint this encoder is intended for when
     * it is about to be brought into service.
     *
     * @param config the endpoint configuration object when being brought into
     *               service
     */
    @Override
    public void init(EndpointConfig config) {

    }

    /**
     * This method is called when the encoder is about to be removed
     * from service in order that any resources the encoder used may
     * be closed gracefully.
     */
    @Override
    public void destroy() {

    }
}
