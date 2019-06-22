package com.speedometer.calculator.app.fragments;

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
import com.speedometer.calculator.app.constants.Constants;
import com.speedometer.calculator.app.db.SQLiteHelper;
import com.speedometer.calculator.app.model.AdapterCallback;
import com.speedometer.calculator.app.model.Vehicle;
import com.speedometer.calculator.app.adapters.VehicleAdapter;

import java.util.ArrayList;

public class VehicleBrandFragment extends BaseFragment {

    //views
    ListView listBrand;
    RelativeLayout rlBack;
    TextView txtNext;

    //variables
    ArrayList<Vehicle> oneSampleBrandList = new ArrayList<>();
    ArrayList<Vehicle> wholeBrandList = new ArrayList<>();
    ArrayList<String> brands = new ArrayList<>();
    int chosenVehiclePosition = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_vehicle_brand, container, false);

        initViews();
        setListeners();

        return baseView;
    }


    private void initViews() {
        listBrand = baseView.findViewById(R.id.list_brand);
        rlBack = baseView.findViewById(R.id.rl_header);
        txtNext = baseView.findViewById(R.id.txt_next);

        //filter brand list
        wholeBrandList.addAll(SQLiteHelper.getInstance(getActivity()).getVehicleList());
        filterBrandList();

        if (getActivity() != null) {
            VehicleAdapter adapterBrand = new VehicleAdapter(getActivity(), R.layout.cell_vehicle, oneSampleBrandList);
            adapterBrand.type = Constants.TYPE_BRAND;
            adapterBrand.callback = new AdapterCallback() {
                @Override
                public void onClickItem(int position) {
                    chosenVehiclePosition = position;
                    txtNext.setBackgroundResource(chosenVehiclePosition == -1 ? R.drawable.shadow_round_gray_01 : R.drawable.shadow_round_brown_01);
                }
            };
            listBrand.setAdapter(adapterBrand);
        }
    }

    private void filterBrandList() {
        for (Vehicle vehicle : wholeBrandList) {
            //check if vehicle brand is already added in list
            if (!checkIfVehicleBrandIsAdded(vehicle.getGeneralInfo().getBrand())) {
                //not add => add now
                brands.add(vehicle.getGeneralInfo().getBrand());
                oneSampleBrandList.add(vehicle);
            }
        }
    }

    private boolean checkIfVehicleBrandIsAdded(String brand) {
        for (String str : brands) {
            if (brand.equals(str)) {
                return true;
            }
        }

        return false;
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
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        //next
        txtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chosenVehiclePosition == -1) {
                    return;
                }

                //checked brand
                String brand = oneSampleBrandList.get(chosenVehiclePosition).getGeneralInfo().getBrand();

                //uncheck chosen vehicles brand
                ArrayList<Vehicle> modelList = new ArrayList<>();
                for (Vehicle vehicle : wholeBrandList) {
                    if (vehicle.getGeneralInfo().getBrand().equals(brand)) {
                        vehicle.setSelected(false);
                        modelList.add(vehicle);
                    }
                }

                //send chosen bran vehicle list
                VehicleModelFragment fragment = new VehicleModelFragment();
                fragment.modelList = modelList;
                addFragment(fragment, "MODEL_01");

            }
        });
    }
}
