package com.acpay.acapytrade.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.acpay.acapytrade.AddOrderActivity;
import com.acpay.acapytrade.Networking.JasonReponser;
import com.acpay.acapytrade.R;
import com.acpay.acapytrade.costs.costs;
import com.acpay.acapytrade.costs.costsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CostFragment extends Fragment {
    public CostFragment() {
        super();
    }

    private int mYear, mMonth, mDay, mHour, mMinute;
    ListView listView;
    costsAdapter adapter;
    String costsJasonResponse;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_cost, container, false);

        listView = (ListView) rootview.findViewById(R.id.costList);
        adapter = new costsAdapter(getContext(), new ArrayList<costs>());
        listView.setAdapter(adapter);

        final EditText dateTo = (EditText) rootview.findViewById(R.id.date_to);

        final EditText dateFrom = (EditText) rootview.findViewById(R.id.date_from);

        final ImageView datePicker = (ImageView) rootview.findViewById(R.id.date_to_add);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dateTo.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        final ImageView timePicker = (ImageView) rootview.findViewById(R.id.date_from_add);
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dateFrom.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        TextView getList = (TextView) rootview.findViewById(R.id.content_save_btn);
        getList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String from = (dateFrom.getText().toString().length() == 0) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) : dateFrom.getText().toString();
                String to = (dateTo.getText().toString().length() == 0) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) :dateTo.getText().toString();
                String api = "https://www.app.acapy-trade.com/getexpendeCostDate.php?from="
                        + from
                        + "&to=" + to;
                Log.w("api", api);
                final JasonReponser costsJason = new JasonReponser();
                costsJason.setFinish(false);
                costsJason.execute(api);
                final Handler boxeshandler = new Handler();
                Runnable boxesrunnableCode = new Runnable() {
                    @Override
                    public void run() {
                        if (costsJason.isFinish()) {
                            costsJasonResponse = costsJason.getUserId();
                            adapter.addAll(fetchJason(costsJasonResponse));
                        } else {
                            boxeshandler.postDelayed(this, 100);
                        }
                    }
                };
                boxeshandler.post(boxesrunnableCode);
            }
        });
        return rootview;
    }

    private ArrayList<costs> fetchJason(String costsJasonResponse) {
        ArrayList<costs> list = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(costsJasonResponse);
            JSONArray sa = jsonObject.names();
            for (int i = 0; i < sa.length(); i++) {
                JSONObject jsonArrayId = jsonObject.getJSONObject(sa.get(i).toString());
                costs items = new costs(jsonArrayId.getString("order_num"), jsonArrayId.getString("amount"),
                        jsonArrayId.getString("details"),
                        jsonArrayId.getString("date"));
                list.add(items);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
