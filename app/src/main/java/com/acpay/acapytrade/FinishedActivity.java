package com.acpay.acapytrade;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.acpay.acapytrade.floadingCell.FoldingCell;
import com.acpay.acapytrade.oreder.Order;
import com.acpay.acapytrade.oreder.OrderAdapter;
import com.acpay.acapytrade.oreder.OrderLoader;

import java.util.ArrayList;
import java.util.List;

public class FinishedActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Order>> {
    private String ApiUrl = " https://www.app.acapy-trade.com/orders.php?type=no";

    OrderAdapter adapter;
    TextView State;
    ProgressBar Loding;
    LoaderManager loaderManager;
    private static final int ORDER_LOADER_ID = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_activity);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.RequestpullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loaderManager.restartLoader(ORDER_LOADER_ID, null, FinishedActivity.this);
                loaderManager.initLoader(ORDER_LOADER_ID, null, FinishedActivity.this);
                pullToRefresh.setRefreshing(false);
                Toast.makeText(FinishedActivity.this, "Updated", Toast.LENGTH_SHORT).show();
            }
        });
        ListView theListView = findViewById(R.id.order_list_pending);

        State = (TextView) findViewById(R.id.no_orders);
        State.setText("Loading");
        Loding = (ProgressBar) findViewById(R.id.lood_progress_pending);
        theListView.setEmptyView(State);

        loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(ORDER_LOADER_ID, null, FinishedActivity.this);

        adapter = new OrderAdapter(this, new ArrayList<Order>(),2);


        theListView.setAdapter(adapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                ((FoldingCell) view).toggle(false);
                adapter.registerToggle(pos);

            }
        });
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
