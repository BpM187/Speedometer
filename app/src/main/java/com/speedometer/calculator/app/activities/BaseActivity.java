package com.speedometer.calculator.app.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.speedometer.calculator.app.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        updateStatusBarColor(getResources().getColor(R.color.colorBlack));
    }


    public void setWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            updateStatusBarColor(getResources().getColor(R.color.colorWhite, getTheme()));
        }
    }

    public void updateStatusBarColor(int color) {// Color must be in hexadecimal fromat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        } else {
            //nothing
        }
    }

    public void addFragment(Fragment fragment, String name) {
        this.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack(name)
                .commit();
    }

    public void replaceFragment(Fragment fragment, String name) {
        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(name)
                .commit();
    }

    public void replaceFragmentWithRightAnimation(Fragment fragment, String name) {
        this.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
                .replace(R.id.container, fragment)
                .addToBackStack(name)
                .commit();
    }

    public void replaceFragmentWithBottomAnimation(Fragment fragment, String name) {
        this.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_top, R.animator.slide_out_bottom, R.animator.slide_in_bottom, R.animator.slide_out_top)
                .replace(R.id.container, fragment)
                .addToBackStack(name)
                .commit();
    }

    public void addFragmentWithRightAnimation(Fragment fragment, String name) {
        this.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
                .add(R.id.container, fragment)
                .addToBackStack(name)
                .commit();
    }


    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }


    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
