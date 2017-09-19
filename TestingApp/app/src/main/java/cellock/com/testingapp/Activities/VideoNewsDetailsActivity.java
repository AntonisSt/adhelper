package cellock.com.testingapp.Activities;

import android.content.Intent;
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


import cellock.com.testingapp.R;

import java.util.concurrent.TimeUnit;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;


/**
 * Created by AntonisSt on 10/7/2017.
 */

public class VideoNewsDetailsActivity extends AppCompatActivity {
    private ImageView image;
    private VideoView ad;
    private Button closeBtn;
    private LinearLayout content;
    private RelativeLayout adHolder;
    private TextView title, description/*, output*/;

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 99) {
            if(resultCode == RESULT_OK) {
                //service.RawApiCall();
            }
        }
        else if(requestCode == 100)
            if(resultCode == RESULT_OK) {
               /* service = null;
                service = new AdService(VideoNewsDetailsActivity.this, "8C225462-6E4A-4EC3-A9E2-CDACF757326A", ad, output);
                service.getAdService();*/
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initViews() {
        content = (LinearLayout) findViewById(R.id.content);
        adHolder = (RelativeLayout) findViewById(R.id.rl_video_holder);

        content.setVisibility(View.GONE);
        adHolder.setVisibility(View.VISIBLE);

        image = (ImageView) findViewById(R.id.iv_new);
        title = (TextView) findViewById(R.id.tv_new);
        description = (TextView) findViewById(R.id.tv_description);
        ad = (VideoView) findViewById(R.id.video_view);
        //output = (TextView) findViewById(R.id.tv_output);
        closeBtn = (Button) findViewById(R.id.btn_close);
        closeBtn.setVisibility(View.GONE);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.setVisibility(View.VISIBLE);
                adHolder.setVisibility(View.GONE);
            }
        });
    }

    private void setValues() {
        try {
            ad.setVideoURI(Uri.parse("http://www.ted.com/talks/download/video/8584/talk/761"));
            ad.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    return false;
                }
            });
            ad.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                }
            });
            ad.start();
            io.reactivex.Observable.interval(5, TimeUnit.SECONDS)
                    .doOnNext(new Consumer<Long>() {
                        @Override
                        public void accept(@NonNull Long aLong) throws Exception {
                            closeBtn.setVisibility(View.VISIBLE);
                        }
                    });

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
                        /*service = new AdService(VideoNewsDetailsActivity.this, "8C225462-6E4A-4EC3-A9E2-CDACF757326A", ad, output);
                        service.getAdService();*/
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
