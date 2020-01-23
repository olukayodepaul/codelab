package com.mobbile.paul.shrine.formActivities;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.codelabs.mdc.java.shrine.LogoRequestAdapter;
import com.google.codelabs.mdc.java.shrine.R;
import com.google.codelabs.mdc.java.shrine.activity.PaymentActivity;
import com.google.codelabs.mdc.java.shrine.activity.SuccessSubmitActivity;
import com.google.codelabs.mdc.java.shrine.adapters.SocialMediaRequestAdapter;
import com.google.codelabs.mdc.java.shrine.models.OrderObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.google.codelabs.mdc.java.shrine.activity.SuccessSubmitActivity.ARGS_ORDER_AMOUNT;
import static com.google.codelabs.mdc.java.shrine.activity.SuccessSubmitActivity.ARGS_ORDER_AMOUNT_IN_DOLLAR;
import static com.google.codelabs.mdc.java.shrine.activity.SuccessSubmitActivity.ARGS_ORDER_ID;

public class SocialMediaActivity extends AppCompatActivity {


    ExtendedFloatingActionButton nextFab;

    ProgressBar progressBar;
    ViewPager pager;

    TextInputEditText brandInfoEditText;

    ToggleButton isPageIncreaseOn;
    RadioGroup pageEngagementRadioGroup;
    RadioGroup pageIncreaseRadioGroup;
    RadioGroup contentManagementRadioGroup;

    String achievementGoal ="";
    String socialMedia = "";
    RadioButton first,second,third, firstEngageRadio,secondEngageRadio,thirdEngageRadio;
    RadioButton firstContentRadio,secondContentRadio,thirdContentRadio,fourContentRadio,fiveContentRadio;
    Chip facebook_chip, twitter_chip, instagram_chip, linkedin_chip;

    CheckBox page_increase_button,page_engagement_button,content_management_button,all_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);
        ImageView backdrop = findViewById(R.id.backdrop);
        Glide.with(this).asGif().load(R.raw.socialmediaengage).into(backdrop);

        SocialMediaRequestAdapter socialMediaRequestAdapter = new SocialMediaRequestAdapter();
        pager = findViewById(R.id.pager);
        pager.setOffscreenPageLimit(4);
        pager.setAdapter(socialMediaRequestAdapter);
        progressBar = findViewById(R.id.progress_total);
        progressBar.setMax(4);
        progressBar.setProgress(1);

        nextFab = findViewById(R.id.fab);
        nextFab.shrink();
        first  = findViewById(R.id.first);
        second  = findViewById(R.id.second);
        third  = findViewById(R.id.third);
        firstEngageRadio  = findViewById(R.id.firstEngageRadio);
        secondEngageRadio  = findViewById(R.id.secondEngageRadio);
        thirdEngageRadio  = findViewById(R.id.thirdEngageRadio);

        firstContentRadio  = findViewById(R.id.firstContentRadio);
        secondContentRadio  = findViewById(R.id.secondContentRadio);
        thirdContentRadio  = findViewById(R.id.thirdContentRadio);
        fourContentRadio  = findViewById(R.id.fourContentRadio);
        fiveContentRadio  = findViewById(R.id.fiveContentRadio);

        page_increase_button =  findViewById(R.id.page_increase_button);
        page_engagement_button = findViewById(R.id.page_engagement_button);
        content_management_button = findViewById(R.id.content_management_button);
        all_button = findViewById(R.id.all_button);

        facebook_chip = findViewById(R.id.facebook_chip);
        twitter_chip = findViewById(R.id.twitter_chip);
        instagram_chip = findViewById(R.id.instagram_chip);
        linkedin_chip = findViewById(R.id.linkedin_chip);



        facebook_chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    socialMedia += "facebook,";
            }
        });

        twitter_chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    socialMedia += "twitter,";
            }
        });


        instagram_chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    socialMedia += "instagram,";
            }
        });

        linkedin_chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    socialMedia += "linkedin,";
            }
        });

        first.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    achievementGoal = getString(R.string.build_up_to_1k_followers_in_3_months);

            }
        });
        second.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    achievementGoal = getString(R.string.get_up_to_5k_followers_in_6_months);

            }
        });
        third.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    achievementGoal = getString(R.string.get_over_20k_followers_in_1_year);

            }
        });

        firstEngageRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    achievementGoal = getString(R.string._10_000_impressions_in_3_months);

            }
        });


        secondEngageRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    achievementGoal = getString(R.string.over_25k_impressions_in_6_months);

            }
        });


        thirdEngageRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    achievementGoal = getString(R.string.over_50k_impressions_in_1_year);

            }
        });



        firstContentRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    achievementGoal = getString(R.string.minimum);

            }
        });



        secondContentRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    achievementGoal = getString(R.string._14_content_per_week);

            }
        });


        thirdContentRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    achievementGoal = getString(R.string._56_per_month_and_6_animated);

            }
        });


        fourContentRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    achievementGoal = getString(R.string._25_interactive_tweet);

            }
        });


        fiveContentRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    achievementGoal = getString(R.string._3_contents_per_week_12_content);

            }
        });

        brandInfoEditText  = findViewById(R.id.brandInfoEditText);
//        brandInfoEditText.requestFocus();
        isPageIncreaseOn = findViewById(R.id.engagement_type_button);
        pageEngagementRadioGroup = findViewById(R.id.pageEngagementRadioGroup);
        contentManagementRadioGroup = findViewById(R.id.contentManagementRadioGroup);
        pageIncreaseRadioGroup = findViewById(R.id.pageIncreaseRadioGroup);


        pageIncreaseRadioGroup.setVisibility(View.GONE);
        pageEngagementRadioGroup.setVisibility(View.GONE);
        contentManagementRadioGroup.setVisibility(View.GONE);

        page_increase_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    pageIncreaseRadioGroup.setVisibility(View.VISIBLE);
                }
                else {
                    pageIncreaseRadioGroup.setVisibility(View.GONE);
                }
            }
        });
        page_engagement_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    pageEngagementRadioGroup.setVisibility(View.VISIBLE);
                }
                else {
                    pageEngagementRadioGroup.setVisibility(View.GONE);
                }
            }
        });

        content_management_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    contentManagementRadioGroup.setVisibility(View.VISIBLE);
                }
                else{
                    contentManagementRadioGroup.setVisibility(View.GONE);
                }
            }
        });

        all_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    pageIncreaseRadioGroup.setVisibility(View.VISIBLE);
                    pageEngagementRadioGroup.setVisibility(View.VISIBLE);
                    contentManagementRadioGroup.setVisibility(View.VISIBLE);
                }
            }
        });



//
//        InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
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
//

                    nextFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestSocialMedia();                        }
                    });
                } else {
                    nextFab.setText(R.string.next);
                    Drawable myFabSrc = getResources().getDrawable(R.drawable.ic_chevron_right);
                    nextFab.setIcon(myFabSrc);
                    nextFab.shrink();
                    nextFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(pager != null)
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

                if(pager != null)
                    pager.setCurrentItem(pager.getCurrentItem() + 1, true);
            }
        });

//        product_image = findViewById(R.id.product_image);
//        product_title = findViewById(R.id.product_title);
//        product_description = findViewById(R.id.product_description);
//        buttonsLayout = findViewById(R.id.buttonsLayout);
//        pageIncreaseChip = findViewById(R.id.pageIncreaseChip);
//        pageEngagementChip = findViewById(R.id.pageEngagementChip);
//
//
//        pageIncreaseChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    findViewById(R.id.pageIncreaseRadioGroup).setVisibility(View.VISIBLE);
//                    achievement = "Page Increase";
//                } else {
//                    findViewById(R.id.pageIncreaseRadioGroup).setVisibility(View.GONE);
//                    achievement = "";
//
//                }
//
//            }
//        });
//
//        pageEngagementChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    findViewById(R.id.pageEngagementRadioGroup).setVisibility(View.VISIBLE);
//                    achievement = "Page Engagement";
//
//                } else {
//                    findViewById(R.id.pageEngagementRadioGroup).setVisibility(View.GONE);
//                    achievement = "";
//                }
//
//            }
//        });
//
//        beginEnterTransition();

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


    private void requestSocialMedia(){
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Snackbar.make(nextFab, R.string.not_logged_in_message, Snackbar.LENGTH_LONG).show();
            return;
        }
        setLoading(true);
        CollectionReference ordersCollection = FirebaseFirestore.getInstance().collection("Orders");

        final String uniqueID = UUID.randomUUID().toString();
        Map<String, Object> data = new HashMap<>();
        data.put("BrandInfo", Objects.requireNonNull(brandInfoEditText.getText()).toString());
        data.put("SocialMedia", socialMedia);
        data.put("AchievementGoal", achievementGoal);

        OrderObject orderObject = new OrderObject(uniqueID, FirebaseAuth.getInstance().getCurrentUser().getUid(), "REQUEST", data, "Social Media Management");

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

    private void displaySuccessRequest(String uniqueId) {
        Intent intent = new Intent(this, SuccessSubmitActivity.class);
        intent.putExtra(ARGS_ORDER_ID, uniqueId);
        intent.putExtra(ARGS_ORDER_AMOUNT, 50000);
        intent.putExtra(ARGS_ORDER_AMOUNT_IN_DOLLAR, 0);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

//beginEnterTransition
//    private void beginEnterTransition() {
//        product_image.post(new Runnable() {
//            @Override
//            public void run() {
//
//                product_title.startAnimation(AnimationUtils.loadAnimation(SocialMediaActivity.this, R.anim.translatedownandfadein));
//                product_description.startAnimation(AnimationUtils.loadAnimation(SocialMediaActivity.this, R.anim.fadein));
//
//            }
//        });
//        product_description.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                buttonsLayout.startAnimation(AnimationUtils.loadAnimation(SocialMediaActivity.this, R.anim.fadein));
//
//            }
//        }, 300);
//    }
}
