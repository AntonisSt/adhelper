package cellock.com.adhelper.Models.SuperClasses;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AntonisS on 11/2/2017.
 */

public class AdInput {

    protected static final String baseUrl = "http://cellockdomain.cloudapp.net/";
    @SerializedName("camkey")
    @Expose
    protected int camKey;
    @SerializedName("adkey")
    @Expose
    protected int adKey;
    @SerializedName("udid")
    @Expose
    protected String udId;
    @SerializedName("uakey")
    @Expose
    protected String uaKey;
    @SerializedName("adtype")
    @Expose
    protected String adType;
    @SerializedName("devicetype")
    @Expose
    protected final String deviceType = "androidsdk";
    @SerializedName("linkurl")
    @Expose
    protected String linkUrl;
    @SerializedName("width")
    @Expose
    protected int width;
    @SerializedName("height")
    @Expose
    protected int height;

    public AdInput(Activity activity) {
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        try {
            udId = tm.getDeviceId();
        }
        catch(NullPointerException e) {
            udId = "device unique id not available, device probably rooted";
        }
        catch(SecurityException e) {
            ActivityCompat.requestPermissions(activity, new String[] {  Manifest.permission.READ_PHONE_STATE  }, 100 );
        }
    }

    public String getUdId() { return udId; }
    public String getUaKey() { return uaKey; }
    public void setCamKey(int camKey) {
        this.camKey = camKey;
    }
    public void setUaKey(String uaKey) {
        this.uaKey = uaKey;
    }
    public void setAdKey(int adKey) {
        this.adKey = adKey;
    }
    public String getBaseUrl() {
        return baseUrl;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
}
