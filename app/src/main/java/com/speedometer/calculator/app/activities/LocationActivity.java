package com.speedometer.calculator.app.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.speedometer.calculator.app.R;
import com.speedometer.calculator.app.constants.Constants;
import com.speedometer.calculator.app.fragments.GraphicFragment;
import com.speedometer.calculator.app.model.Param;
import com.speedometer.calculator.app.model.Result;
import com.speedometer.calculator.app.model.Vehicle;
import com.speedometer.calculator.app.util.ASurface;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class LocationActivity extends BaseActivity {

    //views
    private LinearLayout llResultsList;
    private TextView txtSpeed, txtTime, txtDistance;
    private ASurface surface;

    //variables received from other class
    private Vehicle vehicle;


    //variables
    private ArrayList<Param> resultsParamList = new ArrayList<>();
    private ArrayList<Result> resultsSavedList = new ArrayList<>();

    private double m_maxAuthWeight;
    private double A_wheelbase;
    private double p_power;
    private double Cx_coefficient;

    private long t_time;
    private double v_speed;
    private double d_distance;
    private double alpha_elevation;

    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        updateStatusBarColor(getResources().getColor(R.color.colorBrown));
        getValuesFromBundle();
        initViews();
        handleLocation();
        setListeners();
        hideKeyboard();
    }

    private void getValuesFromBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("params")) {
            vehicle = (Vehicle) bundle.get("params");
            if (vehicle != null) {
                m_maxAuthWeight = Double.parseDouble(vehicle.getVolumeWeights().getWeightMaxAuthorized());
                A_wheelbase = Double.parseDouble(vehicle.getDimensions().getWheelbase());
                p_power = Double.parseDouble(vehicle.getEngine().getPower());
                Cx_coefficient = Double.parseDouble(vehicle.getCoefficient());
            }
        }
    }

    private void initViews() {
        //views
        llResultsList = findViewById(R.id.ll_results_list);
        txtSpeed = findViewById(R.id.txt_speed);
        txtTime = findViewById(R.id.txt_time);
        txtDistance = findViewById(R.id.txt_distance);
        surface = findViewById(R.id.surface);

        setParamList((LinearLayout) findViewById(R.id.ll_info_list), getInfoReceivedParamList());
        resultsParamList = getBasicParamList();
        setParamList(llResultsList, resultsParamList);

        surface.setZOrderOnTop(true);
        surface.getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }


    private void setParamList(LinearLayout parent, ArrayList<Param> list) {
        for (Param param : list) {
            parent.addView(getParamView(parent, param));
        }
    }

    private View getParamView(LinearLayout parent, final Param param) {
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.cell_params, null);
        layout.setTag(parent.getChildCount());

        TextView txtTitle = layout.findViewById(R.id.txt_title);
        ImageView imgCheck = layout.findViewById(R.id.img_check);
        TextView txtName = layout.findViewById(R.id.txt);
        EditText edtValue = layout.findViewById(R.id.edt);
        ImageView imgPhoto = layout.findViewById(R.id.img);

        txtTitle.setVisibility(View.GONE);
        imgCheck.setVisibility(View.GONE);
        imgPhoto.setVisibility(View.GONE);
        edtValue.setEnabled(false);
        edtValue.setTag("edt_" + parent.getChildCount());

        txtName.setText(param.getName());
        edtValue.setText(param.getValue());

        return layout;
    }


    private void handleLocation() {
        //start time
        startTime = Calendar.getInstance().getTimeInMillis();

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
                        v_speed = 0;
                        alpha_elevation = 0;
                        t_time = 0;
                        d_distance = 0;
                    } else {
                        //speed in km/h
                        v_speed = location.getSpeed() * 3.6f; //from m/s in km/h

                        //elevation/altitude
                        alpha_elevation = location.getAltitude();

                        //time in h
                        t_time = Calendar.getInstance().getTimeInMillis() - startTime;

                        //distance in km
                        // distance (m) = speed (m/s) * time(s) => distance (km) = speed(km/h) * time (h)
                        d_distance = location.getSpeed() * (t_time / 1000); //time from ms in h
                        d_distance = v_speed * (t_time / (60 * 60 * 1000)); //time from ms in h
                    }


                    long min = t_time / (60 * 1000);
                    long hour = min / 60;
                    if (hour > 0) {
                        min = min - (hour * 60);
                    }

                    txtSpeed.setText(String.format("%.2f", v_speed));
                    txtTime.setText(String.valueOf(hour + " h " + min + " min"));
                    txtDistance.setText(String.format("%.2f", d_distance) + " km");

                    surface.setProgress(Math.round(v_speed));

                    updateParamsValues();
                    hideKeyboard();
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

    private double getAltitude(Double longitude, Double latitude) {
        if (longitude == 0 || latitude == 0) {
            return 0;
        }
        double result = Double.NaN;
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        String url = "http://gisdata.usgs.gov/"
                + "xmlwebservices2/elevation_service.asmx/"
                + "getElevation?X_Value=" + longitude
                + "&Y_Value=" + latitude
                + "&Elevation_Units=METERS&Source_Layer=-1&Elevation_Only=true";
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(httpGet, localContext);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                int r = -1;
                StringBuffer respStr = new StringBuffer();
                while ((r = instream.read()) != -1)
                    respStr.append((char) r);
                String tagOpen = "<double>";
                String tagClose = "</double>";
                if (respStr.indexOf(tagOpen) != -1) {
                    int start = respStr.indexOf(tagOpen) + tagOpen.length();
                    int end = respStr.indexOf(tagClose);
                    String value = respStr.substring(start, end);
                    result = Double.parseDouble(value);
                }
                instream.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    private ArrayList<Param> getInfoReceivedParamList() {
        ArrayList<Param> params = new ArrayList<>();

        //mandatory
        params.add(new Param(getString(R.string.vehicle_maxim_authorized_weight), true, String.valueOf(m_maxAuthWeight)));

        //optional
        if (vehicle.getDimensions().getLength() != null && !vehicle.getDimensions().getLength().isEmpty()) {
            params.add(new Param(getString(R.string.vehicle_length), true, vehicle.getDimensions().getLength()));
        }
        if (vehicle.getDimensions().getWidth() != null && !vehicle.getDimensions().getWidth().isEmpty()) {
            params.add(new Param(getString(R.string.vehicle_width), true, vehicle.getDimensions().getWidth()));
        }
        if (vehicle.getDimensions().getHeight() != null && !vehicle.getDimensions().getHeight().isEmpty()) {
            params.add(new Param(getString(R.string.vehicle_height), true, vehicle.getDimensions().getHeight()));
        }

        //mandatory
        params.add(new Param(getString(R.string.vehicle_wheelbase), true, String.valueOf(A_wheelbase)));

        //optional
        if (vehicle.getDimensions().getGaugeFront() != null && !vehicle.getDimensions().getGaugeFront().isEmpty()) {
            params.add(new Param(getString(R.string.vehicle_gauge_front), true, vehicle.getDimensions().getGaugeFront()));
        }
        if (vehicle.getDimensions().getGaugeBack() != null && !vehicle.getDimensions().getGaugeBack().isEmpty()) {
            params.add(new Param(getString(R.string.vehicle_gauge_back), true, vehicle.getDimensions().getGaugeBack()));
        }
        if (vehicle.getDimensions().getGaugeBack() != null && !vehicle.getDimensions().getGaugeBack().isEmpty()) {
            params.add(new Param(getString(R.string.vehicle_gauge_back), true, vehicle.getDimensions().getGaugeBack()));
        }

        //mandatory
        params.add(new Param(getString(R.string.vehicle_power), true, String.valueOf(p_power)));
        params.add(new Param(getString(R.string.vehicle_coefficient), true, String.valueOf(Cx_coefficient)));

        return params;
    }

    private ArrayList<Param> getBasicParamList() {
        ArrayList<Param> params = new ArrayList<>();

        params.add(new Param("Rr", false, "0.00"));
        params.add(new Param("Ra", false, "0.00"));
        params.add(new Param("Rd", false, "0.00"));
        params.add(new Param("Rp", false, "0.00"));
        params.add(new Param("SumR", false, "0.00"));
        params.add(new Param("Pr", false, "0.00"));

        return params;
    }

    private void updateParamsValues() {

        //Rolling resistance / Rezistenta rulare
        double paramRr = Constants.f_RESULT_AT_RUN_COEFFICIENT * Math.cos(alpha_elevation) * m_maxAuthWeight * Constants.g_GRAVITY_ACCELERATION;

        //Air resistance / Rezistenta aerului
        double paramRa = 0.5 * Constants.ro_AIR_DENSITY * Cx_coefficient * A_wheelbase * v_speed * v_speed;

        //Start resistance / Rezistenta la demarare
        double paramRd = m_maxAuthWeight * Constants.g_GRAVITY_ACCELERATION * Math.sin(alpha_elevation);

        //Slope resistance / Rezistenta la panta
        double paramRp = Constants.D_MASSES_FOUND_AT_RUN_COEFFICIENT * m_maxAuthWeight * Math.sin(alpha_elevation); //?

        //Resistance sum at...  / Suma rezistentelor la ...
        double paramSumR = paramRr + paramRa + paramRd + paramRp;

        //The power of rolling resistance / Puterea invingerii rezistentei la rulare
        double paramPr = paramSumR * p_power;


        resultsParamList.get(0).setValue(String.format("%.2f", paramRr));
        resultsParamList.get(1).setValue(String.format("%.2f", paramRa));
        resultsParamList.get(2).setValue(String.format("%.2f", paramRd));
        resultsParamList.get(3).setValue(String.format("%.2f", paramRp));
        resultsParamList.get(4).setValue(String.format("%.2f", paramSumR));
        resultsParamList.get(5).setValue(String.format("%.2f", paramPr));


        for (int i = 0; i < llResultsList.getChildCount(); i++) {
            try {
                LinearLayout childParent = (LinearLayout) llResultsList.getChildAt(i);
                RelativeLayout childChildParent = (RelativeLayout) childParent.getChildAt(1);
                EditText edt = getChildEdt(childChildParent, i);
                if (edt != null) {
                    edt.setText(resultsParamList.get(i).getValue());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        addDataToList();
    }

    private void addDataToList() {
        Result rez = new Result();

        rez.setTime(t_time);
        rez.setSpeed(v_speed);
        rez.setAltitude(alpha_elevation);
        rez.setDistance(d_distance);

        //if somehow at conversion with string double values contains "," than replace that with "."
        resultsParamList.get(0).setValue(resultsParamList.get(0).getValue().replace(",", "."));
        resultsParamList.get(1).setValue(resultsParamList.get(1).getValue().replace(",", "."));
        resultsParamList.get(2).setValue(resultsParamList.get(2).getValue().replace(",", "."));
        resultsParamList.get(3).setValue(resultsParamList.get(3).getValue().replace(",", "."));
        resultsParamList.get(4).setValue(resultsParamList.get(4).getValue().replace(",", "."));
        resultsParamList.get(5).setValue(resultsParamList.get(5).getValue().replace(",", "."));

        //add to list
        rez.setRr(Double.parseDouble(resultsParamList.get(0).getValue()));
        rez.setRa(Double.parseDouble(resultsParamList.get(1).getValue()));
        rez.setRd(Double.parseDouble(resultsParamList.get(2).getValue()));
        rez.setRp(Double.parseDouble(resultsParamList.get(3).getValue()));
        rez.setSumR(Double.parseDouble(resultsParamList.get(4).getValue()));
        rez.setPr(Double.parseDouble(resultsParamList.get(5).getValue()));

        resultsSavedList.add(rez);
    }

    private EditText getChildEdt(RelativeLayout parent, int no) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            Object obj = child.getTag();
            if (obj != null && String.valueOf(obj).equals("edt_" + no)) {
                return (EditText) child;
            }
        }
        return null;
    }

    private void setListeners() {
        //back
        findViewById(R.id.rl_header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //stop
        findViewById(R.id.txt_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GraphicFragment fragment = new GraphicFragment();
                fragment.resultList = resultsSavedList;
                replaceFragment(fragment, "GraphicFragment");
            }
        });

        //animation progress
        surface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                surface.setProgress(surface.getProgress() + 10);
            }
        });
    }
}
