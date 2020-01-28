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

import com.mobbile.paul.codelab.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import com.mobbile.paul.shrine.MainActivity;

public class NewLoginActivity extends AppCompatActivity {
    TextView termsPrivacyTextView;
    RelativeLayout loginButtonLayout;
    TextInputEditText emailEditText, passwordEditText;
    TextInputLayout emailLayout, passwordLayout;
    MaterialButton loginButton;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);
        mAuth = FirebaseAuth.getInstance();

        termsPrivacyTextView = findViewById(R.id.termsPrivacyTextView);
        loginButtonLayout = findViewById(R.id.loginButtonLayout);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progress_bar);

        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);

        generatePrivacyTextContent();

        loginButton.setOnClickListener(v -> attemptLogin());
    }


    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();

    }

    private void setLoading(Boolean isLoading) {
        Transition transition = new Fade();
        transition.setDuration(600);
        transition.addTarget(progressBar);
        TransitionManager.beginDelayedTransition(loginButtonLayout, transition);
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        loginButton.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }


    private void attemptLogin() {

        String email, password;

        if (isValidEmail(emailEditText.getText())) {
            email = Objects.requireNonNull(emailEditText.getText()).toString();
            emailLayout.setError(null);
        } else {
            emailLayout.setError(getString(R.string.invalidEmail));
            return;
        }
        if (!TextUtils.isEmpty(passwordEditText.getText())) {
            password = Objects.requireNonNull(passwordEditText.getText()).toString();
            passwordLayout.setError(null);
        } else {
            passwordLayout.setError(getString(R.string.requiredPassword));
            return;
        }

        setLoading(true);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        setLoading(false);
                        currentUser = mAuth.getCurrentUser();
                        startActivity(new Intent(NewLoginActivity.this, MainActivity.class));

                    } else {
                        setLoading(false);
                        String errorMessage = (task.getException() != null) ? task.getException().getLocalizedMessage() : getString(R.string.failedRegistration);
                        Snackbar.make(loginButton, errorMessage, Snackbar.LENGTH_LONG).setAction(R.string.retryText, v -> attemptLogin()).show();
                    }
                });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void generatePrivacyTextContent() {

        SpannableString text = new SpannableString("By creating an account, you agree to our Terms of Use and Privacy Policy");

        text.setSpan(new URLSpan("http://acreativeexpression.ng/"), 41, 54, 0);
        text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorBeige)), 41, 54, 0);

        text.setSpan(new URLSpan("http://acreativeexpression.ng/"), 58, 72, 0);
        text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorBeige)), 58, 72, 0);

        // make our ClickableSpans and URLSpans work
        termsPrivacyTextView.setMovementMethod(LinkMovementMethod.getInstance());
        termsPrivacyTextView.setText(text, TextView.BufferType.SPANNABLE);


        findViewById(R.id.registerButton).setOnClickListener(v -> startActivity(new Intent(NewLoginActivity.this, SignUpActivity.class)));
        findViewById(R.id.loginButton).setOnClickListener(v -> startActivity(new Intent(NewLoginActivity.this, MainActivity.class)));

    }
}
