package com.speedometer.calculator.app.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.speedometer.calculator.app.R;
import com.speedometer.calculator.app.adapters.CustomPagerAdapter;

public class InstructionFragment extends BaseFragment {

    //views
    ViewPager viewPager;

    //variables
    CustomPagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_instruction, container, false);

        initViews();
        setListeners();

        return baseView;
    }

    private void initViews() {
        viewPager = baseView.findViewById(R.id.view_pager);
    }

    private void setListeners() {
        //block base view events
        baseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }
}
