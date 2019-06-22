package com.speedometer.calculator.app.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.speedometer.calculator.app.R;

public class ImageShowFragment extends BaseFragment {

    //variables from previous fragment
    public Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_image_show, container, false);

        if (bitmap != null) {
            ((ImageView) baseView.findViewById(R.id.img)).setImageBitmap(bitmap);
        }

        baseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        return baseView;
    }
}
