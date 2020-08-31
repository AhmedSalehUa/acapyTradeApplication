package com.acpay.acapytrade;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.acpay.acapytrade.Navigations.Order.OrderPended;
import com.acpay.acapytrade.OrderOperations.OrderAdapter;

public class PendingRequests extends AppCompatActivity {
    private String ApiUrl = " https://www.app.acapy-trade.com/orders.php?type=pen";

    OrderAdapter adapter;
    TextView State;
    ProgressBar Loding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_activity);
        getSupportFragmentManager().beginTransaction().replace(R.id.pending_container, new OrderPended()).commit();
    }


}
