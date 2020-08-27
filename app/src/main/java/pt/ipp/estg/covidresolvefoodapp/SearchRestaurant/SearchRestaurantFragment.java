package pt.ipp.estg.covidresolvefoodapp.SearchRestaurant;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import pt.ipp.estg.covidresolvefoodapp.R;

public class SearchRestaurantFragment extends Fragment {

    private RadioGroup mRadioGroupEstab;
    private RadioGroup mRadioGroupCity;
    private CheckBox mCheckBoxChinese;
    private CheckBox mCheckBoxBurger;
    private CheckBox mCheckBoxBrazilian;
    private CheckBox mCheckBoxFastFood;
    private CheckBox mCheckBoxPizza;
    private CheckBox mCheckBoxPortuguese;

    private String contentQueryEstablishment = "";
    private double contentQueryLatCity = Double.MAX_VALUE;
    private double contentQueryLonCity = Double.MAX_VALUE;
    private String contentQueryCuisines = "";

    private Button mButton;

    private OnFragmentSearchRestaurantListener mListener;

    public SearchRestaurantFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_restaurant, container, false);

        this.mRadioGroupEstab = view.findViewById(R.id.radio_group_estabelicimento);
        this.mRadioGroupCity = view.findViewById(R.id.radio_group_cidade);

        this.mCheckBoxChinese = view.findViewById(R.id.check_cozinha_chinese);
        this.mCheckBoxBurger = view.findViewById(R.id.check_cozinha_burger);
        this.mCheckBoxBrazilian = view.findViewById(R.id.check_cozinha_brazilian);
        this.mCheckBoxFastFood = view.findViewById(R.id.check_cozinha_fast_food);
        this.mCheckBoxPizza = view.findViewById(R.id.check_cozinha_pizza);
        this.mCheckBoxPortuguese = view.findViewById(R.id.check_cozinha_portuguese);

        this.mButton = view.findViewById(R.id.search_restaurants);
        this.mButton.setEnabled(false);

        //ID correspondentes a cada tipo de estabelecimento para a Zomato API
        this.mRadioGroupEstab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.check_casual_dining:
                        contentQueryEstablishment = "16";
                        checkContentQueryIsEmpty();
                        break;

                    case R.id.check_snack_bar:
                        contentQueryEstablishment = "241";
                        checkContentQueryIsEmpty();
                        break;

                    case R.id.check_cafe:
                        contentQueryEstablishment = "1";
                        checkContentQueryIsEmpty();
                        break;

                    case R.id.check_bakery:
                        contentQueryEstablishment = "31";
                        checkContentQueryIsEmpty();
                        break;

                    case R.id.check_bar:
                        contentQueryEstablishment = "7";
                        checkContentQueryIsEmpty();
                        break;

                    case R.id.check_kiosk:
                        contentQueryEstablishment = "4";
                        checkContentQueryIsEmpty();
                        break;
                }
            }
        });

        this.mRadioGroupCity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.check_braga:
                        contentQueryLatCity = 41.545448;
                        contentQueryLonCity = -8.426507;
                        checkContentQueryIsEmpty();
                        break;

                    case R.id.check_porto:
                        contentQueryLatCity = 41.157944;
                        contentQueryLonCity = -8.629105;
                        checkContentQueryIsEmpty();
                        break;

                    case R.id.check_lisbon:
                        contentQueryLatCity = 38.722252;
                        contentQueryLonCity = -9.139337;
                        checkContentQueryIsEmpty();
                        break;

                    case R.id.check_algarve:
                        contentQueryLatCity = 37.017956;
                        contentQueryLonCity = -7.930834;
                        checkContentQueryIsEmpty();
                        break;

                    case R.id.check_santarem:
                        contentQueryLatCity = 39.236179;
                        contentQueryLonCity = -8.687080;
                        checkContentQueryIsEmpty();
                        break;
                }
            }
        });

        this.mCheckBoxChinese.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                contentQueryCuisines += "25,";
                checkContentQueryIsEmpty();
            }
        });

        this.mCheckBoxBurger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                contentQueryCuisines += "168,";
                checkContentQueryIsEmpty();
            }
        });

        this.mCheckBoxBrazilian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                contentQueryCuisines += "159,";
                checkContentQueryIsEmpty();
            }
        });

        this.mCheckBoxFastFood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                contentQueryCuisines += "40,";
                checkContentQueryIsEmpty();
            }
        });

        this.mCheckBoxPizza.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                contentQueryCuisines += "82,";
                checkContentQueryIsEmpty();
            }
        });

        this.mCheckBoxPortuguese.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                contentQueryCuisines += "87,";
                checkContentQueryIsEmpty();
            }
        });

        this.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentShowRestaurants(contentQueryLatCity, contentQueryLonCity, contentQueryCuisines, contentQueryEstablishment);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentSearchRestaurantListener) {
            this.mListener = (OnFragmentSearchRestaurantListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentRecipeSearchInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public interface OnFragmentSearchRestaurantListener {
        void onFragmentShowRestaurants(double lat, double lon, String cuisines, String estabelecimento);
    }

    private void checkContentQueryIsEmpty() {
        if (!this.contentQueryEstablishment.equals("") &&
                this.contentQueryLatCity != Double.MAX_VALUE &&
                this.contentQueryLonCity != Double.MAX_VALUE &&
                !this.contentQueryCuisines.equals("")) {
            this.mButton.setEnabled(true);
        } else {
            System.out.println("Preenche tudo !");
        }
    }
}