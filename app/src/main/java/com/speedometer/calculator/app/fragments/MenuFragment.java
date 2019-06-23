package com.speedometer.calculator.app.fragments;

import android.content.Intent;
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

import com.speedometer.calculator.app.R;
import com.speedometer.calculator.app.activities.MainActivity;
import com.speedometer.calculator.app.constants.Constants;
import com.speedometer.calculator.app.model.RefreshCallback;

public class MenuFragment extends BaseFragment {

    //views
    ImageView img;
    LinearLayout llPassword;
    EditText edtPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        baseView = inflater.inflate(R.layout.fragment_menu, container, false);


        updateStatusBarColor(getResources().getColor(R.color.colorBlack));
        initViews();
        setListeners();

        return baseView;
    }

    private void initViews() {
        img = baseView.findViewById(R.id.img_vehicle);
        llPassword = baseView.findViewById(R.id.ll_password);
        edtPassword = baseView.findViewById(R.id.edt_password);
    }


    private void setListeners() {

        //base view
        baseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        //admin
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llPassword.setVisibility(View.VISIBLE);
            }
        });

        //ll password
        llPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llPassword.setVisibility(View.GONE);
            }
        });

        //edt password
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null && editable.toString().equals(Constants.ADMIN_PASSWORD)) {
                    edtPassword.setText("");
                    hideKeyboard();
                    llPassword.setVisibility(View.GONE);
                    img.setImageResource(R.drawable.img_car_02);
                    countDown(new RefreshCallback() {
                        @Override
                        public void doRefresh(boolean refresh) {
                            replaceFragmentWithRightAnimation(new AdminFragment(), "AdminFragment");
                        }
                    }, 500);
                }
            }
        });


        //start
        baseView.findViewById(R.id.txt_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img.setImageResource(R.drawable.img_car_02);
                countDown(new RefreshCallback() {
                    @Override
                    public void doRefresh(boolean refresh) {
                        replaceFragmentWithRightAnimation(new MethodFragment(), "MethodFragment");
                    }
                }, 500);
            }
        });

        //instruction
        baseView.findViewById(R.id.txt_instruction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new InstructionFragment(), "InstructionFragment");
            }
        });

        //historic
        baseView.findViewById(R.id.txt_historic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //about
        baseView.findViewById(R.id.txt_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //exit
        baseView.findViewById(R.id.txt_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Exit me", true);
                startActivity(intent);
                getActivity().finish();

            }
        });

        //exit
        baseView.findViewById(R.id.img_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Exit me", true);
                startActivity(intent);
                getActivity().finish();

            }
        });
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
}
