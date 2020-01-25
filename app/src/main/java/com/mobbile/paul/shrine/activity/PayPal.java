
package com.mobbile.paul.shrine.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.mobbile.paul.codelab.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import java.math.BigDecimal;


public class PayPal extends AppCompatActivity {

    private PayPalConfiguration payPalConfiguration;
    private Double amountCharged = 0.0;
    private Button paymentButton;
    private Integer requestCode  = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal);

        setUpPayPal();
        initView();
        viewListeners();

    }

    private void initView() {
        paymentButton = findViewById(R.id.payment);
    }

    private void viewListeners() {
        paymentButton.setOnClickListener(v -> processPayment());
    }

    private void setUpPayPal() {
        String clientID = "ASDxc9rZ5c_ZjO-xjJUntoYdTIGa0Y86wr-3pf4ewaTjnCxLGmTThWMKsfK0NYIXODcbiLvcMMN0UOOQ"; //
        payPalConfiguration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(clientID);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,payPalConfiguration);
        startService(intent);
    }

    private void processPayment() {
        amountCharged = 20.00; //PLACE HOLDER
        String currency = "USD"; //PLACE HOLDER
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(amountCharged), currency, "Ace Digital", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,payPalConfiguration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent,requestCode);
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
