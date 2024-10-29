package com.techietech.haryanagkinhindienglish.activities.ads;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.techietech.haryanagkinhindienglish.R;
import com.techietech.haryanagkinhindienglish.activities.MainActivity;


public class AdsInterstitialActivity extends AppCompatActivity {

    private int mPbStatus = 0;
    private final Handler mHandler = new Handler();
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adsinterstitialactivity);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        loadInterstitialAd();
        progressBarProcess();



    }

    public void progressBarProcess(){
        final ProgressBar mProgressBar = findViewById(R.id.spin_kit_adsAct);
        mProgressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mPbStatus < 100){
                    mPbStatus++;
                    SystemClock.sleep(50);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setProgress(mPbStatus);
                        }
                    });
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mPbStatus=0;
                        if(mInterstitialAd != null){
                            mInterstitialAd.show(AdsInterstitialActivity.this);
                            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();
                                    mInterstitialAd = null;
                                    Toast.makeText(AdsInterstitialActivity.this, "You Made your Friend", Toast.LENGTH_SHORT).show();
                                    launchMainActivity();

                                }
                            });
                        } else{
                            launchMainActivity();

                        }


                    }
                });
            }
        }).start();
    }

    public void launchMainActivity(){
        Intent desIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(desIntent);
        AdsInterstitialActivity.this.finish();

    }
    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,adsData.interstitialAdId,adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        //Toast.makeText(AdsInterstitialActivity.this, "Loaded", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });

    }


}
