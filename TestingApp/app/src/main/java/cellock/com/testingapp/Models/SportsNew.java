package cellock.com.testingapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AntonisS on 25/2/2017.
 */

public class SportsNew {
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("title")
    private String name;
    @Expose
    @SerializedName("image")
    private String imageUrl;
    @Expose
    @SerializedName("introtext")
    private String description;

    public void setId(int id) { this.id = id; }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() { return name; }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getDescription() {
        return description;
    }
    public int getId() { return id; }
}
