package com.speedometer.calculator.app.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.speedometer.calculator.app.R;
import com.speedometer.calculator.app.constants.Constants;
import com.speedometer.calculator.app.model.AdapterCallback;
import com.speedometer.calculator.app.model.Vehicle;

import java.util.ArrayList;

public class VehicleAdapter extends ArrayAdapter<Vehicle> {

    public int type;
    public AdapterCallback callback;

    public VehicleAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Vehicle> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.cell_vehicle, null);
        }

        try {
            final Vehicle vehicle = getItem(position);
            if (type == Constants.TYPE_BRAND) {
                ((TextView) convertView.findViewById(R.id.txt)).setText(vehicle.getGeneralInfo().getBrand());
//                ((ImageView) convertView.findViewById(R.id.img)).setImageResource(getContext().getResources().getIdentifier(vehicle.getPhotoBrand(), "raw", getContext().getPackageName()));
                ((ImageView) convertView.findViewById(R.id.img)).setImageBitmap(BitmapFactory.decodeByteArray(vehicle.getGeneralInfo().getPhotoBrand(), 0, vehicle.getGeneralInfo().getPhotoBrand().length));
            } else {
                ((TextView) convertView.findViewById(R.id.txt)).setText(vehicle.getGeneralInfo().getModel());
//                ((ImageView) convertView.findViewById(R.id.img)).setImageResource(getContext().getResources().getIdentifier(vehicle.getPhotoModel(), "raw", getContext().getPackageName()));
                ((ImageView) convertView.findViewById(R.id.img)).setImageBitmap(BitmapFactory.decodeByteArray(vehicle.getGeneralInfo().getPhotoModel(), 0, vehicle.getGeneralInfo().getPhotoModel().length));
            }


            RelativeLayout rlCheck = convertView.findViewById(R.id.rl_check);

            if (vehicle.selected) {
                rlCheck.setVisibility(View.VISIBLE);
            } else {
                rlCheck.setVisibility(View.GONE);
            }

            final View finalConvertView = convertView;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uncheckAll(position);
                    vehicle.selected = !vehicle.selected;
                    finalConvertView.findViewById(R.id.rl_check).setVisibility(vehicle.selected ? View.VISIBLE : View.GONE);
                    notifyDataSetChanged();

                    if (callback != null) {
                        callback.onClickItem(vehicle.selected ? position : -1);
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }


    private void uncheckAll(int position) {
        for (int i = 0; i < getCount(); i++) {
            if (position != i) {
                getItem(i).selected = false;
            }
        }
    }
}
