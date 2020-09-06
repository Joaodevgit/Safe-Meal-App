package pt.ipp.estg.covidresolvefoodapp.MainActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pt.ipp.estg.covidresolvefoodapp.R;

public class LogIn extends Fragment {

    private static final String TAG = "EmailPassword";

    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mButtonSign;
    private Button mButtonCreat;
    private ImageView mImageLogo;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private OnFragmentLogInInteractionListener mListener;

    public LogIn() {
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
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);

        // Views
        this.mStatusTextView = view.findViewById(R.id.status);
        this.mDetailTextView = view.findViewById(R.id.detail);
        this.mEmailField = view.findViewById(R.id.fieldEmail);
        this.mPasswordField = view.findViewById(R.id.fieldPassword);


        // Buttons
        this.mButtonSign = view.findViewById(R.id.emailSignInButton);
        this.mButtonCreat = view.findViewById(R.id.emailCreateAccountButton);

        //Image View
        this.mImageLogo = view.findViewById(R.id.image_view_logo);
        this.mImageLogo.setImageResource(R.drawable.safe_meal_logo);

        this.mButtonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
            }
        });

        this.mButtonCreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentCreateAccountInteractionLogIn();
            }
        });


        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);


        return view;
    }


    private void updateUI(FirebaseUser user) {
        if (user != null) {
            mListener.onFragmentLogInInteraction();
        } else {
            this.mStatusTextView.setText(R.string.signed_out);
            this.mDetailTextView.setText(null);
        }
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);

        if (!validateForm()) {
            return;
        }

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();

                            mListener.onFragmentLogInInteraction();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Autenticação Falhou / Sem ligação à rede.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            mStatusTextView.setText(R.string.auth_failed);
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = this.mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            this.mEmailField.setError("Required.");
            valid = false;
        } else {
            this.mEmailField.setError(null);
        }

        String password = this.mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            this.mPasswordField.setError("Required.");
            valid = false;
        } else {
            this.mPasswordField.setError(null);
        }

        return valid;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentLogInInteractionListener) {
            this.mListener = (OnFragmentLogInInteractionListener) context;
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

    public interface OnFragmentLogInInteractionListener {
        void onFragmentLogInInteraction();

        void onFragmentCreateAccountInteractionLogIn();
    }

}