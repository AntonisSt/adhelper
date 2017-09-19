package cellock.com.adhelper.Models.Banner;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import cellock.com.adhelper.Models.SuperClasses.AdOutput;
import cellock.com.adhelper.R;

/**
 * Created by AntonisS on 11/2/2017.
 */

public class BannerOutput extends AdOutput {

    private ImageView image;

    public BannerOutput(ImageView image) {
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

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linkUrl == null)
                    return;

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl));
                v.getContext().startActivity(browserIntent);
            }
        });
    }


}
