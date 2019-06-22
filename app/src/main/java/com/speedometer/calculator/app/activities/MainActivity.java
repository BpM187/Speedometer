package com.speedometer.calculator.app.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.speedometer.calculator.app.R;
import com.speedometer.calculator.app.constants.Constants;
import com.speedometer.calculator.app.db.SQLiteHelper;
import com.speedometer.calculator.app.fragments.MenuFragment;
import com.speedometer.calculator.app.model.RefreshCallback;

public class MainActivity extends BaseActivity {

    boolean doubleBackToExitPressedOnce;
    public RefreshCallback refreshCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //exit app
        if (getIntent().getBooleanExtra("Exit me", false)) {
            finish();
        }

        addFragment(new MenuFragment(), "MENU_01");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean cameraAccepted = true;
        for (int i = 0; i < permissions.length; i++) {
            switch (permissions[i]) {
                case Manifest.permission.CAMERA: {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        cameraAccepted = false;
                    }
                    break;
                }
                case Manifest.permission.WRITE_EXTERNAL_STORAGE: {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        cameraAccepted = false;
                    }
                    break;
                }
                case Manifest.permission.READ_EXTERNAL_STORAGE: {
                    //gallery accepted, go to main menu
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED && requestCode == Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
                        if (refreshCallback != null) {
                            refreshCallback.doRefresh(true);
                        }
                    }
                    break;
                }
            }
        }


        //camera accepted, go to main menu
        if (requestCode == Constants.MY_PERMISSIONS_REQUEST_CAMERA && cameraAccepted && refreshCallback != null) {
            refreshCallback.doRefresh(true);
        }

    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.container);
        if (fragment instanceof MenuFragment) {

            if (doubleBackToExitPressedOnce) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Exit me", true);
                startActivity(intent);
                finish();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
        }
    }
}
