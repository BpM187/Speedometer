package com.speedometer.calculator.app.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.speedometer.calculator.app.GlobalSingleton;
import com.speedometer.calculator.app.R;
import com.speedometer.calculator.app.activities.MainActivity;
import com.speedometer.calculator.app.constants.Constants;
import com.speedometer.calculator.app.model.ImageCallback;
import com.speedometer.calculator.app.model.RefreshCallback;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ImageAddFragment extends BaseFragment {

    //views
    ImageView img;
    TextView txtAction;
    TextView txtRemove;
    TextView txtAdd;
    View viewLine;
    LinearLayout ll;
    LinearLayout llAlert;
    TextView txtCamera;
    TextView txtGallery;

    //variables from another fragment
    public Bitmap bitmap;
    public ImageCallback listener;

    //variables
    Uri imageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_image_add, container, false);

        updateStatusBarColor(getResources().getColor(R.color.colorBrownDark));
        initViews();
        setListeners();

        return baseView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_INTENT_CAMERA && resultCode == RESULT_OK) {
            ll.setAlpha(1f);
            llAlert.setVisibility(View.GONE);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

                String picturePath = getRealPathFromURI(imageUri);
                ExifInterface exif = new ExifInterface(picturePath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                }

                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true); // rotating bitmap

                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                if (width > 800 || height > 800) {
                    width = GlobalSingleton.getInstance().convertFromOneRangeToAnother(width, 0, width > height ? width : height, 0, 800);
                    height = GlobalSingleton.getInstance().convertFromOneRangeToAnother(height, 0, width > height ? width : height, 0, 800);
                }
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

                changeVisibility(bitmap, getString(R.string.change_image), View.VISIBLE);

            } catch (Exception e) {
                Log.e("ON ACTIVITY RESULT", "ERROR | MainMenuFragment | onActivityResult | INTENT_REQUEST_CAMERA | " + e.getMessage());
                e.printStackTrace();
            }
        } else if (requestCode == Constants.REQUEST_INTENT_GALLERY && resultCode == RESULT_OK) {
            ll.setAlpha(1f);
            llAlert.setVisibility(View.GONE);
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize = 2;
                bitmap = BitmapFactory.decodeFile(picturePath, opt);

                if (bitmap == null) {
                    Toast.makeText(getActivity(), "Gallery Error", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    ExifInterface exif = new ExifInterface(picturePath);
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                    Log.d("EXIF", "Exif: " + orientation);
                    Matrix matrix = new Matrix();
                    if (orientation == 6) {
                        matrix.postRotate(90);
                    } else if (orientation == 3) {
                        matrix.postRotate(180);
                    } else if (orientation == 8) {
                        matrix.postRotate(270);
                    }

                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true); // rotating bitmap

                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    if (width > 800 || height > 800) {
                        int oldWidth = width; //i put this here because after above width change it's size, the height will be modified
                        width = GlobalSingleton.getInstance().convertFromOneRangeToAnother(width, 0, width > height ? width : height, 0, 800);
                        height = GlobalSingleton.getInstance().convertFromOneRangeToAnother(height, 0, oldWidth > height ? oldWidth : height, 0, 800);
                    }
                    bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

                    changeVisibility(bitmap, getString(R.string.change_image), View.VISIBLE);
                } catch (Exception e) {
                    Log.e("ON ACTIVITY RESULT", "ERROR | MainMenuFragment | onActivityResult | INTENT_REQUEST_GALLERY | " + e.getMessage());
                    e.printStackTrace();
                }
            } catch (Exception e) {
                Log.e("ON ACTIVITY RESULT", "ERROR | MainMenuFragment | onActivityResult | INTENT_REQUEST_GALLERY | " + e.getMessage());
                e.printStackTrace();
            }

        }
    }

    private void initViews() {
        img = baseView.findViewById(R.id.img);
        txtAction = baseView.findViewById(R.id.txt_action_image);
        txtRemove = baseView.findViewById(R.id.txt_remove_image);
        txtAdd = baseView.findViewById(R.id.txt_action);
        viewLine = baseView.findViewById(R.id.view_line);
        ll = baseView.findViewById(R.id.ll);
        llAlert = baseView.findViewById(R.id.ll_alert);
        txtCamera = baseView.findViewById(R.id.txt_camera);
        txtGallery = baseView.findViewById(R.id.txt_gallery);

        if (bitmap != null) {
            changeVisibility(bitmap, getString(R.string.change_image), View.VISIBLE);
        }
    }

    private void startCameraIntent() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getActivity().getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, Constants.REQUEST_INTENT_CAMERA);
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void showCamera() {
        if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            if (Build.VERSION.SDK_INT >= 23) {
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.MY_PERMISSIONS_REQUEST_CAMERA);
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.MY_PERMISSIONS_REQUEST_CAMERA);
                    }

                    Activity currentActivity = getActivity();
                    if (currentActivity instanceof MainActivity) {
                        ((MainActivity) currentActivity).refreshCallback = new RefreshCallback() {
                            @Override
                            public void doRefresh(boolean doRefresh) {
                                startCameraIntent();
                            }
                        };
                    }
                } else {
                    if (isIntentAvailable(getActivity(), MediaStore.ACTION_IMAGE_CAPTURE)) {
                        startCameraIntent();
                    } else {
                        showToast("No camera application available");
                    }
                }
            } else {
                if (isIntentAvailable(getActivity(), MediaStore.ACTION_IMAGE_CAPTURE)) {
                    startCameraIntent();
                } else {
                    showToast("No camera application available");
                }
            }
        } else {
            showToast("No camera available");
        }
    }

    private void showGallery() {

        if (Build.VERSION.SDK_INT >= 23) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }

                Activity currentActivity = getActivity();
                if (currentActivity instanceof MainActivity) {
                    ((MainActivity) currentActivity).refreshCallback = new RefreshCallback() {
                        @Override
                        public void doRefresh(boolean doRefresh) {
                            if (doRefresh) {
                                Intent i = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, Constants.REQUEST_INTENT_GALLERY);
                            }
                        }
                    };
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, Constants.REQUEST_INTENT_GALLERY);
                }
            } else {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, Constants.REQUEST_INTENT_GALLERY);
            }
        } else {
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, Constants.REQUEST_INTENT_GALLERY);
        }
    }

    private void changeVisibility(Bitmap bmp, String text, int visibility) {
        img.setImageBitmap(bmp);
        txtAction.setText(text);
        img.setVisibility(visibility);
        txtRemove.setVisibility(visibility);
        txtAdd.setVisibility(visibility);
        viewLine.setVisibility(visibility);
    }

    private void setListeners() {
        //base view
        baseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.getDetails(bitmap);
                }
                if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        txtAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll.setAlpha(0.9f);
                llAlert.setVisibility(View.VISIBLE);
            }
        });

        txtRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmap = null;
                changeVisibility(bitmap, getString(R.string.attach_photo), View.GONE);
            }
        });

        txtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.getDetails(bitmap);
                }

                if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        txtCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCamera();
            }
        });

        txtGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGallery();
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img.invalidate();
                BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                ImageShowFragment fragment = new ImageShowFragment();
                fragment.bitmap = bitmap;
                addFragment(fragment, "IMG_SHOW_01");
            }
        });
    }
}
