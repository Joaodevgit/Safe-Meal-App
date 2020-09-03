package pt.ipp.estg.covidresolvefoodapp.Model;

public class FavoriteFirestore {

    private String idUser;
    private String idRestaurant;
    private String nameRestaurant;
    private String city;
    private String cuisines;
    private String thumb;

    public FavoriteFirestore() {

    }

    public FavoriteFirestore(String idUser, String idRestaurant, String nameRestaurant, String city, String cuisines, String thumb) {
        this.idUser = idUser;
        this.idRestaurant = idRestaurant;
        this.nameRestaurant = nameRestaurant;
        this.city = city;
        this.cuisines = cuisines;
        this.thumb = thumb;
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

    @Override
    public String toString() {
        return "FavoriteFirestore{" +
                "idUser='" + idUser + '\'' +
                ", idRestaurant=" + idRestaurant +
                ", nameRestaurant='" + nameRestaurant + '\'' +
                ", city='" + city + '\'' +
                ", cuisines='" + cuisines + '\'' +
                ", thumb='" + thumb + '\'' +
                '}';
    }
}
