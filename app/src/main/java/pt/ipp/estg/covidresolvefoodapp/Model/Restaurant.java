package pt.ipp.estg.covidresolvefoodapp.Model;

public class Restaurant {

    private String name;

    public Restaurant(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    private String lat;
    private String lon;
    private String title;
    private String imageURL;

    public Restaurant(String lat, String lon, String title, String imageURL) {
        this.lat = lat;
        this.lon = lon;
        this.title = title;
        this.imageURL = imageURL;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getTitle() {
        return title;
    }

    public String getImageURL() {
        return imageURL;
    }


}
