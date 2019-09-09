package com.behl.dynamicmodule;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

public class DynamicModule implements DynamicModuleInterface, LocationListener {
    private LocationManager locationManager;
    private String provider;

    @Override
    public String getLocation(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if(locationManager!=null)
        provider = locationManager.getBestProvider(new Criteria(), false);
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
//            locationManager.requestLocationUpdates(provider, 400, 1, this);
            Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
            if (lastKnownLocation != null)
                return lastKnownLocation.getLatitude() + " " + lastKnownLocation.getLongitude();

        }
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
