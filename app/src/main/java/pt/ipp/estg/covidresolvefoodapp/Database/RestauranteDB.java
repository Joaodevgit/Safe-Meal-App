package pt.ipp.estg.covidresolvefoodapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pt.ipp.estg.covidresolvefoodapp.DatabaseModels.Restaurant;
import pt.ipp.estg.covidresolvefoodapp.DatabaseModels.RestaurantDao;

@Database(entities = {Restaurant.class}, version = 2, exportSchema = false)
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
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
