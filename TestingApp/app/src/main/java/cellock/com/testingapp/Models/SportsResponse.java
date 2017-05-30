package com.cellock.testingapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by AntonisS on 5/3/2017.
 */

public class SportsResponse {
    @Expose
    @SerializedName("article")
    private List<SportsNew> news;

    public List<SportsNew> getNews(){ return news; }
    public void setNews(List<SportsNew> news) { this.news = news; }
}
