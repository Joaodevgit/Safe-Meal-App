package pt.ipp.estg.covidresolvefoodapp.Model;

public class FavoriteFirestore {

    private String idUser;
    private String idRestaurant;
    private String nameRestaurant;
    private String city;
    private String cuisines;
    private String thumb;
    private String latitude;
    private String longitude;

    public FavoriteFirestore() {

    }

    public FavoriteFirestore(String idUser, String idRestaurant, String nameRestaurant, String city, String cuisines, String thumb, String latitude, String longitude) {
        this.idUser = idUser;
        this.idRestaurant = idRestaurant;
        this.nameRestaurant = nameRestaurant;
        this.city = city;
        this.cuisines = cuisines;
        this.thumb = thumb;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public FavoriteFirestore(String nameRestaurant, String city, String thumb, String latitude, String longitude) {
        this.nameRestaurant = nameRestaurant;
        this.city = city;
        this.thumb = thumb;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(String idRestaurant) {
        this.idRestaurant = idRestaurant;
    }

    public String getNameRestaurant() {
        return nameRestaurant;
    }

    public void setNameRestaurant(String nameRestaurant) {
        this.nameRestaurant = nameRestaurant;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCuisines() {
        return cuisines;
    }

    public void setCuisines(String cuisines) {
        this.cuisines = cuisines;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
