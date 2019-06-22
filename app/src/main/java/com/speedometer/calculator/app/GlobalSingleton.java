package com.speedometer.calculator.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GlobalSingleton {

    private static GlobalSingleton mInstance;
    public Context mContext;

    private SharedPreferences prefs;


    public static GlobalSingleton getInstance() {
        if (mInstance == null) {
            mInstance = new GlobalSingleton();
        }
        return mInstance;
    }


    //Shared preferences
    private final static String SHARED_PREFERENCES_KEY = "SPEEDOMETER";

    public void setString(String name, String value) {
        if (mContext != null) {
            if (prefs == null) {
                prefs = mContext.getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
            }
            prefs.edit().putString(name, value).apply();
        }
    }

    public String getString(String name) {
        if (mContext != null) {
            if (prefs == null) {
                prefs = mContext.getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
            }
            return prefs.getString(name, "");
        }

        return "";
    }

    public void setInt(String name, int value) {
        if (mContext != null) {
            if (prefs == null) {
                prefs = mContext.getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
            }
            prefs.edit().putInt(name, value).apply();
        }
    }

    public int getInt(String name) {
        if (mContext != null) {
            if (prefs == null) {
                prefs = mContext.getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
            }
            return prefs.getInt(name, 0);
        }

        return 0;
    }

    public void setLong(String name, long value) {
        if (mContext != null) {
            if (prefs == null) {
                prefs = mContext.getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
            }
            prefs.edit().putLong(name, value).apply();
        }
    }

    public long getLong(String name) {
        if (mContext != null) {
            if (prefs == null) {
                prefs = mContext.getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
            }
            return prefs.getLong(name, 0);
        }
        return 0;
    }


    public void cleanSharedPreferences() {
        if (mContext != null) {
            if (prefs == null) {
                prefs = mContext.getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
            }
            prefs.edit().clear().apply();
        }
    }

    public int convertFromOneRangeToAnother(int oldValue, int oldMin, int oldMax, int newMin, int newMax) {
        return (((oldValue - oldMin) * (newMax - newMin)) / (oldMax - oldMin)) + newMin;
    }

    public String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


    public byte[] compress(String data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length());
        GZIPOutputStream gzip = new GZIPOutputStream(bos);
        gzip.write(data.getBytes());
        gzip.close();
        byte[] compressed = bos.toByteArray();
        bos.close();
        return compressed;
    }

    public String decompress(byte[] compressed) throws IOException {
        final int BUFFER_SIZE = 32;
        ByteArrayInputStream is = new ByteArrayInputStream(compressed);
        GZIPInputStream gis = new GZIPInputStream(is, BUFFER_SIZE);
        StringBuilder string = new StringBuilder();
        byte[] data = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = gis.read(data)) != -1) {
            string.append(new String(data, 0, bytesRead));
        }
        gis.close();
        is.close();
        return string.toString();
    }

    public byte[] compressBitmap(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
