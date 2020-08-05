package com.acpay.acapytrade;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
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
import com.acpay.acapytrade.fragments.OrderFragment;
import com.acpay.acapytrade.oreder.Order;
import com.acpay.acapytrade.oreder.OrderAdapter;
import com.acpay.acapytrade.oreder.OrderDone;
import com.acpay.acapytrade.oreder.OrderLoader;

import java.util.ArrayList;
import java.util.List;

public class DeletedActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Order>> {
    private String ApiUrl = " https://www.app.acapy-trade.com/orders.php?type=del";

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
                loaderManager.restartLoader(ORDER_LOADER_ID, null, DeletedActivity.this);
                loaderManager.initLoader(ORDER_LOADER_ID, null, DeletedActivity.this);
                pullToRefresh.setRefreshing(false);
                Toast.makeText(DeletedActivity.this, "Updated", Toast.LENGTH_SHORT).show();
            }
        });
        ListView theListView = findViewById(R.id.order_list_pending);

        State = (TextView) findViewById(R.id.no_orders);
        State.setText("Loading");
        Loding = (ProgressBar) findViewById(R.id.lood_progress_pending);
        theListView.setEmptyView(State);

        loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(ORDER_LOADER_ID, null, DeletedActivity.this);

        adapter = new OrderAdapter(this, new ArrayList<Order>(),4);


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
            for (int i = 0; i < adapter.getCount(); i++) {
                final int postion = i;
                adapter.getItem(i).setEnableBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(DeletedActivity.this);
                        builder.setMessage("تم مراجعة الاوردر انت على وشك اعادة تفعيلة")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                                        final OrderDone orderDone = new OrderDone(adapter.getItem(postion), "enable");
                                        final Handler handler = new Handler();
                                        Runnable runnableCode = new Runnable() {
                                            @Override
                                            public void run() {
                                                if (orderDone.isFinish()) {
                                                    if (orderDone.getResponse().equals("1")) {
                                                        Toast.makeText(DeletedActivity.this, "Pended", Toast.LENGTH_SHORT).show();
                                                        loaderManager.restartLoader(ORDER_LOADER_ID, null, DeletedActivity.this);
                                                        loaderManager.initLoader(ORDER_LOADER_ID, null, DeletedActivity.this);
                                                    } else {
                                                        Toast.makeText(DeletedActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    handler.postDelayed(this, 100);
                                                }
                                            }
                                        };
                                        handler.post(runnableCode);

                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                        dialog.cancel();
                                    }
                                });
                        final AlertDialog alert = builder.create();
                        alert.show();
                    }
                });}
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Order>> loader) {
        adapter.clear();
    }
}
