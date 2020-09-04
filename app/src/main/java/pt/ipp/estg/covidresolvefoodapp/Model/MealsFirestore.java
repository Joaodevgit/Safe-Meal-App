package pt.ipp.estg.covidresolvefoodapp.Model;

public class MealsFirestore {

    private String idRestaurant;
    private String restaurantName;
    private String idUser;
    private String contentMeals;
    private String thumb;

    public MealsFirestore() {

    }

    public MealsFirestore(String idRestaurant, String restaurantName, String idUser, String contentMeals, String thumb) {
        this.idRestaurant = idRestaurant;
        this.restaurantName = restaurantName;
        this.idUser = idUser;
        this.contentMeals = contentMeals;
        this.thumb = thumb;
    }

    public String getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(String idRestaurant) {
        this.idRestaurant = idRestaurant;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getContentMeals() {
        return contentMeals;
    }

    public void setContentMeals(String contentMeals) {
        this.contentMeals = contentMeals;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
