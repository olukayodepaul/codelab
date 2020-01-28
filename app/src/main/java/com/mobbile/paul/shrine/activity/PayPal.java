package com.mobbile.paul.shrine.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.mobbile.paul.codelab.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import static com.mobbile.paul.shrine.activity.SuccessSubmitActivity.ARGS_ORDER_AMOUNT;


public class PayPal extends AppCompatActivity {

    private PayPalConfiguration payPalConfiguration;
    private Double amountCharged = 0.0;
    private Double rate = 0.0;
    private Double amount = 0.0;
    private Button paymentButton;
    private Integer requestCode  = 101;
    private TextView edit_card_number;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal);
        paymentButton = findViewById(R.id.payment);
        edit_card_number = findViewById(R.id.edit_card_number);

        DecimalFormat f = new DecimalFormat("##.00");

        rate = 360.0;//coming from fire base
        amountCharged = (double) getIntent().getIntExtra(ARGS_ORDER_AMOUNT, 0);

        amount = amountCharged / rate;
        edit_card_number.setText("$"+ f.format(amount));

        setUpPayPal();

        paymentButton.setOnClickListener(v ->
            stitchToPayPal()
        );
    }

    private void setUpPayPal() {

        String clientID = "ASDxc9rZ5c_ZjO-xjJUntoYdTIGa0Y86wr-3pf4ewaTjnCxLGmTThWMKsfK0NYIXODcbiLvcMMN0UOOQ"; //
        payPalConfiguration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(clientID);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        startService(intent);
    }

    private void stitchToPayPal() {

        String currency = "USD"; //PLACE HOLDER
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(amount), currency, "Ace Digital", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent inten = new Intent(this, PaymentActivity.class);
        inten.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,payPalConfiguration);
        inten.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(inten,requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101)
        {
            if(resultCode== Activity.RESULT_OK) {
                //this could be fragment of activity
                Intent intent = new Intent(this, ConfirmPayment.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}