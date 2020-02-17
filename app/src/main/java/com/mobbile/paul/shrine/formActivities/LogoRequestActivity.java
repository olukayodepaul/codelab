package com.mobbile.paul.shrine.formActivities;

import android.animation.ObjectAnimator;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.widget.AppCompatButton;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobbile.paul.codelab.R;
import com.mobbile.paul.shrine.LogoRequestAdapter;
import com.mobbile.paul.shrine.activity.PayPal;
import com.mobbile.paul.shrine.activity.SuccessSubmitActivity;
import com.mobbile.paul.shrine.fragments.ColorPickerDialog;
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


public class LogoRequestActivity extends AppCompatActivity {


    TextInputEditText brandNameEditText, inspiration_1, inspiration_2, inspiration_3, inspiration_4, inspiration_5;

    ExtendedFloatingActionButton nextFab;

    ProgressBar progressBar;
    ViewPager pager;
    AppCompatButton colour_1_button, colour_2_button, colour_3_button;
    String colour_1, colour_2, colour_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_request);

        ImageView backdrop = findViewById(R.id.backdrop);
        Glide.with(this).asGif().load(R.raw.logodesign).into(backdrop);

        LogoRequestAdapter logoRequestAdapter = new LogoRequestAdapter();
        pager = findViewById(R.id.pager);

        pager.setOffscreenPageLimit(3);
        pager.setAdapter(logoRequestAdapter);
        progressBar = findViewById(R.id.progress_total);
        progressBar.setMax(3);
        progressBar.setProgress(1);

        brandNameEditText = findViewById(R.id.brandNameEditText);
        inspiration_1 = findViewById(R.id.inspiration_1);
        inspiration_2 = findViewById(R.id.inspiration_2);
        inspiration_3 = findViewById(R.id.inspiration_3);
        inspiration_4 = findViewById(R.id.inspiration_4);
        inspiration_5 = findViewById(R.id.inspiration_5);
        inspiration_1.addTextChangedListener(textWatcher);
        inspiration_2.addTextChangedListener(textWatcher);
        inspiration_3.addTextChangedListener(textWatcher);
        inspiration_4.addTextChangedListener(textWatcher);
        inspiration_5.addTextChangedListener(textWatcher);
        colour_1_button = findViewById(R.id.colour_1);

        colour_2_button = findViewById(R.id.colour_2);

        colour_3_button = findViewById(R.id.colour_3);

        Log.d("LogoRequestActivity_ty","LogoRequestActivity_ty");


        setColorPickers();
//        brandNameEditText.requestFocus();
        nextFab = findViewById(R.id.fab);
        nextFab.shrink();
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

                    //hide the test here
                    nextFab.setText(R.string.submit_request_string);

                    Drawable myFabSrc = getResources().getDrawable(R.drawable.ic_check_black_24dp);
                    nextFab.setIcon(myFabSrc);
                    nextFab.extend();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null && getCurrentFocus() != null){
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                    nextFab.setOnClickListener(v -> requestLogo());
                } else {
                    nextFab.setText(R.string.next);
                    Drawable myFabSrc = getResources().getDrawable(R.drawable.ic_chevron_right);
                    nextFab.setIcon(myFabSrc);
                    nextFab.shrink();
                    nextFab.setOnClickListener(v -> {
                        if (pager != null)
                            pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        nextFab.setOnClickListener(v -> {
            if (pager != null)
                pager.setCurrentItem(pager.getCurrentItem() + 1, true);
        });
    }

    //this is for color picker
    private void setColorPickers() {
        colour_1_button.setOnClickListener(v -> {
            ColorPickerDialog dialog =  ColorPickerDialog.newInstance(Color.BLUE);
            dialog.addOnColorSelectedCallback(color -> {
                v.setBackgroundColor(color);
                colour_1 = String.format("#%06X", 0xFFFFFF & color);
            });
            dialog.show(getSupportFragmentManager(), "Color Wheel 1");
        });

        colour_2_button.setOnClickListener(v -> {
            ColorPickerDialog dialog =  ColorPickerDialog.newInstance(Color.YELLOW);
            dialog.addOnColorSelectedCallback(color -> {
                v.setBackgroundColor(color);
                colour_2 = String.format("#%06X", 0xFFFFFF & color);
            });
            dialog.show(getSupportFragmentManager(), "Color Wheel 2");
        });

        colour_3_button.setOnClickListener(v -> {
            ColorPickerDialog dialog =  ColorPickerDialog.newInstance(Color.GREEN);
            dialog.addOnColorSelectedCallback(color -> {
                v.setBackgroundColor(color);
                colour_3 = String.format("#%06X", 0xFFFFFF & color);
            });

            dialog.show(getSupportFragmentManager(), "Color Wheel 3");
        });
    }

    TextWatcher textWatcher = new TextWatcher() {

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            TextView text = (TextView) getCurrentFocus();

            if (text != null && s.toString().contains(",")) {
                text.setText(s.toString().replace(",", ""));
                View next = text.focusSearch(View.FOCUS_DOWN); // or FOCUS_FORWARD
                if (next != null) {
                    next.requestFocus();
                }
//                doSearch(); // Or whatever
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
        }

        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }

    };


    private void requestLogo() {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Snackbar.make(nextFab, R.string.not_logged_in_message, Snackbar.LENGTH_LONG).show();
            return;
        }

        setLoading(true);
        CollectionReference ordersCollection = FirebaseFirestore.getInstance().collection("Orders");


        final String uniqueID = UUID.randomUUID().toString();
        Map<String, Object> data = new HashMap<>();
        data.put("BrandName", Objects.requireNonNull(brandNameEditText.getText()).toString());
        data.put("BrandColors", String.format("%s,%s,%s", colour_1, colour_2, colour_3));
        data.put("BrandInspiration", String.format("%s,%s,%s,%s,%s", inspiration_1.getText(),
                inspiration_2.getText(), inspiration_3.getText(),
                inspiration_4.getText(), inspiration_5.getText()));

        OrderObject orderObject = new OrderObject(uniqueID, FirebaseAuth.getInstance().getCurrentUser().getUid(), "REQUEST", data, "LOGO");

        ordersCollection.document(uniqueID).set(orderObject).addOnSuccessListener(aVoid -> { setLoading(false);displaySuccessRequest(uniqueID);


        }).addOnFailureListener(e -> {
            setLoading(false);
            Snackbar.make(nextFab, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
        });
    }


    private void displaySuccessRequest(String uniqueId) {

        Intent intent = new Intent(this, SuccessSubmitActivity.class);
        intent.putExtra(ARGS_ORDER_ID, uniqueId);
        intent.putExtra(ARGS_ORDER_AMOUNT, 36000);
        intent.putExtra(ARGS_ORDER_AMOUNT_IN_DOLLAR, 100);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

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

    private void addPayPalIntent() {
        Intent intent = new Intent(this, PayPal.class);
        startActivity(intent);
    }

    /**
     * Begins enter transition.
     //     */
//    private void beginEnterTransition() {
//        product_image.post(new Runnable() {
//            @Override
//            public void run() {
//
//                product_title.startAnimation(AnimationUtils.loadAnimation(LogoRequestActivity.this, R.anim.translatedownandfadein));
//                product_description.startAnimation(AnimationUtils.loadAnimation(LogoRequestActivity.this, R.anim.fadein));
//
//            }
//        });
//        product_description.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                buttonsLayout.startAnimation(AnimationUtils.loadAnimation(LogoRequestActivity.this, R.anim.fadein));
//
//            }
//        }, 300);
//    }
}
