package pt.ipp.estg.covidresolvefoodapp.PerfilUser;

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

import pt.ipp.estg.covidresolvefoodapp.Adapter.UserMealsAdapter;
import pt.ipp.estg.covidresolvefoodapp.Model.FavoriteFirestore;
import pt.ipp.estg.covidresolvefoodapp.Model.MealsFirestore;
import pt.ipp.estg.covidresolvefoodapp.R;

public class UserMealsFragment extends Fragment {

    private FirebaseAuth mAuth;

    private RecyclerView mRecyclerView;
    private UserMealsAdapter mealsAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference mealsRef = db.collection("Meals");

    public UserMealsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        this.mealsAdapter.startListening();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mAuth = FirebaseAuth.getInstance();

        View view = inflater.inflate(R.layout.fragment_user_meals, container, false);

        Query query = this.mealsRef.whereEqualTo("idUser", this.mAuth.getCurrentUser().getUid());

        FirestoreRecyclerOptions<MealsFirestore> options = new FirestoreRecyclerOptions.Builder<MealsFirestore>()
                .setQuery(query, MealsFirestore.class)
                .build();

        this.mealsAdapter = new UserMealsAdapter(options);

        this.mRecyclerView = view.findViewById(R.id.recyclerview_user_meals);

        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        this.mRecyclerView.setAdapter(this.mealsAdapter);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        this.mealsAdapter.stopListening();
    }
}