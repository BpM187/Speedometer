package com.speedometer.calculator.app.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.speedometer.calculator.app.R;
import com.speedometer.calculator.app.model.Instruction;

import java.util.ArrayList;

public class CustomPagerAdapter extends PagerAdapter {

    ArrayList<Instruction> instructionList;
    LayoutInflater inflater;
    Context context;

    public CustomPagerAdapter(ArrayList<Instruction> instructionList, Context context) {
        this.instructionList = instructionList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return instructionList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cell_instructions, container, false);
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
