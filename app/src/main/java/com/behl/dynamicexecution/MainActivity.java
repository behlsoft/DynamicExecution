package com.behl.dynamicexecution;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.behl.dynamicmodule.ModuleLoader;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.os.Environment.getExternalStorageDirectory;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private InputStream inputStream;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        init();
        checkLocationPermission();


    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest
                                        .permission.READ_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest
                                .permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            ((TextView) findViewById(R.id.location)).setText(loadDexFile("myClasses.dex"));

            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(this,
                                    Manifest.permission.READ_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
//                        ((TextView) findViewById(R.id.location)).setText(loadDexFile("myClasses.dex"));
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    private void init() {
        File dir = getExternalStorageDirectory();
        String dirPath = dir.getAbsolutePath();
        String filePath = dirPath.concat("/myClasses.dex");
        try {
            inputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getSourceFilePath(String name) {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).applicationInfo.dataDir + "/files/"
                    + name;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String loadDexFile(String name) {
        return new ModuleLoader(getCacheDir().getAbsolutePath()).
                load(getDexFile(name)).getLocation(this);
    }

    private File getDexFile(String name) {
        File file = new File(getSourceFilePath(name));
        try {
            FileUtils.copyInputStreamToFile(inputStream, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
