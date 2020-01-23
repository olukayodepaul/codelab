package com.mobbile.paul.shrine.staggeredgridlayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.codelabs.mdc.java.shrine.R;

public class StaggeredProductCardViewHolder extends RecyclerView.ViewHolder {

//    public NetworkImageView productImage;
//    public TextView productTitle;
//    public TextView productPrice;
//

    public CardView backgroundView;

    public LottieAnimationView productLogo;

    public TextView titleView;

    public TextView subtitleView;

    public ImageView backdrop;
    StaggeredProductCardViewHolder(@NonNull View itemView) {
        super(itemView);
        backgroundView = itemView.findViewById(R.id.cardView);
        productLogo = itemView.findViewById(R.id.product_image);
        titleView = itemView.findViewById(R.id.product_title);
        subtitleView = itemView.findViewById(R.id.product_description);
        backdrop = itemView.findViewById(R.id.backdrop);
    }
}
