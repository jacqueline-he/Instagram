package com.example.instagram;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

@ParseClassName("Post")
public class Post extends ParseObject implements Serializable {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_LIKES = "likes";

    public Post() {
        super();
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public JSONArray getLikes() {
        JSONArray arr = getJSONArray(KEY_LIKES);
        if (arr == null)
            return new JSONArray();
        return arr;
    }

    public void setLikes(JSONArray arr) {
        put("likes", arr);
    }

    public void likePost() {
        add(KEY_LIKES, ParseUser.getCurrentUser());
    }

    public void unlikePost() {
        ArrayList<ParseUser> user = new ArrayList<ParseUser>();
        user.add(ParseUser.getCurrentUser());
        removeAll(KEY_LIKES, user);
    }

    public boolean isLiked() throws JSONException {
        JSONArray arr = getLikes();
        for (int i = 0; i < arr.length(); i++) {
            if (arr.getJSONObject(i).getString("objectId").equals(ParseUser.getCurrentUser().getObjectId()))
                return true;
        }
        return false;
    }

}
