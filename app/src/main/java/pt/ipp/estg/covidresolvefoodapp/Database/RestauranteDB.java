package pt.ipp.estg.covidresolvefoodapp.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pt.ipp.estg.covidresolvefoodapp.DatabaseModels.Restaurant;
import pt.ipp.estg.covidresolvefoodapp.DatabaseModels.RestaurantDao;

@Database(entities = {Restaurant.class}, version = 1, exportSchema = false)
public abstract class RestauranteDB extends RoomDatabase {

    private static final int NUMBER_OF_THREADS=4;
    private static volatile RestauranteDB INSTANCE;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract RestaurantDao restaurantDao();

    public static RestauranteDB getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (RestauranteDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RestauranteDB.class, "restaurant_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

/*
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(INSTANCE).execute();

        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private RestaurantDao restaurantDao;

        private PopulateDbAsyncTask(RestauranteDB db) {
            this.restaurantDao = db.restaurantDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            restaurantDao.insert(new Restaurant("Title 1", "User1", "Porto", "Rua do Porto", ""));
            restaurantDao.insert(new Restaurant("Title 2", "User2", "Braga", "Rua de Braga", ""));
            restaurantDao.insert(new Restaurant("Title 3", "User 3", "Lisboa", "Rua de Lisboa", ""));
            return null;
        }
    }
*/

}
