package com.example.mehmet.konumum;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Map extends AppCompatActivity implements OnMapReadyCallback  {

    Kisiler kisi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent i = getIntent();
        kisi = (Kisiler)i.getSerializableExtra("kisi");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng galataKulesi = new LatLng(Float.parseFloat(kisi.getEnlem()), Float.parseFloat(kisi.getBoylam()));
        googleMap.addMarker(new MarkerOptions().position(galataKulesi).title("Burası Galata Kulesi"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(galataKulesi));

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(Float.parseFloat(kisi.getEnlem()), Float.parseFloat(kisi.getBoylam())))
                .title(kisi.getAdsoyad()));
    }
}
