package com.mobbile.paul.shrine.formActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mobbile.paul.codelab.R;
import com.mobbile.paul.shrine.activity.SuccessSubmitActivity;
import com.mobbile.paul.shrine.adapters.StoryboardRequestAdapter;
import com.mobbile.paul.shrine.models.OrderObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.mobbile.paul.shrine.activity.SuccessSubmitActivity.ARGS_ORDER_AMOUNT;
import static com.mobbile.paul.shrine.activity.SuccessSubmitActivity.ARGS_ORDER_AMOUNT_IN_DOLLAR;
import static com.mobbile.paul.shrine.activity.SuccessSubmitActivity.ARGS_ORDER_ID;

public class StoryboardRequestActivity extends AppCompatActivity {

    ExtendedFloatingActionButton nextFab;
    ProgressBar progressBar;
    ViewPager pager;
    AppCompatButton upload_button;
    String bannerUrl = "";
    String uniqueID = "";
    RadioButton _30_Sec, _61_Sec, _121_Sec, _6_Min;


    private static final int SELECT_PHOTO = 111;

    String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private boolean checkPermissions() {

        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do something
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storyboard_request);
        ImageView backdrop = findViewById(R.id.backdrop);
        Glide.with(this).asGif().load(R.raw.storyboardgif).into(backdrop);
        uniqueID = UUID.randomUUID().toString();


        if (!checkPermissions()) {
            Toast.makeText(this, "Permission needs to be granted to upload a banner_1", Toast.LENGTH_LONG).show();
        }

        upload_button = findViewById(R.id.upload_button);
        _30_Sec = findViewById(R.id._30_sec);
        _61_Sec = findViewById(R.id._61_sec);
        _121_Sec = findViewById(R.id._121_sec);
        _6_Min = findViewById(R.id._6_min);
        _30_Sec.setChecked(true);

        upload_button.setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                Snackbar.make(nextFab, R.string.not_logged_in_message, Snackbar.LENGTH_LONG).show();
                return;
            }
            pickAnImage(v);
        });

        StoryboardRequestAdapter illustrationAdapter = new StoryboardRequestAdapter();
        pager = findViewById(R.id.pager);
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(illustrationAdapter);
        progressBar = findViewById(R.id.progress_total);
        progressBar.setMax(2);
        progressBar.setProgress(1);

        nextFab = findViewById(R.id.fab);
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

                if (i == 1) {
                    nextFab.setText(R.string.submit_request_string);
                    Drawable myFabSrc = getResources().getDrawable(R.drawable.ic_check_black_24dp);
                    nextFab.setIcon(myFabSrc);
                    nextFab.extend();
                    nextFab.setOnClickListener(v -> {
                        displaySuccessRequest(uniqueID);
                    });
                } else if (i == 0) {
                    nextFab.setText(R.string.next);
                    Drawable myFabSrc = getResources().getDrawable(R.drawable.ic_chevron_right);
                    nextFab.setIcon(myFabSrc);
                    nextFab.extend();
                    nextFab.setOnClickListener(v -> {
                        if (bannerUrl == null || bannerUrl.isEmpty()) {
                            Snackbar.make(nextFab, "Ensure you've uploaded an image before proceeding", Snackbar.LENGTH_LONG).show();
                        } else {
                            pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                            requestIllustration();
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        nextFab.setOnClickListener(v -> {
            if (bannerUrl == null || bannerUrl.isEmpty()) {
                Snackbar.make(nextFab, "Ensure you've uploaded an image before proceeding", Snackbar.LENGTH_LONG).show();
            } else {
                pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                requestIllustration();
            }
        });
    }


    public void pickAnImage(View view) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, SELECT_PHOTO);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent fileReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, fileReturnedIntent);

        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference();

        if (requestCode == SELECT_PHOTO) {
            if (resultCode == RESULT_OK) {
                Uri file = fileReturnedIntent.getData();

                if (file == null) {
                    Toast.makeText(this, "Could not get file from storage", Toast.LENGTH_LONG).show();
                    return;
                }

                final StorageReference storageReference = storageRef.child("storyboard/" + file.getLastPathSegment());

                storageReference.putFile(file);
                UploadTask uploadTask;
                setUploadLoading(true);
                uploadTask = storageReference.putFile(file);


                uploadTask.continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        setUploadLoading(false);
                        if (task.getException() != null) {
                            Snackbar.make(nextFab, task.getException().getLocalizedMessage(), Snackbar.LENGTH_LONG)
                                    .show();
                            throw task.getException();
                        }
                    }
                    // Continue with the task to get the download URL
                    return storageReference.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    setUploadLoading(false);
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        assert downloadUri != null;
                        bannerUrl = downloadUri.toString();

                        upload_button.setText(getString(R.string.upload_complete));
                        Drawable srcTick = getResources().getDrawable(R.drawable.ic_check_black_24dp);
                        upload_button.setCompoundDrawablesWithIntrinsicBounds(null, null, srcTick, null);

                        int color = ContextCompat.getColor(StoryboardRequestActivity.this, R.color.colorWarmGreenBackground);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            DrawableCompat.setTint(srcTick, color);

                        } else {
                            srcTick.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        }

                    } else {
                        // Handle failures
                        // ...
                    }
                });


            }
        }
    }

    private void requestIllustration() {

        if (bannerUrl == null || bannerUrl.isEmpty()) {
            Snackbar.make(nextFab, "Ensure you've uploaded an image before proceeding", Snackbar.LENGTH_LONG).show();
            return;
        }

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Snackbar.make(nextFab, R.string.not_logged_in_message, Snackbar.LENGTH_LONG).show();
            return;
        }

        setLoading(true);
        CollectionReference ordersCollection = FirebaseFirestore.getInstance().collection("Orders");


        Map<String, Object> data = new HashMap<>();
        data.put("bannerUrl", bannerUrl);

        OrderObject orderObject = new OrderObject(uniqueID, FirebaseAuth.getInstance().getCurrentUser().getUid(), "REQUEST", data, "STORYBOARD");

        ordersCollection.document(uniqueID).set(orderObject).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                setLoading(false);
                //displaySuccessRequest(uniqueID);

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


    private void setUploadLoading(Boolean isLoading) {
        RelativeLayout uploadButtonLayout = findViewById(R.id.uploadButtonLayout);
        ProgressBar upload_loading_progress = findViewById(R.id.upload_loading_progress);
        Transition transition = new Fade();
        transition.setDuration(600);
        transition.addTarget(progressBar);
        TransitionManager.beginDelayedTransition(uploadButtonLayout, transition);
        upload_loading_progress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        upload_button.setVisibility(isLoading ? View.INVISIBLE : View.VISIBLE);
    }


    private void displaySuccessRequest(String uniqueId) {

        //switching the amount here
        Double amount = 0.0;

        if (_30_Sec.isChecked()) {
            amount = 54000.00;
        } else if (_61_Sec.isChecked()) {
            amount = 90000.0;
        } else if (_121_Sec.isChecked()) {
            amount = 180000.0;
        } else if (_6_Min.isChecked()) {
            amount = 306000.0;
        }

        Intent intent = new Intent(this, SuccessSubmitActivity.class);
        intent.putExtra(ARGS_ORDER_ID, uniqueId);
        intent.putExtra(ARGS_ORDER_AMOUNT, amount);
        intent.putExtra(ARGS_ORDER_AMOUNT_IN_DOLLAR, amount);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}