package com.acpay.acapytrade.LeftNavigation.Transitions.Pay;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.acpay.acapytrade.LeftNavigation.Transitions.Names.TransitionsNamesFragment;
import com.acpay.acapytrade.LeftNavigation.Transitions.Pay.TransitionDetailsAdapter;
import com.acpay.acapytrade.LeftNavigation.Transitions.Pay.TransitionsApiRespoding;
import com.acpay.acapytrade.LeftNavigation.Transitions.Pay.TransitionsDetails;
import com.acpay.acapytrade.LeftNavigation.Transitions.Transitions;
import com.acpay.acapytrade.MainActivity;
import com.acpay.acapytrade.Networking.JasonReponser;
import com.acpay.acapytrade.R;
import com.acpay.acapytrade.SendNotification;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransitionsDoneFragment extends Fragment {
    BottomNavigationView bottomNav;
    String name;
    TransitionDetailsAdapter adapter;
    public TransitionsDoneFragment(BottomNavigationView bottomNav, String name) {
        super();
        this.bottomNav = bottomNav;
        this.name = name;
    }
    ProgressBar progressBar;
    TextView emptyList;
    TransitionsApiRespoding update;
    TransitionsApiUpdate updatea;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_transitions_details, container, false);
        final ListView listView = rootview.findViewById(R.id.costList);
        progressBar = rootview.findViewById(R.id.costListProgress);
        emptyList = rootview.findViewById(R.id.costListText);
        emptyList.setText("جارى التحميل");
        String api = "https://www.app.acapy-trade.com/getTransitions.php?name=" + name+"&status=true";
        update = new TransitionsApiRespoding();
        update.setFinish(false);
        update.execute(api);
        final Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                if (update.isFinish()) {
                    List<TransitionsDetails> list = extractTransitionsfromapi(update.getUserId());
                    adapter = new TransitionDetailsAdapter(getContext(), list,"done");
                    listView.setAdapter(adapter);
                    listView.setEmptyView(emptyList);
                    progressBar.setVisibility(View.GONE);
                    emptyList.setText("لايوجد انتقالات");
                    for (int x = 0 ; x <adapter.getCount();x++){
                        final TransitionsDetails transitionsDetails = adapter.getItem(x);
                        transitionsDetails.setUnPayBtnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String api = "https://www.app.acapy-trade.com/updateTransitions.php?id="+transitionsDetails.getOrderNum()+"&status=false";
                                updatea = new TransitionsApiUpdate();
                                updatea.setFinish(false);
                                updatea.execute(api);
                                final Handler handler = new Handler();
                                Runnable runnableCode = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (updatea.isFinish()) {
                                            Toast.makeText(getContext(),"done",Toast.LENGTH_SHORT).show();
                                            getFragmentManager().beginTransaction().replace(R.id.transition_container, new TransitionsDoneFragment(bottomNav,name)).commit();
                                            SendNotification send = new SendNotification(getContext(),getReciverName(name),"تنقلات","تم الغاء التحويل تنقلات " + transitionsDetails.getPlace()+" " + transitionsDetails.getLocation());

                                        } else {
                                            handler.postDelayed(this, 100);
                                        }
                                    }


                                };
                                handler.post(runnableCode);
                               }
                        });
                    }
                    Log.e("as", list.toString());
                } else {
                    handler.postDelayed(this, 100);
                }
            }


        };
        handler.post(runnableCode);
        setHasOptionsMenu(true);


        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                getFragmentManager().beginTransaction().replace(R.id.transition_container, new TransitionsNamesFragment(bottomNav)).commit();
                bottomNav.setSelectedItemId(R.id.nav_transitions_names);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("انتقالات");
        return rootview;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    private List<TransitionsDetails> extractTransitionsfromapi(String userId) {
        final List<TransitionsDetails> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(userId);
            JSONArray sa = jsonObject.names();
            for (int i = 0; i < sa.length(); i++) {
                JSONObject jsonArrayId = jsonObject.getJSONObject(sa.get(i).toString());
                List<Transitions> transitions = new ArrayList<>();
                if (jsonArrayId.has("list")) {
                    Log.e("jsonArrayId.has(\"list\")", "ok");
                    JSONObject transitionsjsonObject = new JSONObject(jsonArrayId.getString("list"));
                    JSONArray transitionssa = transitionsjsonObject.names();
                    Log.e("transitionssa", String.valueOf(transitionssa.length()));
                    for (int x = 0; x < transitionssa.length(); x++) {
                        JSONObject transitionsjsonArrayId = transitionsjsonObject.getJSONObject(transitionssa.get(x).toString());
                        transitions.add(new Transitions(transitionsjsonArrayId.getString("amount"), transitionsjsonArrayId.getString("details")));
                    }
                }

                TransitionsDetails transitionsDetails = new TransitionsDetails(jsonArrayId.getString("order_num"),
                        jsonArrayId.getString("date"),
                        jsonArrayId.getString("time"),
                        jsonArrayId.getString("place"),
                        jsonArrayId.getString("location"), transitions);

                list.add(transitionsDetails);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
    private String getReciverName(String name){
        switch (name){
            case "Ahmed":
                return "Ahmed Saleh";
            case "Geroge":
                return "George Elgndy";
            case "Remon":
                return "Remon";
            case "Mohamed":
                return "Mohamed Hammad";
        }
        return "";
    }
}
