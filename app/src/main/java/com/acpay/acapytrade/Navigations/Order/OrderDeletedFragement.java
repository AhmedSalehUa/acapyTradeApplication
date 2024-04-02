package com.acpay.acapytrade.Navigations.Order;

import static com.acpay.acapytrade.MainActivity.getAPIHEADER;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.acpay.acapytrade.AddOrEditeOrderActivity;
import com.acpay.acapytrade.OrderOperations.Order;
import com.acpay.acapytrade.OrderOperations.progress.boxes;
import com.acpay.acapytrade.OrderOperations.progress.boxesAdapter;
import com.acpay.acapytrade.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderDeletedFragement extends Fragment {
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

    public OrderDeletedFragement(Order order) {
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
        String boxesapi =   getAPIHEADER(getContext()) + "/progress.php?order=" + order.getOrderNum();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, boxesapi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        orderString =response;
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
        AddNotes.setOnClickListener(new View.OnClickListener() {
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
                input.setGravity(Gravity.TOP | Gravity.LEFT);
                input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                input.setLines(1);
                input.setMaxLines(1);
                input.setText(order.getNotes());
                container.addView(input, lp);

                alertDialog.setView(container);
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String api = getAPIHEADER(getContext())+"/updatenotes.php?order=" + order.getOrderNum() + "&note=" + input.getText().toString();

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

        ActivieOrder = (TextView) rootview.findViewById(R.id.content_active_btn);
        ActivieOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("تم مراجعة الاوردر انت على وشك اعادة تفعيلة")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                String api = getAPIHEADER(getContext())+"/enableOrder.php?order=" + order.getOrderNum();

                                RequestQueue queue = Volley.newRequestQueue(getContext());
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                if (response.equals("1")) {
                                                    Toast.makeText(getContext(), "Active", Toast.LENGTH_SHORT).show();
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

        EditeOrder = (TextView) rootview.findViewById(R.id.content_edite_btn);
        EditeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddOrEditeOrderActivity.class);
                intent.putExtra("id", order.getOrderNum());
                startActivity(intent);
            }
        });

        PendingOrder = (TextView) rootview.findViewById(R.id.content_request_btn);
        PendingOrder.setVisibility(View.GONE);

        DoneOrder = (TextView) rootview.findViewById(R.id.content_done_btn);
        DoneOrder.setVisibility(View.GONE);

    }

    private void onBackPressed() {
        getFragmentManager().beginTransaction().replace(R.id.pending_container, new OrderDeleted()).commit();
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
