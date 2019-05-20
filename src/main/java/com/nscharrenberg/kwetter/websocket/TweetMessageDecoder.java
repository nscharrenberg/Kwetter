package com.nscharrenberg.kwetter.websocket;

import com.google.gson.Gson;
import com.nscharrenberg.kwetter.dtos.tweets.TweetDto;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class TweetMessageDecoder implements Decoder.Text<TweetDto> {
    private static Gson gson = new Gson();
    /**
     * Decode the given String into an object of type T.
     *
     * @param s string to be decoded.
     * @return the decoded message as an object of type T
     */
    @Override
    public TweetDto decode(String s) throws DecodeException {
        return gson.fromJson(s, TweetDto.class);
    }

    /**
     * Answer whether the given String can be decoded into an object of type T.
     *
     * @param s the string being tested for decodability.
     * @return whether this decoder can decoded the supplied string.
     */
    @Override
    public boolean willDecode(String s) {
        return (s != null);
    }

    /**
     * This method is called with the endpoint configuration object of the
     * endpoint this decoder is intended for when
     * it is about to be brought into service.
     *
     * @param config the endpoint configuration object when being brought into
     *               service
     */
    @Override
    public void init(EndpointConfig config) {

    }

    /**
     * This method is called when the decoder is about to be removed
     * from service in order that any resources the encoder used may
     * be closed gracefully.
     */
    @Override
    public void destroy() {

    }
}
