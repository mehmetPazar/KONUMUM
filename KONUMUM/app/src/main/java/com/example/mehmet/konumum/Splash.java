package com.example.mehmet.konumum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {
    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        iv=findViewById(R.id.acilis);
        Animation animation =AnimationUtils.loadAnimation(this,R.anim.acilisanim);
        final Intent i=new Intent(this,Giris.class);
        Thread zamanlama =new Thread() {
            @Override
            public void run() {
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finally {
                        startActivity(i);
                        finish();
                    }

            }
        };
        zamanlama.start();
    }
}
