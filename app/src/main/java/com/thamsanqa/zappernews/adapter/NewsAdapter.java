package com.thamsanqa.zappernews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thamsanqa.zappernews.R;
import com.thamsanqa.zappernews.pojo.News;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>  {

    private Context context;
    public News news;

    public NewsAdapter(Context context, News news) {
        this.context = context;
        this.news = news;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {

        holder.textViewStoryTitle.setText(news.getData().get(position).getTitle());
        holder.textViewStoryTime.setText(news.getData().get(position).getPublishedAt());
        holder.textViewStorySource.setText(news.getData().get(position).getSource());
        holder.textViewStoryDescription.setText(news.getData().get(position).getDescription());
        Glide.with(context).load(news.getData().get(position).getImage()).into(holder.imageViewStoryLogo);
    }

    @Override
    public int getItemCount() {
        return news.getPagination().getCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewStoryLogo;
        public TextView textViewStoryTitle;
        public TextView textViewStoryTime;
        public TextView textViewStorySource;
        public TextView textViewStoryDescription;


        public ViewHolder(View itemView) {
            super(itemView);

            textViewStoryTitle = (TextView) itemView.findViewById(R.id.tvStoryTitle);
            textViewStorySource = (TextView) itemView.findViewById(R.id.tvStorySource);
            textViewStoryDescription = (TextView) itemView.findViewById(R.id.tvStoryDescription);
            textViewStoryTime = (TextView) itemView.findViewById(R.id.tvStoryTime);
            imageViewStoryLogo = (ImageView) itemView.findViewById(R.id.imgStoryLogo);
        }
    }
}
