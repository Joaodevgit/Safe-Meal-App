package pt.ipp.estg.covidresolvefoodapp.DatabaseModels;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RestaurantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Restaurant restaurant);

    @Delete
    void deleteRestaurant(Restaurant restaurant);

    @Query("DELETE FROM restaurant_table WHERE userName=:userName")
    void deleteAllRestaurants(String userName);

    @Query("SELECT * FROM restaurant_table WHERE userName=:userName ORDER BY name ASC")
    LiveData<List<Restaurant>> getAllUserVisitedRestaurants(String userName);

}
