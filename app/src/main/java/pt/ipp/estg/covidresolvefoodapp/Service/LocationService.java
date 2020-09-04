package pt.ipp.estg.covidresolvefoodapp.Service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import pt.ipp.estg.covidresolvefoodapp.Model.FavoriteFirestore;
import pt.ipp.estg.covidresolvefoodapp.R;

public class LocationService extends Service {

    private final String CHANNEL_ID = "001";
    private final int NOTIFICATION_ID = 001;
    public static final String channel_name = "ANDROID CHANNEL";
    public static final String channel_description = "Notificaçao da localizaçao";
    public static final String TAG = "SERVICE";


    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Location userLocation;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference favRef = db.collection("Favorite");

    private FavoriteFirestore favBestRes = null;

    private String nameRestaurant = "";
    private String city = "";
    private String thumb = "";
    private String latitude = "";
    private String longitude = "";

    @Override
    public void onCreate() {
        super.onCreate();
        this.mAuth = FirebaseAuth.getInstance();
        createNotificationChannel();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "cheguei aqui startCommand");

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //de 5 em 5 min
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    onNewLocation(locationResult.getLastLocation());
                }
            }
        };

        startLocationUpdates();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = channel_name;
            String description = channel_description;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void startLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    private void onNewLocation(Location location) {

        favRef.whereEqualTo("idUser", mAuth.getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                float closestDistance = Float.MAX_VALUE;

                Log.d(TAG, "cheguei aqui dentro do firestore");

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    float distance = distance(location.getLatitude(), location.getLongitude(), Double.parseDouble(documentSnapshot.get("latitude").toString()),
                            Double.parseDouble(documentSnapshot.get("longitude").toString()));

                    if (distance < closestDistance) {

                        nameRestaurant = documentSnapshot.get("nameRestaurant").toString();
                        city = documentSnapshot.get("city").toString();
                        thumb = documentSnapshot.get("thumb").toString();
                        latitude = documentSnapshot.get("latitude").toString();
                        longitude = documentSnapshot.get("longitude").toString();

                        favBestRes = new FavoriteFirestore(nameRestaurant, city, thumb, latitude, longitude);
                        closestDistance = distance;
                    }
                }
                Log.d(TAG, "Nome:" + favBestRes.getNameRestaurant() + "City:" + favBestRes.getCity() + "Url" + favBestRes.getThumb());
                new NotificationSender(getApplicationContext(), favBestRes.getNameRestaurant(), favBestRes.getCity(), favBestRes.getThumb()).execute();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e);
            }
        });

   /*     if (userLocation == null || userLocation.getLongitude() != location.getLastLocation().getLongitude()
                || userLocation.getLatitude() != location.getLastLocation().getLatitude()) {
            userLocation = locationResult.getLastLocation();
        }*/
    }

    class NotificationSender extends AsyncTask<String, Void, Bitmap> {

        private Context mContext;
        private String name, city, imageUrl;

        public NotificationSender(Context context, String name, String city, String imageUrl) {
            super();
            this.mContext = context;
            this.name = name;
            this.city = city;
            this.imageUrl = imageUrl;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            try {
                URL url = new URL(this.imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            Log.d(TAG, "cheguei aqui notificacao");
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, CHANNEL_ID);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle(name)
                    .setContentText(city)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_SERVICE)
                    .build();

            startForeground(1337, notification);
        }
    }

    private float distance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);

        return dist;
    }
}
