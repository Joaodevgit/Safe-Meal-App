package pt.ipp.estg.covidresolvefoodapp.DatabaseModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class RestaurantViewModel extends AndroidViewModel {

    private RestaurantRepository repository;
    private LiveData<List<Restaurant>> allVisitedRestaurants;

    public RestaurantViewModel(@NonNull Application application) {
        super(application);
        repository = new RestaurantRepository(application);
        allVisitedRestaurants = repository.getAllVisitedRestaurants();
    }

    public void insert(Restaurant restaurant) {
        repository.insert(restaurant);
    }

    public void delete(Restaurant restaurant) {
        repository.delete(restaurant);
    }

    public void deleteAllVisitedRestaurants() {
        repository.deleteAllVisitedRestaurants();
    }

    public LiveData<List<Restaurant>> getAllVisitedRestaurants() {
        return allVisitedRestaurants;
    }
}
