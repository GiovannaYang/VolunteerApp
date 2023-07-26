package com.example.volunteerapp.Activity.Project;

import android.app.Activity;
import android.content.Intent;

import com.example.volunteerapp.Activity.Project.MapActivity;
import com.google.android.gms.maps.model.LatLng;

public class MapHelper {
    public static final int REQUEST_CODE_SELECT_ADDRESS = 1;

    public static void startMapActivity(Activity activity) {
        Intent intent = new Intent(activity, MapActivity.class);
        activity.startActivityForResult(intent, REQUEST_CODE_SELECT_ADDRESS);
    }

    public static LatLng getLatLngFromIntent(Intent data) {
        if (data != null && data.hasExtra(MapActivity.EXTRA_SELECTED_LATLNG)) {
            return data.getParcelableExtra(MapActivity.EXTRA_SELECTED_LATLNG);
        }
        return null;
    }
}
