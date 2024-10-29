package com.techietech.haryanagkinhindienglish.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.techietech.haryanagkinhindienglish.R;
import com.techietech.haryanagkinhindienglish.activities.QuestionsActivity;

import java.util.List;

public class SetsAdapter extends BaseAdapter {

    private List<String> sets;
    private String category;
    private InterstitialAd interstitialAd;

    public SetsAdapter(List<String> sets, String category, InterstitialAd interstitialAd) {
        this.sets = sets;
        this.category = category;
        this.interstitialAd = interstitialAd;

    }

    @Override
    public int getCount() {
        return sets.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view;
        if (convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sets_item, parent, false);

        } else {
            view = convertView;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   interstitialAd.setAdListener(new AdListener(){
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        interstitialAd.loadAd(new AdRequest.Builder().build());
                        Intent questionIntent = new Intent(parent.getContext(),QuestionsActivity.class);
                        questionIntent.putExtra("category",category);
                        questionIntent.putExtra("setId",sets.get(position));
                        parent.getContext().startActivity(questionIntent);
                    }
                });

                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                    return;
                } */

                Intent questionIntent = new Intent(parent.getContext(),QuestionsActivity.class);
                questionIntent.putExtra("category",category);
                questionIntent.putExtra("setId",sets.get(position));
                parent.getContext().startActivity(questionIntent);

            }
        });

        ((TextView)view.findViewById(R.id.tv_category_setsView)).setText(("Level "+(position+1)));
        ((TextView)view.findViewById(R.id.tv_sets_setsView)).setText(view.getResources().getString(R.string.questionTen));

        return view;
    }
}
