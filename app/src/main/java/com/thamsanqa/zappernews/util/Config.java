package com.thamsanqa.zappernews.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Config {
    public static final String ACCESS_KEY = "34aad064e45ddcd0ff6d0341699d6b1b";
    public static final String BASE_URL = "http://api.mediastack.com/v1/";
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String SELECTED_NEWS = "selectedNews";

    private static Gson gson;

    public static Gson getGsonParser() {
        if(null == gson) {
            GsonBuilder builder = new GsonBuilder();
            gson = builder.create();
        }
        return gson;
    }

}
