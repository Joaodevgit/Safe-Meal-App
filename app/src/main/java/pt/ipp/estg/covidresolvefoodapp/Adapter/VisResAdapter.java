package pt.ipp.estg.covidresolvefoodapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pt.ipp.estg.covidresolvefoodapp.DatabaseModels.Restaurant;
import pt.ipp.estg.covidresolvefoodapp.R;

public class VisResAdapter extends RecyclerView.Adapter<VisResAdapter.VisResViewHolder> {

    private Context mContext;
    private List<Restaurant> visitedRestaurants = new ArrayList<>();

/*    public VisResAdapter(Context mContext, List<Restaurant> visitedRestaurants) {
        this.mContext = mContext;
        this.visitedRestaurants = visitedRestaurants;
    }*/

    @Override
    public VisResViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get layout inflater from context
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate layout
        View visResViewHolder = inflater.inflate(R.layout.item_vis_res, parent, false);

        // Return a new holder instance
        return new VisResViewHolder(visResViewHolder);
    }

    @Override
    public void onBindViewHolder(VisResViewHolder viewHolder, int position) {
        // Get the data model based on position
        Restaurant currentVisRes = visitedRestaurants.get(position);
        viewHolder.visResNameTxtView.setText(currentVisRes.getName());
        viewHolder.visResCityTxtView.setText(currentVisRes.getCity());
        viewHolder.visResAddressTxtView.setText(currentVisRes.getAddress());

    }

    public int getItemCount() {
        return visitedRestaurants.size();
    }

    public void setRestaurants(List<Restaurant> visitedRestaurants) {
        this.visitedRestaurants = visitedRestaurants;
        notifyDataSetChanged();
    }

    public Restaurant getRestaurantAt(int position) {
        return visitedRestaurants.get(position);
    }

    public class VisResViewHolder extends RecyclerView.ViewHolder {

        public TextView visResNameTxtView;
        public TextView visResCityTxtView;
        public TextView visResAddressTxtView;

        public VisResViewHolder(View itemView) {
            super(itemView);

            visResNameTxtView = itemView.findViewById(R.id.vis_res_name);
            visResCityTxtView = itemView.findViewById(R.id.vis_res_city);
            visResAddressTxtView = itemView.findViewById(R.id.vis_res_address);
        }
    }
}
