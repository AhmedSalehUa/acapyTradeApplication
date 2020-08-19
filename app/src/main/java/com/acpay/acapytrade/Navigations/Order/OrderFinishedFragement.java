package com.acpay.acapytrade.Navigations.Order;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.acpay.acapytrade.OrderOperations.Order;
import com.acpay.acapytrade.OrderOperations.OrderDone;
import com.acpay.acapytrade.OrderOperations.progress.boxes;
import com.acpay.acapytrade.OrderOperations.progress.boxesAdapter;
import com.acpay.acapytrade.OrderOperations.progress.progressReponser;
import com.acpay.acapytrade.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderFinishedFragement extends Fragment {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    TextView orderNum;
    TextView userName;
    TextView date;
    TextView time;
    TextView place;
    TextView location;
    TextView dliverCost;
    TextView fixType;
    TextView classMatter;
    TextView notes;

    TextView AddNotes;
    TextView ActivieOrder;
    TextView EditeOrder;
    TextView PendingOrder;
    TextView DoneOrder;
ImageView back;

    boxesAdapter boxsAdapter;
    ListView boxesList;
    ProgressBar boxesProgressBar;
    TextView boxesListEmpty;
    ArrayList<boxes> list;

    Order order;
    String orderString;

    public OrderFinishedFragement(Order order) {
        this.order = order;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.orders_activity_view_details, container, false);
        boxesList = rootView.findViewById(R.id.checkBoxesList);
        boxsAdapter = new boxesAdapter(getContext(), new ArrayList<boxes>());

        boxesProgressBar = (ProgressBar) rootView.findViewById(R.id.checkBoxesListProgress);
        boxesListEmpty = (TextView) rootView.findViewById(R.id.checkBoxesListNoItems);
        list = new ArrayList<>();
        setBoxListDetails();

        setDetails(rootView);
        AddOnClickListenerEvents(rootView);

        return rootView;
    }

    private void setBoxListDetails() {
        boxsAdapter.clear();
        boxesList.setEmptyView(boxesListEmpty);
        String boxesapi = "https://www.app.acapy-trade.com/progress.php?order=" + order.getOrderNum();

        final progressReponser boxesJason = new progressReponser();
        boxesJason.setFinish(false);
        boxesJason.execute(boxesapi);
        final Handler boxeshandler = new Handler();
        Runnable boxesrunnableCode = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                if (boxesJason.isFinish()) {

                    orderString = boxesJason.getUserId();
                    boxsAdapter.addAll(fetchBoxsJason(orderString));
                    boxesProgressBar.setVisibility(View.GONE);
                    boxesList.setAdapter(boxsAdapter);
                    ListAdapter myListAdapter = boxesList.getAdapter();
                    if (myListAdapter == null) {

                        return;
                    }

                    int totalHeight = 0;
                    for (int size = 0; size < myListAdapter.getCount(); size++) {
                        View listItem = myListAdapter.getView(size, null, boxesList);
                        listItem.measure(0, 0);
                        totalHeight += listItem.getMeasuredHeight() + 85;
                    }

                    ViewGroup.LayoutParams params = boxesList.getLayoutParams();
                    params.height = totalHeight + (boxesList.getDividerHeight() * (myListAdapter.getCount() - 1));
                    boxesList.setLayoutParams(params);

                    for (int i = 0; i < boxsAdapter.getCount(); i++) {
                        final int postion = i;
                        boxsAdapter.getItem(i).setCheckListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                boxes s = boxsAdapter.getItem(postion);
                                if (compoundButton.isChecked()) {
                                    list.add(s);
                                } else {
                                    list.remove(s);
                                }
                            }
                        });
                    }
                } else {
                    boxeshandler.postDelayed(this, 100);
                }
            }
        };
        boxeshandler.post(boxesrunnableCode);
    }

    private void setDetails(View rootview) {

        orderNum = rootview.findViewById(R.id.content_orderNum);
        orderNum.setText(order.getUserName());

        time = rootview.findViewById(R.id.content_time_label);
        time.setText(order.getTime());

        date = rootview.findViewById(R.id.content_date_label);
        date.setText(order.getDate());

        place = rootview.findViewById(R.id.content_place);
        place.setText(order.getPlace());

        location = rootview.findViewById(R.id.content_location);
        location.setText(order.getLocation());

        dliverCost = rootview.findViewById(R.id.content_dliverCost);
        dliverCost.setText(order.getDliverCost());

        classMatter = rootview.findViewById(R.id.content_matter);
        classMatter.setText(order.getClassMatter());

        fixType = rootview.findViewById(R.id.content_fixType);
        fixType.setText(order.getFixType());

        notes = rootview.findViewById(R.id.content_notes);
        notes.setText(order.getNotes());

    }

    private void AddOnClickListenerEvents(View rootview) {
        back = (ImageView)rootview.findViewById(R.id.wrap_list);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        AddNotes = (TextView) rootview.findViewById(R.id.content_note_btn);
        AddNotes.setVisibility(View.GONE);

        ActivieOrder = (TextView) rootview.findViewById(R.id.content_active_btn);
        ActivieOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("تم مراجعة الاوردر انت على وشك اعادة تفعيلة")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                                final OrderDone orderDone = new OrderDone(order, "enable");
                                final Handler handler = new Handler();
                                Runnable runnableCode = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (orderDone.isFinish()) {
                                            if (orderDone.getResponse().equals("1")) {
                                                Toast.makeText(getContext(), "Active", Toast.LENGTH_SHORT).show();
                                                onBackPressed();
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

        EditeOrder = (TextView) rootview.findViewById(R.id.content_edite_btn);
        EditeOrder.setVisibility(View.GONE);

        PendingOrder = (TextView) rootview.findViewById(R.id.content_request_btn);
        PendingOrder.setVisibility(View.GONE);

        DoneOrder = (TextView) rootview.findViewById(R.id.content_done_btn);
        DoneOrder.setVisibility(View.GONE);

    }

    private void onBackPressed() {
        getFragmentManager().beginTransaction().replace(R.id.pending_container, new OrderFinished()).commit();
    }

    private List<boxes> fetchBoxsJason(String boxesJasonResponse) {

        final List<boxes> boxes = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(boxesJasonResponse);
            JSONArray sa = jsonObject.names();
            Log.e("jsonObject", sa.length() + "");
            for (int i = 0; i < sa.length(); i++) {
                JSONObject jsonArrayId = jsonObject.getJSONObject(sa.get(i).toString());
                boxes items = new boxes(jsonArrayId.getString("progress_name"),
                        jsonArrayId.getString("statue"),
                        jsonArrayId.getString("notes"));
                boxsAdapter.add(items);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return boxes;
    }

}
