package pt.ipp.estg.covidresolvefoodapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class UserProfile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        this.mAuth = FirebaseAuth.getInstance();

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