package com.mobbile.paul.shrine.helper;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.mobbile.paul.codelab.R;
import com.mobbile.paul.shrine.CardItem;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ehigiator David on 14/04/2019.
 */
public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<CardItem> mData;
    private float mBaseElevation;
    private Context context;

    public CardPagerAdapter(Context context) {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
        this.context = context;
    }

    public void addCardItem(CardItem item) {
        mViews.add(null);
        mData.add(item);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.row_card_login, container, false);
        container.addView(view);
        CardView cardView = view.findViewById(R.id.cardView);

        bind(mData.get(position), cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(final CardItem item, CardView view) {
        TextView titleTextView = view.findViewById(R.id.product_title);
        TextView descriptionTextView = view.findViewById(R.id.product_description);
        LottieAnimationView productImageView = view.findViewById(R.id.product_image);
        ImageView intro_image = view.findViewById(R.id.intro_image);
        titleTextView.setText(item.getmTitleResource());
        descriptionTextView.setText(item.getmDescription());

        switch (item.getmId()) {

            case 0:
//                productImageView.setImageResource(R.drawable.ic_undraw_reminder);
//                productImageView.setAnimation("onboardingOrderLottie.json");
                intro_image.setImageDrawable(view.getContext().getDrawable(R.drawable.ic_intro_page_1));
                break;
            case 1:
//                view.setCardBackgroundColor(view.getContext().getResources().getColor(R.color.colorWarmDarkPurple));
//                productImageView.setImageResource(R.drawable.ic_undraw_post_online_dkuk);
//                productImageView.setAnimation("onboardingReviewLottie.json");
//                intro_image.setImageResource(R.drawable.intro_page_2);
                intro_image.setImageDrawable(view.getContext().getDrawable(R.drawable.intro_page_2));


                break;
            case 2:
//                intro_image.setImageResource(R.drawable.intro_page_3);
//                titleTextView.setTextColor(titleTextView.getContext().getResources().getColor(R.color.colorWarmDarkPurple));
//                descriptionTextView.setTextColor(descriptionTextView.getContext().getResources().getColor(R.color.colorWarmDarkPurple));
//                view.setCardBackgroundColor(view.getContext().getResources().getColor(R.color.colorControlActivated));
//                productImageView.setImageResource(R.drawable.ic_undraw_order_confirmed);
//                productImageView.setAnimation("onboardingCompleteLottie.json");
                intro_image.setImageDrawable(view.getContext().getDrawable(R.drawable.intro_page_3));

                break;
            default:
                break;
        }
    }


}
