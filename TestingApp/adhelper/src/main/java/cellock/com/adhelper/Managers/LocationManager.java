package cellock.com.adhelper.Managers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.patloew.rxlocation.FusedLocation;
import com.patloew.rxlocation.RxLocation;

import cellock.com.adhelper.Interfaces.ApiInterface;
import cellock.com.adhelper.Models.RawModel.RawInputModel;
import cellock.com.adhelper.Models.RawModel.RawOutputModel;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;

/**
 * Created by AntonisS on 18/2/2017.
 */

public class LocationManager {
    private Retrofit retroClient;
    private RawInputModel model;
    private Context context;

    public LocationManager(RawInputModel model, Retrofit retroClient, Context context) {
        this.model = model;
        this.retroClient = retroClient;
        this.context = context;
        createLocationRequest();
    }

    private void createLocationRequest() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(60000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);

        RxLocation rxLocation = new RxLocation(context);

        if ( ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions((AppCompatActivity)context, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  }, 99 );
            return;
        }

        try {
            FusedLocation location = rxLocation.location();
            Location loco = location.lastLocation().blockingGet();

            model.getStream().setLat(loco.getLatitude());
            model.getStream().setLon(loco.getLongitude());

            try {
                ApiInterface service = retroClient.create(ApiInterface.class);


                MediaType mediaType = MediaType.parse("application/json");

                JsonObject stream = new JsonObject();
                stream.addProperty("useragent", model.getStream().getUserAgent());
                stream.addProperty("channel", model.getStream().getChannel());
                stream.addProperty("width", model.getStream().getWidth());
                stream.addProperty("height", model.getStream().getHeight());

                JsonObject object = new JsonObject();
                object.addProperty("udid", model.getUdId());
                object.addProperty("uakey", model.getUaKey());
                object.addProperty("stream", stream.toString());
                object.addProperty("event", model.getEvent());

                final Observable<RawOutputModel> output = service.postRawService(RequestBody.create(mediaType, object.toString()));

                output.subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .subscribe(new Observer<RawOutputModel>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                d.isDisposed();
                            }

                            @Override
                            public void onNext(RawOutputModel output) {
                                ((Activity)context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "Raw service completed successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onError(final Throwable e) {
                                ((Activity)context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "Raw service failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}