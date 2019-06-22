package com.speedometer.calculator.app.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.speedometer.calculator.app.R;
import com.speedometer.calculator.app.constants.Constants;
import com.speedometer.calculator.app.model.AdapterCallback;
import com.speedometer.calculator.app.model.Vehicle;

import java.util.List;

public class VehicleDetailsAdapter extends ArrayAdapter<Vehicle> {

    //variables from other class
    public int state = Constants.STATE_READ;
    public AdapterCallback listener;

    public VehicleDetailsAdapter(@NonNull Context context, int resource, @NonNull List<Vehicle> objects) {
        super(context, resource, objects);
    }


    class viewHolder {

        //general
        ImageView imgCar;
        TextView txtCar;
        TextView txtGeneration;
        TextView txtMotorType;

        //actions
        ImageView imgAction;
        ImageView imgArrow;

        //content layout
        LinearLayout llContent;

        //volume and weights
        TextView txtWeightOwn;
        TextView txtWeightMax;
        TextView txtVolumeMinTrunk;
        TextView txtVolumeMaxTrunk;
        TextView txtVolumeTrunk;
        TextView txtAdBlueTrunk;

        //dimensions
        TextView txtLength;
        TextView txtWidth;
        TextView txtWidthMirrors;
        TextView txtHeight;
        TextView txtWheelbase;
        TextView txtGaugeFront;
        TextView txtGaugeBack;

        //performance
        TextView txtFuelConsume;
        TextView txtFuelType;
        TextView txtAcceleratin0To100;
        TextView txtSpeedMax;

        //engine
        TextView txtPower;
        TextView txtTorque;

        //coefficients
        TextView txtCoefficient;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.cell_vehicle_details, null);

            viewHolder holder = new viewHolder();

            //general
            holder.imgCar = convertView.findViewById(R.id.img_car);
            holder.txtCar = convertView.findViewById(R.id.txt_car);
            holder.txtGeneration = convertView.findViewById(R.id.txt_generation);
            holder.txtMotorType = convertView.findViewById(R.id.txt_change_motor_type);

            //actions
            holder.imgAction = convertView.findViewById(R.id.img_action);
            holder.imgArrow = convertView.findViewById(R.id.img_arrow);

            //layout
            holder.llContent = convertView.findViewById(R.id.ll_content);

            //volume and weights
            holder.txtWeightOwn = convertView.findViewById(R.id.txt_weight_own);
            holder.txtWeightMax = convertView.findViewById(R.id.txt_weight_max);
            holder.txtVolumeMinTrunk = convertView.findViewById(R.id.txt_volume_min_trunk);
            holder.txtVolumeMaxTrunk = convertView.findViewById(R.id.txt_volume_max_trunk);
            holder.txtVolumeTrunk = convertView.findViewById(R.id.txt_volume_trunk);
            holder.txtAdBlueTrunk = convertView.findViewById(R.id.txt_adblue_tank);

            //dimensions
            holder.txtLength = convertView.findViewById(R.id.txt_length);
            holder.txtWidth = convertView.findViewById(R.id.txt_width);
            holder.txtWidthMirrors = convertView.findViewById(R.id.txt_width_mirrors);
            holder.txtHeight = convertView.findViewById(R.id.txt_height);
            holder.txtWheelbase = convertView.findViewById(R.id.txt_wheelbase);
            holder.txtGaugeFront = convertView.findViewById(R.id.txt_gauge_front);
            holder.txtGaugeBack = convertView.findViewById(R.id.txt_gauge_back);

            //performance
            holder.txtFuelConsume = convertView.findViewById(R.id.txt_fuel_consume);
            holder.txtFuelType = convertView.findViewById(R.id.txt_fuel_type);
            holder.txtAcceleratin0To100 = convertView.findViewById(R.id.txt_acceleration_0_100);
            holder.txtSpeedMax = convertView.findViewById(R.id.txt_maxim_speed);

            //engine
            holder.txtPower = convertView.findViewById(R.id.txt_power);
            holder.txtTorque = convertView.findViewById(R.id.txt_torque);

            //coefficients
            holder.txtCoefficient = convertView.findViewById(R.id.txt_coefficient);

            convertView.setTag(holder);
        }

        final viewHolder holder = (viewHolder) convertView.getTag();
        final Vehicle vehicle = getItem(position);

        try {
            //general information
            holder.imgCar.setImageBitmap(BitmapFactory.decodeByteArray(vehicle.getGeneralInfo().getPhotoModel(), 0, vehicle.getGeneralInfo().getPhotoModel().length));
            holder.txtCar.setText(String.valueOf(vehicle.getGeneralInfo().getBrand() + " " + vehicle.getGeneralInfo().getModel()));
            holder.txtGeneration.setText(String.valueOf(getContext().getString(R.string.vehicle_generation) + ": " + vehicle.getGeneralInfo().getGeneration()));
            holder.txtMotorType.setText(String.valueOf(getContext().getString(R.string.vehicle_change_motor_type) + ": " + vehicle.getGeneralInfo().getChangeMotorType()));

            //volume and weights
            holder.txtWeightOwn.setText(String.valueOf(getContext().getString(R.string.vehicle_own_weight) + ": " + vehicle.getVolumeWeights().getWeight()));
            holder.txtWeightMax.setText(String.valueOf(getContext().getString(R.string.vehicle_maxim_authorized_weight) + ": " + vehicle.getVolumeWeights().getWeightMaxAuthorized()));
            holder.txtVolumeMinTrunk.setText(String.valueOf(getContext().getString(R.string.vehicle_minim_trunk_volume) + ": " + vehicle.getVolumeWeights().getVolumeMinTrunk()));
            holder.txtVolumeMaxTrunk.setText(String.valueOf(getContext().getString(R.string.vehicle_maxim_trunk_volume) + ": " + vehicle.getVolumeWeights().getVolumeMaxTrunk()));
            holder.txtVolumeTrunk.setText(String.valueOf(getContext().getString(R.string.vehicle_tank_volume) + ": " + vehicle.getVolumeWeights().getVolumeTank()));
            holder.txtAdBlueTrunk.setText(String.valueOf(getContext().getString(R.string.vehicle_tank_adblue) + ": " + vehicle.getVolumeWeights().getAdBlueTank()));

            //dimensions
            holder.txtLength.setText(String.valueOf(getContext().getString(R.string.vehicle_length) + ": " + vehicle.getDimensions().getLength()));
            holder.txtWidth.setText(String.valueOf(getContext().getString(R.string.vehicle_width) + ": " + vehicle.getDimensions().getWidth()));
            holder.txtWidthMirrors.setText(String.valueOf(getContext().getString(R.string.vehicle_width_mirrors) + ": " + vehicle.getDimensions().getWidthWithMirrors()));
            holder.txtHeight.setText(String.valueOf(getContext().getString(R.string.vehicle_height) + ": " + vehicle.getDimensions().getHeight()));
            holder.txtWheelbase.setText(String.valueOf(getContext().getString(R.string.vehicle_wheelbase) + ": " + vehicle.getDimensions().getWheelbase()));
            holder.txtGaugeFront.setText(String.valueOf(getContext().getString(R.string.vehicle_gauge_front) + ": " + vehicle.getDimensions().getGaugeFront()));
            holder.txtGaugeBack.setText(String.valueOf(getContext().getString(R.string.vehicle_gauge_back) + ": " + vehicle.getDimensions().getGaugeBack()));

            //performance
            holder.txtFuelConsume.setText(String.valueOf(getContext().getString(R.string.vehicle_fuel_consume_mixt) + ": " + vehicle.getPerformance().getFuelConsume()));
            holder.txtFuelType.setText(String.valueOf(getContext().getString(R.string.vehicle_fuel_type) + ": " + vehicle.getPerformance().getFuelType()));
            holder.txtAcceleratin0To100.setText(String.valueOf(getContext().getString(R.string.vehicle_acceleration_0_100) + ": " + vehicle.getPerformance().getAcceleration0to100()));
            holder.txtSpeedMax.setText(String.valueOf(getContext().getString(R.string.vehicle_maxim_speed) + ": " + vehicle.getPerformance().getMaximSpeed()));

            //engine
            holder.txtPower.setText(String.valueOf(getContext().getString(R.string.vehicle_power) + ": " + vehicle.getEngine().getPower()));
            holder.txtTorque.setText(String.valueOf(getContext().getString(R.string.vehicle_torque) + ": " + vehicle.getEngine().getTorque()));

            //coefficients
            holder.txtCoefficient.setText(String.valueOf(getContext().getString(R.string.vehicle_coefficient) + ": " + vehicle.getCoefficient()));

            if (vehicle.isMaximized()) {
                holder.imgArrow.setImageResource(R.drawable.arrow_up_brown);
                holder.llContent.setVisibility(View.VISIBLE);
            } else {
                holder.imgArrow.setImageResource(R.drawable.arrow_down_brown);
                holder.llContent.setVisibility(View.GONE);
            }

            holder.imgArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    vehicle.setMaximized(!vehicle.isMaximized());
                    if (vehicle.isMaximized()) {
                        holder.imgArrow.setImageResource(R.drawable.arrow_up_brown);
                        holder.llContent.setVisibility(View.VISIBLE);
                    } else {
                        holder.imgArrow.setImageResource(R.drawable.arrow_down_brown);
                        holder.llContent.setVisibility(View.GONE);
                    }
                }
            });

            holder.imgAction.setVisibility(state == Constants.STATE_READ ? View.GONE : View.VISIBLE);
            holder.imgAction.setImageResource(state == Constants.STATE_UPDATE ? R.drawable.icon_edit_brown : R.drawable.icon_delete);
            holder.imgAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onClickItem(position);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


        return convertView;
    }
}
