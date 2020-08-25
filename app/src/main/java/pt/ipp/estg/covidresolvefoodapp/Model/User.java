package pt.ipp.estg.covidresolvefoodapp.Model;

public class User {

    private String email;
    private double rating;

    public User(String email, double rating) {
        this.email = email;
        this.rating = rating;
    }

    public String getEmail() {
        return email;
    }

    public double getRating() {
        return rating;
    }

}
