package com.acpay.acapytrade.LeftNavigation.Transitions;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.acpay.acapytrade.LeftNavigation.Transitions.Names.TransitionsNamesFragment;
import com.acpay.acapytrade.MainActivity;
import com.acpay.acapytrade.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TransitionsFragment extends Fragment {
    private BottomNavigationView bottomNav;
    FrameLayout frameLayout;

    public TransitionsFragment(FrameLayout frameLayout) {

        super();
        this.frameLayout=frameLayout;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) frameLayout.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        frameLayout.setLayoutParams(params);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_transitions, container, false);

        bottomNav = (BottomNavigationView) rootview.findViewById(R.id.transition_navigation);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_transitions_names:
                        getFragmentManager().beginTransaction().replace(R.id.transition_container, new TransitionsNamesFragment(bottomNav)).commit();
                        break;

                }
                return true;
            }
        });

        enableBottomNav();
        bottomNav.setSelectedItemId(R.id.nav_transitions_names);
        disableBottomNav();


        setHasOptionsMenu(true);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("انتقالات");
        return rootview;
    }

    private void enableBottomNav() {
        for (int i = 0; i < bottomNav.getMenu().size(); i++) {
            bottomNav.getMenu().getItem(i).setEnabled(true);
        }
    }

    private void disableBottomNav() {
        for (int i = 0; i < bottomNav.getMenu().size(); i++) {
            bottomNav.getMenu().getItem(i).setEnabled(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

}
