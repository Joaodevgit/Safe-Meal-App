package pt.ipp.estg.covidresolvefoodapp.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ZomatoAPI {

    @Headers("user-key: e11bb0aef95db8820287691d71f0e616")
    @GET("search")
    Call<RestaurantsRetro> searchRestaurants(@Query("lat") double latitude,
                                             @Query("lon") double longitude,
                                             @Query("1") String cuisines,
                                             @Query("establishment_type") String estabelecimento);

}