package pt.ipp.estg.covidresolvefoodapp.Retrofit.Model;

public class RestaurantRetro {

    private RestaurantInfoRetro restaurant;

    public RestaurantRetro(RestaurantInfoRetro restaurant) {
        this.restaurant = restaurant;
    }

    public RestaurantInfoRetro getRestaurant() {
        return restaurant;
    }

    @Override
    public String toString() {
        return "RestaurantRetro{" +
                "restaurant=" + restaurant +
                '}';
    }
}
