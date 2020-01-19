package com.example.mehmet.konumum;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ActivityHesap extends AppCompatActivity {


    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";

    final List<Kisiler> kisilerr=new ArrayList<Kisiler>();
    Kisiler kisim,kisim2;
    OzelAdapter adaptorumuz;
    ListView listemiz;
    View rootLayout;
    FirebaseDatabase database;
    DatabaseReference myRef;
    TextView txt4,txt5,txt8;
    ImageView img;
    String mail;
    int i;
    private int revealX;
    private int revealY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hesap);

        final Intent intent = getIntent();
        mail=intent.getStringExtra("username");
        rootLayout = findViewById(R.id.root_layout);
        img=findViewById(R.id.imageView);
        txt4=findViewById(R.id.textView4);
        txt5=findViewById(R.id.textView5);
        txt8=findViewById(R.id.textView8);

        listemiz = findViewById(R.id.liste);
        adaptorumuz=new OzelAdapter(this, kisilerr);
        listemiz.setAdapter(adaptorumuz);

        listemiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                kisim2 = new Kisiler(kisilerr.get(2).getKullaniciadi(), kisilerr.get(2).getSifre(), kisilerr.get(2).getAdsoyad(), kisilerr.get(2).getAdsoyad(), kisilerr.get(2).getResim(), kisilerr.get(2).getEnlem(), kisilerr.get(2).getBoylam(),kisilerr.get(2).getTarih());

                final Intent intent2 = new Intent().setClass(getApplicationContext(), Map.class);
                intent2.putExtra("kisi",(Serializable)kisim2);
                startActivity(intent2);
            }
        });

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Kullanıcılar");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot prodSnapshot : dataSnapshot.getChildren()) {
                    kisim = prodSnapshot.getValue(Kisiler.class);

                    if (mail.equals(kisim.getMail())) {
                        txt4.setText(kisim.getAdsoyad());
                        txt5.setText(kisim.getMail());
                        txt8.setText(kisim.getTarih());
                        Picasso.get().load(kisim.getResim()).into(img);
                        }
                        kisilerr.add(kisim);
                    }

                    adaptorumuz.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
        });


        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {
            rootLayout.setVisibility(View.INVISIBLE);

            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);


            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        revealActivity(revealX, revealY);
                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        } else {
            rootLayout.setVisibility(View.VISIBLE);
        }

    }
    protected void revealActivity(int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);

            Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, x, y, 0, finalRadius);
            circularReveal.setDuration(400);
            circularReveal.setInterpolator(new AccelerateInterpolator());

            rootLayout.setVisibility(View.VISIBLE);
            circularReveal.start();
        } else {
            finish();
        }
    }
    protected void unRevealActivity() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            finish();
        } else {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                    rootLayout, revealX, revealY, finalRadius, 0);

            circularReveal.setDuration(400);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rootLayout.setVisibility(View.INVISIBLE);
                    finish();
                }
            });


            circularReveal.start();
        }
    }
}
