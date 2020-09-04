package pt.ipp.estg.covidresolvefoodapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import pt.ipp.estg.covidresolvefoodapp.Model.ReviewFirestore;
import pt.ipp.estg.covidresolvefoodapp.R;
import pt.ipp.estg.covidresolvefoodapp.Retrofit.Model.RestaurantInfoRetro;
import pt.ipp.estg.covidresolvefoodapp.Retrofit.ZomatoAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserReviewProfileAdapter extends FirestoreRecyclerAdapter<ReviewFirestore, UserReviewProfileAdapter.UserReviewProfileViewHolder> {

    private FirebaseAuth mAuth;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference userRef = db.collection("User");

    public UserReviewProfileAdapter(@NonNull FirestoreRecyclerOptions<ReviewFirestore> options) {
        super(options);
    }

    @NonNull
    @Override
    public UserReviewProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.mAuth = FirebaseAuth.getInstance();

        // Get layout inflater from context
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_review, parent, false);
        return new UserReviewProfileViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserReviewProfileViewHolder viewHolder, int position, @NonNull ReviewFirestore reviewFirestore) {
        TextView mNameRestaurant = viewHolder.mNameRestaurant;

        this.getAPIZomato().getRestaurant(reviewFirestore.getIdRestaurant()).enqueue(new Callback<RestaurantInfoRetro>() {
            @Override
            public void onResponse(Call<RestaurantInfoRetro> call, Response<RestaurantInfoRetro> response) {
                mNameRestaurant.setText(response.body().getName());
            }

            @Override
            public void onFailure(Call<RestaurantInfoRetro> call, Throwable t) {
                System.out.println(call.request());
            }
        });

        RatingBar mRatingBar = viewHolder.mRatingBar;
        mRatingBar.setRating(reviewFirestore.getAvaliation());

        TextView mNotaReview = viewHolder.mNotaReview;
        mNotaReview.setText(String.valueOf(reviewFirestore.getAvaliation()));

        TextView mContentMessage = viewHolder.mContentMessage;
        mContentMessage.setText(reviewFirestore.getContentReview());
    }

    public class UserReviewProfileViewHolder extends RecyclerView.ViewHolder {
        public TextView mNameRestaurant;
        public RatingBar mRatingBar;
        public TextView mNotaReview;
        public TextView mContentMessage;

        public UserReviewProfileViewHolder(View itemView) {
            super(itemView);

            this.mNameRestaurant = itemView.findViewById(R.id.anonymous_review);
            this.mRatingBar = itemView.findViewById(R.id.estrelas_review);
            this.mNotaReview = itemView.findViewById(R.id.nota_review);
            this.mContentMessage = itemView.findViewById(R.id.content_message_review);
        }
    }


    private Retrofit getRetrofitZomatoAPI() {
        return new Retrofit.Builder()
                .baseUrl("https://developers.zomato.com/api/v2.1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private ZomatoAPI getAPIZomato() {
        return this.getRetrofitZomatoAPI().create(ZomatoAPI.class);
    }
}
