package cellock.com.adhelper.Managers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.patloew.rxlocation.FusedLocation;
import com.patloew.rxlocation.RxLocation;

import java.util.ArrayList;
import java.util.List;

import cellock.com.adhelper.Interfaces.ApiInterface;
import cellock.com.adhelper.Models.RawModel.RawInputModel;
import cellock.com.adhelper.Models.RawModel.RawOutputModel;
import cellock.com.adhelper.Models.SuperClasses.LoadUsersInput;
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
        RxLocation rxLocation = new RxLocation(context);

        if ( ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions((AppCompatActivity)context, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  }, 99 );
            return;
        }

        try {
            FusedLocation location = rxLocation.location();
            Observable loco = location.lastLocation().toObservable();

            loco.subscribe(new Consumer() {
                @Override
                public void accept(@NonNull Object o) throws Exception {
                    model.getStream().setLat(((Location)o).getLatitude());
                    model.getStream().setLon(((Location)o).getLongitude());

                    try {
                        ApiInterface service = retroClient.create(ApiInterface.class);

                        final PackageManager pm = context.getPackageManager();
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
                                        checkStatus(output.body());
                                        ((Activity)context).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(context, "Raw service completed successfully.", Toast.LENGTH_SHORT).show();
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
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void checkStatus(RawOutputModel output) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        boolean serverEmpty = (output.getDateOfBirth() == null
                && output.getEmail() == null
                && output.getGender() == null
                && output.getNationality() == null
                && output.getUdid() == null);

        boolean clientEmpty = (!prefs.contains("email")
                && !prefs.contains("birth")
                && !prefs.contains("gender")
                && !prefs.contains("nationality")
                && !prefs.contains("udid"));

        if(clientEmpty && serverEmpty) return;


        String email = checkStringDifferences(output.getEmail(), prefs.getString("email", ""));
        String birth = checkStringDifferences(output.getDateOfBirth(), prefs.getString("birth", ""));
        String nationality = checkStringDifferences(output.getNationality(), prefs.getString("nationality", ""));
        String gender = checkStringDifferences(output.getGender(), prefs.getString("gender", ""));
        String udid = checkStringDifferences(output.getUdid(), prefs.getString("udid", ""));


        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", email);
        editor.putString("birth", birth);
        editor.putString("nationality", nationality);
        editor.putString("gender", gender);
        editor.putString("udid", udid);

        LoadUsersInput input = new LoadUsersInput();

        input.setEmail(email);
        input.setGender(gender);
        input.setBirth(birth);
        input.setNationality(nationality);
        input.setUdid(udid);

        ApiInterface service = retroClient.create(ApiInterface.class);

        service.postUsersService(input);

    }

    private String checkStringDifferences(String clientString, String serverString) {
        return (serverString == null) ?  clientString : serverString;
    }
}
