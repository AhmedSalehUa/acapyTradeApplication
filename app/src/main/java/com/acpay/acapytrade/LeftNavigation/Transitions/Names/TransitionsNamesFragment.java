package com.acpay.acapytrade.LeftNavigation.Transitions.Names;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.acpay.acapytrade.LeftNavigation.Transitions.Pay.TransitionsDetailsFragment;
import com.acpay.acapytrade.LeftNavigation.Transitions.Pay.TransitionsDoneFragment;
import com.acpay.acapytrade.MainActivity;
import com.acpay.acapytrade.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class TransitionsNamesFragment extends Fragment {
    BottomNavigationView bottomNav;
    public TransitionsNamesFragment(BottomNavigationView bottomNav) {
        super();
        this.bottomNav=bottomNav;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_transitions_names, container, false);

        ListView listView = (ListView)rootview.findViewById(R.id.nameCostList);
        List<TransitionsName> list = new ArrayList<>();
        list.add(new TransitionsName("Ahmed"));
        list.add(new TransitionsName("George"));
        list.add(new TransitionsName("Remon"));
        list.add(new TransitionsName("Mohamed"));

        final TransitionsNameAdapter adapter = new TransitionsNameAdapter(getContext(),list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TransitionsName transitionsName = adapter.getItem(i);
                getFragmentManager().beginTransaction().replace(R.id.transition_container,new TransitionsDetailsFragment(bottomNav,transitionsName.getName())).commit();
                bottomNav.setSelectedItemId(R.id.nav_transitions_pay);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                TransitionsName transitionsName = adapter.getItem(i);
                getFragmentManager().beginTransaction().replace(R.id.transition_container,new TransitionsDoneFragment(bottomNav,transitionsName.getName())).commit();
                bottomNav.setSelectedItemId(R.id.nav_transitions_paied);
                return false;
            }
        });
        setHasOptionsMenu(true);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("انتقالات");
        return rootview;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }


    private void enableBottomNav( ) {
        bottomNav.getMenu().getItem(0).setCheckable(true);
        bottomNav.getMenu().getItem(1).setCheckable(true);
        bottomNav.getMenu().getItem(2).setCheckable(true);
    }

    private void disableBottomNav( ) {
        bottomNav.getMenu().getItem(0).setCheckable(false);
        bottomNav.getMenu().getItem(1).setCheckable(false);
        bottomNav.getMenu().getItem(2).setCheckable(false);
    }

}
