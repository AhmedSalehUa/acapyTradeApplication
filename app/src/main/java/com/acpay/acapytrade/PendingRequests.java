package com.acpay.acapytrade;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.Loader;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.acpay.acapytrade.floadingCell.FoldingCell;
import com.acpay.acapytrade.oreder.Order;
import com.acpay.acapytrade.oreder.OrderAdapter;
import com.acpay.acapytrade.oreder.OrderDone;
import com.acpay.acapytrade.oreder.OrderLoader;

import java.util.ArrayList;
import java.util.List;

public class PendingRequests extends AppCompatActivity implements LoaderCallbacks<List<Order>> {
    private String ApiUrl = " https://www.app.acapy-trade.com/orders.php?type=pen";

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
                loaderManager.restartLoader(ORDER_LOADER_ID, null, PendingRequests.this);
                loaderManager.initLoader(ORDER_LOADER_ID, null, PendingRequests.this);
                pullToRefresh.setRefreshing(false);
                Toast.makeText(PendingRequests.this, "Updated", Toast.LENGTH_SHORT).show();
            }
        });
        ListView theListView = findViewById(R.id.order_list_pending);

        State = (TextView) findViewById(R.id.no_orders);
        State.setText("Loading");
        Loding = (ProgressBar) findViewById(R.id.lood_progress_pending);
        theListView.setEmptyView(State);

        loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(ORDER_LOADER_ID, null, PendingRequests.this);

        adapter = new OrderAdapter(this, new ArrayList<Order>(),3);

        adapter.setDefaultPendingBtnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(PendingRequests.this, "1", Toast.LENGTH_SHORT).show();
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
                        final AlertDialog.Builder builder = new AlertDialog.Builder(PendingRequests.this);
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
                                                        Toast.makeText(PendingRequests.this, "Pended", Toast.LENGTH_SHORT).show();
                                                        loaderManager.restartLoader(ORDER_LOADER_ID, null, PendingRequests.this);
                                                        loaderManager.initLoader(ORDER_LOADER_ID, null, PendingRequests.this);
                                                    } else {
                                                        Toast.makeText(PendingRequests.this, "Failed", Toast.LENGTH_SHORT).show();
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
                adapter.getItem(i).setDeleteBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(PendingRequests.this);
                        builder.setMessage("تم مراجعة الاوردر انت على وشك اتمامة")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                                        final OrderDone orderDone = new OrderDone(adapter.getItem(postion), "done");
                                        final Handler handler = new Handler();
                                        Runnable runnableCode = new Runnable() {
                                            @Override
                                            public void run() {
                                                if (orderDone.isFinish()) {
                                                    if (orderDone.getResponse().equals("1")) {
                                                        Toast.makeText(PendingRequests.this, "Saved", Toast.LENGTH_SHORT).show();
                                                        loaderManager.restartLoader(ORDER_LOADER_ID, null, PendingRequests.this);
                                                        loaderManager.initLoader(ORDER_LOADER_ID, null, PendingRequests.this);
                                                    } else {
                                                        Toast.makeText(PendingRequests.this, "Failed", Toast.LENGTH_SHORT).show();
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
                adapter.getItem(i).setAddNotesBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PendingRequests.this);
                        alertDialog.setTitle("اضافة للملاحظات");
                        LinearLayout container = new LinearLayout(PendingRequests.this);
                        container.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(20, 20, 20, 20);
                        final EditText input = new EditText(PendingRequests.this);
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
                                                Toast.makeText(PendingRequests.this, "Updated", Toast.LENGTH_SHORT).show();
                                                loaderManager.restartLoader(ORDER_LOADER_ID, null, PendingRequests.this);
                                                loaderManager.initLoader(ORDER_LOADER_ID, null, PendingRequests.this);
                                            } else {
                                                Toast.makeText(PendingRequests.this, "Failed", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(PendingRequests.this, "canceled", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                    }
                });
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Order>> loader) {
        adapter.clear();
    }
}
