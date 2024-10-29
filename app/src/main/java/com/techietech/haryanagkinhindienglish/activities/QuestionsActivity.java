package com.techietech.haryanagkinhindienglish.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techietech.haryanagkinhindienglish.R;
import com.techietech.haryanagkinhindienglish.activities.ads.adsData;
import com.techietech.haryanagkinhindienglish.models.QuestionModel;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;



public class QuestionsActivity extends AppCompatActivity {

    public static final String File_NAME = "QUIZZER";
    public static final String KEY_NAME = "QUESTION";
    public static final String Points_DATA = "StorePoints";
    public static final String TEXT = "text";


    private TextView questions;
    private TextView noIndicator;
    private TextView plusPoint;
    private TextView points;
    private TextView tvBookmarks;
    private LinearLayout btnBookmarks,optionsContainer,btnShare;
    private Button btnNextQsn;
    private List<QuestionModel> list;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    private int position = 0;
    private int count=0;
    private int score =0;
    private String setId;
    private String getLevel;
    private String pointsDataWrite="3";
    private String categoryValue,levelValue;
    private Dialog loadingDialig;
    private List<QuestionModel> bookmarksList;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ImageView reportUs;
    private Gson gson;
    private int matchedQsnPsn;

    private InterstitialAd mInterstitialAd;
    private static final String TAG = "QuestionsActivity";

    private RewardedAd rewardedAd;

    private LinearLayout freeHints,hints;




    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullscreen();
        setContentView(R.layout.activity_questions);
        Toolbar toolbar = findViewById(R.id.toolBar_questions);
        setSupportActionBar(toolbar);
        loadLangValue();

        /*MobileAds.initialize(this, initializationStatus -> {
        });*/
        //loadsAds();
        loadAdsNew();
        //createAndLoadRewardedAd();
        createAndLoadRewardedAdNew();


        questions = findViewById(R.id.tv_question);
        noIndicator = findViewById(R.id.no_indicator);
        plusPoint =findViewById(R.id.plus_points);
        points = findViewById(R.id.points);
        tvBookmarks = findViewById(R.id.tvBookmark);
        freeHints = findViewById(R.id.freeHint);
        reportUs = findViewById(R.id.report_us);
        hints = findViewById(R.id.hint);
        btnBookmarks = findViewById(R.id.bookmarks);
        optionsContainer = findViewById(R.id.options_container);
        btnShare = findViewById(R.id.button_share);
        btnNextQsn = findViewById(R.id.button_next);
        points.setText(String.valueOf(10));

        SharedPreferences fisrtShared = getSharedPreferences("prefs",Context.MODE_PRIVATE);
        boolean firstStart = fisrtShared.getBoolean("firstStart",true);
        preferences = getSharedPreferences(File_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();

        getBookmarks();
        loadLangValueData();

        btnBookmarks.setOnClickListener(view -> {
            if (modelMatch()){
                bookmarksList.remove(matchedQsnPsn);
                tvBookmarks.setText(getString(R.string.bookmark));
                btnBookmarks.setBackground(getDrawable(R.drawable.button_transparent));
            } else {
                bookmarksList.add(list.get(position));
                tvBookmarks.setText(getString(R.string.bookmarked));
                btnBookmarks.setBackground(getDrawable(R.drawable.red_background));
            }
        });

        btnBookmarks.setOnLongClickListener(view -> {
            startActivity(new Intent(QuestionsActivity.this,BookmarkActivity.class));
            return true;
        });

        plusPoint.setOnClickListener(view -> earnVideoPoints());

        freeHints.setOnClickListener(view -> earnVideoPoints());



        hints.setOnClickListener(view -> {
            int finalPoints,chkPoints;
            chkPoints = Integer.parseInt(points.getText().toString());
            String hintValue = list.get(position).getAnswer();

            if (chkPoints >= 3){
                finalPoints = chkPoints-3;
                points.setText(String.valueOf(finalPoints));
                Toast.makeText(QuestionsActivity.this,hintValue,Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(QuestionsActivity.this,getString(R.string.no_enough_points),Toast.LENGTH_SHORT).show();
            }

        });

        reportUs.setOnClickListener(view -> reportUsError());

        reportUs.setOnLongClickListener(view -> {
            Toast.makeText(QuestionsActivity.this,getString(R.string.report_us),Toast.LENGTH_SHORT).show();
            return false;
        });


        if(firstStart){
            checkFirstStart();
        } else {
            getPoints();
        }


        //category = getIntent().getStringExtra("category");
        setId = getIntent().getStringExtra("setId");

        loadingDialig = new Dialog(this);
        loadingDialig.setContentView(R.layout.loading);
        Objects.requireNonNull(loadingDialig.getWindow()).setBackgroundDrawable(getDrawable(R.drawable.button_abcd_bg));
        Objects.requireNonNull(loadingDialig.getWindow()).setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialig.setCancelable(false);


        list = new ArrayList<>();
        loadingDialig.show();
        myRef.child(levelValue).child(setId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String id = dataSnapshot1.getKey();
                    String question = Objects.requireNonNull(dataSnapshot1.child("question").getValue()).toString();
                    String a = Objects.requireNonNull(dataSnapshot1.child("optionA").getValue()).toString();
                    String b = Objects.requireNonNull(dataSnapshot1.child("optionB").getValue()).toString();
                    String c = Objects.requireNonNull(dataSnapshot1.child("optionC").getValue()).toString();
                    String d = Objects.requireNonNull(dataSnapshot1.child("optionD").getValue()).toString();
                    String correctANS = Objects.requireNonNull(dataSnapshot1.child("correctANS").getValue()).toString();

                    list.add(new QuestionModel(id,question,a,b,c,d,correctANS,setId));
                }

                if (list.size() > 0){

                    for (int i = 0; i<4; i++){
                        optionsContainer.getChildAt(i).setOnClickListener(view -> checkAnswer(((Button)view)));
                    }

                    playAnim(questions,0,list.get(position).getQuestion());
                    btnNextQsn.setOnClickListener(view -> {
                        btnNextQsn.setEnabled(false);
                        enableOption(true);
                        position++;
                        if (position == list.size()){
                            /*if(interstitialAd.isLoaded()){
                                interstitialAd.show();*/
                            if (mInterstitialAd != null) {
                                mInterstitialAd.show(QuestionsActivity.this);

                            return;
                            }

                            //start Score Activity
                            Intent scoreIntent = new Intent(getApplicationContext(),ScoreActivity.class);
                            scoreIntent.putExtra("score",score);
                            scoreIntent.putExtra("total",list.size());
                            scoreIntent.putExtra("level",setId);
                            startActivity(scoreIntent);
                            finish();
                            enableOption(false);
                            return;
                        }
                        count = 0;
                        playAnim(questions,0,list.get(position).getQuestion());

                    });

                    btnShare.setOnClickListener(view -> {
                        String body = "Que. "+list.get(position).getQuestion()+ "\n" +"\n" +
                                "A. "+list.get(position).getA()+ "\n" +
                                "B. "+list.get(position).getB()+ "\n" +
                                "C. "+list.get(position).getC()+ "\n" +
                                "D. "+list.get(position).getD();

                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.app_name));
                        shareIntent.putExtra(Intent.EXTRA_TEXT,body);
                        startActivity(Intent.createChooser(shareIntent,"Share via"));

                    });

                } else{
                    finish();
                    Toast.makeText(QuestionsActivity.this, "No Questions", Toast.LENGTH_SHORT).show();
                }
                loadingDialig.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(QuestionsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void checkFirstStart() {
        SharedPreferences firsShared = getSharedPreferences("prefs",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = firsShared.edit();
        editor.putBoolean("firstStart",false);
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        storeBookmarks();
        savePoints();
    }

    private void playAnim(final View view, final int value, final String data){

        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (value == 0 && count < 4){
                    String option = "";
                    if (count == 0){
                        option = list.get(position).getA();
                    } else if (count == 1){
                        option = list.get(position).getB();
                    } else if (count == 2){
                        option = list.get(position).getC();
                    } else if (count == 3){
                        option = list.get(position).getD();
                    }
                    playAnim(optionsContainer.getChildAt(count),0,option);
                    count++;
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //data change

                if (value == 0){
                    try {
                        ((TextView)view).setText(data);
                        noIndicator.setText(MessageFormat.format("{0}{1}{2}", position + 1, getString(R.string.saLashNew), list.size()));

                        if (modelMatch()){
                            tvBookmarks.setText(getString(R.string.bookmarked));
                            btnBookmarks.setBackground(getDrawable(R.drawable.red_background));
                        } else {
                            tvBookmarks.setText(getString(R.string.bookmark));
                            btnBookmarks.setBackground(getDrawable(R.drawable.button_transparent));
                        }

                    } catch (ClassCastException ex){
                        ((Button)view).setText(data);
                    }

                    view.setTag(data);
                    playAnim(view,1,data);
                } else {
                    enableOption(true);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void checkAnswer(Button selectedOption){
        enableOption(false);
        btnNextQsn.setEnabled(true);
        if (selectedOption.getText().toString().equals(list.get(position).getAnswer())){
            //for Correct Ans
            score++;
            selectedOption.setBackground(getResources().getDrawable(R.drawable.green_background));
            Toast.makeText(getApplicationContext(),"Correct",Toast.LENGTH_SHORT).show();

        } else {
            //for Incorrect Ans
            selectedOption.setBackground(getResources().getDrawable(R.drawable.red_background));
            Button correctOption = optionsContainer.findViewWithTag(list.get(position).getAnswer());
            correctOption.setBackground(getResources().getDrawable(R.drawable.green_background));
        }

    }


    private void enableOption(boolean enable){
        for(int i = 0; i < 4; i++){
            optionsContainer.getChildAt(i).setEnabled(enable);
            if (enable){
                optionsContainer.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.button_abcd_bg));
            }
        }
    }


    private void fullscreen(){
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void getBookmarks(){
        String json = preferences.getString(KEY_NAME,"");
        Type type = new TypeToken<List<QuestionModel>>(){}.getType();
        bookmarksList = gson.fromJson(json,type);

        if (bookmarksList == null){
            bookmarksList = new ArrayList<>();
        }

    }

    private boolean modelMatch(){
        boolean matched = false;
        int i=0;
        for(QuestionModel model: bookmarksList ){
            if(model.getQuestion().equals(list.get(position).getQuestion())
                    && model.getAnswer().equals(list.get(position).getAnswer())
                    && model.getSet().equals(list.get(position).getSet())){
                matched = true;
                matchedQsnPsn=i;
            }
            i++;

        }

        return matched;
    }

    private void storeBookmarks(){
        String json = gson.toJson(bookmarksList);
        editor.putString(KEY_NAME,json);
        editor.commit();

        if (bookmarksList == null){
            bookmarksList = new ArrayList<>();
        }

    }

     /*private void loadsAds() {
        /*AdView mAdView = findViewById(R.id.adView_qA);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = findViewById(R.id.adView_qA);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.adMob_interstitialId));
        interstitialAd.loadAd(new AdRequest.Builder().build());

        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                interstitialAd.loadAd(new AdRequest.Builder().build());
                Intent scoreIntent = new Intent(getApplicationContext(),ScoreActivity.class);
                scoreIntent.putExtra("score",score);
                scoreIntent.putExtra("total",list.size());
                scoreIntent.putExtra("level",setId);
                startActivity(scoreIntent);
                finish();
                enableOption(false);
            }
        });

    } */

    private void showInterstitialAd(){
        if (mInterstitialAd != null) {
            mInterstitialAd.show(QuestionsActivity.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }

    private void loadAdsNew() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, adsData.interstitialAdId, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                        adsCallBack();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }

    private void adsCallBack(){
        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
            @Override
            public void onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad dismissed fullscreen content.");
                mInterstitialAd = null;
                //interstitialAd.loadAd(new AdRequest.Builder().build());
                Intent scoreIntent = new Intent(getApplicationContext(),ScoreActivity.class);
                scoreIntent.putExtra("score",score);
                scoreIntent.putExtra("total",list.size());
                scoreIntent.putExtra("level",setId);
                startActivity(scoreIntent);
                finish();
                enableOption(false);
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.");
                mInterstitialAd = null;
            }

            @Override
            public void onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.");
            }
        });
    }

    /*private void createAndLoadRewardedAd() {
        if (rewardedAd == null || !rewardedAd.isLoaded()) {
            rewardedAd = new RewardedAd(this, getString(R.string.adMob_rewardedId));
            rewardedAd.loadAd(
                    new AdRequest.Builder().build(), new RewardedAdLoadCallback() {

                        @Override
                        public void onRewardedAdFailedToLoad(int errorCode) {
                            // Ad failed to load.
                            createAndLoadRewardedAd();
                        }
                    });
        }

    } */


    private void createAndLoadRewardedAdNew() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, adsData.rewardedAdId,
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.toString());
                        rewardedAd = null;
                        createAndLoadRewardedAdNew();
                    }
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        Log.d(TAG, "Ad was loaded.");
                        showRewardedVideoCallBackNew();
                    }
                });
    }


    private void addCoins(int coins) {
        int sum = Integer.parseInt(points.getText().toString());
        int hds = sum+coins;
        points.setText(String.valueOf(hds));

    }

    /*private void showRewardedVideo() {
        if (rewardedAd.isLoaded()) {
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                    // Ad opened.
                    Toast.makeText(QuestionsActivity.this, "onRewardedAdOpened", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRewardedAdClosed() {
                    // Ad closed.
                    // Preload the next video ad.
                    QuestionsActivity.this.createAndLoadRewardedAd();

                }

                @Override
                public void onUserEarnedReward(RewardItem rewardItem) {
                    // User earned reward.
                    addCoins(rewardItem.getAmount());

                }

                @Override
                public void onRewardedAdFailedToShow(int errorCode) {
                    // Ad failed to display
                    QuestionsActivity.this.createAndLoadRewardedAd();
                }
            };
            rewardedAd.show(this, adCallback);
        } else {
            Toast.makeText(QuestionsActivity.this,getString(R.string.no_ads_available),Toast.LENGTH_LONG).show();
        }


    } */

    private void showRewardedVideoCallBackNew(){
        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad dismissed fullscreen content.");
                rewardedAd = null;
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.");
                rewardedAd = null;
                createAndLoadRewardedAdNew();
            }

            @Override
            public void onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.");
                createAndLoadRewardedAdNew();
            }
        });

    }

    private void showRewardedAdNew(){
        if (rewardedAd != null) {
            Activity activityContext = QuestionsActivity.this;
            rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d(TAG, "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                    addCoins(rewardAmount);
                    showRewardedVideoCallBackNew();
                }
            });
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
        }

    }

    private void savePoints(){
        SharedPreferences sPPoints = getSharedPreferences(Points_DATA,Context.MODE_PRIVATE);
        SharedPreferences.Editor pointsEditor = sPPoints.edit();
        pointsEditor.putString(TEXT,points.getText().toString());
        pointsEditor.apply();

    }

    private void getPoints(){
        SharedPreferences getsPPoints = getSharedPreferences(Points_DATA,Context.MODE_PRIVATE);
        pointsDataWrite = getsPPoints.getString(TEXT,"0");
        points.setText(pointsDataWrite);

    }

    private void earnVideoPoints(){
        final Dialog earnDialog;
        LinearLayout earnVideo;
        earnDialog = new Dialog(this);
        earnDialog.setContentView(R.layout.earn_video);
        Objects.requireNonNull(earnDialog.getWindow()).setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        earnDialog.show();

        earnVideo = earnDialog.findViewById(R.id.earnVideoBtn);
        earnVideo.setOnClickListener(view -> {
            //showRewardedVideo();
            showRewardedAdNew();
            earnDialog.dismiss();
        });
    }

    private void loadLangValueData(){
        SharedPreferences getSavedLang = getSharedPreferences("GolMal", Activity.MODE_PRIVATE);
        getLevel = getSavedLang.getString("save_level","");

    }

    private void reportUsError(){
        String to = getString(R.string.nav_header_subtitle);
        String body = "Que. "+list.get(position).getQuestion()+ "\n" +"\n" +
                "A. "+list.get(position).getA()+ "\n" +
                "B. "+list.get(position).getB()+ "\n" +
                "C. "+list.get(position).getC()+ "\n" +
                "D. "+list.get(position).getD()+ "\n" +
                "Ans. "+list.get(position).getAnswer()+ "\n" + "\n" + "Explain about error";

        Intent shareIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("mailto:"+ to));
        //shareIntent.setData(Uri.parse("mailto:"));
        shareIntent.putExtra(Intent.EXTRA_EMAIL,to);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.app_name));
        shareIntent.putExtra(Intent.EXTRA_TEXT,body);
        startActivity(shareIntent);
    }

    private void loadLangValue(){
        SharedPreferences getSavedLang = getSharedPreferences("GolMal", Activity.MODE_PRIVATE);
        categoryValue = getSavedLang.getString("save_lang","");
        levelValue = getSavedLang.getString("save_level","");
    }



}
