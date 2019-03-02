package controllers.viewModels;

import java.io.Serializable;

public class TweetViewModel implements Serializable {
    private String message;
    private int author;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }
}
