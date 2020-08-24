package pt.ipp.estg.covidresolvefoodapp.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pt.ipp.estg.covidresolvefoodapp.R;

public class CreateAccount extends Fragment {

    private static final String TAG = "EmailPassword";

    private EditText mNewUserEmail;
    private EditText mNewUserPassword;
    private Button mCreatNewUser;

    private OnFragmentCreateAccountInteractionListener mListener;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    public CreateAccount() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // [START initialize_auth]
        // Initialize Firebase Auth
        this.mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);

        this.mNewUserEmail = view.findViewById(R.id.newUserEmail);
        this.mNewUserPassword = view.findViewById(R.id.newUserPassword);
        this.mCreatNewUser = view.findViewById(R.id.creatNewUser);

        // [START initialize_auth]
        // Initialize Firebase Auth
        this.mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]


        this.mCreatNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(mNewUserEmail.getText().toString(), mNewUserPassword.getText().toString());
            }
        });

        return view;
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);

        if (!validateForm()) {
            return;
        }

        // [START create_user_with_email]
        this.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            mListener.onFragmentCreateAccountInteractionMenu();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        // [END create_user_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = this.mNewUserEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            this.mNewUserEmail.setError("Required.");
            valid = false;
        } else {
            this.mNewUserEmail.setError(null);
        }

        String password = this.mNewUserPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            this.mNewUserPassword.setError("Required.");
            valid = false;
        } else {
            this.mNewUserPassword.setError(null);
        }

        return valid;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreateAccount.OnFragmentCreateAccountInteractionListener) {
            this.mListener = (CreateAccount.OnFragmentCreateAccountInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentRecipeSearchInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public interface OnFragmentCreateAccountInteractionListener {
        void onFragmentCreateAccountInteractionMenu();
    }
}