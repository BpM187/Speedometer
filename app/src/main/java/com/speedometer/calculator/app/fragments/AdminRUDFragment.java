package com.speedometer.calculator.app.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.speedometer.calculator.app.R;
import com.speedometer.calculator.app.adapters.VehicleDetailsAdapter;
import com.speedometer.calculator.app.constants.Constants;
import com.speedometer.calculator.app.db.SQLiteHelper;
import com.speedometer.calculator.app.model.AdapterCallback;
import com.speedometer.calculator.app.model.Vehicle;

import java.util.ArrayList;

public class AdminRUDFragment extends BaseFragment {
    //RUD -  READ, UPDATE, DELETE

    //views
    ListView listView;
    LinearLayout llBlank;
    LinearLayout llAlert;
    RelativeLayout rlAlertIn;
    TextView txtAlertYes;
    TextView txtAlertNo;

    //variables from other class
    public int state = Constants.STATE_READ;

    //variables
    ArrayList<Vehicle> vehicles;
    int chosenPosition = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_admin_rud, container, false);

        updateStatusBarColor(getResources().getColor(R.color.colorBrown));
        if (state == Constants.STATE_UPDATE) {
            ((TextView) baseView.findViewById(R.id.txt_back)).setText(getString(R.string.crud_update));
        } else if (state == Constants.STATE_DELETE) {
            ((TextView) baseView.findViewById(R.id.txt_back)).setText(getString(R.string.crud_delete));
        }

        initParams();
        setListeners();

        return baseView;
    }

    private void initParams() {

        listView = baseView.findViewById(R.id.list_vehicle);
        llBlank = baseView.findViewById(R.id.ll_blank);
        llAlert = baseView.findViewById(R.id.ll_alert);
        rlAlertIn = baseView.findViewById(R.id.rl_alert_in);
        txtAlertYes = baseView.findViewById(R.id.txt_yes);
        txtAlertNo = baseView.findViewById(R.id.txt_no);

        vehicles = (ArrayList<Vehicle>) SQLiteHelper.getInstance(getActivity()).getVehicleList();


        final VehicleDetailsAdapter adapter = new VehicleDetailsAdapter(getActivity(), R.layout.cell_vehicle_details, vehicles != null ? vehicles : new ArrayList<Vehicle>());
        adapter.state = state;
        adapter.listener = new AdapterCallback() {
            @Override
            public void onClickItem(int position) {
                if (state == Constants.STATE_UPDATE) {
                    AdminCUFragment fragment = new AdminCUFragment();
                    fragment.state = state;
                    fragment.vehicleToUpdate = ((vehicles != null && vehicles.size() > position) ? vehicles.get(position) : null);
                    replaceFragmentWithRightAnimation(fragment, "AdminCUFragment");
                } else if (state == Constants.STATE_DELETE) {
                    if (vehicles != null && vehicles.size() > position) {
                        updateStatusBarColor(getResources().getColor(R.color.colorBrownDark));
                        chosenPosition = position;
                        llAlert.setVisibility(View.VISIBLE);
                    }
                }
            }
        };
        listView.setAdapter(adapter);

        checkListIsEmpty(vehicles);

    }

    private void checkListIsEmpty(ArrayList list) {
        if (list == null || list.size() == 0) {
            llBlank.setVisibility(View.VISIBLE);
        } else {
            llBlank.setVisibility(View.GONE);
        }
    }

    private void setListeners() {
        //base view
        baseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //back button
        baseView.findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        //ll alert
        llAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatusBarColor(getResources().getColor(R.color.colorBrown));
                llAlert.setVisibility(View.GONE);
                chosenPosition = -1;
            }
        });

        //rl alert in
        rlAlertIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //block ll alert event
            }
        });

        //alert yes
        txtAlertYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llAlert.setVisibility(View.GONE);
                updateStatusBarColor(getResources().getColor(R.color.colorBrown));
                if (chosenPosition != -1) {
                    SQLiteHelper.getInstance(getActivity()).deleteVehicle(vehicles.get(chosenPosition).getId());
                    vehicles.clear();
                    vehicles.addAll(SQLiteHelper.getInstance(getActivity()).getVehicleList());
                    ((VehicleDetailsAdapter) listView.getAdapter()).notifyDataSetChanged();
                    checkListIsEmpty(vehicles);
                }

            }
        });

        //alert no
        txtAlertNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatusBarColor(getResources().getColor(R.color.colorBrown));
                llAlert.setVisibility(View.GONE);
                chosenPosition = -1;
            }
        });

    }
}
