package com.acpay.acapytrade;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.Loader;

import com.acpay.acapytrade.Navigations.Order.OrderPended;
import com.acpay.acapytrade.OrderOperations.Order;
import com.acpay.acapytrade.OrderOperations.OrderAdapter;
import com.acpay.acapytrade.OrderOperations.OrderLoader;

import java.util.List;

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
