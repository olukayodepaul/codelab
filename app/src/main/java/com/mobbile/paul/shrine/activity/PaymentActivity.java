package com.mobbile.paul.shrine.activity;


import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.mobbile.paul.codelab.R;
import com.mobbile.paul.shrine.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.exceptions.ExpiredAccessCodeException;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import static com.mobbile.paul.shrine.activity.SuccessSubmitActivity.ARGS_ORDER_AMOUNT;
import static com.mobbile.paul.shrine.activity.SuccessSubmitActivity.ARGS_ORDER_ID;


public class PaymentActivity extends AppCompatActivity {

    EditText mEditCardNum;
    EditText mEditCVC;
    EditText mEditExpiryMonth;
    EditText mEditExpiryYear;
    private Charge charge;
    private Transaction transaction;

    String orderId;

    int amount;

    private FirebaseAuth mAuth;

    Button mButtonPerformLocalTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        orderId = getIntent().getStringExtra(ARGS_ORDER_ID);
        amount = getIntent().getIntExtra(ARGS_ORDER_AMOUNT, 0);

        mEditCardNum = findViewById(R.id.edit_card_number);
        mEditCVC = findViewById(R.id.edit_cvc);
        mEditExpiryMonth = findViewById(R.id.edit_expiry_month);
        mEditExpiryYear = findViewById(R.id.edit_expiry_year);


        mButtonPerformLocalTransaction = findViewById(R.id.button_perform_local_transaction);

        mButtonPerformLocalTransaction.setText(String.format(Locale.getDefault(), "Pay - NGN%d", amount));
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentActivity.this, MainActivity.class));
            }
        });

        mButtonPerformLocalTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                } catch (Exception ignored) {
                }
                try {
                    setLoading(true);
                    startAFreshCharge();
                } catch (Exception e) {
                    Snackbar.make(view, String.format("An error occurred while charging card: %s %s", e.getClass().getSimpleName(), e.getMessage()), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }


    private void startAFreshCharge() {
        // initialize the charge
        charge = new Charge();
        charge.setCard(loadCardFromForm());

        mAuth = FirebaseAuth.getInstance();
        int amountInKobo = amount * 100;
        FirebaseUser currentUser = mAuth.getCurrentUser();
        charge.setAmount(amountInKobo);
        if (currentUser != null) {
            charge.setEmail(currentUser.getEmail());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            charge.setReference("Ace_" + Calendar.getInstance().getTimeInMillis());
        }
        try {
            charge.putCustomField("Charged From", "Android SDK");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        chargeCard();

    }

    private void submitPayment(String ref){
        Map<String, Object> data = new HashMap<>();
        data.put("OrderId", Objects.requireNonNull(orderId));
        data.put("paymentRef", Objects.requireNonNull(ref));

        CollectionReference paymentsCollection = FirebaseFirestore.getInstance().collection("Payments");

        paymentsCollection.document(ref).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                setLoading(false);

                Snackbar.make(PaymentActivity.this.mEditCardNum, String.format("Your payment was successful - Ref: %s", PaymentActivity.this.transaction.getReference()), Snackbar.LENGTH_INDEFINITE).setAction("Complete", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                setLoading(false);

                Snackbar.make(PaymentActivity.this.mEditCardNum, String.format("Payment was made, but not registered, give support this reference: %s", PaymentActivity.this.transaction.getReference()), Snackbar.LENGTH_INDEFINITE).show();

            }
        });
    }
    /**
     * Method to validate the form, and set errors on the edittexts.
     */
    private Card loadCardFromForm() {
        //validate fields
        Card card;

        String cardNum = mEditCardNum.getText().toString().trim();

        //build card object with ONLY the number, update the other fields later
        card = new Card.Builder(cardNum, 0, 0, "").build();
        String cvc = mEditCVC.getText().toString().trim();
        //update the cvc field of the card
        card.setCvc(cvc);

        //validate expiry month;
        String sMonth = mEditExpiryMonth.getText().toString().trim();
        int month = 0;
        try {
            month = Integer.parseInt(sMonth);
        } catch (Exception ignored) {
        }

        card.setExpiryMonth(month);

        String sYear = mEditExpiryYear.getText().toString().trim();
        int year = 0;
        try {
            year = Integer.parseInt(sYear);
        } catch (Exception ignored) {
        }
        card.setExpiryYear(year);

        return card;
    }


    private void chargeCard() {
        transaction = null;
        PaystackSdk.chargeCard(this, charge, new Paystack.TransactionCallback() {
            // This is called only after transaction is successful
            @Override
            public void onSuccess(Transaction transaction) {

                PaymentActivity.this.transaction = transaction;
                CollectionReference ordersCollection = FirebaseFirestore.getInstance().collection("Orders");

                DocumentReference orderReference = ordersCollection.document(orderId);

                Map<String, Object> updates = new HashMap<>();
                updates.put("Status", "PAID");
                updates.put("AmountPaid", amount);
                updates.put("PaymentReference", transaction.getReference());
                updates.put("DatePaid", FieldValue.serverTimestamp());

                orderReference
                        .update(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                submitPayment(PaymentActivity.this.transaction.getReference());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                setLoading(false);

                                Snackbar.make(PaymentActivity.this.mEditCardNum, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                            }
                        });


            }

            // This is called only before requesting OTP
            // Save reference so you may send to server if
            // error occurs with OTP
            // No need to dismiss dialog
            @Override
            public void beforeValidate(Transaction transaction) {
                PaymentActivity.this.transaction = transaction;
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                setLoading(false);
                // If an access code has expired, simply ask your server for a new one
                // and restart the charge instead of displaying error
                PaymentActivity.this.transaction = transaction;
                if (error instanceof ExpiredAccessCodeException) {
                    PaymentActivity.this.startAFreshCharge();
                    PaymentActivity.this.chargeCard();
                    return;
                }
                if (transaction.getReference() != null) {
                    Snackbar.make(PaymentActivity.this.mEditCardNum, String.format("%s  concluded with error: %s %s", transaction.getReference(), error.getClass().getSimpleName(), error.getMessage()), Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(PaymentActivity.this.mEditCardNum, String.format("Error: %s %s", error.getClass().getSimpleName(), error.getMessage()), Snackbar.LENGTH_LONG).show();

                }
            }

        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PaymentActivity.this, MainActivity.class));
    }

    private void setLoading(Boolean isLoading) {
        RelativeLayout loadingButtonLayout = findViewById(R.id.loadingButtonLayout);
        ProgressBar loading_progress = findViewById(R.id.loading_progress);
        Transition transition = new Fade();
        transition.setDuration(600);
        TransitionManager.beginDelayedTransition(loadingButtonLayout, transition);
        loading_progress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        mButtonPerformLocalTransaction.setVisibility(isLoading ? View.INVISIBLE : View.VISIBLE);
    }

}
