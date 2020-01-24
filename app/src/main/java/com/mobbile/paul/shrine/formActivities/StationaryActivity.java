package com.mobbile.paul.shrine.formActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mobbile.paul.codelab.R;
import com.mobbile.paul.shrine.activity.SuccessSubmitActivity;
import com.mobbile.paul.shrine.adapters.StationaryAdapter;
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

public class StationaryActivity extends AppCompatActivity {

    ExtendedFloatingActionButton nextFab;

    CheckBox a5RadioButton,a4RadioButton, letterheadRadioButton, businessCardRadioButton,penBrandingButton,cupBrandingButton,receiptBoolketsButton,carrierBagsButton;
    ViewPager pager;
    ProgressBar progressBar;

    ImageView backdrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stationary);

        StationaryAdapter brandingRequestAdapter = new StationaryAdapter();

        backdrop = findViewById(R.id.backdrop);
        Glide.with(this).asGif().load(R.raw.stationery).into(backdrop);

        pager = findViewById(R.id.pager);
        pager.setOffscreenPageLimit(1);
        pager.setAdapter(brandingRequestAdapter);
        progressBar = findViewById(R.id.progress_total);
        progressBar.setMax(1);
        progressBar.setProgress(1);
        nextFab = findViewById(R.id.fab);
//        nextFab.shrink();

        a4RadioButton = findViewById(R.id.a4RadioButton);
        letterheadRadioButton = findViewById(R.id.letterheadRadioButton);
        businessCardRadioButton = findViewById(R.id.businessCardRadioButton);

        a5RadioButton = findViewById(R.id.a5RadioButton);
        penBrandingButton = findViewById(R.id.penBrandingButton);
        cupBrandingButton = findViewById(R.id.cupBrandingButton);
        carrierBagsButton = findViewById(R.id.carrierBagsButton);
        receiptBoolketsButton = findViewById(R.id.receiptBoolketsButton);

        nextFab.setText(R.string.request_catalog);
        Drawable myFabSrc = getResources().getDrawable(R.drawable.ic_check_black_24dp);
        nextFab.setIcon(myFabSrc);
        nextFab.extend();

        nextFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestBranding();
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


    private void requestBranding() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Snackbar.make(nextFab, R.string.not_logged_in_message, Snackbar.LENGTH_LONG).show();
            return;
        }
        setLoading(true);
        CollectionReference ordersCollection = FirebaseFirestore.getInstance().collection("Orders");

        String stationaryType = "";
        if (a4RadioButton.isChecked()) {
            stationaryType = "A4 Envelop,";
        } else if (letterheadRadioButton.isChecked()) {
            stationaryType += "Letterhead,";
        }else if (businessCardRadioButton.isChecked()) {
            stationaryType += "Business card,";
        }
        else if (a5RadioButton.isChecked()) {
            stationaryType += "A5 Envelop,";
        }else if (penBrandingButton.isChecked()) {
            stationaryType += "Pen Branding,";
        }else if (cupBrandingButton.isChecked()) {
            stationaryType += "Cup Branding,";
        }else if (carrierBagsButton.isChecked()) {
            stationaryType += "Carrier Bags,";
        }else if (receiptBoolketsButton.isChecked()) {
            stationaryType += "Receipt Booklets";
        }
        final String uniqueID = UUID.randomUUID().toString();
        Map<String, Object> data = new HashMap<>();
        data.put("StationaryType", Objects.requireNonNull(stationaryType));

        OrderObject orderObject = new OrderObject(uniqueID, FirebaseAuth.getInstance().getCurrentUser().getUid(), "REQUEST", data, "Stationary");

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
        intent.putExtra(ARGS_ORDER_AMOUNT, 54000);
        intent.putExtra(ARGS_ORDER_AMOUNT_IN_DOLLAR, 150);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }
}