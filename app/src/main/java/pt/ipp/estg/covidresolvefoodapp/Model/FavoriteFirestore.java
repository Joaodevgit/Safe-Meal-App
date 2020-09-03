package pt.ipp.estg.covidresolvefoodapp.Model;

public class FavoriteFirestore {

    private String idUser;
    private int idRestaurant;

    public FavoriteFirestore() {

    }

    public FavoriteFirestore(String idUser, int idRestaurant) {
        this.idUser = idUser;
        this.idRestaurant = idRestaurant;
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
}
