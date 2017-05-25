package cellock.com.adhelper.Models.ViewPager;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cellock.com.adhelper.Models.SuperClasses.AdOutput;

/**
 * Created by AntonisS on 25/2/2017.
 */

public class ViewPagerOutput extends AdOutput {

    private ImageView image;

    public ViewPagerOutput(ImageView image) {
        this.image = image;
    }

    @Override
    public void setResult() {
        Glide.with(image.getContext())
                .load(contentUrl)
                .fitCenter()
                .into(image);
    }
}

