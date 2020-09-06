package pt.ipp.estg.covidresolvefoodapp.DatabaseModels;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import pt.ipp.estg.covidresolvefoodapp.Database.RestauranteDB;

public class RestaurantRepository {

    private RestaurantDao restaurantDao;
    private LiveData<List<Restaurant>> allVisitedRestaurants;
    private FirebaseAuth mAuth;

    public RestaurantRepository(Application application) {
        RestauranteDB database = RestauranteDB.getInstance(application);
        this.restaurantDao = database.restaurantDao();
        this.mAuth = FirebaseAuth.getInstance();
        this.allVisitedRestaurants = restaurantDao.
                getAllUserVisitedRestaurants(mAuth.getCurrentUser().getEmail());
    }

    public void insert(final Restaurant restaurant) {
        RestauranteDB.databaseWriteExecutor.execute(() -> {
            restaurantDao.insert(restaurant);
        });
    }

    public void delete(Restaurant restaurant) {
        RestauranteDB.databaseWriteExecutor.execute(() -> {
            restaurantDao.deleteRestaurant(restaurant);
        });
    }

    public void deleteAllVisitedRestaurants() {
        RestauranteDB.databaseWriteExecutor.execute(() -> {
            restaurantDao.deleteAllRestaurants(mAuth.getCurrentUser().getEmail());
        });
    }

    public LiveData<List<Restaurant>> getAllVisitedRestaurants() {
        return allVisitedRestaurants;
    }
}
