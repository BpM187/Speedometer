package com.speedometer.calculator.app.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.speedometer.calculator.app.GlobalSingleton;
import com.speedometer.calculator.app.R;
import com.speedometer.calculator.app.constants.Constants;
import com.speedometer.calculator.app.db.SQLiteHelper;
import com.speedometer.calculator.app.model.Dimensions;
import com.speedometer.calculator.app.model.Engine;
import com.speedometer.calculator.app.model.GeneralInfo;
import com.speedometer.calculator.app.model.ImageCallback;
import com.speedometer.calculator.app.model.Param;
import com.speedometer.calculator.app.model.Performance;
import com.speedometer.calculator.app.model.RefreshCallback;
import com.speedometer.calculator.app.model.Vehicle;
import com.speedometer.calculator.app.model.VolumeWeights;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class AdminCUFragment extends BaseFragment {
    //CU - CREATE, UPDATE

    //views
    RelativeLayout rlRoot;
    LinearLayout llContainer;
    TextView txtAction;

    //variables from other class
    public int state = Constants.STATE_CREATE;
    public Vehicle vehicleToUpdate;

    //variables
    ArrayList<Param> paramList = new ArrayList<>();
    String lastTitle = "";
    byte[] photoBrand = new byte[0];
    byte[] photoModel = new byte[0];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_admin_cu, container, false);

        updateStatusBarColor(getResources().getColor(R.color.colorBrown));
        initViews();

        if (state == Constants.STATE_UPDATE) {
            paramList = getVehicleParamList();
            txtAction.setText(R.string.edit);
            ((TextView) baseView.findViewById(R.id.txt_back)).setText(getString(R.string.crud_update));
        } else {
            paramList = getBasicParamList();
        }

        setParamList();
        setListeners();

        return baseView;
    }

    private void initViews() {
        rlRoot = baseView.findViewById(R.id.rl_root);
        llContainer = baseView.findViewById(R.id.ll_container);
        txtAction = baseView.findViewById(R.id.txt_action);
    }

    public ArrayList<Param> getBasicParamList() {
        ArrayList<Param> paramList = new ArrayList<>();

        paramList.add(new Param(getString(R.string.vehicle_brand), true, "", getString(R.string.vehicle_header_general_info)));
        paramList.add(new Param(getString(R.string.vehicle_brand_photo), true, "", getString(R.string.vehicle_header_general_info)));
        paramList.add(new Param(getString(R.string.vehicle_model), true, "", getString(R.string.vehicle_header_general_info)));
        paramList.add(new Param(getString(R.string.vehicle_model_photo), true, "", getString(R.string.vehicle_header_general_info)));
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

    public ArrayList<Param> getVehicleParamList() {
        ArrayList<Param> paramList = new ArrayList<>();
        photoBrand = vehicleToUpdate.getGeneralInfo().getPhotoBrand();
        photoModel = vehicleToUpdate.getGeneralInfo().getPhotoModel();
        byte[] emptyByte = new byte[0];


        paramList.add(new Param(getString(R.string.vehicle_brand), !vehicleToUpdate.getGeneralInfo().getBrand().equals(getString(R.string.blocked)), vehicleToUpdate.getGeneralInfo().getBrand(), getString(R.string.vehicle_header_general_info)));
        try {
            paramList.add(new Param(getString(R.string.vehicle_brand_photo), photoBrand != GlobalSingleton.getInstance().compress(getString(R.string.blocked)), !Arrays.equals(photoBrand, emptyByte) ? getString(R.string.exist) : "", getString(R.string.vehicle_header_general_info)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        paramList.add(new Param(getString(R.string.vehicle_model), !vehicleToUpdate.getGeneralInfo().getModel().equals(getString(R.string.blocked)), vehicleToUpdate.getGeneralInfo().getModel(), getString(R.string.vehicle_header_general_info)));
        try {
            paramList.add(new Param(getString(R.string.vehicle_model_photo), photoModel != GlobalSingleton.getInstance().compress(getString(R.string.blocked)), !Arrays.equals(photoModel, emptyByte) ? getString(R.string.exist) : "", getString(R.string.vehicle_header_general_info)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        paramList.add(new Param(getString(R.string.vehicle_generation), !vehicleToUpdate.getGeneralInfo().getGeneration().equals(getString(R.string.blocked)), vehicleToUpdate.getGeneralInfo().getGeneration(), getString(R.string.vehicle_header_general_info)));
        paramList.add(new Param(getString(R.string.vehicle_change_motor_type), !vehicleToUpdate.getGeneralInfo().getChangeMotorType().equals(getString(R.string.blocked)), vehicleToUpdate.getGeneralInfo().getChangeMotorType(), getString(R.string.vehicle_header_general_info)));
        paramList.add(new Param(getString(R.string.vehicle_own_weight), !vehicleToUpdate.getVolumeWeights().getWeight().equals(getString(R.string.blocked)), vehicleToUpdate.getVolumeWeights().getWeight(), getString(R.string.vehicle_header_volume_weights)));
        paramList.add(new Param(getString(R.string.vehicle_maxim_authorized_weight), !vehicleToUpdate.getVolumeWeights().getWeightMaxAuthorized().equals(getString(R.string.blocked)), vehicleToUpdate.getVolumeWeights().getWeightMaxAuthorized(), getString(R.string.vehicle_header_volume_weights)));
        paramList.add(new Param(getString(R.string.vehicle_minim_trunk_volume), !vehicleToUpdate.getVolumeWeights().getVolumeMinTrunk().equals(getString(R.string.blocked)), vehicleToUpdate.getVolumeWeights().getVolumeMinTrunk(), getString(R.string.vehicle_header_volume_weights)));
        paramList.add(new Param(getString(R.string.vehicle_maxim_trunk_volume), !vehicleToUpdate.getVolumeWeights().getVolumeMaxTrunk().equals(getString(R.string.blocked)), vehicleToUpdate.getVolumeWeights().getVolumeMaxTrunk(), getString(R.string.vehicle_header_volume_weights)));
        paramList.add(new Param(getString(R.string.vehicle_tank_volume), !vehicleToUpdate.getVolumeWeights().getVolumeTank().equals(getString(R.string.blocked)), vehicleToUpdate.getVolumeWeights().getVolumeTank(), getString(R.string.vehicle_header_volume_weights)));
        paramList.add(new Param(getString(R.string.vehicle_tank_adblue), !vehicleToUpdate.getVolumeWeights().getAdBlueTank().equals(getString(R.string.blocked)), vehicleToUpdate.getVolumeWeights().getAdBlueTank(), getString(R.string.vehicle_header_volume_weights)));
        paramList.add(new Param(getString(R.string.vehicle_length), !vehicleToUpdate.getDimensions().getLength().equals(getString(R.string.blocked)), vehicleToUpdate.getDimensions().getLength(), getString(R.string.vehicle_header_dimensions)));
        paramList.add(new Param(getString(R.string.vehicle_width), !vehicleToUpdate.getDimensions().getWidth().equals(getString(R.string.blocked)), vehicleToUpdate.getDimensions().getWidth(), getString(R.string.vehicle_header_dimensions)));
        paramList.add(new Param(getString(R.string.vehicle_width_mirrors), !vehicleToUpdate.getDimensions().getWidthWithMirrors().equals(getString(R.string.blocked)), vehicleToUpdate.getDimensions().getWidthWithMirrors(), getString(R.string.vehicle_header_dimensions)));
        paramList.add(new Param(getString(R.string.vehicle_height), !vehicleToUpdate.getDimensions().getHeight().equals(getString(R.string.blocked)), vehicleToUpdate.getDimensions().getHeight(), getString(R.string.vehicle_header_dimensions)));
        paramList.add(new Param(getString(R.string.vehicle_wheelbase), !vehicleToUpdate.getDimensions().getWheelbase().equals(getString(R.string.blocked)), vehicleToUpdate.getDimensions().getWheelbase(), getString(R.string.vehicle_header_dimensions)));
        paramList.add(new Param(getString(R.string.vehicle_gauge_front), !vehicleToUpdate.getDimensions().getGaugeFront().equals(getString(R.string.blocked)), vehicleToUpdate.getDimensions().getGaugeFront(), getString(R.string.vehicle_header_dimensions)));
        paramList.add(new Param(getString(R.string.vehicle_gauge_back), !vehicleToUpdate.getDimensions().getGaugeBack().equals(getString(R.string.blocked)), vehicleToUpdate.getDimensions().getGaugeBack(), getString(R.string.vehicle_header_dimensions)));
        paramList.add(new Param(getString(R.string.vehicle_fuel_consume_mixt), !vehicleToUpdate.getPerformance().getFuelConsume().equals(getString(R.string.blocked)), vehicleToUpdate.getPerformance().getFuelConsume(), getString(R.string.vehicle_header_performance)));
        paramList.add(new Param(getString(R.string.vehicle_fuel_type), !vehicleToUpdate.getPerformance().getFuelType().equals(getString(R.string.blocked)), vehicleToUpdate.getPerformance().getFuelType(), getString(R.string.vehicle_header_performance)));
        paramList.add(new Param(getString(R.string.vehicle_acceleration_0_100), !vehicleToUpdate.getPerformance().getAcceleration0to100().equals(getString(R.string.blocked)), vehicleToUpdate.getPerformance().getAcceleration0to100(), getString(R.string.vehicle_header_performance)));
        paramList.add(new Param(getString(R.string.vehicle_maxim_speed), !vehicleToUpdate.getPerformance().getMaximSpeed().equals(getString(R.string.blocked)), vehicleToUpdate.getPerformance().getMaximSpeed(), getString(R.string.vehicle_header_performance)));
        paramList.add(new Param(getString(R.string.vehicle_power), !vehicleToUpdate.getEngine().getPower().equals(getString(R.string.blocked)), vehicleToUpdate.getEngine().getPower(), getString(R.string.vehicle_header_engine)));
        paramList.add(new Param(getString(R.string.vehicle_torque), !vehicleToUpdate.getEngine().getTorque().equals(getString(R.string.blocked)), vehicleToUpdate.getEngine().getTorque(), getString(R.string.vehicle_header_engine)));
        paramList.add(new Param(getString(R.string.vehicle_coefficient), !vehicleToUpdate.getCoefficient().equals(getString(R.string.blocked)), vehicleToUpdate.getCoefficient(), getString(R.string.vehicle_header_coefficient)));

        txtAction.setBackgroundResource(R.drawable.shadow_round_brown_01);

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
        if (state == Constants.STATE_UPDATE) {
            //update
            imgCheck.setImageResource(param.isChecked() ? R.drawable.check_black : R.drawable.uncheck_black);
        }
        imgCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (param.getName().equals(getString(R.string.vehicle_brand))
                        || param.getName().equals(getString(R.string.vehicle_model))
                        || param.getName().equals(getString(R.string.vehicle_maxim_authorized_weight))
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
        final ImageView img = layout.findViewById(R.id.img);
        final EditText edt = layout.findViewById(R.id.edt);

        //value as image
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                addImage(param, edt, img);
            }
        });
        //value as txt
        if (param.getName().equals(getString(R.string.vehicle_brand_photo)) || param.getName().equals(getString(R.string.vehicle_model_photo))) {
            if (state == Constants.STATE_UPDATE) {
                if (param.getValue() != null && !param.getValue().isEmpty()) {
                    img.setVisibility(View.VISIBLE);
                    edt.setVisibility(View.GONE);
                    if (param.getName().equals(getString(R.string.vehicle_brand_photo))) {
                        if (vehicleToUpdate.getGeneralInfo().getPhotoBrand() != null) {
                            img.setImageBitmap(BitmapFactory.decodeByteArray(vehicleToUpdate.getGeneralInfo().getPhotoBrand(), 0, vehicleToUpdate.getGeneralInfo().getPhotoBrand().length));
                        }
                    } else if (param.getName().equals(getString(R.string.vehicle_model_photo))) {
                        if (vehicleToUpdate.getGeneralInfo().getPhotoBrand() != null) {
                            img.setImageBitmap(BitmapFactory.decodeByteArray(vehicleToUpdate.getGeneralInfo().getPhotoModel(), 0, vehicleToUpdate.getGeneralInfo().getPhotoModel().length));
                        }
                    }
                }
            }

            edt.setHint(R.string.add_photo);
            edt.setFocusable(false);
            edt.setClickable(true);

            edt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideKeyboard();
                    addImage(param, edt, img);
                }
            });
        } else {
            if (state == Constants.STATE_UPDATE) {
                edt.setText(param.isChecked() ? (param.getValue() != null ? param.getValue() : "") : getString(R.string.blocked));
            }
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

                    txtAction.setBackgroundResource(checkMandatoryParamsAreComplete().isEmpty() ? R.drawable.shadow_round_brown_01 : R.drawable.shadow_round_gray_01);
                }
            });
        }


        lastTitle = param.getCategory();

        return layout;
    }

    private void addImage(final Param param, final EditText edt, final ImageView img) {
        //take current image if ImageView has one
        img.invalidate();
        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
        Bitmap bitmap = null;
        if (drawable != null) {
            bitmap = drawable.getBitmap();
        }


        //send data to ImageAddFragment
        ImageAddFragment fragment = new ImageAddFragment();
        fragment.bitmap = bitmap;
        fragment.listener = new ImageCallback() {
            @Override
            public void getDetails(Bitmap bmp) {

                if (bmp == null) {
                    edt.setVisibility(View.VISIBLE);
                    img.setImageBitmap(null);
                    img.setVisibility(View.GONE);
                } else {
                    if (param.getName().contains("brand") || param.getName().contains("Brand")) {
                        photoBrand = GlobalSingleton.getInstance().compressBitmap(bmp);
                    } else {
                        photoModel = GlobalSingleton.getInstance().compressBitmap(bmp);
                    }
                    param.setValue("");
                    edt.setVisibility(View.GONE);
                    img.setImageBitmap(bmp);
                    img.setVisibility(View.VISIBLE);
                }

                updateStatusBarColor(getResources().getColor(R.color.colorBrown));
            }
        };
        addFragment(fragment, "IMG_01");
    }

    private String checkMandatoryParamsAreComplete() {
        if (paramList.get(0).getValue().isEmpty()) { //max auth weight
            return (paramList.get(0).getName() + " " + getString(R.string.parameter_must_complete));
        }
        if (paramList.get(2).getValue().isEmpty()) { //wheelbase
            return (paramList.get(2).getName() + " " + getString(R.string.parameter_must_complete));
        }
        if (paramList.get(7).getValue().isEmpty()) { //max auth weight
            return (paramList.get(7).getName() + " " + getString(R.string.parameter_must_complete));
        }
        if (paramList.get(16).getValue().isEmpty()) { //wheelbase
            return (paramList.get(16).getName() + " " + getString(R.string.parameter_must_complete));
        }
        if (paramList.get(23).getValue().isEmpty()) { //power
            return (paramList.get(23).getName() + " " + getString(R.string.parameter_must_complete));
        }
        if (paramList.get(25).getValue().isEmpty()) { // C(x)
            return (paramList.get(25).getName() + " " + getString(R.string.parameter_must_complete));
        }

        return "";
    }

    private Vehicle getVehicle() {
        String blocked = getString(R.string.blocked);

        GeneralInfo vehicleGeneralInfo = new GeneralInfo();
        vehicleGeneralInfo.setBrand(paramList.get(0).isChecked() ? paramList.get(0).getValue() : blocked);
//                vehicleGeneralInfo.setPhotoBrand(paramList.get(1).isChecked() ? paramList.get(1).getValue() : "");
        vehicleGeneralInfo.setModel(paramList.get(2).isChecked() ? paramList.get(2).getValue() : blocked);
//                vehicleGeneralInfo.setPhotoModel(paramList.get(3).isChecked() ? paramList.get(3).getValue() : "");
        vehicleGeneralInfo.setGeneration(paramList.get(4).isChecked() ? paramList.get(4).getValue() : blocked);
        vehicleGeneralInfo.setChangeMotorType(paramList.get(5).isChecked() ? paramList.get(5).getValue() : blocked);

        VolumeWeights vehicleVolumeWeights = new VolumeWeights();
        vehicleVolumeWeights.setWeight(paramList.get(6).isChecked() ? paramList.get(6).getValue() : blocked);
        vehicleVolumeWeights.setWeightMaxAuthorized(paramList.get(7).isChecked() ? paramList.get(7).getValue() : blocked);
        vehicleVolumeWeights.setVolumeMinTrunk(paramList.get(8).isChecked() ? paramList.get(8).getValue() : blocked);
        vehicleVolumeWeights.setVolumeMaxTrunk(paramList.get(9).isChecked() ? paramList.get(9).getValue() : blocked);
        vehicleVolumeWeights.setVolumeTank(paramList.get(10).isChecked() ? paramList.get(10).getValue() : blocked);
        vehicleVolumeWeights.setAdBlueTank(paramList.get(11).isChecked() ? paramList.get(11).getValue() : blocked);

        Dimensions vehicleDimensions = new Dimensions();
        vehicleDimensions.setLength(paramList.get(12).isChecked() ? paramList.get(12).getValue() : blocked);
        vehicleDimensions.setWidth(paramList.get(13).isChecked() ? paramList.get(13).getValue() : blocked);
        vehicleDimensions.setWidthWithMirrors(paramList.get(14).isChecked() ? paramList.get(14).getValue() : blocked);
        vehicleDimensions.setHeight(paramList.get(15).isChecked() ? paramList.get(15).getValue() : blocked);
        vehicleDimensions.setWheelbase(paramList.get(16).isChecked() ? paramList.get(16).getValue() : blocked);
        vehicleDimensions.setGaugeFront(paramList.get(17).isChecked() ? paramList.get(17).getValue() : blocked);
        vehicleDimensions.setGaugeBack(paramList.get(18).isChecked() ? paramList.get(18).getValue() : blocked);

        Performance vehiclePerformance = new Performance();
        vehiclePerformance.setFuelConsume(paramList.get(19).isChecked() ? paramList.get(19).getValue() : blocked);
        vehiclePerformance.setFuelType(paramList.get(20).isChecked() ? paramList.get(20).getValue() : blocked);
        vehiclePerformance.setAcceleration0to100(paramList.get(21).isChecked() ? paramList.get(21).getValue() : blocked);
        vehiclePerformance.setMaximSpeed(paramList.get(22).isChecked() ? paramList.get(22).getValue() : blocked);

        Engine vehicleEngine = new Engine();
        vehicleEngine.setPower(paramList.get(23).isChecked() ? paramList.get(23).getValue() : blocked);
        vehicleEngine.setTorque(paramList.get(24).isChecked() ? paramList.get(24).getValue() : blocked);

        Vehicle vehicle = new Vehicle();
        vehicle.setGeneralInfo(vehicleGeneralInfo);
        vehicle.setVolumeWeights(vehicleVolumeWeights);
        vehicle.setDimensions(vehicleDimensions);
        vehicle.setPerformance(vehiclePerformance);
        vehicle.setEngine(vehicleEngine);
        vehicle.setCoefficient(paramList.get(25).isChecked() ? paramList.get(25).getValue() : blocked);

        return vehicle;
    }

    private void countDown(final RefreshCallback refreshCallback, int time) {
        CountDownTimer count = new CountDownTimer(time, 500) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                if (refreshCallback != null) {
                    refreshCallback.doRefresh(true);
                }
            }
        };
        count.start();
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


        //add/update
        txtAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();

                //check if mandatory params are complete
                String mandatory = checkMandatoryParamsAreComplete();
                if (!mandatory.isEmpty()) {
                    showToast(mandatory);
                    return;
                }

                //add to database
                String str = new Gson().toJson(getVehicle());
                try {
                    byte[] compressed = GlobalSingleton.getInstance().compress(str);

                    if (state == Constants.STATE_CREATE) {
                        SQLiteHelper.getInstance(getActivity()).createVehicle(
                                compressed,
                                paramList.get(1).isChecked() ? photoBrand : GlobalSingleton.getInstance().compress(getString(R.string.blocked)),
                                paramList.get(3).isChecked() ? photoModel : GlobalSingleton.getInstance().compress(getString(R.string.blocked)));
                    } else if (state == Constants.STATE_UPDATE) {
                        SQLiteHelper.getInstance(getActivity()).updateVehicle(
                                vehicleToUpdate.getId(),
                                compressed,
                                paramList.get(1).isChecked() ? photoBrand : GlobalSingleton.getInstance().compress(getString(R.string.blocked)),
                                paramList.get(3).isChecked() ? photoModel : GlobalSingleton.getInstance().compress(getString(R.string.blocked)));
                    }


                    if (state == Constants.STATE_UPDATE) {
                        ((TextView) baseView.findViewById(R.id.txt_alert)).setText(getString(R.string.alert_vehicle_updated));
                    }
                    baseView.findViewById(R.id.ll_alert).setVisibility(View.VISIBLE);
                    updateStatusBarColor(getResources().getColor(R.color.colorBrownDark));

                    countDown(new RefreshCallback() {
                        @Override
                        public void doRefresh(boolean refresh) {
                            //go back
                            if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        }
                    }, 500);


                } catch (IOException e) {
                    showToast(getString(R.string.error_database));
                    e.printStackTrace();
                }
            }
        });
    }


}
