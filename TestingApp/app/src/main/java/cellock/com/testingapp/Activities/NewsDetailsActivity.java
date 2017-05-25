package cellock.com.testingapp.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.ConnectException;
import java.net.UnknownHostException;

import cellock.com.adhelper.Service.AdService;
import cellock.com.testingapp.Models.SportsResponse;
import cellock.com.testingapp.R;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by AntonisS on 4/3/2017.
 */

public class NewsDetailsActivity extends AppCompatActivity  {

    private ImageView image, ad;
    private TextView title, description, output;
    private AdService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

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
                service.RawApiCall();
            }
        }
        else if(requestCode == 100)
            if(resultCode == RESULT_OK) {
                service = null;
                service = new AdService(NewsDetailsActivity.this, "8C225462-6E4A-4EC3-A9E2-CDACF757326A", ad, output);
                service.getAdService();
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initViews() {
        image = (ImageView) findViewById(R.id.iv_new);
        title = (TextView) findViewById(R.id.tv_new);
        description = (TextView) findViewById(R.id.tv_description);
        ad = (ImageView) findViewById(R.id.iv_ad);
        output = (TextView) findViewById(R.id.tv_output);
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
                        service = new AdService(NewsDetailsActivity.this, "8C225462-6E4A-4EC3-A9E2-CDACF757326A", ad, output);
                        service.getAdService();
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