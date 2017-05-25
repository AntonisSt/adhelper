package cellock.com.adhelper.Interfaces;

import cellock.com.adhelper.Models.RawModel.RawInputModel;
import cellock.com.adhelper.Models.RawModel.RawOutputModel;
import cellock.com.adhelper.Models.SuperClasses.AdInput;
import cellock.com.adhelper.Models.SuperClasses.AdOutput;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

/**
 * Created by AntonisS on 1/3/2017.
 */

public interface ApiInterface {
    @Headers("Content-Type: application/json")
    @POST("adv")
    Observable<AdOutput> postAdService(@Body AdInput input);

    @Headers({
        "Content-Type: application/json",
        "cache-control: no-cache",
        "ads-api-key: 16CDF36F65767"})

    @POST("raw")
    Observable<RawOutputModel> postRawService(@Body RequestBody input);
}