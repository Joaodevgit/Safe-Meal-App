package pt.ipp.estg.covidresolvefoodapp.Retrofit.Model;

public class Location {

    private String city;

    public Location(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return "Location{" +
                "city='" + city + '\'' +
                '}';
    }
}
