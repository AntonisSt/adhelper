package com.cellock.testingapp.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.microsoft.azure.mobile.MobileCenter;
import com.microsoft.azure.mobile.analytics.Analytics;
import com.microsoft.azure.mobile.crashes.Crashes;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.List;

import com.cellock.testingapp.Adapters.NewsAdapter;
import com.cellock.testingapp.Interfaces.ApiInterface;
import com.cellock.testingapp.Models.SportsNew;
import com.cellock.testingapp.Models.SportsResponse;
import com.cellock.testingapp.R;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView newsList;
    private ProgressBar progressBar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private List<SportsNew> news;
    private NewsAdapter adapter;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MobileCenter.start(getApplication(), "0e37873c-463d-414d-afe8-67bd939870eb",
                Analytics.class, Crashes.class);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);

        if (toolbar == null) return;
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle(R.string.home);
        initViews();
        setClickListeners();
        getNews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        }
        else {
            super.onBackPressed();
        }
    }

    private void getNews() {
        try {
            Retrofit retrofitClient = new Retrofit.Builder()
                    .baseUrl("http://sports1.com.cy/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiInterface service = retrofitClient.create(ApiInterface.class);
            Observable<SportsResponse> output = service.getHomeNews();
            output.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<SportsResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            d.isDisposed();
                        }

                        @Override
                        public void onNext(SportsResponse response) {
                            news = response.getNews();
                            adapter = new NewsAdapter(news);
                            newsList.setAdapter(adapter);
                            newsList.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            if(e instanceof ConnectException || e instanceof UnknownHostException) {
                                progressBar.setVisibility(View.GONE);
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "No connection", Snackbar.LENGTH_INDEFINITE)
                                        .setAction("Retry", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                newsList.setVisibility(View.GONE);
                                                progressBar.setVisibility(View.VISIBLE);
                                                getNews();
                                            }
                                        });
                                snackbar.setActionTextColor(Color.parseColor("#4286f4"));
                                snackbar.show();
                                return;
                            }
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void getCategoryNews(int id) {
        try {
            Retrofit retrofitClient = new Retrofit.Builder()
                    .baseUrl("http://sports1.com.cy/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiInterface service = retrofitClient.create(ApiInterface.class);
            Observable<SportsResponse> output = service.getCategoryNews(id);
            output.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<SportsResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            d.isDisposed();
                        }

                        @Override
                        public void onNext(SportsResponse response) {
                            news.clear();
                            news.addAll(response.getNews());
                            newsList.getAdapter().notifyDataSetChanged();
                            newsList.scrollToPosition(0);
                            newsList.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            if(e instanceof ConnectException || e instanceof UnknownHostException) {
                                progressBar.setVisibility(View.GONE);
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "No connection", Snackbar.LENGTH_INDEFINITE)
                                        .setAction("Retry", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                newsList.setVisibility(View.GONE);
                                                progressBar.setVisibility(View.VISIBLE);
                                                getNews();
                                            }
                                        });
                                snackbar.setActionTextColor(Color.parseColor("#4286f4"));
                                snackbar.show();
                                return;
                            }
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);

        newsList = (RecyclerView) findViewById(R.id.rv_news);
        newsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.sidemenu);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.home, R.string.home);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        navigationView.inflateHeaderView(R.layout.sidemenu_header);
    }

    private void setClickListeners() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                setTitle(item.getTitle());
                switch(item.getItemId()) {
                    case R.id.first_division:
                        newsList.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        getCategoryNews(187);
                        break;
                    case R.id.second_division:
                        newsList.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        getCategoryNews(242);
                        break;
                    case R.id.third_division:
                        newsList.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        getCategoryNews(243);
                        break;
                    case R.id.greece:
                        newsList.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        getCategoryNews(250);
                        break;
                    case R.id.international:
                        newsList.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        getCategoryNews(224);
                        break;
                    case R.id.under:
                        newsList.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        getCategoryNews(241);
                        break;
                    case R.id.other_sports:
                        newsList.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        getCategoryNews(235);
                        break;
                    case R.id.hot_break:
                        newsList.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        getCategoryNews(247);
                        break;
                    case R.id.gossip:
                        newsList.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        getCategoryNews(248);
                        break;
                    case R.id.lifestyle:
                        newsList.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        getCategoryNews(245);
                        break;
                    case R.id.car:
                        newsList.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        getCategoryNews(246);
                        break;
                    case R.id.weird:
                        newsList.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        getCategoryNews(251);
                        break;
                    case R.id.sports_one_tv:
                        newsList.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        getCategoryNews(237);
                        break;
                    case R.id.program:
                        newsList.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        getCategoryNews(236);
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }
}
