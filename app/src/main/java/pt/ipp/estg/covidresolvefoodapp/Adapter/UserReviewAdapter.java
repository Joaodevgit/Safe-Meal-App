package pt.ipp.estg.covidresolvefoodapp.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import pt.ipp.estg.covidresolvefoodapp.Model.ReviewFirestore;
import pt.ipp.estg.covidresolvefoodapp.Model.UserFirestore;
import pt.ipp.estg.covidresolvefoodapp.R;

public class UserReviewAdapter extends FirestoreRecyclerAdapter<ReviewFirestore, UserReviewAdapter.UserReviewViewHolder> {

    private FirebaseAuth mAuth;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference userRef = db.collection("User");

    public UserReviewAdapter(FirestoreRecyclerOptions<ReviewFirestore> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(UserReviewViewHolder userReviewViewHolder, int i, ReviewFirestore reviewFirestore) {
        this.mAuth = FirebaseAuth.getInstance();

        final TextView mAnonimousUser = userReviewViewHolder.mAnonimousUser;

        if (this.mAuth.getCurrentUser().getUid().equals(reviewFirestore.getIdUser())) {
            this.userRef.whereEqualTo("idUser", this.mAuth.getCurrentUser().getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                UserFirestore user = queryDocumentSnapshot.toObject(UserFirestore.class);

                                mAnonimousUser.setText(user.getUserEmail());
                                mAnonimousUser.setTextColor(Color.RED);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println(e);
                        }
                    });
        } else {
            this.userRef.whereEqualTo("idUser", reviewFirestore.getIdUser())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                UserFirestore user = queryDocumentSnapshot.toObject(UserFirestore.class);

                                mAnonimousUser.setText(user.getAnonymous());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println(e);
                        }
                    });
        }

        RatingBar mRatingBar = userReviewViewHolder.mRatingBar;
        mRatingBar.setRating(reviewFirestore.getAvaliation());

        TextView mNotaReview = userReviewViewHolder.mNotaReview;
        mNotaReview.setText(String.valueOf(reviewFirestore.getAvaliation()));

        TextView mContentMessage = userReviewViewHolder.mContentMessage;
        mContentMessage.setText(reviewFirestore.getContentReview());
    }

    @NonNull
    @Override
    public UserReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_review, parent, false);
        return new UserReviewViewHolder(v);
    }

    public class UserReviewViewHolder extends RecyclerView.ViewHolder {

        public TextView mAnonimousUser;
        public RatingBar mRatingBar;
        public TextView mNotaReview;
        public TextView mContentMessage;

        public UserReviewViewHolder(View itemView) {
            super(itemView);

            this.mAnonimousUser = itemView.findViewById(R.id.anonymous_review);
            this.mRatingBar = itemView.findViewById(R.id.estrelas_review);
            this.mNotaReview = itemView.findViewById(R.id.nota_review);
            this.mContentMessage = itemView.findViewById(R.id.content_message_review);
        }
    }

}
