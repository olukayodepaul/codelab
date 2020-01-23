package com.mobbile.paul.shrine.helper;

import androidx.cardview.widget.CardView;

/**
 * Created by Ehigiator David on 14/04/2019.
 */
public interface CardAdapter {

    int MAX_ELEVATION_FACTOR = 8;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}
