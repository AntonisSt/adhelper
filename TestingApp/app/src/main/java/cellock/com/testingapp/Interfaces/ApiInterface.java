package cellock.com.testingapp.Interfaces;

import cellock.com.testingapp.Models.SportsResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by AntonisS on 4/3/2017.
 */

public interface ApiInterface {
    @GET("MobApp/index.php?")
    Observable<SportsResponse> getCategoryNews(@Query("c") int id);
    @GET("MobApp/index.php")
    Observable<SportsResponse> getHomeNews();
}
