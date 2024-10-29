package com.techietech.haryanagkinhindienglish.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import com.techietech.haryanagkinhindienglish.R;

public class LanguageActivity extends AppCompatActivity {

    private ConstraintLayout clEng, clHry, clHin;
    private Button getStarted;
    private String checkLangData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullscreen();
        setContentView(R.layout.activity_language);

        clEng = findViewById(R.id.layout_profile_english);
        clHin = findViewById(R.id.layout_profile_hindi);
        getStarted = findViewById(R.id.btnGetStarted);

        clEng.setOnClickListener(view -> {

            Toast.makeText(LanguageActivity.this, "All Questions & Answers will be in English", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor saveLangEditor = getSharedPreferences("GolMal", Context.MODE_PRIVATE).edit();
            saveLangEditor.putString("save_lang","Categories");
            saveLangEditor.putString("save_level","Levels");
            saveLangEditor.apply();
            getStarted.setVisibility(View.VISIBLE);


        });


        clHin.setOnClickListener(view -> {


            Toast.makeText(LanguageActivity.this, "सभी प्रश्न और उत्तर हिंदी में होंगे", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor saveLangEditor = getSharedPreferences("GolMal", Context.MODE_PRIVATE).edit();
            saveLangEditor.putString("save_lang","HindiCategory");
            saveLangEditor.putString("save_level","HindiLevel");
            saveLangEditor.apply();
            getStarted.setVisibility(View.VISIBLE);



        });



        getStarted.setOnClickListener(view -> {
            saveCurrentData();
            // changed after the google safty form policy. redirect to main activity as register
            //Intent mainActivity = new Intent(getApplicationContext(), RegisterActivity.class);
            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivity);
            finish();

        });

    }

    private void fullscreen(){
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void saveCurrentData(){
        SharedPreferences cLSSData = getSharedPreferences("cLSSDataValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor cLSSDataEditor = cLSSData.edit();
        cLSSDataEditor.putString("LoginSignStatus","on");
        cLSSDataEditor.apply();
    }


}


