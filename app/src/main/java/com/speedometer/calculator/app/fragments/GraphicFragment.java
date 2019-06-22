package com.speedometer.calculator.app.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.speedometer.calculator.app.R;
import com.speedometer.calculator.app.model.Result;

import java.util.ArrayList;

public class GraphicFragment extends BaseFragment {

    //views

    //variables received from other class
    public ArrayList<Result> resultList;

    //variables

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_graphic, container, false);

        updateStatusBarColor(getResources().getColor(R.color.colorBrown));
        initViews();
        setListeners();

        return baseView;
    }

    private void initViews() {

    }

    private void setListeners() {

        //block base view
        baseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




    }
}
