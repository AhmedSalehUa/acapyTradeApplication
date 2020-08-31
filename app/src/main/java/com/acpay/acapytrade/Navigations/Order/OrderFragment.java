package com.acpay.acapytrade.Navigations.Order;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.acpay.acapytrade.AddOrEditeOrderActivity;
import com.acpay.acapytrade.DeletedActivity;
import com.acpay.acapytrade.FinishedActivity;
import com.acpay.acapytrade.OrderOperations.OrderAdapter;
import com.acpay.acapytrade.PendingRequests;
import com.acpay.acapytrade.R;
import com.acpay.acapytrade.OrderOperations.Order;
import com.acpay.acapytrade.OrderOperations.OrderLoader;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Order>> {
    String uid;
    private String ApiUrl = " https://www.app.acapy-trade.com/orders.php?type=ok";

    OrderAdapter adapter;
    LoaderManager loaderManager;
    private static final int ORDER_LOADER_ID = 1;

    ProgressBar progressBar;
    TextView emptyList;

    public OrderFragment() {
        super();
    }

    public OrderFragment(String method) {
        super();
        switch (method) {
            case "pended":
                Intent intent = new Intent(getContext(), PendingRequests.class);
                startActivity(intent);
                break;
            case "finished":
                Intent intent3 = new Intent(getContext(), FinishedActivity.class);
                startActivity(intent3);
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.orders_activity, container, false);
        final SwipeRefreshLayout pullToRefresh = rootView.findViewById(R.id.pullToRefresh);
        progressBar = (ProgressBar) rootView.findViewById(R.id.listProgressOrder);

        emptyList = (TextView) rootView.findViewById(R.id.listEmptyOrder);
        emptyList.setText("جارى التحميل");
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressBar.setVisibility(View.VISIBLE);

                ConnectivityManager cm =
                        (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected) {
                    loaderManager = getLoaderManager();
                    loaderManager.initLoader(ORDER_LOADER_ID, null, OrderFragment.this);
                } else {
                    emptyList.setText("لا يوجد اتصال بالانترنت");
                }
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
        theListView.setEmptyView(emptyList);


        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            loaderManager = getLoaderManager();
            loaderManager.initLoader(ORDER_LOADER_ID, null, OrderFragment.this);
        } else {
            emptyList.setText("لا يوجد اتصال بالانترنت");
        }
        adapter = new OrderAdapter(getContext(), new ArrayList<Order>());


        theListView.setAdapter(adapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Order order = adapter.getItem(pos);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrderDetailsFragement(order)).commit();

            }
        });
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Orders");
        setHasOptionsMenu(true);
        return rootView;
    }


    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        return new OrderLoader(getContext(), ApiUrl);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Order>> loader, List<Order> data) {
        progressBar.setVisibility(View.GONE);
        emptyList.setText("لا يوجد اوردرات");
        adapter.clear();

        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        adapter.clear();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_catalog, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.pending_request:
                Intent intent = new Intent(getContext(), PendingRequests.class);
                startActivity(intent);
                return true;
            case R.id.deleted_order:

                Intent intent2 = new Intent(getContext(), DeletedActivity.class);
                startActivity(intent2);
                return true;
            case R.id.done_orders:
                Intent intent3 = new Intent(getContext(), FinishedActivity.class);
                startActivity(intent3);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
