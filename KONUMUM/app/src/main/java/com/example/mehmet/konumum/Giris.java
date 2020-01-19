package com.example.mehmet.konumum;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Giris extends AppCompatActivity {
    ConstraintLayout myLayout;
    AnimationDrawable animationDrawable;
    private Button btnCreate,btnCreate2,btnCreate3;
    ProgressBar progressbar2;
    EditText username,password;
    Switch hatirla;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String a="1";
    FirebaseDatabase database;
    DatabaseReference myRef1;
    private FirebaseAuth auth;
    int i;
    String email;
    String parola;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        myLayout=findViewById(R.id.myLayout);
        animationDrawable=(AnimationDrawable) myLayout.getBackground();
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.setExitFadeDuration(4500);
        animationDrawable.start();

        database = FirebaseDatabase.getInstance();

        auth = FirebaseAuth.getInstance();
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        btnCreate = findViewById(R.id.btn);
        btnCreate2=findViewById(R.id.btn2);
        btnCreate3=findViewById(R.id.btn3);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        hatirla=findViewById(R.id.hatirla);
        progressbar2=findViewById(R.id.progressBar2);
        btnCreate.setVisibility(View.VISIBLE);
        progressbar2.setVisibility(View.INVISIBLE);


        hatirla.setChecked(pref.getBoolean("saved", false));
        if(hatirla.isChecked())
        {
            username.setText(pref.getString("savedusername", null));
            password.setText(pref.getString("savedpassword", null));
        }
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    username.setText("");
                }
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    password.setText("");
                }
            }
        });
        hatirla.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(hatirla.isChecked()){
                    username.setText(pref.getString("savedusername", null));
                    password.setText(pref.getString("savedpassword", null));
                    editor.commit();
                }
                else{
                    username.setText("");
                    password.setText("");
                }
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                progressbar2.setVisibility(View.VISIBLE);
                btnCreate.setVisibility(View.INVISIBLE);
                email = username.getText().toString();
                parola = password.getText().toString();

                //Email girilmemiş ise kullanıcıyı uyarıyoruz.
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Lütfen emailinizi giriniz!", Toast.LENGTH_SHORT).show();
                    btnCreate.setVisibility(View.VISIBLE);
                    return;
                }
                //Parola girilmemiş ise kullanıcıyı uyarıyoruz.
                if (TextUtils.isEmpty(parola)) {
                    Toast.makeText(getApplicationContext(), "Lütfen parolanızı giriniz!", Toast.LENGTH_SHORT).show();
                    btnCreate.setVisibility(View.VISIBLE);
                    return;
                }
                //Firebase üzerinde kullanıcı doğrulamasını başlatıyoruz
                //Eğer giriş başarılı olursa task.isSuccessful true dönecek ve MainActivity e geçilecek
                auth.signInWithEmailAndPassword(email, parola)
                        .addOnCompleteListener(Giris.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    presentActivity(v);

                                }
                                else {
                                    Log.e("Giriş Hatası",task.getException().getMessage());
                                    btnCreate.setVisibility(View.VISIBLE);
                                    Toast.makeText(getApplicationContext(), "Lütfen e-mail ve parolanızı kontrol ediniz!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                editor.putString("savedusername", username.getText().toString());
                editor.putString("savedpassword", password.getText().toString());
                editor.putBoolean("saved", hatirla.isChecked());
                editor.commit();
            }
        });
        btnCreate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentActivityy(v);
            }
        });
        btnCreate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentActivityyy(v);

            }
        });


    }

    public void presentActivity(View view) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, "transition");
        int revealX = (int) (view.getX() + view.getWidth() / 2);
        int revealY = (int) (view.getY() + view.getHeight() / 2);

        final Intent intent = new Intent(this, ActivityHesap.class);

        intent.putExtra("username",username.getText().toString());
        intent.putExtra(ActivityHesap.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(ActivityHesap.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        ActivityCompat.startActivity(this, intent, options.toBundle());
    }
    public void presentActivityy(View view) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, "transition");
        int revealX = (int) (view.getX() + view.getWidth() / 2);
        int revealY = (int) (view.getY() + view.getHeight() / 2);

        Intent intent = new Intent(this, Kayit.class);
        intent.putExtra(ActivityHesap.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(ActivityHesap.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        ActivityCompat.startActivity(this, intent, options.toBundle());
    }
    public void presentActivityyy(View view) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, "transition");
        int revealX = (int) (view.getX() + view.getWidth() / 2);
        int revealY = (int) (view.getY() + view.getHeight() / 2);

        Intent intent = new Intent(this, Sifre.class);
        intent.putExtra(ActivityHesap.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(ActivityHesap.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

}
