package com.acpay.acapytrade.LeftNavigation.StaticIp;

import static com.acpay.acapytrade.MainActivity.getAPIHEADER;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.acpay.acapytrade.R;
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

public class StaticIp extends Fragment {
    private BottomNavigationView bottomNav;
    FrameLayout frameLayout;
    ListView staticList;
    TextView emptyList;
    ProgressBar progressList;
    StaticAdapter adapter;

    public StaticIp(FrameLayout frameLayout) {

        super();
        this.frameLayout = frameLayout;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) frameLayout.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        frameLayout.setLayoutParams(params);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.static_ip, container, false);
        staticList = root.findViewById(R.id.static_list);
        emptyList = root.findViewById(R.id.static_list_empty);
        progressList = root.findViewById(R.id.static_list_progress);
        staticList.setEmptyView(emptyList);
        progressList.setVisibility(View.VISIBLE);
        adapter = new StaticAdapter(getContext(), new ArrayList<Static>());
        staticList.setAdapter(adapter);
        String api = getAPIHEADER(getContext())+"/getStaticIp.php";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressList.setVisibility(View.GONE);
                        adapter.clear();
                        adapter.addAll(getStaticsFromApi(response));
                        adapter.notifyDataSetChanged();
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

        staticList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        staticList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Static aStatic = adapter.getItem(i);
                ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("ip", aStatic.getIp());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getContext(), "Copied", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return root;
    }

    private List<Static> getStaticsFromApi(String jason) {
        List<Static> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jason);
            JSONArray sa = jsonObject.names();
            for (int i = 0; i < sa.length(); i++) {
                JSONObject jsonArrayId = jsonObject.getJSONObject(sa.get(i).toString());
                Static statics = new Static(jsonArrayId.getString("place"),
                        jsonArrayId.getString("ip")
                );
                list.add(statics);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
