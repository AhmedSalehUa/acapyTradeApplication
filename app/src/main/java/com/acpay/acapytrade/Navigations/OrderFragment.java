package com.acpay.acapytrade.Navigations;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.acpay.acapytrade.AddOrEditeOrderActivity;
import com.acpay.acapytrade.Order.OrderAdapter;
import com.acpay.acapytrade.R;
import com.acpay.acapytrade.FloadingCell.FoldingCell;
import com.acpay.acapytrade.Order.Order;
import com.acpay.acapytrade.Order.OrderDone;
import com.acpay.acapytrade.Order.OrderLoader;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Order>> {
    String uid;
    private String ApiUrl = " https://www.app.acapy-trade.com/orders.php?type=ok";

    OrderAdapter adapter;
    TextView State;
    ProgressBar Loding;
    LoaderManager loaderManager;
    private static final int ORDER_LOADER_ID = 1;

    public OrderFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.orders_activity, container, false);
        final SwipeRefreshLayout pullToRefresh = rootView.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loaderManager.restartLoader(ORDER_LOADER_ID, null, OrderFragment.this);
                loaderManager.initLoader(ORDER_LOADER_ID, null, OrderFragment.this);
                pullToRefresh.setRefreshing(false);
                Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddOrEditeOrderActivity.class);
                startActivity(intent);
            }
        });
        ListView theListView = rootView.findViewById(R.id.orderMainList);

        State = (TextView) rootView.findViewById(R.id.no_books);
        State.setText("Loading");
        Loding = (ProgressBar) rootView.findViewById(R.id.lood_progress);
        theListView.setEmptyView(State);

        loaderManager = getLoaderManager();
        loaderManager.initLoader(ORDER_LOADER_ID, null, OrderFragment.this);

        adapter = new OrderAdapter(getContext(), new ArrayList<Order>());


        theListView.setAdapter(adapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Order order = adapter.getItem(pos);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrderDetailsFragement(order)).commit();


            }
        });

        return rootView;
    }


    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        return new OrderLoader(getContext(), ApiUrl);
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
    public void onLoaderReset(@NonNull Loader loader) {
        adapter.clear();
    }
}
