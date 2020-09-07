package pt.ipp.estg.covidresolvefoodapp.Retrofit;

import pt.ipp.estg.covidresolvefoodapp.Retrofit.Model.RestaurantInfoRetro;
import pt.ipp.estg.covidresolvefoodapp.Retrofit.Model.RestaurantsRetro;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ZomatoAPI {

    @Headers("user-key: e11bb0aef95db8820287691d71f0e616")
    @GET("search")
    Call<RestaurantsRetro> searchRestaurants(@Query("lat") double latitude,
                                             @Query("lon") double longitude,
                                             @Query("cuisines") String cuisines,
                                             @Query("establishment_type") String estabelecimento);

    @Headers("user-key: e11bb0aef95db8820287691d71f0e616")
    @GET("restaurant")
    Call<RestaurantInfoRetro> getRestaurant(@Query("res_id") int res_id);

    @Headers("user-key: e11bb0aef95db8820287691d71f0e616")
    @GET("search")
    Call<RestaurantsRetro> nearbyRestaurants(@Query("lat") double latitude,
                                             @Query("lon") double longitude,
                                             @Query("sort") String sort,
                                             @Query("order") String order);
}
