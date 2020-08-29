package pt.ipp.estg.covidresolvefoodapp.Retrofit.Model;

import java.util.List;

public class RestaurantInfoRetro {

    private String id;
    private String name;
    private String cuisines;
    private String timings;
    private String thumb;
    private Location location;
    private UserRating user_rating;
    private List<String> establishment;

    public RestaurantInfoRetro(String id, String name, String cuisines, String timings, String thumb, Location location, UserRating user_rating, List<String> establishment) {
        this.id = id;
        this.name = name;
        this.cuisines = cuisines;
        this.timings = timings;
        this.thumb = thumb;
        this.location = location;
        this.user_rating = user_rating;
        this.establishment = establishment;
    }

    public RestaurantInfoRetro(String name, String thumb, Location location) {
        this.name = name;
        this.thumb = thumb;
        this.location = location;
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

    public String getTimings() {
        return timings;
    }

    public String getThumb() {
        return thumb;
    }

    public Location getLocation() {
        return location;
    }

    public UserRating getUser_rating() {
        return user_rating;
    }

    public List<String> getEstablishment() {
        return establishment;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @Override
    public String toString() {
        return "RestaurantInfoRetro{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cuisines='" + cuisines + '\'' +
                ", timings='" + timings + '\'' +
                ", thumb='" + thumb + '\'' +
                ", location=" + location +
                ", user_rating=" + user_rating +
                ", establishment=" + establishment +
                '}';
    }
}
