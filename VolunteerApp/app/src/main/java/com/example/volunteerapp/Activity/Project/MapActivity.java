package com.example.volunteerapp.Activity.Project;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.volunteerapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String EXTRA_SELECTED_LATLNG = "selected_latlng";
    private static final LatLng DEFAULT_LOCATION = new LatLng(30.5728, 104.0668); // 成都市的经纬度

    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // 初始化地图
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 10));

        // 设置地图点击事件监听器
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // 在地图上添加标记
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(latLng));

                // 将选中的经纬度返回给调用方
                Intent resultIntent = new Intent();
                resultIntent.putExtra(EXTRA_SELECTED_LATLNG, latLng);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
