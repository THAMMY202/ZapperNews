package com.thamsanqa.zappernews.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.thamsanqa.zappernews.R;
import com.thamsanqa.zappernews.pojo.Datum;
import com.thamsanqa.zappernews.util.Config;

public class DetailFragment extends Fragment {

    private ImageView imageView;
    private TextView textViewDescription;
    private TextView textViewAuthor;
    private TextView textViewPublishedAt;
    private TextView textViewSource;
    private TextView textViewTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_detail, container, false);

        imageView = root.findViewById(R.id.imgDetailsStoryLogo);
        textViewDescription = root.findViewById(R.id.tvStoryDetailsDescription);
        textViewAuthor = root.findViewById(R.id.tvStoryDetailsAuthor);
        textViewPublishedAt = root.findViewById(R.id.tvStoryDetailsTime);
        textViewSource = root.findViewById(R.id.tvStoryDetailsSource);
        textViewTitle = root.findViewById(R.id.tvStoryDetailsTitle);

        Bundle args = getArguments();
        if (args != null) {
            String personJsonString = args.getString(Config.SELECTED_NEWS);
            Datum datum = Config.getGsonParser().fromJson(personJsonString, Datum.class);
            displayNews(datum);
        }
        return root;
    }

    private void displayNews(Datum datum) {

        Glide.with(getActivity()).load(datum.getImage()).into(imageView);
        textViewTitle.setText(datum.getTitle());
        textViewDescription.setText(datum.getDescription());
        textViewPublishedAt.setText(datum.getPublishedAt());
        textViewSource.setText( String.format("in %s",datum.getSource()));

        if (datum.getAuthor() != null) {
            textViewAuthor.setText(String.format("by %s", datum.getAuthor()));
        } else {
            textViewAuthor.setText(String.format("by %s", "anonymous"));
        }

       // datum.getCategory()
    }

    private void showMsg(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}