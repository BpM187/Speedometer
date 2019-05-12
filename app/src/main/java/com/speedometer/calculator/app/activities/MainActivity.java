package com.speedometer.calculator.app.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.speedometer.calculator.app.R;
import com.speedometer.calculator.app.constants.Constants;

public class MainActivity extends AppCompatActivity {

    TextView startTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startTime = findViewById(R.id.startTime);

        // Register class as listener
        LocationManager locationObject = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Check permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.MY_LOCATION_PERMISSION_CODE);
        } else if (locationObject != null) {

            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    if (location == null) {
                        startTime.setText("-.- m/s");
                    } else {
                        float nCurrentSpeed = location.getSpeed();
                        startTime.setText(nCurrentSpeed + " m/s");
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            };


            Criteria criteria = new Criteria();
//            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            String provider = locationObject.getBestProvider(criteria, true);
            locationObject.requestLocationUpdates(provider, 0, 0, locationListener);
//            locationObject.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
//            locationObject.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        }
    }
}
