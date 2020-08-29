package pt.ipp.estg.covidresolvefoodapp.Retrofit.Model;

import java.util.List;

public class NearbyRestaurant {

    private List<RestaurantRetro> nearby_restaurants;

    public NearbyRestaurant(List<RestaurantRetro> nearby_restaurants) {
        this.nearby_restaurants = nearby_restaurants;
    }

    public List<RestaurantRetro> getNearby_restaurants() {
        return nearby_restaurants;
    }

    @Override
    public String toString() {
        return "NearbyRestaurant{" +
                "nearby_restaurants=" + nearby_restaurants +
                '}';
    }
}
