package pt.ipp.estg.covidresolvefoodapp.Retrofit;

import java.util.List;

public class RestaurantsRetro {

    private List<RestaurantRetro> restaurants;

    public RestaurantsRetro(List<RestaurantRetro> restaurants) {
        this.restaurants = restaurants;
    }

    public List<RestaurantRetro> getRestaurants() {
        return restaurants;
    }

    @Override
    public String toString() {
        return "RestaurantsRetro{" +
                "restaurants=" + restaurants +
                '}';
    }
}
