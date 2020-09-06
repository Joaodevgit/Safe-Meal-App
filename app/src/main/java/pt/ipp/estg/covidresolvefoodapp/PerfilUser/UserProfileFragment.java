package pt.ipp.estg.covidresolvefoodapp.PerfilUser;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import pt.ipp.estg.covidresolvefoodapp.R;

public class UserProfileFragment extends Fragment {

    private FirebaseAuth mAuth;

    private ImageView profileImg;

    private TextView userNameTxtView;
    private TextView totalUserMeals;

    private Button btnFavorRest;
    private Button btnVisitRest;
    private Button btnUserReview;
    private Button mButtonMeals;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference mealsRef = db.collection("Meals");

    private OnFragmentUserProfileInteractionListener mListener;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.mAuth = FirebaseAuth.getInstance();

        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);


        // Text Views
        userNameTxtView = view.findViewById(R.id.restaurantName);
        userNameTxtView.setText(mListener.onFragmentUserName());

        totalUserMeals = view.findViewById(R.id.textViewTotalMeals);

        this.mealsRef.whereEqualTo("idUser", this.mAuth.getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                totalUserMeals.setText("Nº total de refeições efetuadas: " + queryDocumentSnapshots.size());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e);
            }
        });

        // Image Views
        profileImg = view.findViewById(R.id.imageViewProfile);
        profileImg.setImageResource(R.drawable.ic_user_profile_img);

        btnFavorRest = view.findViewById(R.id.buttonFavoriteRestaurants);
        btnVisitRest = view.findViewById(R.id.buttonUserVisitedRestaurants);
        btnUserReview = view.findViewById(R.id.buttonUserReviews);
        this.mButtonMeals = view.findViewById(R.id.button_user_meals);

        btnFavorRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentFavoriteRestaurantClick();
            }
        });

        btnVisitRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentVisitedRestaurantClick();
            }
        });

        btnUserReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentUserReviewClick();
            }
        });

        this.mButtonMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentMealsClick();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentUserProfileInteractionListener) {
            this.mListener = (OnFragmentUserProfileInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentUserProfileInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public interface OnFragmentUserProfileInteractionListener {
        void onFragmentFavoriteRestaurantClick();

        void onFragmentVisitedRestaurantClick();

        void onFragmentUserReviewClick();

        void onFragmentMealsClick();

        String onFragmentUserName();
    }
}