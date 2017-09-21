package cellock.com.adhelper.Service;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import cellock.com.adhelper.Managers.LocationManager;
import cellock.com.adhelper.Models.RawModel.RawInputModel;
import cellock.com.adhelper.Models.Stream;
import cellock.com.adhelper.Models.SuperClasses.AdInput;
import cellock.com.adhelper.Models.SuperClasses.AdOutput;
import cellock.com.adhelper.Interfaces.ApiInterface;
import cellock.com.adhelper.Models.Banner.BannerInput;
import cellock.com.adhelper.Models.Banner.BannerOutput;
import cellock.com.adhelper.Models.Video.VideoOutput;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by AntonisS on 11/2/2017.
 */

public class AdService {

    private AdInput inputModel;
    private AdOutput outputModel;
    private Retrofit retrofitClient;
    private Activity activity;

    public AdService(Activity activity, String uaKey, ImageView image)
    {
        this.activity = activity;
        inputModel = new BannerInput(activity);
        inputModel.setUaKey(uaKey);
        outputModel = new BannerOutput(image);


        OkHttpClient.Builder client = new OkHttpClient.Builder();

        retrofitClient = new Retrofit.Builder()
                .baseUrl(inputModel.getBaseUrl())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public AdService(Activity activity, String uaKey, RelativeLayout videoParent)
    {
        this.activity = activity;
        inputModel = new BannerInput(activity);
        inputModel.setUaKey(uaKey);
        outputModel = new VideoOutput(videoParent);


        OkHttpClient.Builder client = new OkHttpClient.Builder();

        retrofitClient = new Retrofit.Builder()
                .baseUrl(inputModel.getBaseUrl())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public void getAdService() {
        AdApiCall();
    }

    public void RawApiCall() {
        RawInputModel model =  new RawInputModel();

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        model.setUdId(inputModel.getUdId());
        model.setUaKey(inputModel.getUaKey());
        model.setEvent(1 + "");
        Stream stream = new Stream();

        stream.setUserAgent("");
        stream.setChannel("android sdk");
        stream.setHeight(metrics.heightPixels + "");
        stream.setWidth(metrics.widthPixels + "");
        model.setStream(stream);
        new LocationManager(model, retrofitClient, activity);
    }


    private void AdApiCall() {
        try {
            final ApiInterface service = retrofitClient.create(ApiInterface.class);

            final Observable<Response<AdOutput>> output = service.postAdService(inputModel);

            output.subscribeOn(Schedulers.newThread())
                    .timeout(10, TimeUnit.SECONDS)
                    .observeOn(Schedulers.newThread())
                    .subscribe(new Observer<Response<AdOutput>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            d.isDisposed();
                        }

                        @Override
                        public void onNext(final Response<AdOutput> adOutput) {
                            outputModel.setContentUrl(adOutput.body().getContentUrl());
                            outputModel.setLinkUrl(adOutput.body().getLinkUrl());
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "Ad service completed successfully", Toast.LENGTH_SHORT).show();
                                    outputModel.setResult();
                                }
                            });
                            inputModel.setAdKey(adOutput.body().getAdKey());
                            inputModel.setCamKey(adOutput.body().getCamKey());
                            RawApiCall();
                        }

                        @Override
                        public void onError(final Throwable e) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "Ad service failed with exception:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    outputModel.setResult();
                                }
                            });
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch(Exception e) {
            Log.d("error", e.getMessage());
        }


    }
}
