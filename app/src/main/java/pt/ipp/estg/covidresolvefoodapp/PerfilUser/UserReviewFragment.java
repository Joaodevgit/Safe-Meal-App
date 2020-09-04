package pt.ipp.estg.covidresolvefoodapp.PerfilUser;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import pt.ipp.estg.covidresolvefoodapp.Adapter.UserReviewAdapter;
import pt.ipp.estg.covidresolvefoodapp.Adapter.UserReviewProfileAdapter;
import pt.ipp.estg.covidresolvefoodapp.Model.FavoriteFirestore;
import pt.ipp.estg.covidresolvefoodapp.Model.ReviewFirestore;
import pt.ipp.estg.covidresolvefoodapp.Model.User;
import pt.ipp.estg.covidresolvefoodapp.R;

public class UserReviewFragment extends Fragment {

    private FirebaseAuth mAuth;

    private RecyclerView mRecyclerViewUserReview;
    private UserReviewProfileAdapter mResAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference reviewRef = db.collection("Review");

    public UserReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        this.mResAdapter.startListening();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mAuth = FirebaseAuth.getInstance();

        View view = inflater.inflate(R.layout.fragment_user_review, container, false);

        Query query = this.reviewRef.whereEqualTo("idUser", this.mAuth.getCurrentUser().getUid());

        FirestoreRecyclerOptions<ReviewFirestore> options = new FirestoreRecyclerOptions.Builder<ReviewFirestore>()
                .setQuery(query, ReviewFirestore.class)
                .build();

        this.mResAdapter = new UserReviewProfileAdapter(options);

        this.mRecyclerViewUserReview = view.findViewById(R.id.m_recyclerview_user_review);

        // Set LayoutManager
        this.mRecyclerViewUserReview.setLayoutManager(new LinearLayoutManager(getContext()));

        this.mRecyclerViewUserReview.setAdapter(this.mResAdapter);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        this.mResAdapter.stopListening();
    }

}