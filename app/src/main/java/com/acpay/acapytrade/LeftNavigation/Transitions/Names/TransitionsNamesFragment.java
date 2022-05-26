package com.acpay.acapytrade.LeftNavigation.Transitions.Names;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.acpay.acapytrade.DatabaseHelper.DatabaseHandler;
import com.acpay.acapytrade.LeftNavigation.Transitions.Pay.TransitionsDetailsFragment;
import com.acpay.acapytrade.LeftNavigation.Transitions.Pay.TransitionsDoneFragment;
import com.acpay.acapytrade.MainActivity;
import com.acpay.acapytrade.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TransitionsNamesFragment extends Fragment {
    BottomNavigationView bottomNav;
    DatabaseHandler db;
    TransitionsNameAdapter adapter;

    public TransitionsNamesFragment(BottomNavigationView bottomNav) {
        super();
        this.bottomNav = bottomNav;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_transitions_names, container, false);
        db = new DatabaseHandler(getContext());
        ListView listView = (ListView) rootview.findViewById(R.id.nameCostList);
        List<TransitionsName> list = db.getNames();
        FloatingActionButton addName = rootview.findViewById(R.id.addName);
        addName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("clicked", "ok");
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Add New User");
                LinearLayout container = new LinearLayout(getContext());
                container.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(20, 20, 20, 20);
                final EditText input = new EditText(getContext());
                input.setLayoutParams(lp);
                input.setHint("NAME");
                input.setGravity(Gravity.TOP | Gravity.LEFT);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setLines(1);
                input.setMaxLines(1);
                container.addView(input, lp);
                final EditText token = new EditText(getContext());
                token.setLayoutParams(lp);
                token.setGravity(Gravity.TOP | Gravity.LEFT);
                token.setInputType(InputType.TYPE_CLASS_TEXT);
                token.setLines(1);token.setHint("token");
                token.setMaxLines(1);
                container.addView(token, lp);
                alertDialog.setView(container);
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        db.addUser(input.getText().toString(), token.getText().toString());
                        adapter.clear();
                        adapter.addAll(db.getNames());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(getContext(), "canceled", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });
        adapter = new TransitionsNameAdapter(getContext(), list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Chosoe");

                alertDialog.setPositiveButton("لم يتم الدفع", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        TransitionsName transitionsName = adapter.getItem(i);
                        getFragmentManager().beginTransaction().replace(R.id.transition_container, new TransitionsDetailsFragment(bottomNav, transitionsName.getName())).commit();
                        bottomNav.setSelectedItemId(R.id.nav_transitions_pay);
                    }
                }).setNegativeButton("تم الدفع", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        TransitionsName transitionsName = adapter.getItem(i);
                        getFragmentManager().beginTransaction().replace(R.id.transition_container, new TransitionsDoneFragment(bottomNav, transitionsName.getName())).commit();
                        bottomNav.setSelectedItemId(R.id.nav_transitions_paied);
                    }
                }).show();

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("سيتم حذف "+ adapter.getItem(i).getName())
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                db.deleteUser(adapter.getItem(i).getName());  adapter.clear();
                                adapter.addAll(db.getNames());
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
                return true;
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


    private void enableBottomNav() {
        bottomNav.getMenu().getItem(0).setCheckable(true);
        bottomNav.getMenu().getItem(1).setCheckable(true);
        bottomNav.getMenu().getItem(2).setCheckable(true);
    }

    private void disableBottomNav() {
        bottomNav.getMenu().getItem(0).setCheckable(false);
        bottomNav.getMenu().getItem(1).setCheckable(false);
        bottomNav.getMenu().getItem(2).setCheckable(false);
    }

}
