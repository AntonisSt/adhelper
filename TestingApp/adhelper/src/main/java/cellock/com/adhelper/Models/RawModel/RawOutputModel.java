package cellock.com.adhelper.Models.RawModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gayst on 4/6/2017.
 */

public class RawOutputModel {
    @SerializedName("msg")
    protected String status;

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
}
