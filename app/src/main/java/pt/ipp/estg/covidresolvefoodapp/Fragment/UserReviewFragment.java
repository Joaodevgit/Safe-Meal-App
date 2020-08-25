package pt.ipp.estg.covidresolvefoodapp.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import pt.ipp.estg.covidresolvefoodapp.Adapter.UserReviewAdapter;
import pt.ipp.estg.covidresolvefoodapp.Model.Restaurant;
import pt.ipp.estg.covidresolvefoodapp.Model.User;
import pt.ipp.estg.covidresolvefoodapp.R;

public class UserReviewFragment extends Fragment {

    private RecyclerView mRecyclerViewUserReview;
    private UserReviewAdapter mResAdapter;
    private OnFragmentUserReviewInteractionListener mListener;


    public UserReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_review, container, false);

        // Create contact list
        ArrayList<User> users = createUsersList(5);

        // Create contacts adapter
        mResAdapter = new UserReviewAdapter(getContext(), users);

        // Set RecyclerView adapter
        mRecyclerViewUserReview = view.findViewById(R.id.mRecyclerViewUserReview);
        mRecyclerViewUserReview.setAdapter(mResAdapter);

        // Set LayoutManager
        mRecyclerViewUserReview.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }


    private ArrayList<User> createUsersList(int qty) {
        ArrayList<User> users = new ArrayList<>();
        User newUser;
        for (int i = 0; i < qty; i++) {
            newUser = new User("Email" + 1, i + 0.5);
            users.add(newUser);
        }
        return users;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UserReviewFragment.OnFragmentUserReviewInteractionListener) {
            this.mListener = (UserReviewFragment.OnFragmentUserReviewInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentUserReviewInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public interface OnFragmentUserReviewInteractionListener {
        //String onFragmentUserReviewInfo(String email);
    }
}