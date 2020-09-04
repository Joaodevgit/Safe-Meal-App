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

import pt.ipp.estg.covidresolvefoodapp.Adapter.FavResAdapter;
import pt.ipp.estg.covidresolvefoodapp.Model.FavoriteFirestore;
import pt.ipp.estg.covidresolvefoodapp.R;

public class UserFavResFragment extends Fragment {

    private FirebaseAuth mAuth;

    private RecyclerView mRecyclerViewFavRes;
    private FavResAdapter mResAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference favRef = db.collection("Favorite");

    public UserFavResFragment() {
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

        View view = inflater.inflate(R.layout.fragment_user_fav_res, container, false);

        Query query = this.favRef.whereEqualTo("idUser", this.mAuth.getCurrentUser().getUid());

        FirestoreRecyclerOptions<FavoriteFirestore> options = new FirestoreRecyclerOptions.Builder<FavoriteFirestore>()
                .setQuery(query, FavoriteFirestore.class)
                .build();

        this.mResAdapter = new FavResAdapter(options, getContext());

        this.mRecyclerViewFavRes = view.findViewById(R.id.recyclerview_fav_res);

        this.mRecyclerViewFavRes.setLayoutManager(new LinearLayoutManager(getContext()));

        this.mRecyclerViewFavRes.setAdapter(this.mResAdapter);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        this.mResAdapter.stopListening();
    }

}