package pt.ipp.estg.covidresolvefoodapp.Retrofit.Model;

public class Location {

    private String city;
    private String latitude;
    private String longitude;

    public Location(String city, String latitude, String longitude) {
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Location{" +
                "city='" + city + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
