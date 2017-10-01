package cellock.com.adhelper.Service;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cellock.com.adhelper.Managers.LocationManager;
import cellock.com.adhelper.Models.RawModel.RawInputModel;
import cellock.com.adhelper.Models.RawModel.RawOutputModel;
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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
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

    public void RawApiNoLocationCall() {
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

        callRawService(model);
    }

    private void callRawService(RawInputModel model) {
        try {
            ApiInterface service = retrofitClient.create(ApiInterface.class);

            final PackageManager pm = activity.getPackageManager();
            List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

            String packageNames = "";
            for(int i = 0; i < 10; i++) {
                packageNames += packages.get(i).packageName + ",";
            }

            StringBuilder sb = new StringBuilder(packageNames);
            packageNames = sb.deleteCharAt(sb.length() - 1).toString();

            MediaType mediaType = MediaType.parse("application/json");

            JsonObject stream = new JsonObject();
            stream.addProperty("useragent", model.getStream().getUserAgent());
            stream.addProperty("channel", model.getStream().getChannel());
            stream.addProperty("apps", packageNames);
            stream.addProperty("width", model.getStream().getWidth());
            stream.addProperty("height", model.getStream().getHeight());

            final JsonObject object = new JsonObject();
            object.addProperty("udid", model.getUdId());
            object.addProperty("uakey", model.getUaKey());
            object.addProperty("stream", stream.toString());
            object.addProperty("event", model.getEvent());

            final RequestBody body = RequestBody.create(mediaType, object.toString());

            final Observable<retrofit2.Response<RawOutputModel>> output = service.postRawService(body);

            output.subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.newThread())
                    .subscribe(new Observer<retrofit2.Response<RawOutputModel>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            d.isDisposed();
                        }

                        @Override
                        public void onNext(final retrofit2.Response<RawOutputModel> output) {
                            //checkStatus(output.body());
                            ((Activity)activity).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "Raw service completed successfully.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onError(final Throwable e) {
                            ((Activity)activity).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "Raw service failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onComplete() {
                            Log.d("raw service", "completed");
                        }
                    });
        } catch(Exception e) {
            Log.d("error", e.getMessage());
        }
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
