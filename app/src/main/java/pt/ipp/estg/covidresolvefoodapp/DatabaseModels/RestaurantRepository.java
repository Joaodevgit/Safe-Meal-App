package pt.ipp.estg.covidresolvefoodapp.DatabaseModels;

import android.app.Application;
import android.os.AsyncTask;

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
        //new InsertRestaurantAsyncTask(restaurantDao).execute(restaurant);
    }

    public void delete(Restaurant restaurant) {
        RestauranteDB.databaseWriteExecutor.execute(() -> {
            restaurantDao.deleteRestaurant(restaurant);
        });
        //new DeleteRestaurantAsyncTask(restaurantDao).execute(restaurant);
    }

    public void deleteAllVisitedRestaurants() {
        RestauranteDB.databaseWriteExecutor.execute(() -> {
            restaurantDao.deleteAllRestaurants(mAuth.getCurrentUser().getEmail());
        });
        //new DeleteAllRestaurantsAsyncTask(restaurantDao).execute();
    }

    public LiveData<List<Restaurant>> getAllVisitedRestaurants() {
        return allVisitedRestaurants;
    }


   /* private static class InsertRestaurantAsyncTask extends AsyncTask<Restaurant, Void, Void> {

        private RestaurantDao restaurantDao;

        private InsertRestaurantAsyncTask(RestaurantDao restaurantDao) {
            this.restaurantDao = restaurantDao;
        }

        @Override
        protected Void doInBackground(Restaurant... restaurants) {
            restaurantDao.insert(restaurants[0]);
            return null;
        }
    }

    private static class DeleteRestaurantAsyncTask extends AsyncTask<Restaurant, Void, Void> {
        private RestaurantDao restaurantDao;

        public DeleteRestaurantAsyncTask(RestaurantDao restaurantDao) {
            this.restaurantDao = restaurantDao;
        }

        @Override
        protected Void doInBackground(Restaurant... restaurants) {
            restaurantDao.deleteRestaurant(restaurants[0]);
            return null;
        }
    }

    private static class DeleteAllRestaurantsAsyncTask extends AsyncTask<Void, Void, Void> {
        private RestaurantDao restaurantDao;
        private FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //private AuthViewModelBase mAuth;

        private DeleteAllRestaurantsAsyncTask(RestaurantDao restaurantDao) {
            this.restaurantDao = restaurantDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            restaurantDao.deleteAllRestaurants(mAuth.getCurrentUser().getEmail());
            return null;
        }
    }*/
}
