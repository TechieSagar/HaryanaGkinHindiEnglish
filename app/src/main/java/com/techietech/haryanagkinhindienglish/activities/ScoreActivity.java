package com.techietech.haryanagkinhindienglish.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.techietech.haryanagkinhindienglish.R;


import java.text.MessageFormat;

public class ScoreActivity extends AppCompatActivity {

    private TextView donGiveUp, bLuckNt, totalPercent, outOf, level;
    private ImageView iv_scoreActivity;
    private Button btnNextSa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        donGiveUp = findViewById(R.id.tv1_scoreA);
        bLuckNt = findViewById(R.id.tv2_scoreA);
        totalPercent = findViewById(R.id.tv3_scoreA);
        outOf = findViewById(R.id.tv4_scoreA);
        level = findViewById(R.id.tv5_scoreA);
        iv_scoreActivity = findViewById(R.id.iv_Sa);
        btnNextSa = findViewById(R.id.btnNext_Sa);




        // MobileAds.initialize(this);
       /* AdLoader adLoader = new AdLoader.Builder(this, getString(R.string.adMob_nativeAdId))
                .forUnifiedNativeAd(unifiedNativeAd -> {
                    //native ad will be available
                    @SuppressLint("InflateParams") UnifiedNativeAdView unifiedNativeAdView = (UnifiedNativeAdView)getLayoutInflater().inflate(R.layout.native_ad_layout,null);
                    mapUnifiedNativeAdtoLayout(unifiedNativeAd, unifiedNativeAdView);


                    FrameLayout nativeAdLayout = findViewById(R.id.nativeAd);
                    nativeAdLayout.removeAllViews();
                    nativeAdLayout.addView(unifiedNativeAdView);

                }).build();
        adLoader.loadAd(new AdRequest.Builder().build()); */



        int total = getIntent().getIntExtra("total", 0);
        int got = getIntent().getIntExtra("score", 0);
        //String levelNo = String.valueOf(getIntent().getIntExtra("level",0));
        String levelNo = getIntent().getStringExtra("level");


        if (got < 8) {
            donGiveUp.setText(R.string.dontGiveUp);
        } else if (got > 8 && got < 13) {
            donGiveUp.setText(getString(R.string.wellPlayed));
        } else if (got == 14) {
            donGiveUp.setText(getString(R.string.wellPlayed));
            bLuckNt.setText(R.string.practiceMakes);
        } else if (got == 15) {
            donGiveUp.setText(getString(R.string.winGame));
            bLuckNt.setText(R.string.congratulations);
        }

        int tp = (got * 100 / total);
        totalPercent.setText(MessageFormat.format("{0}{1}", tp, getString(R.string.percentSymbol)));

        outOf.setText(String.format("%s/%s", got, total));
        //level.setText(String.format("%s %s", getString(R.string.level), levelNo));


        btnNextSa.setOnClickListener(view -> finish());

        Animation myanim2 = AnimationUtils.loadAnimation(this, R.anim.rotate);
        iv_scoreActivity.startAnimation(myanim2);

    }




}

   /* private void mapUnifiedNativeAdtoLayout(UnifiedNativeAd adFromGoogle, UnifiedNativeAdView myAdview) {
        MediaView mediaView = myAdview.findViewById(R.id.ad_media);
        myAdview.setMediaView(mediaView);

        myAdview.setHeadlineView(myAdview.findViewById(R.id.ad_headline));
        myAdview.setBodyView(myAdview.findViewById(R.id.ad_body));
        myAdview.setCallToActionView(myAdview.findViewById(R.id.ad_call_to_action));
        myAdview.setIconView(myAdview.findViewById(R.id.ad_icon));
        myAdview.setPriceView(myAdview.findViewById(R.id.ad_price));
        myAdview.setStarRatingView(myAdview.findViewById(R.id.ad_rating));
        myAdview.setStoreView(myAdview.findViewById(R.id.ad_store));
        myAdview.setAdvertiserView(myAdview.findViewById(R.id.ad_advertiser));

        ((TextView)myAdview.getHeadlineView()).setText(adFromGoogle.getHeadline());

        if (adFromGoogle.getBody() == null){
            myAdview.getBodyView().setVisibility(View.GONE);
        } else {
            ((TextView)myAdview.getBodyView()).setText(adFromGoogle.getBody());
        }

        if (adFromGoogle.getCallToAction() == null){
            myAdview.getCallToActionView().setVisibility(View.GONE);
        } else {
            ((Button)myAdview.getCallToActionView()).setText(adFromGoogle.getCallToAction());
        }

        if (adFromGoogle.getIcon() == null){
            myAdview.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView)myAdview.getIconView()).setImageDrawable(adFromGoogle.getIcon().getDrawable());
        }

        if (adFromGoogle.getPrice() == null){
            myAdview.getPriceView().setVisibility(View.GONE);
        } else {
            ((TextView)myAdview.getPriceView()).setText(adFromGoogle.getPrice());
        }

        if (adFromGoogle.getStarRating() == null){
            myAdview.getStarRatingView().setVisibility(View.GONE);
        } else {
            ((RatingBar)myAdview.getStarRatingView()).setRating(adFromGoogle.getStarRating().floatValue());
        }

        if (adFromGoogle.getAdvertiser() == null){
            myAdview.getAdvertiserView().setVisibility(View.GONE);
        } else {
            ((TextView)myAdview.getAdvertiserView()).setText(adFromGoogle.getAdvertiser());
        }

        if (adFromGoogle.getBody() == null){
            myAdview.getBodyView().setVisibility(View.GONE);
        } else {
            ((TextView)myAdview.getBodyView()).setText(adFromGoogle.getBody());
        }

        myAdview.setNativeAd(adFromGoogle);


    }
} */