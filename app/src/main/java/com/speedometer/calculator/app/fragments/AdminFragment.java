package com.speedometer.calculator.app.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.speedometer.calculator.app.R;
import com.speedometer.calculator.app.constants.Constants;

public class AdminFragment extends BaseFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_admin, container, false);

        updateStatusBarColor(getResources().getColor(R.color.colorBrown));
        setListeners();

        return baseView;
    }

    private void setListeners() {
        //base view
        baseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        //back
        baseView.findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        //create
        baseView.findViewById(R.id.img_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragmentWithBottomAnimation(new AdminCUFragment(), "AdminCUFragment");
            }
        });

        //read
        baseView.findViewById(R.id.img_read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragmentWithBottomAnimation(new AdminRUDFragment(), "AdminRUDFragment");
            }
        });

        //update
        baseView.findViewById(R.id.img_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminRUDFragment fragment = new AdminRUDFragment();
                fragment.state = Constants.STATE_UPDATE;
                replaceFragmentWithBottomAnimation(fragment, "AdminRUDFragment");
            }
        });

        //delete
        baseView.findViewById(R.id.img_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminRUDFragment fragment = new AdminRUDFragment();
                fragment.state = Constants.STATE_DELETE;
                replaceFragmentWithBottomAnimation(fragment, "AdminRUDFragment");
            }
        });


    }
}
