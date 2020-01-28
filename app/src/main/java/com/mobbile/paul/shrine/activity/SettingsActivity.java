package com.mobbile.paul.shrine.activity;

import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobbile.paul.codelab.R;

import io.kommunicate.Kommunicate;
import io.kommunicate.callbacks.KMLogoutHandler;

public class SettingsActivity extends AppCompatActivity {

    ImageView product_image;

    TextView product_title;

    TextView product_description;

    LinearLayout buttonsLayout;

    TextInputEditText nameEditText,emailEditText,companyEditText;
    FirebaseUser currentUser;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mAuth = FirebaseAuth.getInstance();

        product_image = findViewById(R.id.product_image);
        product_title = findViewById(R.id.product_title);
        product_description = findViewById(R.id.product_description);
        buttonsLayout = findViewById(R.id.buttonsLayout);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        companyEditText = findViewById(R.id.companyEditText);

        beginEnterTransition();

        currentUser = mAuth.getCurrentUser();
        //check if user is not null
        if(currentUser != null){
            emailEditText.setText(currentUser.getEmail());
            if (currentUser.getDisplayName() != null) {
                nameEditText.setText(currentUser.getDisplayName());
            }
            // TODO: 06/05/2019 Display name and company
        }
        else {
            //user is not logged in.
        }
        findViewById(R.id.logout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Kommunicate.logout(SettingsActivity.this, new KMLogoutHandler() {
                    @Override
                    public void onSuccess(Context context) {

                    }

                    @Override
                    public void onFailure(Exception exception) {
//                        Log.i("Logout","Failed");
                    }
                });
                startActivity(new Intent(SettingsActivity.this, SplashScreenActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void beginEnterTransition() {
        product_image.post(new Runnable() {
            @Override
            public void run() {

                product_title.startAnimation(AnimationUtils.loadAnimation(SettingsActivity.this, R.anim.translatedownandfadein));
                product_description.startAnimation(AnimationUtils.loadAnimation(SettingsActivity.this, R.anim.fadein));

            }
        });
        product_description.postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonsLayout.startAnimation(AnimationUtils.loadAnimation(SettingsActivity.this, R.anim.fadein));
                buttonsLayout.setVisibility(View.VISIBLE);


            }
        }, 300);
    }
}
