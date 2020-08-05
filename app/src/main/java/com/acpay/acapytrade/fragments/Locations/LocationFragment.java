package com.acpay.acapytrade.fragments.Locations;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.acpay.acapytrade.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class LocationFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<location>> {
    GoogleMap goMap;
    private String ApiUrl = "https://www.app.acapy-trade.com/locations.php";

    locationAdapter adapter;
    LoaderManager loaderManager;
    private static final int LOCATION_LOADER_ID = 1;
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
        final SwipeRefreshLayout pullToRefresh = rootView.findViewById(R.id.location_list_swap);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loaderManager.restartLoader(LOCATION_LOADER_ID, null, LocationFragment.this);
                loaderManager.initLoader(LOCATION_LOADER_ID, null, LocationFragment.this);
                pullToRefresh.setRefreshing(false);
                Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
            }
        });
        final ListView theListView = rootView.findViewById(R.id.location_list);
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
                location location =adapter.getItem(postion);
                MarkerOptions markerOptions = new MarkerOptions();
                LatLng latLng= location.latlang();
                markerOptions.position(latLng);
                markerOptions.title(location.getLatitude() + ":" + location.getLonglatitude());
                goMap.clear();
                goMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                goMap.addMarker(markerOptions);
            }
        });

        loaderManager = getLoaderManager();
        loaderManager.initLoader(LOCATION_LOADER_ID, null, LocationFragment.this);

        adapter = new locationAdapter(getContext(), new ArrayList<location>());
    theListView.setAdapter(adapter);

        return rootView;
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

    @NonNull
    @Override
    public Loader<List<location>> onCreateLoader(int id, @Nullable Bundle args) {
        return new locationLoader(getContext(), ApiUrl);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<location>> loader, List<location> data) {
        adapter.clear();
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);

        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<location>> loader) {
        adapter.clear();
    }
}