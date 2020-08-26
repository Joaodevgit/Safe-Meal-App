package pt.ipp.estg.covidresolvefoodapp.Retrofit;

public class RestaurantInfoRetro {

    private String id;
    private String name;
    private String cuisines;
    private String currency;

    public RestaurantInfoRetro(String id, String name, String cuisines, String currency) {
        this.id = id;
        this.name = name;
        this.cuisines = cuisines;
        this.currency = currency;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCuisines() {
        return cuisines;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return "RestaurantRetro{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cuisines='" + cuisines + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
