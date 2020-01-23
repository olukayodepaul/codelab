package com.mobbile.paul.shrine.formActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.viewpager.widget.ViewPager;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.mobbile.paul.codelab.R;
import com.mobbile.paul.shrine.activity.SuccessSubmitActivity;
import com.mobbile.paul.shrine.adapters.InfluencerRequestAdapter;
import com.mobbile.paul.shrine.models.InfluencerRequest;
import com.mobbile.paul.shrine.models.OrderObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.mobbile.paul.shrine.activity.SuccessSubmitActivity.ARGS_ORDER_AMOUNT;
import static com.mobbile.paul.shrine.activity.SuccessSubmitActivity.ARGS_ORDER_AMOUNT_IN_DOLLAR;
import static com.mobbile.paul.shrine.activity.SuccessSubmitActivity.ARGS_ORDER_ID;

public class InfluencerRequestActivity extends AppCompatActivity {

    ExtendedFloatingActionButton nextFab;

    ProgressBar progressBar;
    ViewPager pager;

    TextInputEditText influencerEditText;

    Chip facebook_chip,twitter_chip,snapchat_chip,instagram_chip;

    RadioButton imagesRadioButton,videosRadioButton,bothRadioButton;

    String socialMediaSelected = "";
    String modeSelected = BOTH_MODE;

    final static String IMAGE_MODE = "Images";
    final static String VIDEO_MODE = "Videos";
    final static String BOTH_MODE = "Both Images & Videos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_influencer_request);

        ImageView backdrop = findViewById(R.id.backdrop);
        Glide.with(this).asGif().load(R.raw.influencermarketing).into(backdrop);

        InfluencerRequestAdapter influencerRequestAdapter = new InfluencerRequestAdapter();
        pager = findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(influencerRequestAdapter);
        progressBar = findViewById(R.id.progress_total);
        progressBar.setMax(3);
        progressBar.setProgress(1);

        facebook_chip = findViewById(R.id.facebook_chip);
        twitter_chip = findViewById(R.id.twitter_chip);
        snapchat_chip = findViewById(R.id.snapchat_chip);
        instagram_chip = findViewById(R.id.instagram_chip);

        imagesRadioButton = findViewById(R.id.imagesRadioButton);
        videosRadioButton = findViewById(R.id.videosRadioButton);
        bothRadioButton = findViewById(R.id.bothRadioButton);
        influencerEditText = findViewById(R.id.influencerEditText);

        nextFab = findViewById(R.id.fab);
        nextFab.shrink();


        imagesRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    modeSelected = IMAGE_MODE;
                }
            }
        });

        videosRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    modeSelected = VIDEO_MODE;
                }
            }
        });

        bothRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    modeSelected = BOTH_MODE;
                }
            }
        });
//        influencerEditText.requestFocus();
//
//        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                int progress = i + 1;
                ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", progress);
                animation.setDuration(500);
                animation.setInterpolator(new DecelerateInterpolator());
                animation.start();

                if (i == (Objects.requireNonNull(pager.getAdapter()).getCount() - 1)) {

                    nextFab.setText(R.string.submit_request_string);
                    Drawable myFabSrc = getResources().getDrawable(R.drawable.ic_check_black_24dp);
                    nextFab.setIcon(myFabSrc);
                    nextFab.extend();
                    nextFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestInfluencer();
                        }
                    });
                } else {
                    nextFab.setText(R.string.next);
                    Drawable myFabSrc = getResources().getDrawable(R.drawable.ic_chevron_right);
                    nextFab.setIcon(myFabSrc);
                    nextFab.shrink();
                    nextFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (pager != null)
                                pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        nextFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pager != null)
                    pager.setCurrentItem(pager.getCurrentItem() + 1, true);
            }
        });

    }

    public void requestInfluencer() {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Snackbar.make(nextFab, R.string.not_logged_in_message, Snackbar.LENGTH_LONG).show();
            return;
        }

        if (facebook_chip.isChecked()){
            socialMediaSelected = "Facebook,";
        }
       else if (twitter_chip.isChecked()){
            socialMediaSelected += "Twitter,";
        }
        else if (snapchat_chip.isChecked()){
            socialMediaSelected += "Snapchat,";
        }
        else if  (instagram_chip.isChecked()){
            socialMediaSelected += "Instagram,";
        }
        setLoading(true);
        CollectionReference ordersCollection = FirebaseFirestore.getInstance().collection("Orders");

        final String uniqueID = UUID.randomUUID().toString();
        Map<String, Object> data = new HashMap<>();
        data.put("influencer", Objects.requireNonNull(influencerEditText.getText()).toString());
        data.put("socialMedia", socialMediaSelected);
        data.put("mode", modeSelected);

        OrderObject orderObject = new OrderObject(uniqueID, FirebaseAuth.getInstance().getCurrentUser().getUid(), "REQUEST", data, "INFLUENCER");

        ordersCollection.document(uniqueID).set(orderObject).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                setLoading(false);
                displaySuccessRequest(uniqueID);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                setLoading(false);

                Snackbar.make(nextFab, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

    }
    private void setLoading(Boolean isLoading) {
        RelativeLayout loadingButtonLayout = findViewById(R.id.loadingButtonLayout);
        ProgressBar loading_progress = findViewById(R.id.loading_progress);
        Transition transition = new Fade();
        transition.setDuration(600);
        transition.addTarget(progressBar);
        TransitionManager.beginDelayedTransition(loadingButtonLayout, transition);
        loading_progress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        nextFab.setVisibility(isLoading ? View.INVISIBLE : View.VISIBLE);
    }

    private void displaySuccessRequest(String uniqueId) {
        Intent intent = new Intent(this, SuccessSubmitActivity.class);
        intent.putExtra(ARGS_ORDER_ID, uniqueId);
        intent.putExtra(ARGS_ORDER_AMOUNT, 0);
        intent.putExtra(ARGS_ORDER_AMOUNT_IN_DOLLAR, 0);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }
}
