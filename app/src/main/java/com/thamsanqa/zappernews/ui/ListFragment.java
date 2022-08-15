package com.thamsanqa.zappernews.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thamsanqa.zappernews.R;
import com.thamsanqa.zappernews.adapter.NewsAdapter;
import com.thamsanqa.zappernews.api.ApiClient;
import com.thamsanqa.zappernews.pojo.News;
import com.thamsanqa.zappernews.util.Config;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListFragment extends Fragment {

    private News news;
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        findViewByIds(root);

        initRetrofit();
        return root;
    }

    private void findViewByIds(View view) {
        recyclerView = view.findViewById(R.id.recyclerviewStories);
    }

    private void initRetrofit() {
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
                    news = response.body();
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    newsAdapter = new NewsAdapter(getActivity(), news);
                    recyclerView.setAdapter(newsAdapter);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                showMsg(t.toString());
            }
        });
    }

    private void showMsg(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}