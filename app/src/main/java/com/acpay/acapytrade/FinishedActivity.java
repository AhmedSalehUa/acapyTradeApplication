package com.acpay.acapytrade;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.acpay.acapytrade.Navigations.Order.OrderFinished;

public class FinishedActivity extends AppCompatActivity   {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_activity);
        getSupportFragmentManager().beginTransaction().replace(R.id.pending_container, new OrderFinished()).commit();
    }


}
