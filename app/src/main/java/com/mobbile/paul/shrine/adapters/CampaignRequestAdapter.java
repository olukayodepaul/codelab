package com.mobbile.paul.shrine.adapters;


import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.mobbile.paul.codelab.R;


public class CampaignRequestAdapter extends PagerAdapter {

    @NonNull
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {

        int resId = 0;
        switch (position) {
            case 0:
                resId = R.id.page_one;
                break;
            case 1:
                resId = R.id.page_two;
                break;
            case 2:
                resId = R.id.page_three;
                break;
            case 3:
                resId = R.id.page_four;
                break;
            case 4:
                resId = R.id.page_five;
                break;
            case 5:
                resId = R.id.page_six;
                break;
            case 6:
                resId = R.id.page_seven;
                break;
            case 7:
                resId = R.id.page_eight;
                break;
            case 8:
                resId = R.id.page_nine;
                break;
        }
        return collection.findViewById(resId);
    }

    @Override
    public int getCount() {
        return 9;
    }

    @Override
    public boolean isViewFromObject(@NonNull View arg0, @NonNull Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}



