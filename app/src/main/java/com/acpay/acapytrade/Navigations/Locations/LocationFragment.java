package com.acpay.acapytrade.Navigations.Locations;

import static com.acpay.acapytrade.MainActivity.getAPIHEADER;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.acpay.acapytrade.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class LocationFragment extends Fragment {
    GoogleMap goMap;
    private String ApiUrl ="";

    locationAdapter adapter;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            goMap = googleMap;
            goMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(latLng.latitude + ":" + latLng.longitude);
                    goMap.clear();
                    goMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    goMap.addMarker(markerOptions);
                }
            });

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.location_activity, container, false);
        ApiUrl = getAPIHEADER(getContext())+"/locations.php";

        final SwipeRefreshLayout pullToRefresh = rootView.findViewById(R.id.location_list_swap);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               performApi();
                pullToRefresh.setRefreshing(false);
                Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
            }
        });
        final ListView theListView = rootView.findViewById(R.id.location_list);
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
                location location = adapter.getItem(postion);
                MarkerOptions markerOptions = new MarkerOptions();
                LatLng latLng = location.latlang();
                markerOptions.position(latLng);
                markerOptions.title(location.getLatitude() + ":" + location.getLonglatitude());
                goMap.clear();
                goMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                goMap.addMarker(markerOptions);
            }
        });

        performApi();

        adapter = new locationAdapter(getContext(), new ArrayList<location>());
        theListView.setAdapter(adapter);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Location");
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
                        List<location> orders = locationUtilies.extractFeuterFromJason(response);
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    public void setupAdpater( List<location> data) {
        adapter.clear();
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);

        }
    }

}