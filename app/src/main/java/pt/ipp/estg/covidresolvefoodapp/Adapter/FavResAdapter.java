package pt.ipp.estg.covidresolvefoodapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ipp.estg.covidresolvefoodapp.Model.Restaurant;
import pt.ipp.estg.covidresolvefoodapp.R;

public class FavResAdapter extends RecyclerView.Adapter<FavResAdapter.FavResViewHolder> {

    private Context mContext;
    private List<Restaurant> mFavRes;

    public FavResAdapter(Context mContext, List<Restaurant> mFavRes) {
        this.mContext = mContext;
        this.mFavRes = mFavRes;
    }

    public int getItemCount() {
        return mFavRes.size();
    }

    @Override
    public FavResViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get layout inflater from context
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate layout
        View favResViewHolder = inflater.inflate(R.layout.item_fav_res, parent, false);

        // Return a new holder instance
        return new FavResViewHolder(favResViewHolder);
    }

    @Override
    public void onBindViewHolder(FavResViewHolder viewHolder, int position) {
        // Get the data model based on position
        Restaurant favRes = mFavRes.get(position);

        // Set name
        TextView textView = viewHolder.favResNameTxtView;
        textView.setText(favRes.getName());

        //Set image
        ImageView imageView = viewHolder.favResImageView;
        imageView.setImageResource(R.drawable.restaurant_default);
    }

    public class FavResViewHolder extends RecyclerView.ViewHolder {

        private TextView favResNameTxtView;
        private ImageView favResImageView;
        private Button deleteButton;
        private Button moreInfoButton;

        public FavResViewHolder(View itemView) {
            super(itemView);
            favResNameTxtView = itemView.findViewById(R.id.favResName);
            favResImageView = itemView.findViewById(R.id.imageViewRes);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Apagaste Restaurante", Toast.LENGTH_SHORT).show();
                }
            });
            moreInfoButton = itemView.findViewById(R.id.buttonMoreInfo);
            moreInfoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Mais Info Restaurante", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
