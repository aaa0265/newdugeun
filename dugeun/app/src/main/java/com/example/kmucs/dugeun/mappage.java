package com.example.kmucs.dugeun;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//구글 샘플 코드
//androidManifest에 구글Key값 입력되어있음.


    public class mappage extends FragmentActivity implements OnMapReadyCallback {

        private GoogleMap mMap;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_mappage);

            //layout에 있는 map호출
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);

            //자동으로 onMapReady 호출
            mapFragment.getMapAsync(this);
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            //위도 경도 지정
            LatLng SEOUL = new LatLng(37.527089, 127.028480);

            //지도에 출력될 마커에 대한 설정
            mMap.addMarker(new MarkerOptions().position(SEOUL).title("Marker in SEOUL"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        }
    }