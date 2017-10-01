package cellock.com.testingapp.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;


import cellock.com.adhelper.Service.AdService;
import cellock.com.testingapp.R;

import java.util.concurrent.TimeUnit;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;


/**
 * Created by AntonisSt on 10/7/2017.
 */

public class VideoNewsDetailsActivity extends AppCompatActivity {
    private ImageView image;
    private LinearLayout content;
    private RelativeLayout adHolder;
    private TextView title, description;
    private AdService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_news_details);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);

        if (toolbar == null) return;
        setSupportActionBar(toolbar);

        setTitle(getIntent().getStringExtra("title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        initViews();
        setValues();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if(requestCode == 99) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                service.RawApiCall();
            }
            else {
                service.RawApiNoLocationCall();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 99) {
            if(resultCode == RESULT_OK) {
                service.RawApiCall();
            }
            else if(resultCode == RESULT_CANCELED){
                service.RawApiNoLocationCall();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initViews() {
        content = (LinearLayout) findViewById(R.id.content);
        adHolder = (RelativeLayout) findViewById(R.id.rl_video_holder);

        image = (ImageView) findViewById(R.id.iv_new);
        title = (TextView) findViewById(R.id.tv_new);
        description = (TextView) findViewById(R.id.tv_description);
    }

    private void setValues() {
        try {
            title.setText(getIntent().getStringExtra("title"));

            description.setText(Html.fromHtml(getIntent().getStringExtra("description")));

            Glide.with(this)
                    .load(getIntent().getStringExtra("image"))
                    .fitCenter()
                    .into(image);


            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        service = new AdService(VideoNewsDetailsActivity.this, "F1D27CAC-9FD3-4F71-BF9F-B34AFC9E54BC", adHolder);
                        service.getAdService();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                content.setVisibility(View.VISIBLE);
                            }
                        });
                    } catch(SecurityException e) {

                    }
                }
            });
            thread.start();

        }catch(Exception e) {
            e.printStackTrace();
        }
    }

}
