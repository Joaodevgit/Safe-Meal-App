package pt.ipp.estg.covidresolvefoodapp.PerfilUser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import pt.ipp.estg.covidresolvefoodapp.R;

public class UserMealsFragment extends Fragment {

    private FirebaseAuth mAuth;
    private RecyclerView mRecyclerView;

    public UserMealsFragment() {
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

        View view = inflater.inflate(R.layout.fragment_user_meals, container, false);

        this.mRecyclerView = view.findViewById(R.id.recyclerview_user_meals);

        return view;
    }
}