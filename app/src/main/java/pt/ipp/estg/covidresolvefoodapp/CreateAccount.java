package pt.ipp.estg.covidresolvefoodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccount extends AppCompatActivity {

    private static final String TAG = "EmailPassword";

    private EditText mNewUserEmail;
    private EditText mNewUserPassword;
    private Button mCreatNewUser;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        this.mNewUserEmail = findViewById(R.id.newUserEmail);
        this.mNewUserPassword = findViewById(R.id.newUserPassword);
        this.mCreatNewUser = findViewById(R.id.creatNewUser);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]


        this.mCreatNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createAccount(mNewUserEmail.getText().toString(), mNewUserPassword.getText().toString());

                Intent intent = new Intent(CreateAccount.this, Login.class);
                startActivity(intent);
            }
        });

    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccount.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        // [END create_user_with_email]
    }


    private boolean validateForm() {
        boolean valid = true;

        String email = mNewUserEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mNewUserEmail.setError("Required.");
            valid = false;
        } else {
            mNewUserEmail.setError(null);
        }

        String password = mNewUserPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mNewUserPassword.setError("Required.");
            valid = false;
        } else {
            mNewUserPassword.setError(null);
        }

        return valid;
    }

}