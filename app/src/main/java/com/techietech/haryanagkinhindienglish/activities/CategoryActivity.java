package com.techietech.haryanagkinhindienglish.activities;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techietech.haryanagkinhindienglish.R;
import com.techietech.haryanagkinhindienglish.adapters.CategoryAdapter;
import com.techietech.haryanagkinhindienglish.models.CategoryModel;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    public static List<CategoryModel> list;
    private Dialog loadingDialig;
    private String categoryValue,levelValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullscreen();
        setContentView(R.layout.activity_category);
        Toolbar toolbar = findViewById(R.id.toolBar_questions);
        AdView mAdView;


        loadLangValue();

        //MobileAds.initialize(this);
        //loadsAds(); disabled because non compliant due to drawer covers it

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.categories));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadingDialig = new Dialog(this);
        loadingDialig.setContentView(R.layout.loading);
        Objects.requireNonNull(loadingDialig.getWindow()).setBackgroundDrawable(getDrawable(R.drawable.button_abcd_bg)); //(getDrawable(R.drawable.button_abcd_bg));
        Objects.requireNonNull(loadingDialig.getWindow()).setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialig.setCancelable(false);

        recyclerView = findViewById(R.id.rv_categories);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        final CategoryAdapter adapter = new CategoryAdapter(list);
        recyclerView.setAdapter(adapter);

        //loadLocale();
        loadingDialig.show();
        myRef.child(categoryValue).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    List<String> sets = new ArrayList<>();
                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.child("sets").getChildren()){
                        sets.add(dataSnapshot2.getKey());
                    }

                    list.add(new CategoryModel(Objects.requireNonNull(dataSnapshot1.child("name").getValue()).toString(),
                            sets,
                            Objects.requireNonNull(dataSnapshot1.child("url").getValue()).toString(),
                            dataSnapshot1.getKey()));
                }

                adapter.notifyDataSetChanged();
                loadingDialig.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CategoryActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialig.dismiss();
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void fullscreen(){
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void loadsAds() {
        /*AdView mAdView = findViewById(R.id.adView_cA);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = findViewById(R.id.adView_cA);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    private void loadLangValue(){
        SharedPreferences getSavedLang = getSharedPreferences("GolMal", Activity.MODE_PRIVATE);
        categoryValue = getSavedLang.getString("save_lang","");
        levelValue = getSavedLang.getString("save_level","");
    }

}
