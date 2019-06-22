package com.speedometer.calculator.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.speedometer.calculator.app.R;
import com.speedometer.calculator.app.activities.LocationActivity;
import com.speedometer.calculator.app.model.Dimensions;
import com.speedometer.calculator.app.model.Engine;
import com.speedometer.calculator.app.model.GeneralInfo;
import com.speedometer.calculator.app.model.Param;
import com.speedometer.calculator.app.model.Performance;
import com.speedometer.calculator.app.model.Vehicle;
import com.speedometer.calculator.app.model.VolumeWeights;

import java.util.ArrayList;

public class ParamsFragment extends BaseFragment {

    //views
    LinearLayout llContainer;
    TextView txtStart;

    //variables
    ArrayList<Param> paramList = new ArrayList<>();
    String lastTitle = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_init_params, container, false);

        initViews();
        paramList = getBasicParamList();
        setParamList();
        setListeners();

        return baseView;
    }

    private void initViews() {
        llContainer = baseView.findViewById(R.id.ll_container);
        txtStart = baseView.findViewById(R.id.txt_start);
    }

    public ArrayList<Param> getBasicParamList() {
        ArrayList<Param> paramList = new ArrayList<>();

        paramList.add(new Param(getString(R.string.vehicle_generation), true, "", getString(R.string.vehicle_header_general_info)));
        paramList.add(new Param(getString(R.string.vehicle_change_motor_type), true, "", getString(R.string.vehicle_header_general_info)));
        paramList.add(new Param(getString(R.string.vehicle_own_weight), true, "", getString(R.string.vehicle_header_volume_weights)));
        paramList.add(new Param(getString(R.string.vehicle_maxim_authorized_weight), true, "", getString(R.string.vehicle_header_volume_weights)));
        paramList.add(new Param(getString(R.string.vehicle_minim_trunk_volume), true, "", getString(R.string.vehicle_header_volume_weights)));
        paramList.add(new Param(getString(R.string.vehicle_maxim_trunk_volume), true, "", getString(R.string.vehicle_header_volume_weights)));
        paramList.add(new Param(getString(R.string.vehicle_tank_volume), true, "", getString(R.string.vehicle_header_volume_weights)));
        paramList.add(new Param(getString(R.string.vehicle_tank_adblue), true, "", getString(R.string.vehicle_header_volume_weights)));
        paramList.add(new Param(getString(R.string.vehicle_length), true, "", getString(R.string.vehicle_header_dimensions)));
        paramList.add(new Param(getString(R.string.vehicle_width), true, "", getString(R.string.vehicle_header_dimensions)));
        paramList.add(new Param(getString(R.string.vehicle_width_mirrors), true, "", getString(R.string.vehicle_header_dimensions)));
        paramList.add(new Param(getString(R.string.vehicle_height), true, "", getString(R.string.vehicle_header_dimensions)));
        paramList.add(new Param(getString(R.string.vehicle_wheelbase), true, "", getString(R.string.vehicle_header_dimensions)));
        paramList.add(new Param(getString(R.string.vehicle_gauge_front), true, "", getString(R.string.vehicle_header_dimensions)));
        paramList.add(new Param(getString(R.string.vehicle_gauge_back), true, "", getString(R.string.vehicle_header_dimensions)));
        paramList.add(new Param(getString(R.string.vehicle_fuel_consume_mixt), true, "", getString(R.string.vehicle_header_performance)));
        paramList.add(new Param(getString(R.string.vehicle_fuel_type), true, "", getString(R.string.vehicle_header_performance)));
        paramList.add(new Param(getString(R.string.vehicle_acceleration_0_100), true, "", getString(R.string.vehicle_header_performance)));
        paramList.add(new Param(getString(R.string.vehicle_maxim_speed), true, "", getString(R.string.vehicle_header_performance)));
        paramList.add(new Param(getString(R.string.vehicle_power), true, "", getString(R.string.vehicle_header_engine)));
        paramList.add(new Param(getString(R.string.vehicle_torque), true, "", getString(R.string.vehicle_header_engine)));
        paramList.add(new Param(getString(R.string.vehicle_coefficient), true, "", getString(R.string.vehicle_header_coefficient)));

        return paramList;
    }


    private void setParamList() {
        for (Param param : paramList) {
            llContainer.addView(getParamView(param));
        }
    }

    private View getParamView(final Param param) {
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.cell_params, null);
        layout.setTag(llContainer.getChildCount());

        //title
        if (!param.getCategory().equals(lastTitle)) {
            TextView txtTitle = layout.findViewById(R.id.txt_title);
            txtTitle.setText(param.getCategory());
            txtTitle.setVisibility(View.VISIBLE);
        }

        //check
        final ImageView imgCheck = layout.findViewById(R.id.img_check);
        imgCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (param.getName().equals(getString(R.string.vehicle_maxim_authorized_weight))
                        || param.getName().equals(getString(R.string.vehicle_wheelbase))
                        || param.getName().equals(getString(R.string.vehicle_power))
                        || param.getName().equals(getString(R.string.vehicle_coefficient))) {
                    showToast(param.getName() + " " + getString(R.string.parameter_mandatory));
                    return;
                }

                param.setChecked(!param.isChecked());

                if (param.isChecked()) {
                    imgCheck.setImageResource(R.drawable.check_black);
                } else {
                    imgCheck.setImageResource(R.drawable.uncheck_black);
                }
            }
        });

        //name
        ((TextView) layout.findViewById(R.id.txt)).setText(param.getName());

        //values
        final EditText edt = layout.findViewById(R.id.edt);
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null) {
                    param.setValue(editable.toString());
                }

                txtStart.setBackgroundResource(checkMandatoryParamsAreComplete().isEmpty() ? R.drawable.shadow_round_brown_01 : R.drawable.shadow_round_gray_01);
            }
        });

        lastTitle = param.getCategory();

        return layout;
    }

    private Vehicle getAllParams() {
        String blocked = getString(R.string.blocked);

        GeneralInfo vehicleGeneralInfo = new GeneralInfo();
        vehicleGeneralInfo.setBrand("");
        vehicleGeneralInfo.setPhotoBrand(new byte[0]);
        vehicleGeneralInfo.setModel("");
        vehicleGeneralInfo.setPhotoModel(new byte[0]);
        vehicleGeneralInfo.setGeneration(paramList.get(0).isChecked() ? paramList.get(0).getValue() : blocked);
        vehicleGeneralInfo.setChangeMotorType(paramList.get(1).isChecked() ? paramList.get(1).getValue() : blocked);

        VolumeWeights vehicleVolumeWeights = new VolumeWeights();
        vehicleVolumeWeights.setWeight(paramList.get(2).isChecked() ? paramList.get(2).getValue() : blocked);
        vehicleVolumeWeights.setWeightMaxAuthorized(paramList.get(3).isChecked() ? paramList.get(3).getValue() : blocked);
        vehicleVolumeWeights.setVolumeMinTrunk(paramList.get(4).isChecked() ? paramList.get(4).getValue() : blocked);
        vehicleVolumeWeights.setVolumeMaxTrunk(paramList.get(5).isChecked() ? paramList.get(5).getValue() : blocked);
        vehicleVolumeWeights.setVolumeTank(paramList.get(6).isChecked() ? paramList.get(6).getValue() : blocked);
        vehicleVolumeWeights.setAdBlueTank(paramList.get(7).isChecked() ? paramList.get(7).getValue() : blocked);

        Dimensions vehicleDimensions = new Dimensions();
        vehicleDimensions.setLength(paramList.get(8).isChecked() ? paramList.get(8).getValue() : blocked);
        vehicleDimensions.setWidth(paramList.get(9).isChecked() ? paramList.get(9).getValue() : blocked);
        vehicleDimensions.setWidthWithMirrors(paramList.get(10).isChecked() ? paramList.get(10).getValue() : blocked);
        vehicleDimensions.setHeight(paramList.get(11).isChecked() ? paramList.get(11).getValue() : blocked);
        vehicleDimensions.setWheelbase(paramList.get(12).isChecked() ? paramList.get(12).getValue() : blocked);
        vehicleDimensions.setGaugeFront(paramList.get(13).isChecked() ? paramList.get(13).getValue() : blocked);
        vehicleDimensions.setGaugeBack(paramList.get(14).isChecked() ? paramList.get(14).getValue() : blocked);

        Performance vehiclePerformance = new Performance();
        vehiclePerformance.setFuelConsume(paramList.get(15).isChecked() ? paramList.get(15).getValue() : blocked);
        vehiclePerformance.setFuelType(paramList.get(16).isChecked() ? paramList.get(16).getValue() : blocked);
        vehiclePerformance.setAcceleration0to100(paramList.get(17).isChecked() ? paramList.get(17).getValue() : blocked);
        vehiclePerformance.setMaximSpeed(paramList.get(18).isChecked() ? paramList.get(18).getValue() : blocked);

        Engine vehicleEngine = new Engine();
        vehicleEngine.setPower(paramList.get(19).isChecked() ? paramList.get(19).getValue() : blocked);
        vehicleEngine.setTorque(paramList.get(20).isChecked() ? paramList.get(20).getValue() : blocked);

        Vehicle vehicle = new Vehicle();
        vehicle.setGeneralInfo(vehicleGeneralInfo);
        vehicle.setVolumeWeights(vehicleVolumeWeights);
        vehicle.setDimensions(vehicleDimensions);
        vehicle.setPerformance(vehiclePerformance);
        vehicle.setEngine(vehicleEngine);
        vehicle.setCoefficient(paramList.get(21).isChecked() ? paramList.get(21).getValue() : blocked);

        return vehicle;
    }

    private String checkMandatoryParamsAreComplete() {
        if (paramList.get(3).getValue().isEmpty()) { //max auth weight
            return (paramList.get(3).getName() + " " + getString(R.string.parameter_must_complete));

        }
        if (paramList.get(12).getValue().isEmpty()) { //wheelbase
            return (paramList.get(12).getName() + " " + getString(R.string.parameter_must_complete));
        }
        if (paramList.get(19).getValue().isEmpty()) { //power
            return (paramList.get(19).getName() + " " + getString(R.string.parameter_must_complete));

        }
        if (paramList.get(21).getValue().isEmpty()) { // C(x)
            return (paramList.get(21).getName() + " " + getString(R.string.parameter_must_complete));
        }

        return "";
    }

    private void setListeners() {

        //base view
        baseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //back
        baseView.findViewById(R.id.rl_header).setOnClickListener(new View.OnClickListener() {
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

                //check if mandatory params are complete
                String mandatory = checkMandatoryParamsAreComplete();
                if (!mandatory.isEmpty()) {
                    showToast(mandatory);
                    return;
                }

                //send params to LocationActivity
                Intent intent = new Intent(getActivity(), LocationActivity.class);
                intent.putExtra("params", getAllParams());
                startActivity(intent);
            }
        });
    }
}
