package com.acpay.acapytrade.Navigations.Order;

import static com.acpay.acapytrade.MainActivity.getAPIHEADER;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
import com.acpay.acapytrade.OrderOperations.OrderUtilies;
import com.acpay.acapytrade.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class OrderPended extends Fragment {
    String uid;
    private String ApiUrl = "";
    OrderAdapter adapter;
    TextView State;
    ProgressBar Loding;
    ProgressBar progressBar;
    TextView emptyList;
    public OrderPended() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.orders_activity, container, false);
        ApiUrl = getAPIHEADER(getContext()) +"/orders.php?type=pen";

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
                if (isConnected) {performApi();
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
           performApi();
        } else {
            emptyList.setText("لا يوجد اتصال بالانترنت");
        }

        adapter = new OrderAdapter(getContext(), new ArrayList<Order>());


        theListView.setAdapter(adapter);
        theListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                int topRowVerticalPosition = (theListView == null || theListView.getChildCount() == 0) ? 0 : theListView.getChildAt(0).getTop();
                pullToRefresh.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);

            }
        });
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Order order = adapter.getItem(pos);
                getFragmentManager().beginTransaction().replace(R.id.pending_container, new OrderPendingFragement(order)).commit();
            }
        });

        return rootView;
    }
    private void performApi() {

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("onResponse", response);
                        List<Order> orders = OrderUtilies.extractFeuterFromJason(response);
                        setupAdpater(orders);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("onResponse", error.toString());
            }
        });
        stringRequest.setShouldCache(false);
        stringRequest.setShouldRetryConnectionErrors(true);
        stringRequest.setShouldRetryServerErrors(true);
        queue.add(stringRequest);
    }

    public void setupAdpater(List<Order> data) {
        progressBar.setVisibility(View.GONE);
        emptyList.setText("لا يوجد اوردرات");
        adapter.clear();
         if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }
    }

}
