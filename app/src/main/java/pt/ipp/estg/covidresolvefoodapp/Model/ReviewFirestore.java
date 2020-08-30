package pt.ipp.estg.covidresolvefoodapp.Model;

public class ReviewFirestore {

    private int avaliation;
    private String email;
    private int idRestaurant;
    private String contentReview;

    public ReviewFirestore(int avaliation, String email, int idRestaurant, String contentReview) {
        this.avaliation = avaliation;
        this.email = email;
        this.idRestaurant = idRestaurant;
        this.contentReview = contentReview;
    }

    public int getAvaliation() {
        return avaliation;
    }

    public void setAvaliation(int avaliation) {
        this.avaliation = avaliation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(int idRestaurant) {
        this.idRestaurant = idRestaurant;
    }

    public String getContentReview() {
        return contentReview;
    }

    public void setContentReview(String contentReview) {
        this.contentReview = contentReview;
    }
}
