package com.mobbile.paul.shrine.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mobbile.paul.codelab.R;

import java.text.DecimalFormat;

import static com.mobbile.paul.shrine.activity.SuccessSubmitActivity.ARGS_ORDER_AMOUNT;

public class ConfirmPayment extends AppCompatActivity {

    private Double amountCharged = 0.0;
    private Double rate = 0.0;
    private Double amount = 0.0;
    private TextView product_description;
    private ImageButton BackButton;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_payment);
        product_description = findViewById(R.id.product_description);
        BackButton = findViewById(R.id.backButton);

        rate = 360.0;//

        DecimalFormat f = new DecimalFormat("##.00");
        amountCharged = (double) getIntent().getIntExtra(ARGS_ORDER_AMOUNT, 0);
        amount = amountCharged / rate;
        String des = "$ "+f.format(amount);

        product_description.setText("The sum of"+ des +" is Sucessfully deducted from your Paypal account");

        BackButton.setOnClickListener(v ->
                onBackPressed()
        );

    }
}
