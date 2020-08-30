package pt.ipp.estg.covidresolvefoodapp.Model;

import com.google.firebase.firestore.Exclude;

public class UserFirestore {

    private String anonymous;
    private String userEmail;

    public UserFirestore(String anonymous, String userEmail) {
        this.anonymous = anonymous;
        this.userEmail = userEmail;
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
