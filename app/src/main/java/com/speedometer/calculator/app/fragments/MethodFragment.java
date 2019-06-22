package com.speedometer.calculator.app.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.speedometer.calculator.app.R;
import com.speedometer.calculator.app.model.RefreshCallback;


public class MethodFragment extends BaseFragment {

    //views
    ImageView imgParams, imgVehicle;
    RelativeLayout rlBack;


    //variables


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_method, container, false);

        initViews();
        updateStatusBarColor(getResources().getColor(R.color.colorBrown));
        setListeners();

        return baseView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() != null && !getActivity().isFinishing() && imgParams != null && imgVehicle != null) {
            imgParams.setAlpha(0.5f);
            imgVehicle.setAlpha(0.5f);
        }
    }

    private void initViews() {
        imgParams = baseView.findViewById(R.id.img_params);
        imgVehicle = baseView.findViewById(R.id.img_vehicle);
        rlBack = baseView.findViewById(R.id.rl_header);


        imgParams.setAlpha(0.5f);
        imgVehicle.setAlpha(0.5f);
    }

    private void setListeners() {
        //base view
        baseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //init params
        baseView.findViewById(R.id.rl_params).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imgParams.setAlpha(1f);
                countDown(new RefreshCallback() {
                    @Override
                    public void doRefresh(boolean refresh) {
                        replaceFragmentWithRightAnimation(new ParamsFragment(), "INTRO_PARAMS_01");
                    }
                }, 500);
            }
        });

        //choose vehicle
        baseView.findViewById(R.id.rl_vehicle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgVehicle.setAlpha(1f);
                countDown(new RefreshCallback() {
                    @Override
                    public void doRefresh(boolean refresh) {
                        replaceFragmentWithRightAnimation(new VehicleBrandFragment(), "CHOOSE_VEHICLE_01");
                    }
                }, 500);
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
    }


    private void countDown(final RefreshCallback listener, int time) {
        CountDownTimer count = new CountDownTimer(time, 500) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if (listener != null) {
                    listener.doRefresh(true);
                }
            }
        };

        count.start();
    }
}
