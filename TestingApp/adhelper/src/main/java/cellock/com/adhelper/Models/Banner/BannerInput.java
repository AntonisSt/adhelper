package cellock.com.adhelper.Models.Banner;

import android.app.Activity;

import cellock.com.adhelper.Models.SuperClasses.AdInput;

/**
 * Created by AntonisS on 13/2/2017.
 */

public class BannerInput extends AdInput {

    public BannerInput(Activity context){
        super(context);
        adType = "Banner";
    }

}
