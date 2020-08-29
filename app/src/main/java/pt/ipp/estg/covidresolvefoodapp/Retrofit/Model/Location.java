package pt.ipp.estg.covidresolvefoodapp.Retrofit.Model;

public class Location {

    private String city;
    private String latitude;
    private String longitude;
    private String address;

    public Location(String city, String latitude, String longitude, String address) {
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public Location(String latitude, String longitude) {
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

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Location{" +
                "city='" + city + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
