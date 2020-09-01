package pt.ipp.estg.covidresolvefoodapp.Model;


import com.google.firebase.Timestamp;

import java.time.Instant;

public class ReviewFirestore {

    private float avaliation;
    private String idUser;
    private int idRestaurant;
    private String contentReview;
    private Timestamp timestamp;

    public ReviewFirestore() {

    }

    public ReviewFirestore(float avaliation, String idUser, int idRestaurant, String contentReview, Timestamp timestamp) {
        this.avaliation = avaliation;
        this.idUser = idUser;
        this.idRestaurant = idRestaurant;
        this.contentReview = contentReview;
        this.timestamp = timestamp;
    }

    public float getAvaliation() {
        return avaliation;
    }

    public void setAvaliation(float avaliation) {
        this.avaliation = avaliation;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
