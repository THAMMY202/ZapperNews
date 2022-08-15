package com.thamsanqa.zappernews.api;
import com.thamsanqa.zappernews.pojo.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface ApiClient {
    @GET("news?")
    Call<News> getNews(@Query("access_key") String key);
}
