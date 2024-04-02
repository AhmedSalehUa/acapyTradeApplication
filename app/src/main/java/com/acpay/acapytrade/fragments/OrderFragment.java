package com.acpay.acapytrade.fragments;

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

import com.acpay.acapytrade.AddOrderActivity;
import com.acpay.acapytrade.PendingRequests;
import com.acpay.acapytrade.R;
import com.acpay.acapytrade.oreder.Order;
import com.acpay.acapytrade.oreder.OrderAdapter;
import com.acpay.acapytrade.oreder.OrderDone;
import com.acpay.acapytrade.oreder.OrderLoader;
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
                Intent intent = new Intent(getContext(), AddOrderActivity.class);
                startActivity(intent);
            }
        });
        ListView theListView = rootView.findViewById(R.id.order_list);

        State = (TextView) rootView.findViewById(R.id.no_books);
        State.setText("Loading");
        Loding = (ProgressBar) rootView.findViewById(R.id.lood_progress);
        theListView.setEmptyView(State);

        loaderManager = getLoaderManager();
        loaderManager.initLoader(ORDER_LOADER_ID, null, OrderFragment.this);

        adapter = new OrderAdapter(getContext(), new ArrayList<Order>(), 1);

        adapter.setDefaultPendingBtnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "1", Toast.LENGTH_SHORT).show();
            }
        });

        theListView.setAdapter(adapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                ((FoldingCell) view).toggle(false);
                adapter.registerToggle(pos);

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

            for (int i = 0; i < adapter.getCount(); i++) {
                final int postion = i;
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
                                final OrderDone orderDone = new OrderDone(adapter.getItem(postion), "updateNotes",input.getText().toString());
                                final Handler handler = new Handler();
                                Runnable runnableCode = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (orderDone.isFinish()) {
                                            if (orderDone.getResponse().equals("1")) {
                                                Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                                                loaderManager.restartLoader(ORDER_LOADER_ID, null, OrderFragment.this);
                                                loaderManager.initLoader(ORDER_LOADER_ID, null, OrderFragment.this);
                                            } else {
                                                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            handler.postDelayed(this, 100);
                                        }
                                    }
                                };
                                handler.post(runnableCode);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(getContext(), "canceled", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
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

                                        final OrderDone orderDone = new OrderDone(adapter.getItem(postion), "delete");
                                        final Handler handler = new Handler();
                                        Runnable runnableCode = new Runnable() {
                                            @Override
                                            public void run() {
                                                if (orderDone.isFinish()) {
                                                    if (orderDone.getResponse().equals("1")) {
                                                        Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                                                        loaderManager.restartLoader(ORDER_LOADER_ID, null, OrderFragment.this);
                                                        loaderManager.initLoader(ORDER_LOADER_ID, null, OrderFragment.this);
                                                    } else {
                                                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
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
                });
                adapter.getItem(i).setPendingBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("تم مراجعة الاوردر انت على وشك تعليقة")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                                        final OrderDone orderDone = new OrderDone(adapter.getItem(postion), "pending");
                                        final Handler handler = new Handler();
                                        Runnable runnableCode = new Runnable() {
                                            @Override
                                            public void run() {
                                                if (orderDone.isFinish()) {
                                                    if (orderDone.getResponse().equals("1")) {
                                                        Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                                                        loaderManager.restartLoader(ORDER_LOADER_ID, null, OrderFragment.this);
                                                        loaderManager.initLoader(ORDER_LOADER_ID, null, OrderFragment.this);
                                                    } else {
                                                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
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
                });
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        adapter.clear();
    }
}
