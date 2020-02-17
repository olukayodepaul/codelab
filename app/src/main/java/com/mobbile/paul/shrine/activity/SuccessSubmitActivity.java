package com.mobbile.paul.shrine.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.dropin.utils.PaymentMethodType;
import com.braintreepayments.api.exceptions.BraintreeError;
import com.braintreepayments.api.exceptions.ErrorWithResponse;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.google.android.material.snackbar.Snackbar;
import com.mobbile.paul.codelab.R;
import com.mobbile.paul.shrine.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.Locale;

import cz.msebera.android.httpclient.Header;


public class SuccessSubmitActivity extends AppCompatActivity {

    String orderId;
    AsyncHttpClient client;

    int amount = 0;
    int amountInDollars = 0;
    public static String ARGS_ORDER_ID = "ARGS_ORDER_ID";
    public static String ARGS_ORDER_AMOUNT = "ARGS_ORDER_AMOUNT";
    public static String ARGS_ORDER_AMOUNT_IN_DOLLAR = "ARGS_ORDER_AMOUNT_IN_DOLLAR";

    public static int REQUEST_CODE = 1101;
    private static String clientToken = "sandbox_d5t3mnpx_qmr7j4yc9rvscxh2";

    boolean isPaymentRequired = true;
    boolean isDollarPaymentRequired = true;
    BraintreeFragment mBraintreeFragment;
    TextView description;
    Snackbar progressSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_submit);


        orderId = getIntent().getStringExtra(ARGS_ORDER_ID);
        amount = getIntent().getIntExtra(ARGS_ORDER_AMOUNT, 0);
        amountInDollars = getIntent().getIntExtra(ARGS_ORDER_AMOUNT_IN_DOLLAR, 0);

        Button completeButton = findViewById(R.id.completeButton);
        Snackbar progressSnackbar = Snackbar.make(completeButton, "Please Wait...", Snackbar.LENGTH_INDEFINITE);

        ViewGroup contentLay = (ViewGroup) progressSnackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text).getParent();
        ProgressBar item = new ProgressBar(this);
        contentLay.addView(item, 0);

        description = findViewById(R.id.product_description);

        if (amount == 0) {
            isPaymentRequired = false;
        }
        if (amountInDollars == 0) {
            isDollarPaymentRequired = false;
        }
//        description.setText(getString(R.string.success_no_payment_string));
        if (!isPaymentRequired && isDollarPaymentRequired) {
            description.setText(String.format(Locale.getDefault(), "%s Amount: $%d", getString(R.string.finalize_payment), amountInDollars));
            findViewById(R.id.paymentButton).setVisibility(View.GONE);
        } else if (!isDollarPaymentRequired && isPaymentRequired) {
            description.setText(String.format(Locale.getDefault(), "%s Amount: NGN%d", getString(R.string.finalize_payment), amount));
            //findViewById(R.id.paypalPaymentButton).setVisibility(View.GONE);
        } else {
            description.setText(String.format(Locale.getDefault(), "%s Amount $%dAmount , NGN%d", getString(R.string.finalize_payment), amountInDollars, amount));
        }

        if (!isDollarPaymentRequired && !isPaymentRequired) {
            description.setText(getString(R.string.success_no_payment_string));
            findViewById(R.id.paymentButton).setVisibility(View.GONE);
            findViewById(R.id.paypalPaymentButton).setVisibility(View.GONE);
        }


        findViewById(R.id.paymentButton).setOnClickListener(v -> {
            Intent intent = new Intent(SuccessSubmitActivity.this, PaymentActivity.class);
            intent.putExtra(ARGS_ORDER_ID, orderId);
            intent.putExtra(ARGS_ORDER_AMOUNT, amount);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.paypalPaymentButton).setOnClickListener(v -> {
            Intent intent = new Intent(SuccessSubmitActivity.this, PayPal.class);
            intent.putExtra(ARGS_ORDER_ID, orderId);
            intent.putExtra(ARGS_ORDER_AMOUNT, amount);
            startActivity(intent);
            finish();
        });

        completeButton.setOnClickListener(v -> {
            Intent intent = new Intent(SuccessSubmitActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

    }


    public void onBraintreeSubmit(View v) {
        DropInRequest dropInRequest = new DropInRequest().clientToken(clientToken)
                .disableCard();
        startActivityForResult(dropInRequest.getIntent(SuccessSubmitActivity.this), REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                // use the result to update your UI and send the payment method nonce to your server
                PaymentMethodNonce paymentMethodNonce = result.getPaymentMethodNonce();

                PaymentMethodType paymentMethodType = result.getPaymentMethodType();

                String nonce = (paymentMethodNonce != null ? paymentMethodNonce.getNonce() : null);

                postNonceToServer(nonce);

            } else if (resultCode == RESULT_CANCELED) {
                // the user canceled
            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                if (error instanceof ErrorWithResponse) {

                    ErrorWithResponse errorWithResponse = (ErrorWithResponse) error;

                    BraintreeError cardErrors = errorWithResponse.errorFor("creditCard");

                    if (cardErrors != null) {

                        // There is an issue with the credit card.

                        BraintreeError expirationMonthError = cardErrors.errorFor("expirationMonth");

                        if (expirationMonthError != null) {

                            // There is an issue with the expiration month.

                        }

                    }

                }
            }
        }
    }

    private void postNonceToServer(String paymentNonce) {
        progressSnackbar.show();
        String serverUrl = "https://us-central1-acedigital-499f6.cloudfunctions.net/createBraintreeTransaction";
        RequestParams params = new RequestParams();
        params.put("payment_method_nonce", paymentNonce);
        params.put("amountToPay", amountInDollars);

        AsyncHttpClient androidClient = new AsyncHttpClient();
        androidClient.post(serverUrl, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressSnackbar.dismiss();
                Log.e("NONCE_PAYPAL_ERROR", "Error: Failed to create a transaction");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                progressSnackbar.dismiss();
                Log.d("NONCE_PAYPAL_SUCCESS", "Output " + responseString);
            }
        });
    }


}
