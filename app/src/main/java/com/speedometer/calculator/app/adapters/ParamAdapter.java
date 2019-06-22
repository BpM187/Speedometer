package com.speedometer.calculator.app.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.speedometer.calculator.app.R;
import com.speedometer.calculator.app.model.Param;

import java.util.List;

public class ParamAdapter extends ArrayAdapter<Param> {

    private int resource;

    public ParamAdapter(@NonNull Context context, int resource, @NonNull List<Param> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    class viewHolder {
        TextView txtTitle;
        ImageView imgCheck;
        TextView txtName;
        EditText edtValue;
        ImageView imgPhoto;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(getContext(), resource, null);

            viewHolder holder = new viewHolder();
            holder.txtTitle = convertView.findViewById(R.id.txt_title);
            holder.imgCheck = convertView.findViewById(R.id.img_check);
            holder.txtName = convertView.findViewById(R.id.txt);
            holder.edtValue = convertView.findViewById(R.id.edt);
            holder.imgPhoto = convertView.findViewById(R.id.img);

            convertView.setTag(holder);
        }

        viewHolder holder = (viewHolder) convertView.getTag();
        Param param = getItem(position);

        holder.txtTitle.setVisibility(View.GONE);
        holder.imgCheck.setVisibility(View.GONE);
        holder.imgPhoto.setVisibility(View.GONE);
        holder.edtValue.setEnabled(false);

        try {
            if (param != null) {
                holder.txtName.setText(param.getName());
                holder.edtValue.setText(param.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
