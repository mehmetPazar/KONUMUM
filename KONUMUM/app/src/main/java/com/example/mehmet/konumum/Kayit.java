package com.example.mehmet.konumum;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.MimeTypeFilter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.jar.Pack200;

public class Kayit extends AppCompatActivity {

    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    Kisiler kisi;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    FirebaseStorage storage;
    StorageReference storageReference;
    private LocationManager ubicacion;
    String provider;
    Uri pickedImgUri;
    View rootLayout;
    ImageView imgView;
    private int revealX;
    private int revealY;
    EditText edittext, kkpassword, kkconfirmed, kkname, kkmail;
    ProgressBar progressbar3;
    Button btn;
    Uri downloadUri;
    private FirebaseAuth auth;
    Double lat, log;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);

        final Intent intent = getIntent();
        kisi = new Kisiler("", "", "", "", "", "", "","");
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        rootLayout = findViewById(R.id.root_kayit);
        rootLayout = findViewById(R.id.root_kayit);

        edittext = findViewById(R.id.edittext);
        kkpassword = findViewById(R.id.kkpassword);
        kkconfirmed = findViewById(R.id.kkconfirmed);
        kkname = findViewById(R.id.kkname);
        kkmail = findViewById(R.id.kkmail);
        imgView = findViewById(R.id.imgView);
        btn = findViewById(R.id.btn);
        progressbar3 = findViewById(R.id.progressBar3);

        btn.setVisibility(View.VISIBLE);
        progressbar3.setVisibility(View.INVISIBLE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = edittext.getText().toString();
                String b = kkpassword.getText().toString();
                String c = kkname.getText().toString();
                String d = kkmail.getText().toString();
                String e = kkconfirmed.getText().toString();

                progressbar3.setVisibility(View.VISIBLE);
                btn.setVisibility(View.INVISIBLE);

                if (b.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Parola en az 6 haneli olmalıdır!", Toast.LENGTH_SHORT).show();
                    btn.setVisibility(View.VISIBLE);
                } else if (a.isEmpty() || b.isEmpty() || c.isEmpty() || d.isEmpty() || imgView.getDrawable() == null) {
                    Toast.makeText(getApplicationContext(), "Lütfen boş alan bırakmayınız!", Toast.LENGTH_SHORT).show();
                    btn.setVisibility(View.VISIBLE);
                } else {
                    if (b.equals(e)) {
                        uploadImageandData();

                    } else {
                        Toast.makeText(getApplicationContext(), "Şifrenizi kontrol ediniz!", Toast.LENGTH_SHORT).show();
                        btn.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22) {
                    checkAndRequestForPermission();
                } else {
                    openGallery();
                }

            }
        });

        edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edittext.setText("");
                    kkpassword.setText("");
                    kkconfirmed.setText("");
                    kkname.setText("");
                    kkmail.setText("");
                }
            }
        });


        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {
        } else {
            rootLayout.setVisibility(View.VISIBLE);
        }

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
        } else

        {
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

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(Kayit.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Kayit.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(Kayit.this, "Lütfen gerekli izinleri kabul edin!", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(Kayit.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        } else {
            openGallery();
        }

    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {
            pickedImgUri = data.getData();
            imgView.setImageURI(pickedImgUri);
            Picasso.get().load(pickedImgUri).into(imgView);
        }
    }

    private void uploadImageandData() {

        if (pickedImgUri != null) {

            final StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(pickedImgUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        downloadUri = task.getResult();

                        auth.createUserWithEmailAndPassword(kkmail.getText().toString(), kkpassword.getText().toString())
                                .addOnCompleteListener(Kayit.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(Kayit.this, "Kaydınız yapılamamıştır!",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(Kayit.this, "Kaydınız başarıyla yapılmıştır!",
                                                    Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), Giris.class);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                                            finish();
                                        }

                                    }
                                });

                        try {
                            SimpleDateFormat bicim2=new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                            Date simdikiZaman = new Date();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference ddRef = database.getReference("Kullanıcılar");
                            localization();
                            kisi.setKullaniciadi(edittext.getText().toString());
                            kisi.setSifre(kkpassword.getText().toString());
                            kisi.setAdsoyad(kkname.getText().toString());
                            kisi.setMail(kkmail.getText().toString());
                            kisi.setResim(downloadUri.toString());
                            kisi.setTarih(bicim2.format(simdikiZaman));
                            /*kisi.setEnlem(String.valueOf(lat));
                            kisi.setBoylam(String.valueOf(log));*/
                            ddRef.child(kisi.getKullaniciadi()).setValue(kisi);
                        } catch (Exception e) {
                            Toast.makeText(Kayit.this, e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Toast.makeText(Kayit.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void localization() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},1000);

        }
        ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location loc = ubicacion.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(ubicacion != null && loc != null)
        {
            kisi.setEnlem(String.valueOf(loc.getLatitude()));
            kisi.setBoylam(String.valueOf(loc.getLongitude()));
        }

    }
}



