package com.thamsanqa.zappernews.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thamsanqa.zappernews.R;
import com.thamsanqa.zappernews.adapter.NewsAdapter;
import com.thamsanqa.zappernews.api.ApiClient;
import com.thamsanqa.zappernews.pojo.Datum;
import com.thamsanqa.zappernews.pojo.News;
import com.thamsanqa.zappernews.util.Config;
import com.thamsanqa.zappernews.viewModel.NewsViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListFragment extends Fragment {

    private News searchedNewsValues;
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private SearchView searchView;
    private NewsViewModel mViewModel;
    private com.airbnb.lottie.LottieAnimationView lottieAnimationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_list, container, false);

        mViewModel = new ViewModelProvider(requireActivity()).get(NewsViewModel.class);

        findViewByIds(root);
        initRetrofit();
        return root;
    }

    private void goToDetails(Datum news) {

        Bundle args = new Bundle();
        String personJsonString = Config.getGsonParser().toJson(news);
        args.putString(Config.SELECTED_NEWS, personJsonString);

        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(args);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.FragmentContainer, detailFragment)
                .addToBackStack("tag")
                .commit();
    }

    private void findViewByIds(View view) {
        recyclerView = view.findViewById(R.id.recyclerviewStories);
        searchView = view.findViewById(R.id.searchView);
        lottieAnimationView = view.findViewById(R.id.loading);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                resetData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                resetData(newText);
                return false;
            }
        });
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // TODO Add your menu entries here
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                //clearing old data
                mViewModel.news = null;

                initRetrofit();
                break;
        }
        return true;

    }

    private void initRetrofit() {

        lottieAnimationView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        if (mViewModel.news == null) {
            // showMsg("view model is null");
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
                        mViewModel.news = response.body();
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        newsAdapter = new NewsAdapter(getActivity(), mViewModel.news, new NewsAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                goToDetails(mViewModel.news.getData().get(position));
                            }
                        });
                        recyclerView.setAdapter(newsAdapter);
                        lottieAnimationView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {
                    showMsg(t.toString());
                }
            });


        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);
            newsAdapter = new NewsAdapter(getActivity(), mViewModel.news, new NewsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    goToDetails(mViewModel.news.getData().get(position));
                }
            });
            recyclerView.setAdapter(newsAdapter);
            lottieAnimationView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }


    }


    private void resetData(String query) {

        if (query != null && !query.isEmpty()) {
            List<Datum> data = new ArrayList<>();
            searchedNewsValues = new News();

            for (int i = 0; i < mViewModel.news.getPagination().getCount(); i++) {

                if (mViewModel.news.getData().get(i).getSource() != null && mViewModel.news.getData().get(i).getSource().contains(query) ||
                        mViewModel.news.getData().get(i).getAuthor().contains(query)) {
                    data.add(mViewModel.news.getData().get(i));
                    searchedNewsValues.setData(data);
                }
            }

            if (data.size() > 0) {
                newsAdapter = new NewsAdapter(getActivity(), mViewModel.news, new NewsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        goToDetails(mViewModel.news.getData().get(position));
                    }
                });

                recyclerView.setAdapter(newsAdapter);
                lottieAnimationView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showMsg(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}