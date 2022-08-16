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
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.thamsanqa.zappernews.R;
import com.thamsanqa.zappernews.pojo.News;
import com.thamsanqa.zappernews.util.Config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

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
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        SimpleDateFormat sdf = new SimpleDateFormat(Config.DATE_FORMAT);
        try {
            Date mDate = sdf.parse(news.getData().get(position).getPublishedAt());
            holder.textViewStoryTime.setText(TimeAgo.using(mDate.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
            holder.textViewStoryTime.setText(news.getData().get(position).getPublishedAt());
        }

        holder.textViewStoryTitle.setText(news.getData().get(position).getTitle());
        holder.textViewStorySource.setText(news.getData().get(position).getSource());
        holder.textViewStoryDescription.setText(news.getData().get(position).getDescription());
        Glide.with(context).load(news.getData().get(position).getImage()).into(holder.imageViewStoryLogo);
    }

    @Override
    public int getItemCount() {
        int size = 0;

        if (news.getPagination() != null) {
            size = news.getPagination().getCount();
        } else {
            size = news.getData().size();
        }
        return size;
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
