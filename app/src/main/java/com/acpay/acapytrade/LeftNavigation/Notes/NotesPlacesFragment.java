package com.acpay.acapytrade.LeftNavigation.Notes;

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

import com.acpay.acapytrade.LeftNavigation.Transitions.Names.TransitionsName;
import com.acpay.acapytrade.LeftNavigation.Transitions.Names.TransitionsNameAdapter;
import com.acpay.acapytrade.LeftNavigation.Transitions.Pay.TransitionsDetailsFragment;
import com.acpay.acapytrade.LeftNavigation.Transitions.Pay.TransitionsDoneFragment;
import com.acpay.acapytrade.MainActivity;
import com.acpay.acapytrade.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class NotesPlacesFragment extends Fragment {
    BottomNavigationView bottomNav;
    public NotesPlacesFragment(BottomNavigationView bottomNav) {
        super();
        this.bottomNav=bottomNav;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_notes_places, container, false);

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
