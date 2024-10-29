package com.techietech.haryanagkinhindienglish.activities;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;
import com.techietech.haryanagkinhindienglish.R;
import com.techietech.haryanagkinhindienglish.activities.ads.adsData;

import java.util.Objects;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullscreen();
        //loadLocale();
        setContentView(R.layout.activity_main);

        Button startQuizBtn = findViewById(R.id.button_startQuiz);
        Button setsPracticeBtn = findViewById(R.id.button_practiceSets);
        Button bookmarksBtn = findViewById(R.id.button_bookmarks);



        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.haryana_gk);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //MobileAds.initialize(this);
        //loadsAds(); disabled after google new policy and drawer covers it

        startQuizBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),CategoryActivity.class);
            startActivity(intent);
        });

        bookmarksBtn.setOnClickListener(view -> {
            Intent bookmarksIntent = new Intent(getApplicationContext(),BookmarkActivity.class);
            startActivity(bookmarksIntent);
        });

        //setsPracticeBtn.setOnClickListener(view -> Toast.makeText(MainActivity.this,R.string.available_soon,Toast.LENGTH_SHORT).show());
        setsPracticeBtn.setOnClickListener(view -> {
            Intent practiceSetIntent = new Intent(getApplicationContext(), PracticeSetsActivity.class);
            startActivity(practiceSetIntent);
            //Toast.makeText(this, adsData.appId, Toast.LENGTH_SHORT).show();
            Log.d(TAG, adsData.appId);
            Log.d(TAG, adsData.bannerAdId);
            Log.d(TAG, adsData.interstitialAdId);
            Log.d(TAG, adsData.rewardedAdId);
            Log.d(TAG, adsData.nativeAdId);
        });
    }

    private void loadsAds() {
        /*AdView mAdView = findViewById(R.id.adView_mA);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest); */ //unrap this for add implementation

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = findViewById(R.id.adView_mA);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    private void fullscreen(){
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_english) {
            englishLanguage();
            return true;
        } else if (id == R.id.action_hindi){
            hindiLanguage();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_rate) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
            }

        } else if (id == R.id.nav_more_apps) {
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/developer?id=Techie+Tech")));


        } else if (id == R.id.nav_language) {
            languageDialog();

        } else if (id == R.id.nav_share) {

            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            String shareBody = "Hey there, try this app";
            String shareSub = "https://play.google.com/store/apps/details?id=" + getPackageName();
            myIntent.putExtra(Intent.EXTRA_SUBJECT,shareBody);
            myIntent.putExtra(Intent.EXTRA_TEXT,shareSub);
            startActivity(Intent.createChooser(myIntent, "Share Via"));
        }
        else if (id == R.id.nav_pp) {
            privacyPolicyShow(getString(R.string.privacy_policy));

        } else if (id == R.id.nav_about) {
            aboutDialogShow();

        } else if (id == R.id.nav_exit) {
            SharedPreferences cLSSData = getSharedPreferences("cLSSDataValue",MODE_PRIVATE);
            SharedPreferences.Editor cLSSDataEditor = cLSSData.edit();
            cLSSDataEditor.putString("LoginSignStatus","off");
            cLSSDataEditor.apply();
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void languageDialog(){
        Context context = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Choose Language")
                .setTitle("Language")
                .setPositiveButton("Hindi", (dialogInterface, i) -> hindiLanguage())
                .setNegativeButton("English", (dialogInterface, i) -> englishLanguage())
                .setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void hindiLanguage(){
        Toast.makeText(MainActivity.this, "सभी प्रश्न और उत्तर हिंदी में होंगे", Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor saveLangEditor = getSharedPreferences("GolMal", Context.MODE_PRIVATE).edit();
        saveLangEditor.putString("save_lang","HindiCategory");
        saveLangEditor.putString("save_level","HindiLevel");
        saveLangEditor.apply();
    }

    private void englishLanguage(){
        Toast.makeText(MainActivity.this, "All Questions & Answers will be in English", Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor saveLangEditor = getSharedPreferences("GolMal", Context.MODE_PRIVATE).edit();
        saveLangEditor.putString("save_lang","Categories");
        saveLangEditor.putString("save_level","Levels");
        saveLangEditor.apply();
    }

    private void aboutDialogShow(){
        final Dialog aboutDialog = new Dialog(this);
        aboutDialog.setContentView(R.layout.about);
        Button btnPp = aboutDialog.findViewById(R.id.btn_pp);
        Button btnExit = aboutDialog.findViewById(R.id.btn_exit);
        Objects.requireNonNull(aboutDialog.getWindow()).setBackgroundDrawable(getDrawable(R.drawable.button_abcd_bg));
        aboutDialog.setCancelable(false);
        btnPp.setOnClickListener(view -> privacyPolicyShow(getString(R.string.privacy_policy)));

        btnExit.setOnClickListener(view -> aboutDialog.dismiss());
        aboutDialog.show();
    }

    private void privacyPolicyShow(String url){
        Intent ppIntent = new Intent(Intent.ACTION_VIEW);
        ppIntent.setData(Uri.parse(url));
        startActivity(ppIntent);
    }

}
