package com.thamsanqa.zappernews;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thamsanqa.zappernews.api.ApiClient;
import com.thamsanqa.zappernews.pojo.News;
import com.thamsanqa.zappernews.util.Config;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                showMsg(t.toString());
            }
        });
    }

    private void showMsg(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}