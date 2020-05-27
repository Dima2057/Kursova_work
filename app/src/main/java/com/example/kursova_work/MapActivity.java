package com.example.kursova_work;

import android.Manifest;
import android.annotation.SuppressLint;

import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * MapActivity - displaying the location of users, interaction with the server
 * (updating location data, obtaining the location of signed users)
 * @version 1.0
 */
public class MapActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String email;
    private CreateUser iam;
    private GoogleMap mMap;
    private ArrayList<String> ids;

    private static final String TAG = "MapActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ids = new ArrayList<>();
        Intent myIntent = getIntent();
        if (myIntent != null) {
            email = myIntent.getStringExtra("email");
        }

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        initMap();
        reference.child("Users").addValueEventListener(new ValueEventListener() {
            /**
             * Drawing markers every time data is updated.
             * */
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMap.clear();
                Iterable<DataSnapshot> ds = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iter = ds.iterator();
                for (; iter.hasNext(); ) {
                    CreateUser data = iter.next().getValue(CreateUser.class);
                    if (data.getEmail().equals(email)) {
                            iam = data;
                            LatLng my = new LatLng(Double.parseDouble(data.getLat()), Double.parseDouble(data.getLng()));
                            MarkerOptions you = new MarkerOptions().position(my).title("You(" + data.getCode() + ")");
                            mMap.addMarker(you);
                        continue;
                    }

                    if (ids.contains(data.getCode())) {

                            LatLng my = new LatLng(Double.parseDouble(data.getLat()), Double.parseDouble(data.getLng()));
                            MarkerOptions user = new MarkerOptions().position(my).title(data.getName() + "(" + data.getCode() + ")");
                            mMap.addMarker(user);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Adding a new user by id, to display his location.
     * */
    public void addMember(View view) {
        TextView text = findViewById(R.id.uniId);
        if (!ids.contains(text.getText().toString())) {
            ids.add(text.getText().toString());
            updateDataBase();
        }

    }

    /**
     * Timer (refresh rate)
     * */
    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                for (; ; ) {
                    Thread.sleep(10000);
                    updateDataBase();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
    /**
     * Updating your location, getting locations signed by users.
     * */
    private void updateDataBase() {
        double[] location = getLocation();
        DatabaseReference ref = reference.child("Users").child(user.getUid());
        String key = ref.getKey();
        if(location != null) {
            iam.setLat(String.valueOf(location[0]));
            iam.setLng(String.valueOf(location[1]));
        }else{
            iam.setLat("0");
            iam.setLng("0");
        }
        if (iam.getIsSharing().equals("false")) {
            iam.setIsSharing("true");
        } else {
            iam.setIsSharing("false");
        }
        Map<String, Object> m = new HashMap<>();
        m.put("code", iam.getCode());
        m.put("email", iam.getEmail());
        m.put("imageUrl", iam.getImageUrl());
        m.put("isSharing", iam.getIsSharing());
        m.put("lat", iam.getLat());
        m.put("lng", iam.getLng());
        m.put("name", iam.getName());
        m.put("password", iam.getPassword());

        ref.updateChildren(m);

    }
    /**
     * Receiving user location via the Internet.
     * */
    public double[] getLocation() {

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            @SuppressLint("MissingPermission") Location locationGPS = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            double lat,lon;
            try {
                lat = locationGPS.getLatitude ();
                lon = locationGPS.getLongitude ();
                return new double[]{lat, lon};
            }
            catch (NullPointerException e){
                e.printStackTrace();
                System.out.println(e.getMessage());
                return null;
            }

    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MapActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MapActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * Map initialization, 1 time during the first launch of the window.
     * */
    public void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
            }
        });
    }
}
