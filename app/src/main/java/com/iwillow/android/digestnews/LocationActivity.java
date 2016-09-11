package com.iwillow.android.digestnews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;


public class LocationActivity extends AppCompatActivity {
    private final String TAG = "LocationActivity";
    private MapView mMapView;
    private AMap aMap;
    private static final double DEFAULT_LATITUDE = 39.990;
    private static final double DEFAULT_LONGITUDE = 116.3;
    private static final int DEFAULT_ZOOM_LEVEL = 8;
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String ZOOM_LEVER = "zoomLevel";
    public static final String CAPTION = "caption";
    public static final String NAME = "name";
    private double latitude;
    private double longitude;
    private int zoomLevel;
    private String caption;
    private String name;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        latitude = getIntent().getDoubleExtra(LATITUDE, DEFAULT_LATITUDE);
        longitude = getIntent().getDoubleExtra(LONGITUDE, DEFAULT_LONGITUDE);
        zoomLevel = getIntent().getIntExtra(ZOOM_LEVER, DEFAULT_ZOOM_LEVEL);
        if (zoomLevel < DEFAULT_ZOOM_LEVEL) {
            zoomLevel = DEFAULT_ZOOM_LEVEL;
        }
        caption = getIntent().getStringExtra(CAPTION);
        name = getIntent().getStringExtra(NAME);
        latLng = new LatLng(latitude, longitude);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.mapView);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            // aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
            setUpMap();
        }
    }

    private void setUpMap() {
        aMap.setMapLanguage(AMap.ENGLISH);
        aMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                latLng, zoomLevel, 0, 0));
        aMap.moveCamera(cameraUpdate);
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }
}
