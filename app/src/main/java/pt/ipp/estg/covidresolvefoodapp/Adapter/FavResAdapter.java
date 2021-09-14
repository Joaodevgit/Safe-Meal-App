package pt.ipp.estg.covidresolvefoodapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.squareup.picasso.Picasso;

import pt.ipp.estg.covidresolvefoodapp.Model.FavoriteFirestore;
import pt.ipp.estg.covidresolvefoodapp.R;
import pt.ipp.estg.covidresolvefoodapp.SearchRestaurant.InfoRestaurantActivity;

public class FavResAdapter extends FirestoreRecyclerAdapter<FavoriteFirestore, FavResAdapter.FavResViewHolder> {

    private Context mContext;

    private FirebaseAuth mAuth;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference favRef = db.collection("Favorite");

    public FavResAdapter(FirestoreRecyclerOptions<FavoriteFirestore> options, Context context) {
        super(options);

        this.mContext = context;
    }

    @Override
    public FavResViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mAuth = FirebaseAuth.getInstance();

        // Get layout inflater from context
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate layout
        View favResViewHolder = inflater.inflate(R.layout.item_fav_res, parent, false);

        // Return a new holder instance
        return new FavResViewHolder(favResViewHolder);
    }

    @Override
    protected void onBindViewHolder(FavResViewHolder favResViewHolder, int position, FavoriteFirestore infoRetro) {

        TextView textView = favResViewHolder.favResNameTxtView;
        textView.setText("Restaurant: " + infoRetro.getNameRestaurant() + "\nCity: " + infoRetro.getCity() + "\nCooking: " + infoRetro.getCuisines());

        ImageView imageView = favResViewHolder.favResImageView;

        if (!infoRetro.getThumb().equals("")) {
            Picasso.get().load(infoRetro.getThumb()).into(imageView);
        } else {
            Picasso.get().load("https://i.postimg.cc/zfX7My2F/tt.jpg").into(imageView);
        }

        Button buttonInfo = favResViewHolder.moreInfoButton;

        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, InfoRestaurantActivity.class);
                intent.putExtra("idRestaurant", infoRetro.getIdRestaurant());

                mContext.startActivity(intent);
            }
        });

        Button buttonRemove = favResViewHolder.deleteButton;

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favRef.whereEqualTo("idUser", mAuth.getCurrentUser().getUid())
                        .whereEqualTo("idRestaurant", infoRetro.getIdRestaurant())
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        WriteBatch writeBatch = db.batch();

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            writeBatch.delete(documentSnapshot.getReference());
                        }

                        writeBatch.commit();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(e);
                    }
                });
            }
        });

    }

    public class FavResViewHolder extends RecyclerView.ViewHolder {

        private TextView favResNameTxtView;
        private ImageView favResImageView;
        private Button deleteButton;
        private Button moreInfoButton;

        public FavResViewHolder(View itemView) {
            super(itemView);

            this.favResNameTxtView = itemView.findViewById(R.id.fav_res_name);
            this.favResImageView = itemView.findViewById(R.id.imageViewRes);
            this.deleteButton = itemView.findViewById(R.id.button_delete);
            this.moreInfoButton = itemView.findViewById(R.id.button_more_info);
        }
    }
}
