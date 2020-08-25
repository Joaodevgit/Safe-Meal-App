package pt.ipp.estg.covidresolvefoodapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class UserProfileAct extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar myToolbar;
    private TextView userNameTxtView;
    private ImageView profileImg;
    private TextView totalUserMeals;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();

        // Text Views
        userNameTxtView = findViewById(R.id.userEmail);
        //Toast.makeText(getApplicationContext(), FirebaseAuth.getInstance().getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
        userNameTxtView.setText("Email: " + mAuth.getCurrentUser().getEmail());

        totalUserMeals = findViewById(R.id.textViewTotalMeals);
        // Falta fazer a contagem das refeições efetuadas pelo cliente
        totalUserMeals.setText("Nº total de refeições efetuadas: 6");

        // Image Views
        profileImg = findViewById(R.id.imageViewProfile);
        profileImg.setImageResource(R.drawable.ic_user_profile_img);


        this.myToolbar = findViewById(R.id.toolbarUser);
        setSupportActionBar(this.myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}