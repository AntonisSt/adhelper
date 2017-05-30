package com.cellock.testingapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.cellock.testingapp.Models.SportsNew;
import com.cellock.testingapp.R;
import com.cellock.testingapp.ViewHolders.NewsViewHolder;

/**
 * Created by AntonisS on 4/3/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter {
    private List<SportsNew> news;

    public NewsAdapter(List<SportsNew> news) {
        this.news = news;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_rv_category, parent, false);
        NewsViewHolder rowViewHeader = new NewsViewHolder(view, news);
        return rowViewHeader;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewsViewHolder vh = (NewsViewHolder) holder;
        if(vh == null) return;

        vh.setTitle(news.get(position).getName());
        vh.setImage(news.get(position).getImageUrl());
    }

    @Override
    public int getItemCount() {
        return news.size();
    }
}
