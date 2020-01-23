package com.mobbile.paul.shrine.staggeredgridlayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.codelabs.mdc.java.shrine.R;
import com.google.codelabs.mdc.java.shrine.network.ImageRequester;
import com.google.codelabs.mdc.java.shrine.network.ProductEntry;

import java.util.List;

/**
 * Adapter used to show an asymmetric grid of products, with 2 items in the first column, and 1
 * item in the second column, and so on.
 */
public class StaggeredProductCardRecyclerViewAdapter extends RecyclerView.Adapter<StaggeredProductCardViewHolder> {

    private List<ProductEntry> productList;
    private ImageRequester imageRequester;

    private onProductClickedListener onProductClickedListener;

    public StaggeredProductCardRecyclerViewAdapter(List<ProductEntry> productList, onProductClickedListener onProductClickedListener) {
        this.productList = productList;
        imageRequester = ImageRequester.getInstance();
        this.onProductClickedListener = onProductClickedListener;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 3;
    }

    @NonNull
    @Override
    public StaggeredProductCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.row_card_discover;
//        if (viewType == 1) {
//            layoutId = R.layout.shr_staggered_product_card_second;
//        } else if (viewType == 2) {
//            layoutId = R.layout.shr_staggered_product_card_third;
//        }


        View layoutView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new StaggeredProductCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull StaggeredProductCardViewHolder holder, final int position) {
        if (productList != null && position < productList.size()) {
            final ProductEntry product = productList.get(position);
            holder.titleView.setText(product.title);
            holder.subtitleView.setText(product.description);
            holder.backgroundView.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.transparent));

            switch (position) {
                case 1:
//                    holder.backgroundView.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.colorBeige));
//                    holder.backdrop.setBackground(holder.itemView.getContext().getDrawable(R.drawable.ic_flyers));
//                    holder.titleView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.colorBeigeDark));
                    holder.subtitleView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
                    Glide.with(holder.backdrop.getContext()).asGif().load(R.raw.socialmediaengage).into(holder.backdrop);
                    holder.subtitleView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.colorLogoBlue));

//                        holder.productLogo.setImageResource(R.drawable.ic_undraw_social);
//                    holder.productLogo.setAnimation("socialMediaLottie.json");
                    break;
                case 2:
                    holder.titleView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.colorWhite));
                    holder.subtitleView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.colorLogoBlue));
//                    holder.productLogo.setImageResource(R.drawable.ic_undraw_people_search);
//                    holder.productLogo.setAnimation("influencerLottie.json");
//                    holder.backdrop.setBackground(holder.itemView.getContext().getDrawable(R.drawable.ic_influencer));
                    Glide.with(holder.backdrop.getContext()).asGif().load(R.raw.influencermarketing).into(holder.backdrop);

                    break;
                case 3:
//                    holder.productLogo.setImageResource(R.drawable.ic_undraw_files_6b3d);
//                    holder.productLogo.setAnimation("bannerLottie.json");
//                    holder.backdrop.setBackground(holder.itemView.getContext().getDrawable(R.drawable.ic_banners));
                    Glide.with(holder.backdrop.getContext()).asGif().load(R.raw.banner).into(holder.backdrop);

                    break;
                case 4:
//                    holder.backgroundView.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.colorWarmGreen));
                    holder.titleView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.colorWarmGreenText));
                    holder.subtitleView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.colorLogoBlue));
//                    holder.productLogo.setImageResource(R.drawable.ic_undraw_scrum_board);
//                    holder.productLogo.setAnimation("storyboard.json");
//                    holder.backdrop.setBackground(holder.itemView.getContext().getDrawable(R.drawable.ic_storyboards_design));
                    Glide.with(holder.backdrop.getContext()).asGif().load(R.raw.storyboardgif).into(holder.backdrop);

                    break;
                case 5:
//                    holder.backgroundView.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.colorWarmPink));
                    holder.titleView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.colorWarmDarkPurple));
                    holder.subtitleView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.colorBlack));
//                    holder.productLogo.setAnimation("brandingLottie.json");
//                    holder.backdrop.setBackground(holder.itemView.getContext().getDrawable(R.drawable.ic_branding));
                    Glide.with(holder.backdrop.getContext()).asGif().load(R.raw.branding).into(holder.backdrop);

                    break;
//                case 6:
//                    holder.backgroundView.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.colorWarmDarkPurple));
//                    holder.titleView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.colorWhite));
//                    holder.subtitleView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.colorWhite));
//                    holder.productLogo.setImageResource(R.drawable.ic_undraw_online_shopping);
//                    break;

                case 6:
//                    holder.backgroundView.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.colorPrimary));
                    holder.titleView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
                    holder.subtitleView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
//                    holder.productLogo.setAnimation("illustrationLottie.json");
//                    holder.backgroundView.setBackground(holder.itemView.getContext().getDrawable(R.drawable.ic_illustration));
                    Glide.with(holder.backdrop.getContext()).asGif().load(R.raw.illustration).into(holder.backdrop);

                    break;
                case 7:
//                    holder.productLogo.setImageResource(R.drawable.ic_undraw_business_plan_5i9d);
//                    holder.productLogo.setAnimation("campaignLottie.json");
                    holder.backdrop.setBackground(holder.itemView.getContext().getDrawable(R.drawable.ic_campaign));
                    holder.subtitleView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.colorWhite));

                    break;
                case 8:
//                    holder.productLogo.setImageResource(R.drawable.ic_undraw_business_plan_5i9d);
//                    holder.productLogo.setAnimation("campaignLottie.json");
                    holder.subtitleView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.colorLogoBlue));
                    Glide.with(holder.backdrop.getContext()).asGif().load(R.raw.stationery).into(holder.backdrop);

                    break;
                default:
//                        holder.productLogo.setAnimation("logoLottie.json");
                    holder.subtitleView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.colorWhite));
//                        holder.backdrop.setBackground(holder.itemView.getContext().getDrawable(R.drawable.ic_logo_design));
                    Glide.with(holder.backdrop.getContext()).asGif().load(R.raw.logodesign).into(holder.backdrop);

                    break;

            }
//            holder.productPrice.setText(product.price);
//            imageRequester.setImageFromUrl(holder.productImage, product.url);
//
            holder.productLogo.playAnimation();
            if (onProductClickedListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        onProductClickedListener.onProductClicked(position);

                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public interface onProductClickedListener {

        void onProductClicked(int position);
    }
}
