package com.thamsanqa.zappernews;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thamsanqa.zappernews.adapter.NewsAdapter;
import com.thamsanqa.zappernews.api.ApiClient;
import com.thamsanqa.zappernews.pojo.News;
import com.thamsanqa.zappernews.util.Config;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This activity is the app entry point and it fetch data from the api
 */

public class MainActivity extends AppCompatActivity {

    private ArrayList<News> arrayListNews = new ArrayList<News>();
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerviewStories);

        Gson gson = new GsonBuilder()
                .setDateFormat(Config.DATE_FORMAT)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiClient apiService = retrofit.create(ApiClient.class);

        Call<News> call = apiService.getNews(Config.ACCESS_KEY);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {

                if (response != null && response.isSuccessful()) {
                    News news = response.body();
                    setData(news);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                showMsg(t.toString());
            }
        });
    }

    private void setData(News news){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 1, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        newsAdapter = new NewsAdapter(MainActivity.this, news);
        recyclerView.setAdapter(newsAdapter);

    }

    private void showMsg(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}