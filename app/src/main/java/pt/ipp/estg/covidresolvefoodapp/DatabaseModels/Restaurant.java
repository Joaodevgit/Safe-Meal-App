package pt.ipp.estg.covidresolvefoodapp.DatabaseModels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "restaurant_table", primaryKeys = {"userName", "address"})
public class Restaurant {

    @NonNull
    private String name;

    @NonNull
    private String userName;

    @NonNull
    private String city;

    @NonNull

    private String address;

    private String image;

    public Restaurant(@NonNull String name, @NonNull String userName, @NonNull String city, @NonNull String address, String image) {
        this.name = name;
        this.userName = userName;
        this.city = city;
        this.address = address;
        this.image = image;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getUserName() {
        return userName;
    }

    @NonNull
    public String getCity() {
        return city;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public String getImage() {
        return image;
    }

}

