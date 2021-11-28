package com.acpay.acapytrade;

import static com.acpay.acapytrade.MainActivity.getAPIHEADER;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.acpay.acapytrade.Navigations.Order.OrderDeleted;
import com.acpay.acapytrade.OrderOperations.Order;
import com.acpay.acapytrade.OrderOperations.OrderAdapter;
import com.acpay.acapytrade.OrderOperations.OrderUtilies;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

public class DeletedActivity extends AppCompatActivity {
    private String ApiUrl = "";

    OrderAdapter adapter;
    TextView State;
    ProgressBar Loding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_activity);
        ApiUrl = getAPIHEADER(DeletedActivity.this) + "/orders.php?type=del";

        getSupportFragmentManager().beginTransaction().replace(R.id.pending_container, new OrderDeleted()).commit();
        RequestQueue queue = Volley.newRequestQueue(this);
        adapter = new OrderAdapter(this,new ArrayList<Order>());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<Order> orders = OrderUtilies.extractFeuterFromJason(response);
                        adapter.clear();
                        adapter.addAll(orders);


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

}
