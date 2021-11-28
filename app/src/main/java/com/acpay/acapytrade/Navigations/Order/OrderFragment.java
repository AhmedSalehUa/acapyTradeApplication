package com.acpay.acapytrade.Navigations.Order;

import static com.acpay.acapytrade.MainActivity.getAPIHEADER;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.acpay.acapytrade.AddOrEditeOrderActivity;
import com.acpay.acapytrade.DeletedActivity;
import com.acpay.acapytrade.FinishedActivity;
import com.acpay.acapytrade.OrderOperations.OrderFoldingCellAdapter;
import com.acpay.acapytrade.OrderOperations.OrderUtilies;
import com.acpay.acapytrade.PendingRequests;
import com.acpay.acapytrade.R;
import com.acpay.acapytrade.OrderOperations.Order;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {
    String uid;
    private String ApiUrl = "";
    ListView theListView;
    OrderFoldingCellAdapter adapter;

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
ApiUrl=getAPIHEADER(getContext()) + "/orders.php?type=ok";

        final SwipeRefreshLayout pullToRefresh = rootView.findViewById(R.id.pullToRefresh);
        progressBar = rootView.findViewById(R.id.listProgressOrder);

        emptyList = rootView.findViewById(R.id.listEmptyOrder);
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
                    performApi();
                } else {
                    emptyList.setText("لا يوجد اتصال بالانترنت");
                }
                pullToRefresh.setRefreshing(false);
                Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
            }
        });
        FloatingActionButton fab = rootView.findViewById(R.id.fab_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddOrEditeOrderActivity.class);
                startActivity(intent);
            }
        });
        theListView = rootView.findViewById(R.id.orderMainList);
        theListView.setEmptyView(emptyList);
        theListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (theListView == null || theListView.getChildCount() == 0) ? 0 : theListView.getChildAt(0).getTop();
                pullToRefresh.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);

            }
        });

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
        adapter = new OrderFoldingCellAdapter(getContext(), new ArrayList<Order>());


        theListView.setAdapter(adapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                ((FoldingCell) view).toggle(false);

                adapter.registerToggle(pos);
                adapter.notifyDataSetChanged();
            }
        });

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Orders");
        setHasOptionsMenu(true);
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
            for (int i = 0; i < adapter.getCount(); i++) {
                final int postion = i;
                adapter.getItem(i).setTogBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapter.registerToggle(postion);
                        adapter.notifyDataSetChanged();
                    }
                });
                adapter.getItem(i).setAddNotesBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        alertDialog.setTitle("اضافة للملاحظات");
                        LinearLayout container = new LinearLayout(getContext());
                        container.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(20, 20, 20, 20);
                        final EditText input = new EditText(getContext());
                        input.setLayoutParams(lp);
                        input.setGravity(android.view.Gravity.TOP | android.view.Gravity.LEFT);
                        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                        input.setLines(1);
                        input.setMaxLines(1);
                        input.setText(adapter.getItem(postion).getNotes());
                        container.addView(input, lp);

                        alertDialog.setView(container);
                        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String api = getAPIHEADER(getContext()) + "/updatenotes.php?order=" + adapter.getItem(postion).getOrderNum() + "&note=" + input.getText().toString();

                                RequestQueue queue = Volley.newRequestQueue(getContext());
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                if (response.equals("1")) {
                                                    Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                                                    onBackPressed();
                                                } else {
                                                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                                }
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
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(getContext(), "canceled", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                    }
                });
                adapter.getItem(i).setEditBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), AddOrEditeOrderActivity.class);
                        intent.putExtra("id", adapter.getItem(postion).getOrderNum());
                        startActivity(intent);
                    }
                });
                adapter.getItem(i).setEnableBtn(View.GONE);
                adapter.getItem(i).setPendingBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("تم مراجعة الاوردر انت على وشك تعليقة")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                        String api = getAPIHEADER(getContext()) + "/pendingOrder.php?order=" + adapter.getItem(postion).getOrderNum();

                                        RequestQueue queue = Volley.newRequestQueue(getContext());
                                        StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        if (response.equals("1")) {
                                                            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                                                            onBackPressed();
                                                        } else {
                                                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                                        }
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
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                        dialog.cancel();
                                    }
                                });
                        final AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
                adapter.getItem(i).setDeleteBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("تم مراجعة الاوردر انت على وشك حذفة")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                        String api = getAPIHEADER(getContext()) + "/deleteOrder.php?order=" + adapter.getItem(postion).getOrderNum();

                                        RequestQueue queue = Volley.newRequestQueue(getContext());
                                        StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        if (response.equals("1")) {
                                                            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                                                            onBackPressed();
                                                        } else {
                                                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                                        }
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
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                        dialog.cancel();
                                    }
                                });
                        final AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
            }
            theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                    ((FoldingCell) view).toggle(false);

                    adapter.registerToggle(pos);
                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_catalog, menu);

    }

    private void onBackPressed() {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrderFragment()).commit();
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

            case R.id.setStaticIp:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("set Static Ip");
                LinearLayout container = new LinearLayout(getContext());
                container.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(20, 20, 20, 20);
                final EditText input = new EditText(getContext());
                input.setLayoutParams(lp);
                input.setGravity(Gravity.TOP | Gravity.LEFT);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setLines(1);
                input.setMaxLines(1);
                input.setText(getAPIHEADER(getContext()));
                container.addView(input, lp);
                alertDialog.setView(container);
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (input.getText().toString().length() > 1) {
                            SharedPreferences.Editor sharedPreferences = getContext().getSharedPreferences("MainActivity", 0).edit();
                            sharedPreferences.putString("api", input.getText().toString());
                            sharedPreferences.commit();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(getContext(), "canceled", Toast.LENGTH_SHORT).show();
                    }
                }).show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
