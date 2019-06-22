package com.speedometer.calculator.app.constants;

public class Constants {


    //permission
    public static final int MY_LOCATION_PERMISSION_CODE = 1;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 3;

    //requests / intent
    public static final int REQUEST_INTENT_CAMERA = 1;
    public static final int REQUEST_INTENT_GALLERY = 2;
    public static final int STATE_CREATE = 0;
    public static final int STATE_READ = 1;
    public static final int STATE_UPDATE = 2;
    public static final int STATE_DELETE = 3;


    //shared preferences keys

    //constants
    public static int TYPE_BRAND = 1;
    public static int TYPE_MODEL = 2;

    //default coefficients
    public static double f_RESULT_AT_RUN_COEFFICIENT = 0.08;
    public static double g_GRAVITY_ACCELERATION = 9.81;
    public static double D_MASSES_FOUND_AT_RUN_COEFFICIENT = 1.1;
    public static double ro_AIR_DENSITY = 0.879;

    //admin
    public static String ADMIN_PASSWORD = "1234";
}
