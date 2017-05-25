package cellock.com.adhelper.Models.SuperClasses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AntonisS on 11/2/2017.
 */

public class AdOutput {

    @SerializedName("msg")
    protected String status;
    @SerializedName("date")
    protected String date;
    @SerializedName("contenturl")
    protected String contentUrl;
    @SerializedName("contenttype")
    protected String contentType;
    @SerializedName("width")
    protected int width;
    @SerializedName("height")
    protected int height;
    @SerializedName("linkurl")
    protected String linkUrl;
    @SerializedName("iconurl")
    protected String iconUrl;
    @SerializedName("camkey")
    protected int camKey;
    @SerializedName("adkey")
    protected int adKey;

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getDate() {
        return date;
    }
    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
    public String getContentUrl() {
        return contentUrl;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    public String getContentType() {
        return contentType;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getWidth() {
        return width;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getHeight() {
        return height;
    }
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
    public String getLinkUrl() {
        return linkUrl;
    }
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
    public String getIconUrl() {
        return iconUrl;
    }
    public void setCamKey(int camKey) {
        this.camKey = camKey;
    }
    public int getCamKey() {
        return camKey;
    }
    public void setAdKey(int adKey) {
        this.adKey = adKey;
    }
    public int getAdKey() {
        return adKey;
    }

    public void setResult() {}
}
