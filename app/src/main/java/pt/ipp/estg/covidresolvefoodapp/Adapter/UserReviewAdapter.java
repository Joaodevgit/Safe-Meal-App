package pt.ipp.estg.covidresolvefoodapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ipp.estg.covidresolvefoodapp.Model.User;
import pt.ipp.estg.covidresolvefoodapp.R;

public class UserReviewAdapter extends RecyclerView.Adapter<UserReviewAdapter.UserReviewViewHolder> {

    private Context mContext;
    private List<User> mUserReviews;

    public UserReviewAdapter(Context mContext, List<User> mUserReviews) {
        this.mContext = mContext;
        this.mUserReviews = mUserReviews;
    }

    public int getItemCount() {
        return mUserReviews.size();
    }

    @Override
    public UserReviewAdapter.UserReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get layout inflater from context
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate layout
        View userReviewViewHolder = inflater.inflate(R.layout.item_user_review, parent, false);

        // Return a new holder instance
        return new UserReviewAdapter.UserReviewViewHolder(userReviewViewHolder);
    }

    @Override
    public void onBindViewHolder(UserReviewAdapter.UserReviewViewHolder viewHolder, int position) {
        // Get the data model based on position
        User userReviews = mUserReviews.get(position);

        // Set name
        TextView userEmail = viewHolder.userReviewEmail;
        userEmail.setText(userReviews.getEmail());
        TextView userRating = viewHolder.userReviewRating;
        userRating.setText(Double.toString(userReviews.getRating()));

    }

    public class UserReviewViewHolder extends RecyclerView.ViewHolder {

        public TextView userReviewEmail;
        public TextView userReviewRating;

        public UserReviewViewHolder(View itemView) {
            super(itemView);
            userReviewEmail = itemView.findViewById(R.id.userReviewEmail);
            userReviewRating = itemView.findViewById(R.id.userReviewRating);

        }
    }

}
