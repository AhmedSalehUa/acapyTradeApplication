package com.acpay.acapytrade;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.acpay.acapytrade.Navigations.Order.OrderDeleted;
import com.acpay.acapytrade.OrderOperations.Order;
import com.acpay.acapytrade.OrderOperations.OrderAdapter;
import com.acpay.acapytrade.OrderOperations.OrderLoader;

import java.util.List;

public class DeletedActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Order>> {
    private String ApiUrl = " https://www.app.acapy-trade.com/orders.php?type=del";

    OrderAdapter adapter;
    TextView State;
    ProgressBar Loding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_activity);
        getSupportFragmentManager().beginTransaction().replace(R.id.pending_container, new OrderDeleted()).commit();
    }

    @NonNull
    @Override
    public Loader<List<Order>> onCreateLoader(int id, @Nullable Bundle args) {
        return new OrderLoader(this, ApiUrl);
    }

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
}
