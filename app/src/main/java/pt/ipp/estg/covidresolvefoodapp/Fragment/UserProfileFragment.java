package pt.ipp.estg.covidresolvefoodapp.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import pt.ipp.estg.covidresolvefoodapp.R;

public class UserProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private TextView userNameTxtView;
    private ImageView profileImg;
    private TextView totalUserMeals;
    private Button btnFavorRest;
    private Button btnVisitRest;
    private Button btnUserReview;

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
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);


        // Text Views
        userNameTxtView = view.findViewById(R.id.restaurantName);
        userNameTxtView.setText(mListener.onFragmentUserName());

        totalUserMeals = view.findViewById(R.id.textViewTotalMeals);
        // Falta fazer a contagem das refeições efetuadas pelo cliente
        totalUserMeals.setText("Nº total de refeições efetuadas: 6");

        // Image Views
        profileImg = view.findViewById(R.id.imageViewProfile);
        profileImg.setImageResource(R.drawable.ic_user_profile_img);

        btnFavorRest = view.findViewById(R.id.buttonFavoriteRestaurants);
        btnVisitRest = view.findViewById(R.id.buttonUserVisitedRestaurants);
        btnUserReview = view.findViewById(R.id.buttonUserReviews);

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

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UserProfileFragment.OnFragmentUserProfileInteractionListener) {
            this.mListener = (UserProfileFragment.OnFragmentUserProfileInteractionListener) context;
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

        String onFragmentUserName();
    }
}