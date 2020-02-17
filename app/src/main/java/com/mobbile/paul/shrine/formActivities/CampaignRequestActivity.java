package com.mobbile.paul.shrine.formActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.viewpager.widget.ViewPager;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.mobbile.paul.codelab.R;
import com.mobbile.paul.shrine.activity.SuccessSubmitActivity;
import com.mobbile.paul.shrine.adapters.CampaignRequestAdapter;
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

public class CampaignRequestActivity extends AppCompatActivity {

    ExtendedFloatingActionButton nextFab;

    ProgressBar progressBar;
    ViewPager pager;
    TextInputEditText objectivesEditText;
    TextInputEditText relativeInfoEditText;
    TextInputEditText competitorsEditText;
    TextInputEditText milestoneEditText;
    TextInputEditText budgetEditText;
    TextInputEditText deliverableEditText;
    TextInputEditText aspirationsEditText;
    TextInputEditText featuresEditText;
    TextInputEditText benefitEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_request);
        CampaignRequestAdapter campaignRequestAdapter = new CampaignRequestAdapter();

        pager = findViewById(R.id.pager);
        pager.setOffscreenPageLimit(9);
        pager.setAdapter(campaignRequestAdapter);
        progressBar = findViewById(R.id.progress_total);
        progressBar.setMax(9);
        progressBar.setProgress(1);
        objectivesEditText= findViewById(R.id.objectivesEditText);
        relativeInfoEditText= findViewById(R.id.relativeInfoEditText);
        competitorsEditText= findViewById(R.id.competitorsEditText);
        milestoneEditText= findViewById(R.id.milestoneEditText);
        budgetEditText= findViewById(R.id.budgetEditText);
        deliverableEditText= findViewById(R.id.deliverableEditText);
        aspirationsEditText= findViewById(R.id.aspirationsEditText);
        featuresEditText= findViewById(R.id.featuresEditText);
        benefitEditText= findViewById(R.id.benefitEditText);

//        backgroundEditText.requestFocus();
        nextFab = findViewById(R.id.fab);
        nextFab.shrink();

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
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (getCurrentFocus() != null){
                        imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
                    }
                    nextFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestCampaign();
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

    }


    private void requestCampaign() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Snackbar.make(nextFab, R.string.not_logged_in_message, Snackbar.LENGTH_LONG).show();
            return;
        }

        setLoading(true);
        CollectionReference ordersCollection = FirebaseFirestore.getInstance().collection("Orders");


        final String uniqueID = UUID.randomUUID().toString();
        Map<String, Object> data = new HashMap<>();

        objectivesEditText= findViewById(R.id.objectivesEditText);
        relativeInfoEditText= findViewById(R.id.relativeInfoEditText);
        competitorsEditText= findViewById(R.id.competitorsEditText);
        milestoneEditText= findViewById(R.id.milestoneEditText);
        budgetEditText= findViewById(R.id.budgetEditText);
        deliverableEditText= findViewById(R.id.deliverableEditText);
        aspirationsEditText= findViewById(R.id.aspirationsEditText);
        featuresEditText= findViewById(R.id.featuresEditText);
        benefitEditText= findViewById(R.id.benefitEditText);

        data.put("objectives", Objects.requireNonNull(objectivesEditText.getText()).toString());
        data.put("relativeInformation", Objects.requireNonNull(relativeInfoEditText.getText()).toString());
        data.put("competitors", Objects.requireNonNull(competitorsEditText.getText()).toString());
        data.put("milestone", Objects.requireNonNull(milestoneEditText.getText()).toString());
        data.put("budget", Objects.requireNonNull(budgetEditText.getText()));
        data.put("deliverable", Objects.requireNonNull(deliverableEditText.getText()).toString());
        data.put("aspirations", Objects.requireNonNull(aspirationsEditText.getText()).toString());
        data.put("features", Objects.requireNonNull(featuresEditText.getText()).toString());
        data.put("benefit", Objects.requireNonNull(benefitEditText.getText()).toString());


        OrderObject orderObject = new OrderObject(uniqueID, FirebaseAuth.getInstance().getCurrentUser().getUid(), "REQUEST", data, "CAMPAIGN");

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
        intent.putExtra(ARGS_ORDER_AMOUNT, 400000);
        intent.putExtra(ARGS_ORDER_AMOUNT_IN_DOLLAR, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }
}
