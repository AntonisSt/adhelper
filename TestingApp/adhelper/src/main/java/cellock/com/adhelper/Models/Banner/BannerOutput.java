package cellock.com.adhelper.Models.Banner;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cellock.com.adhelper.Models.SuperClasses.AdOutput;
import cellock.com.adhelper.R;

/**
 * Created by AntonisS on 11/2/2017.
 */

public class BannerOutput extends AdOutput {

    private ImageView image;
    private TextView text;

    public BannerOutput(ImageView image, TextView text) {
        this.text = text;
        this.image = image;
    }

    @Override
    public void setResult() {
        if(((Activity)image.getContext()).isFinishing()) return;

        Glide.with(image.getContext())
                .load(contentUrl)
                .error(R.mipmap.header_image)
                .fitCenter()
                .into(image);

        StringBuilder builder = new StringBuilder();
        builder.append("status: " + status + "\n");
        builder.append("date: " + date + "\n");
        builder.append("contentUrl: " + contentUrl + "\n");
        builder.append("contentType: " + contentType + "\n");
        builder.append("width: " + width + "\n");
        builder.append("height: " + height + "\n");
        builder.append("linkUrl: " + linkUrl + "\n");
        builder.append("iconUrl: " + iconUrl + "\n");
        builder.append("camKey: " + camKey + "\n");
        builder.append("adKey: " + adKey + "\n");
        text.setText(builder.toString());
    }

    public TextView getText() {
        return text;
    }

}
