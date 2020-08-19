package com.acpay.acapytrade.Navigations.Order;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.acpay.acapytrade.OrderOperations.Order;
import com.acpay.acapytrade.OrderOperations.OrderAdapter;
import com.acpay.acapytrade.OrderOperations.OrderLoader;
import com.acpay.acapytrade.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class OrderDeleted extends Fragment implements LoaderManager.LoaderCallbacks<List<Order>> {

    private String ApiUrl = " https://www.app.acapy-trade.com/orders.php?type=del";

    OrderAdapter adapter;
    TextView State;
    ProgressBar Loding;
    LoaderManager loaderManager;
    private static final int ORDER_LOADER_ID = 1;
    ProgressBar progressBar;
    TextView emptyList;
    public OrderDeleted() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.orders_activity, container, false);
        final SwipeRefreshLayout pullToRefresh = rootView.findViewById(R.id.pullToRefresh);
        progressBar=(ProgressBar)rootView.findViewById(R.id.listProgressOrder);
        emptyList=(TextView)rootView.findViewById(R.id.listEmptyOrder);
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
                    loaderManager.initLoader(ORDER_LOADER_ID, null, OrderDeleted.this);
                } else {
                    emptyList.setText("لا يوجد اتصال بالانترنت");
                }
                pullToRefresh.setRefreshing(false);
                Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab_button);
        fab.setVisibility(View.GONE);
        ListView theListView = rootView.findViewById(R.id.orderMainList);
        theListView.setEmptyView(emptyList);


        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            loaderManager = getLoaderManager();
            loaderManager.initLoader(ORDER_LOADER_ID, null, OrderDeleted.this);
        } else {
            emptyList.setText("لا يوجد اتصال بالانترنت");
        }
        adapter = new OrderAdapter(getContext(), new ArrayList<Order>());


        theListView.setAdapter(adapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Order order = adapter.getItem(pos);
                getFragmentManager().beginTransaction().replace(R.id.pending_container, new OrderDeletedFragement(order)).commit();
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
}
