package cellock.com.adhelper.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Gayst on 5/8/2017.
 */

public class Stream {

    @SerializedName("useragent")
    @Expose
    private String userAgent;
    @SerializedName("channel")
    @Expose
    private String channel;
    //@SerializedName("keywords")
    //@Expose
    //private ArrayList<String> keywords;
    @SerializedName("width")
    @Expose
    private String width;
    @SerializedName("height")
    @Expose
    private String height;
    //@SerializedName("lat")
    //@Expose
    //private double lat;
    //@SerializedName("lon")
    //@Expose
    //private double lon;

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    public void setChannel(String channel) { this.channel = channel; }
    public void setWidth(String width) {
        this.width = width;
    }
    public void setHeight(String height) { this.height = height; }
    public void setLat(double lat) {
    //    this.lat = lat;
    }
    public void setLon(double lon) {
        //this.lon = lon;
    }
    //public void setKeywords(ArrayList<String> keywords) {
    //   this.keywords = keywords;
    // }
    public String getUserAgent() {
        return userAgent;
    }
    public String getChannel() {
        return channel;
    }
    public String getWidth() {
        return width;
    }
    public String getHeight() {
        return height;
    }
    public void getLat() {
        //return lat;
    }
    public void getLon() {
        //return lon;
    }
    //  public ArrayList<String> getKeywords() {
    //    return keywords;
    //}
}
