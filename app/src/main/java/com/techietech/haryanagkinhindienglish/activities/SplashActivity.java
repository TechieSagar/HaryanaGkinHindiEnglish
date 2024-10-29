package com.techietech.haryanagkinhindienglish.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techietech.haryanagkinhindienglish.R;
import com.techietech.haryanagkinhindienglish.activities.ads.adsData;

public class SplashActivity extends AppCompatActivity {

    private ImageView iv1;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        splashScreen.setKeepOnScreenCondition(() -> false );
        //fullscreen();
        getAds();

        setContentView(R.layout.activity_splash);

        iv1 = findViewById(R.id.imageView1_splash);
        tv = findViewById(R.id.tv_haryana);

        Animation myanim1 = AnimationUtils.loadAnimation(this,R.anim.zoomin);
        Animation myanim2 = AnimationUtils.loadAnimation(this,R.anim.blink_anim);
        iv1.startAnimation(myanim1);
        tv.startAnimation(myanim2);


        final Intent i = new Intent(getApplicationContext(),LanguageActivity.class);
        Thread timer = new Thread() {
            public void run () {
                try {
                    sleep(4000); //it was on 4000 initially but after splash update changed to 1000
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    getSaveData();
                    finish();

                }
            }
        }; timer.start();


    }

    private void fullscreen(){
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void getSaveData(){
        SharedPreferences cLSSData = getSharedPreferences("cLSSDataValue", Context.MODE_PRIVATE);
        String checkCLSSData = cLSSData.getString("LoginSignStatus","off");

        if (checkCLSSData.equals("on")){
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this,LanguageActivity.class));
        }

    }

    private void getAds() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Ads");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String appId = snapshot.child("appId").getValue(String.class);
                String banner = snapshot.child("banner").getValue(String.class);
                String interstitial = snapshot.child("interstitial").getValue(String.class);
                String rewarded = snapshot.child("rewarded").getValue(String.class);
                String nativeId = snapshot.child("native").getValue(String.class);

                adsData.appId = appId;
                adsData.bannerAdId = banner;
                adsData.interstitialAdId = interstitial;
                adsData.rewardedAdId = rewarded;
                adsData.nativeAdId = nativeId;

                try{
                    ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
                    Bundle bundle = applicationInfo.metaData;
                    applicationInfo.metaData.putString("com.google.android.gms.ads.APPLICATION_ID", adsData.appId);
                    String apiKey = bundle.getString("com.google.android.gms.ads.APPLICATION_ID");
                    Log.d("AppID", "The Saved id is " + adsData.appId);
                    Log.d("AppID", "The Saved id is " + apiKey);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();

                } catch (NullPointerException e){
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
