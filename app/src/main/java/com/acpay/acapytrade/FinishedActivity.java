package com.acpay.acapytrade;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

<<<<<<< HEAD
import com.acpay.acapytrade.FloadingCell.FoldingCell;
import com.acpay.acapytrade.Navigations.OrderDeleted;
import com.acpay.acapytrade.Navigations.OrderFinished;
import com.acpay.acapytrade.Order.Order;
import com.acpay.acapytrade.Order.OrderAdapter;
import com.acpay.acapytrade.Order.OrderLoader;
=======
import com.acpay.acapytrade.Navigations.Order.OrderFinished;
>>>>>>> 4d8adbf7f1e4ccbed605560ec2c90d1b8bdba1f1

public class FinishedActivity extends AppCompatActivity   {

<<<<<<< HEAD
public class FinishedActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Order>> {
    private String ApiUrl = " https://www.app.acapy-trade.com/orders.php?type=no";

    OrderAdapter adapter;
    TextView State;
    ProgressBar Loding;

=======
>>>>>>> 4d8adbf7f1e4ccbed605560ec2c90d1b8bdba1f1

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_activity);
        getSupportFragmentManager().beginTransaction().replace(R.id.pending_container, new OrderFinished()).commit();
    }


}
