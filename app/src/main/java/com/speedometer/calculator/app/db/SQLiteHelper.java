package com.speedometer.calculator.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.google.gson.Gson;
import com.speedometer.calculator.app.GlobalSingleton;
import com.speedometer.calculator.app.model.GeneralInfo;
import com.speedometer.calculator.app.model.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Speedometer.db";
    private static final String TAG = "DBHelper";
    private static SQLiteHelper sInstance;

    //table
    private static final String TABLE_VEHICLE = "vehicle";

    //columns
    private static final String KEY_ID = "id";
    private static final String KEY_VEHICLE = "vehicle";
    private static final String KEY_PHOTO_BRAND = "brand";
    private static final String KEY_PHOTO_MODEL = "model";


    public static synchronized SQLiteHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SQLiteHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CAR_TABLE = "CREATE TABLE " + TABLE_VEHICLE
                + "( "
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_VEHICLE + " TEXT, "
                + KEY_PHOTO_BRAND + " TEXT, "
                + KEY_PHOTO_MODEL + " TEXT "
                + ")";

        db.execSQL(CREATE_CAR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(String.valueOf("DROP TABLE IF EXISTS" + TABLE_VEHICLE));
        onCreate(db);
    }


    public void createVehicle(byte[] bytes, byte[] photo1, byte[] photo2) {
        SQLiteDatabase db = this.getWritableDatabase();

//        String delSql = "DELETE FROM " + TABLE_VEHICLE;
//        SQLiteStatement delStmt = db.compileStatement(delSql);
//        delStmt.execute();

        String sql = "INSERT INTO " + TABLE_VEHICLE
                + "( "
                + KEY_VEHICLE + ", "
                + KEY_PHOTO_BRAND + ", "
                + KEY_PHOTO_MODEL
                + ")"
                + " VALUES(?,?,?)";

        SQLiteStatement insertStmt = db.compileStatement(sql);
        insertStmt.clearBindings();
        insertStmt.bindBlob(1, bytes);
        insertStmt.bindBlob(2, photo1);
        insertStmt.bindBlob(3, photo2);
        insertStmt.executeInsert();
        db.close();
    }

    long getVehicleCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_VEHICLE);
        db.close();
        return count;
    }


    public Vehicle getVehicle(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Vehicle vehicle = null;

//        Cursor cursor = db.query(TABLE_VEHICLE, new String[]{KEY_ID, KEY_BRAND, KEY_MODEL,
//                        KEY_PHOTO_BRAND, KEY_PHOTO_MODEL, KEY_GENERATION}, KEY_ID + "=?",
//                new String[]{String.valueOf(id)}, null, null, null, null);

        Cursor cursor = db.query(TABLE_VEHICLE, new String[]{KEY_ID, KEY_VEHICLE, KEY_PHOTO_BRAND, KEY_PHOTO_MODEL}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        try {
            if (cursor != null) {
                cursor.moveToFirst();
                String json = (GlobalSingleton.getInstance().decompress(cursor.getBlob(1)));
                vehicle = new Gson().fromJson(json, Vehicle.class);

                GeneralInfo generalInfo = vehicle.getGeneralInfo();
                generalInfo.setPhotoBrand(cursor.getBlob(2));
                generalInfo.setPhotoModel(cursor.getBlob(3));

                vehicle.setGeneralInfo(generalInfo);
                vehicle.setId(cursor.getInt(0));
//                vehicle.setBrand(cursor.getString(1));
//                vehicle.setModel(cursor.getString(2));
//                vehicle.setPhotoBrand(cursor.getString(3));
//                vehicle.setPhotoModel(cursor.getString(4));
//                vehicle.setGeneration(cursor.getString(5));
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get vehicle from database!");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return vehicle;
    }

    public List<Vehicle> getVehicleList() {
        List<Vehicle> vehicleList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_VEHICLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Vehicle vehicle = null;
        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    String json = (GlobalSingleton.getInstance().decompress(cursor.getBlob(1)));
                    vehicle = new Gson().fromJson(json, Vehicle.class);

                    GeneralInfo generalInfo = vehicle.getGeneralInfo();
                    generalInfo.setPhotoBrand(cursor.getBlob(2));
                    generalInfo.setPhotoModel(cursor.getBlob(3));

                    vehicle.setGeneralInfo(generalInfo);
                    vehicle.setId(cursor.getInt(0));

                    vehicleList.add(vehicle);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get vehicles from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return vehicleList;
    }

    public void updateVehicle(int id, byte[] vehicle, byte[] photo1, byte[] photo2) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_VEHICLE, vehicle);
            values.put(KEY_PHOTO_BRAND, photo1);
            values.put(KEY_PHOTO_MODEL, photo2);

            // updating row
            db.update(TABLE_VEHICLE, values, KEY_ID + " = ?",
                    new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to update vehicle!");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteVehicle(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_VEHICLE, KEY_ID + " = ?",
                    new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete user!");
        } finally {
            db.endTransaction();
        }
    }
}
