package com.speedometer.calculator.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.speedometer.calculator.app.R;
import com.speedometer.calculator.app.activities.LocationActivity;
import com.speedometer.calculator.app.constants.Constants;
import com.speedometer.calculator.app.model.AdapterCallback;
import com.speedometer.calculator.app.model.Vehicle;
import com.speedometer.calculator.app.adapters.VehicleAdapter;

import java.util.ArrayList;

public class VehicleModelFragment extends BaseFragment {

    //views
    ListView listModel;
    RelativeLayout rlBack;
    TextView txtStart;

    //variables from another class
    public ArrayList<Vehicle> modelList = new ArrayList<>();


    //variables
    int chosenVehiclePosition = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_vehicle_model, container, false);

        initViews();
        setListeners();

        return baseView;
    }

    private void initViews() {
        listModel = baseView.findViewById(R.id.list_model);
        rlBack = baseView.findViewById(R.id.rl_header);
        txtStart = baseView.findViewById(R.id.txt_next);


        //set
        if (getActivity() != null) {
            VehicleAdapter adapterModel = new VehicleAdapter(getActivity(), R.layout.cell_vehicle, modelList);
            adapterModel.type = Constants.TYPE_MODEL;
            adapterModel.callback = new AdapterCallback() {
                @Override
                public void onClickItem(int position) {
                    chosenVehiclePosition = position;
                    txtStart.setBackgroundResource(chosenVehiclePosition == -1 ? R.drawable.shadow_round_gray_01 : R.drawable.shadow_round_brown_01);
                }
            };
            listModel.setAdapter(adapterModel);
        }

    }

    private void setListeners() {

        //base view
        baseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //back
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        //start
        txtStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chosenVehiclePosition == -1) {
                    return;
                }

                //send chosen vehicle
                Intent intent = new Intent(getActivity(), LocationActivity.class);
                intent.putExtra("params", modelList.get(chosenVehiclePosition));
                startActivity(intent);
            }
        });

    }
}
