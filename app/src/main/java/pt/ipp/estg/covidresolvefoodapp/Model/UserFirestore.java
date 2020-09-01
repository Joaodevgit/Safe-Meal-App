package pt.ipp.estg.covidresolvefoodapp.Model;

import com.google.firebase.firestore.Exclude;

public class UserFirestore {

    private String idUser;
    private String anonymous;
    private String userEmail;

    public UserFirestore() {
    }

    public UserFirestore(String idUser, String anonymous, String userEmail) {
        this.idUser = idUser;
        this.anonymous = anonymous;
        this.userEmail = userEmail;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
