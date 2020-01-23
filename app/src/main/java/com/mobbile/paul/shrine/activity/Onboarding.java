package com.mobbile.paul.shrine.activity;

import android.content.Intent;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.mobbile.paul.codelab.R;
import com.mobbile.paul.shrine.helper.CardPagerAdapter;
import com.mobbile.paul.shrine.helper.ShadowTransformer;

public class Onboarding extends AppCompatActivity {


    ViewPager mViewPager;
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        mViewPager = findViewById(R.id.viewPager);
        mCardAdapter = new CardPagerAdapter(this);

        mCardAdapter.addCardItem(new CardItem(0, "Step 01: Order", "Select your branding option & preferences"));
        mCardAdapter.addCardItem(new CardItem(1, "Step 02: Revise", "Review & revise response from our team."));
        mCardAdapter.addCardItem(new CardItem(2, "Step 03: Complete", "Confirm & receive your branded design."));


        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);
        final Animation animation =
                AnimationUtils.loadAnimation(this, R.anim.item_animation_from_right);
        animation.setStartOffset(100);
        mViewPager.startAnimation(animation);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            boolean lastPageChange = false;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int lastIdx = mCardAdapter.getCount() - 1;

                if (lastPageChange && position == lastIdx) {
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                int lastIdx = mCardAdapter.getCount() - 1;

                int curItem = mViewPager.getCurrentItem();
                if(curItem==lastIdx  && state==1){
                    lastPageChange = true;
                    startActivity(new Intent(Onboarding.this, IntroActivity.class));
                }else  {
                    lastPageChange = false;
                }
            }
        });


    }
}
