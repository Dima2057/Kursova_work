package com.example.kursova_work;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InviteCodeActivity extends AppCompatActivity {

    String name, email, password, date, isSharing, code;
    Uri imageUri;
    ProgressDialog progressDialog;

    TextView t1;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);
        t1 = (TextView) findViewById(R.id.textView);
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        ActivityCompat.requestPermissions(this, permissions, 1);

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        Intent myIntent = getIntent();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        if (myIntent != null) {

            name = myIntent.getStringExtra("name");
            email = myIntent.getStringExtra("email");
            password = myIntent.getStringExtra("password");
            code = myIntent.getStringExtra("code");
            isSharing = myIntent.getStringExtra("isSharing");
            imageUri = myIntent.getParcelableExtra("imageUri");
        }
        t1.setText(code);
    }

    public void registerUser(View v) {
        progressDialog.setMessage("Please wait while we are creating an account for you");
        progressDialog.show();


        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // insert values in real time database
                    double[] loc = getLocation();
                    CreateUser createUser;
                    if(loc != null) {
                        createUser = new CreateUser(name, email, password, code, "false", String.valueOf(loc[0]), String.valueOf(loc[1]), "na");
                    }else{
                        createUser = new CreateUser(name, email, password, code, "false", "0", "0", "na");
                    }
                    user = auth.getCurrentUser();
                    userId = user.getUid();

                    reference.child(userId).setValue(createUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_SHORT).show();

                                Toast.makeText(getApplicationContext(),"User logged in successfully", Toast.LENGTH_LONG).show();
                                Intent myIntent = new Intent(InviteCodeActivity.this, MapActivity.class);
                                myIntent.putExtra("email", email);
                                startActivity(myIntent);


                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Could not register user.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            }
        });

    }

    public double[] getLocation()
    {
        // Get the location manager

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

    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION};
        ActivityCompat.requestPermissions(this, permissions, 1);
    }
}
