package com.acpay.acapytrade.LeftNavigation.Transitions.Pay;

import static com.acpay.acapytrade.MainActivity.getAPIHEADER;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.acpay.acapytrade.LeftNavigation.Transitions.Transitions;
import com.acpay.acapytrade.Navigations.Messages.sendNotification.Data;
import com.acpay.acapytrade.R;
import com.acpay.acapytrade.SendNotification;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TransitionsDetailsFragment extends Fragment {
    BottomNavigationView bottomNav;
    String name;

    public TransitionsDetailsFragment(BottomNavigationView bottomNav, String name) {
        super();
        this.bottomNav = bottomNav;
        this.name = name;
    }

    TransitionDetailsAdapter adapter;

    ProgressBar progressBar;
    TextView emptyList;
    TextView totalText;
    Button payAll;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_transitions_details, container, false);
        final ListView listView = rootview.findViewById(R.id.costList);
        progressBar = rootview.findViewById(R.id.costListProgress);
        totalText = rootview.findViewById(R.id.total);
        payAll = rootview.findViewById(R.id.payAll);
        emptyList = rootview.findViewById(R.id.costListText);
        emptyList.setText("جارى التحميل");
        String api = getAPIHEADER(getContext()) + "/getTransitions.php?name=" + name + "&status=false";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<TransitionsDetails> list = extractTransitionsfromapi(response);
                        adapter = new TransitionDetailsAdapter(getContext(), list, "details");
                        listView.setAdapter(adapter);
                        listView.setEmptyView(emptyList);
                        progressBar.setVisibility(View.GONE);
                        emptyList.setText("لايوجد انتقالات");
                        double total = 0;
                        for (int x = 0; x < adapter.getCount(); x++) {
                            final TransitionsDetails transitionsDetails = adapter.getItem(x);

                            for (int y = 0; y < transitionsDetails.getList().size(); y++) {
                                total += Double.parseDouble(transitionsDetails.getList().get(y).getAmount());
                            }

                            transitionsDetails.setPayBtnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String api = getAPIHEADER(getContext()) + "/updateTransitions.php?id=" + transitionsDetails.getOrderNum() + "&status=true";
                                    RequestQueue queue = Volley.newRequestQueue(getContext());
                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();
                                                    getFragmentManager().beginTransaction().replace(R.id.transition_container, new TransitionsDetailsFragment(bottomNav, name)).commit();
                                                    Data data = new Data("تنقلات", "تم تحويل تنقلات " + transitionsDetails.getPlace() + " " + transitionsDetails.getLocation(), "costs");
                                                    SendNotification send = new SendNotification(getContext(), getReciverName(name), data);
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
                            });
                        }
                        totalText.setText(String.valueOf(total));
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
        setHasOptionsMenu(true);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                getFragmentManager().beginTransaction().replace(R.id.transition_container, new TransitionsNamesFragment(bottomNav)).commit();
                bottomNav.setSelectedItemId(R.id.nav_transitions_names);
            }
        };
        payAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter.getCount() == 0) {
                    Log.e("adapter.getCount()", String.valueOf(adapter.getCount()));
                } else {
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    for (int x = 0; x < adapter.getCount(); x++) {
                        final TransitionsDetails transitionsDetails = adapter.getItem(x);
                        String api = getAPIHEADER(getContext()) + "/updateTransitions.php?id=" + transitionsDetails.getOrderNum() + "&status=true";
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.e("response", response);
//                                        Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();
                                        Data data = new Data("تنقلات", "تم تحويل تنقلات " + transitionsDetails.getPlace() + " " + transitionsDetails.getLocation(), "costs");
                                        SendNotification send = new SendNotification(getContext(), getReciverName(name), data);
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
                    getFragmentManager().beginTransaction().replace(R.id.transition_container, new TransitionsDetailsFragment(bottomNav, name)).commit();
                }

            }
        });
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("انتقالات");
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

    private String getReciverName(String name) {
        switch (name) {
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
