package com.acpay.acapytrade;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

<<<<<<< HEAD
import com.acpay.acapytrade.FloadingCell.FoldingCell;
import com.acpay.acapytrade.Navigations.OrderDeleted;
import com.acpay.acapytrade.Navigations.OrderPended;
import com.acpay.acapytrade.Order.Order;
import com.acpay.acapytrade.Order.OrderAdapter;
import com.acpay.acapytrade.Order.OrderDone;
import com.acpay.acapytrade.Order.OrderLoader;
=======
import com.acpay.acapytrade.Navigations.Order.OrderPended;
import com.acpay.acapytrade.OrderOperations.OrderAdapter;
>>>>>>> 4d8adbf7f1e4ccbed605560ec2c90d1b8bdba1f1

public class PendingRequests extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_activity);
        getSupportFragmentManager().beginTransaction().replace(R.id.pending_container, new OrderPended()).commit();
    }


<<<<<<< HEAD
    @Override
    public void onLoadFinished(@NonNull Loader<List<Order>> loader, List<Order> data) {

        adapter.clear();
        Loding.setVisibility(View.GONE);
        State.setText("No Orders");
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Order>> loader) {
        adapter.clear();
    }
=======
>>>>>>> 4d8adbf7f1e4ccbed605560ec2c90d1b8bdba1f1
}
