package com.example.instagram;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.Serializable;

@ParseClassName("Comment")
public class Comment extends ParseObject implements Serializable {
    public static final String KEY_USER = "user";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_POST = "post";

    public Comment() {
        super();
    }
    
    public String getContent() {
        return getString(KEY_CONTENT);
    }

    public void setContent(String content) {
        put(KEY_CONTENT, content);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

     public void setUser(ParseUser user) {
         put(KEY_USER, user);
     }

    public ParseObject getPost() {
        return getParseObject(KEY_POST);
    }

    public void setPost(ParseObject post) {
        put(KEY_POST, post);
    }

}
