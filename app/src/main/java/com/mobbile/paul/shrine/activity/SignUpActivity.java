package com.mobbile.paul.shrine.activity;

import android.content.Intent;

import androidx.annotation.NonNull;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.mobbile.paul.codelab.R;
import com.mobbile.paul.shrine.MainActivity;
import com.mobbile.paul.shrine.models.UserProfile;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import static com.mobbile.paul.shrine.models.UserProfile.USER_DB_REFERENCE;

public class SignUpActivity extends AppCompatActivity {

    TextInputLayout nameLayout, emailLayout, passwordLayout, companyLayout;
    TextInputEditText nameEditText, emailEditText, passwordEditText, companyEditText;
    TextView termsPrivacyTextView;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ProgressBar progressBar;
    MaterialButton registerButton;
    RelativeLayout registerButtonLayout;

    String email, password, fullName, company;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        termsPrivacyTextView = findViewById(R.id.termsPrivacyTextView);
        nameLayout = findViewById(R.id.nameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        companyLayout = findViewById(R.id.companyLayout);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        companyEditText = findViewById(R.id.companyEditText);
        progressBar = findViewById(R.id.progress_bar);
        registerButton = findViewById(R.id.registerButton);
        registerButtonLayout = findViewById(R.id.registerButtonLayout);
        preparePrivacyTextView();

        findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpActivity.super.onBackPressed();
            }
        });

        findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegistration();
            }
        });
    }


    private void attemptRegistration() {
        if (isFieldsValid()) {
            registerUser();
        }
    }

    private boolean isFieldsValid() {
        if (!TextUtils.isEmpty(nameEditText.getText())) {
            fullName = Objects.requireNonNull(nameEditText.getText()).toString();
            nameLayout.setError(null);
        } else {
            nameLayout.setError(getString(R.string.nameRequiredError));
            return false;

        }

        if (isValidEmail(emailEditText.getText())) {
            email = Objects.requireNonNull(emailEditText.getText()).toString();
            emailLayout.setError(null);

        } else {
            emailLayout.setError(getString(R.string.invalidEmail));
            return false;

        }

        if (!TextUtils.isEmpty(nameEditText.getText())) {
            fullName = Objects.requireNonNull(nameEditText.getText()).toString();
            nameLayout.setError(null);

        } else {
            nameLayout.setError(getString(R.string.nameRequiredError));
            return false;

        }

        if (!TextUtils.isEmpty(passwordEditText.getText()) && !(Objects.requireNonNull(passwordEditText.getText()).toString().length() < 6)) {
            password = Objects.requireNonNull(passwordEditText.getText()).toString();
            passwordLayout.setError(null);

        } else {
            passwordLayout.setError(getString(R.string.invalidPassword));
            return false;

        }

        if (!TextUtils.isEmpty(companyEditText.getText())) {
            company = Objects.requireNonNull(companyEditText.getText()).toString();
            companyLayout.setError(null);
        } else {
            companyLayout.setError(getString(R.string.companyRequiredError));
            return false;
        }


        return true;

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void registerUser() {
        setLoading(true);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                updateUsersProfile(user);
                            }
                        } else {

                            setLoading(false);
                            String errorMessage = (task.getException() != null) ? task.getException().getLocalizedMessage() : getString(R.string.failedRegistration);
                            Snackbar.make(registerButtonLayout, errorMessage, Snackbar.LENGTH_LONG).setAction(R.string.retryText, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    attemptRegistration();
                                }
                            }).show();

                        }

                    }
                });
    }


    private void updateUsersProfile(@NonNull FirebaseUser user) {
        UserProfile userProfile = new UserProfile(fullName, user.getUid(), company, user.getEmail());
        db.collection(USER_DB_REFERENCE).document(user.getUid()).set(userProfile).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                setLoading(false);
                Snackbar.make(registerButtonLayout, R.string.successfulRegistration, Snackbar.LENGTH_LONG).setAction(R.string.loginText, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));

                    }
                }).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                setLoading(false);
                Snackbar.make(registerButtonLayout, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void setLoading(Boolean isLoading) {
        Transition transition = new Fade();
        transition.setDuration(600);
        transition.addTarget(progressBar);
        TransitionManager.beginDelayedTransition(registerButtonLayout, transition);
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        registerButton.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();

    }

    private void preparePrivacyTextView() {
        SpannableString text = new SpannableString("By creating an account, you agree to our Terms of Use and Privacy Policy");

        text.setSpan(new URLSpan("http://www.google.com"), 41, 54, 0);
        text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorDarkShade)), 41, 54, 0);

        text.setSpan(new URLSpan("http://www.google.com"), 58, 72, 0);
        text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorDarkShade)), 58, 72, 0);

        // make our ClickableSpans and URLSpans work
        termsPrivacyTextView.setMovementMethod(LinkMovementMethod.getInstance());
        termsPrivacyTextView.setText(text, TextView.BufferType.SPANNABLE);
    }
}
