package cellock.com.adhelper.Models.RawModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cellock.com.adhelper.Models.Stream;

/**
 * Created by AntonisSt on 30/5/2017.
 */

public class RawInputTemp {
    @SerializedName("event")
    @Expose
    protected String event;

    @SerializedName("stream")
    @Expose
    protected String stream;

    @SerializedName("uakey")
    @Expose
    protected String uaKey;

    @SerializedName("udid")
    @Expose
    protected String udId;





    public void setUdId(String udId) {
        this.udId = udId;
    }

    public void setUaKey(String uaKey) {
        this.uaKey = uaKey;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getUdId() {
        return udId;
    }
    public String getUaKey() {
        return uaKey;
    }
    public String getStream() {
        return stream;
    }
    public String getEvent() { return event; }
}
