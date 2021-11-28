package com.acpay.acapytrade;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.acpay.acapytrade.LeftNavigation.Notes.NotesFragment;
import com.acpay.acapytrade.LeftNavigation.StaticIp.StaticIp;
import com.acpay.acapytrade.LeftNavigation.Transitions.TransitionsFragment;
import com.acpay.acapytrade.LeftNavigation.ip.IpSearchFragment;
import com.acpay.acapytrade.Navigations.HomeFragment;
import com.acpay.acapytrade.Navigations.Locations.LocationFragment;
import com.acpay.acapytrade.Navigations.Messages.Message;
import com.acpay.acapytrade.Navigations.Messages.MessageUsers;
import com.acpay.acapytrade.Navigations.Messages.MessegeChildsFragment;
import com.acpay.acapytrade.Navigations.Messages.MessegeFragment;
import com.acpay.acapytrade.Navigations.Order.OrderFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNav;

//    public static final String APIHEADER = "http://41.178.166.110/acapy-trade/app";
    NavigationView navigationView;
    private DrawerLayout drawer;
    FirebaseUser user;
    String token;
    int countMessage = 0;
    int BottomMargin;
    public static String getAPIHEADER(Context athis) {
        if (athis == null) {
            Log.e("api", "error");
        }
        SharedPreferences sharedPreferences = athis.getSharedPreferences("MainActivity", MODE_PRIVATE);
        return sharedPreferences.getString("api", "http://41.178.166.108/acapy-trade/app");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = this.getSharedPreferences("MainActivity", MODE_PRIVATE);
        if(sharedPreferences.getString("api", "no").equals("no")){
            sharedPreferences.edit().putString("api", "http://41.178.166.108/acapy-trade/app");
            sharedPreferences.edit().commit();
        }
        FrameLayout framaeLayouat = (FrameLayout) findViewById(R.id.fragment_container);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) framaeLayouat.getLayoutParams();
        BottomMargin = params.bottomMargin;
        Dexter.withActivity(this).withPermissions(Arrays.asList(Manifest.permission.FOREGROUND_SERVICE, Manifest.permission.ACCESS_WIFI_STATE)).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                Log.e("permission ok", "ok");
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                Snackbar.make(findViewById(android.R.id.content), "Permissions Denied", Snackbar.LENGTH_SHORT).show();
            }
        }).check();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        user = FirebaseAuth.getInstance().getCurrentUser();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        bottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_bot_order:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrderFragment()).commit();
                        break;
                    case R.id.nav_bot_messege:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MessegeFragment()).commit();
                        break;
                    case R.id.nav_bot_location:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LocationFragment()).commit();
                        break;
                }
                return true;
            }
        });
        bottomNav.setVisibility(View.VISIBLE);

        Intent noty = this.getIntent();
        String type = noty.getStringExtra("type");
        if (type != null) {
            if (type.equals("message")) {
                bottomNav.setSelectedItemId(R.id.nav_bot_messege);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MessegeChildsFragment(noty.getStringExtra("sender"))).commit();
            } else if (type.equals("pended")) {
                bottomNav.setSelectedItemId(R.id.nav_bot_order);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrderFragment("pended")).commit();

            } else if (type.equals("finished")) {
                bottomNav.setSelectedItemId(R.id.nav_bot_order);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrderFragment("finished")).commit();
            } else if (type.equals("ordernotes")) {
                bottomNav.setSelectedItemId(R.id.nav_bot_order);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrderFragment()).commit();
            }
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrderFragment()).commit();
        }


        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {

                if (bottomNav.getSelectedItemId() != R.id.nav_bot_order) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrderFragment()).commit();
                    bottomNav.setSelectedItemId(R.id.nav_bot_order);
                } else if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);

        setMessageCount();

    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.lef_nav_home:
                FrameLayout framaeLayouat = (FrameLayout) findViewById(R.id.fragment_container);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) framaeLayouat.getLayoutParams();
                params.setMargins(0, 0, 0, BottomMargin);
                framaeLayouat.setLayoutParams(params);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                bottomNav.setVisibility(View.VISIBLE);
                bottomNav.setSelectedItemId(R.id.nav_bot_order);
                break;
            case R.id.lef_nav_cost:

                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fragment_container);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TransitionsFragment(frameLayout)).commit();
                bottomNav.setVisibility(View.INVISIBLE);
                break;
            case R.id.lef_nav_ip:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new IpSearchFragment()).commit();
                bottomNav.setVisibility(View.INVISIBLE);
                break;
            case R.id.lef_nav_notes:
                FrameLayout framaeLayout = (FrameLayout) findViewById(R.id.fragment_container);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotesFragment(framaeLayout)).commit();
                bottomNav.setVisibility(View.INVISIBLE);
                break;
            case R.id.lef_nav_static_ip:
                FrameLayout framaesLayout = (FrameLayout) findViewById(R.id.fragment_container);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StaticIp(framaesLayout)).commit();
                bottomNav.setVisibility(View.INVISIBLE);
                break;
            case R.id.lef_nav_sales:
                FrameLayout frameSalesLayout = (FrameLayout) findViewById(R.id.fragment_container);
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Sales(frameSalesLayout)).commit();
                bottomNav.setVisibility(View.INVISIBLE);
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void status(String status) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getDisplayName());
        mDatabaseReference.removeValue();
        User Fireuser = new User(firebaseUser.getDisplayName(), firebaseUser.getUid(), status);
        mDatabaseReference.push().setValue(Fireuser);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

    private void setMessageCount() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("messages");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // setCurrentCount();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                setCurrentCount();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        reference.addChildEventListener(childEventListener);
        setCurrentCount();
    }

    private void setCurrentCount() {
        BadgeDrawable orCreateBadge = bottomNav.getOrCreateBadge(R.id.nav_bot_messege);
        orCreateBadge.setVisible(false);
        orCreateBadge.setNumber(0);
        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference().child("messages");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    MessageUsers user = new MessageUsers(ds.getKey());
                    getMessageCount(user.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        itemsRef.addListenerForSingleValueEvent(eventListener);
    }

    Message respon;


    private void getMessageCount(final String user) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("messages").child(user);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    respon = snapshot.getValue(Message.class);

                }
                if (respon.getPhotoUrl() != null) {

                } else {

                }
                if (!respon.getName().equals(firebaseUser.getDisplayName())) {
                    if (respon.isSeen()) {

                    } else {
                        BadgeDrawable orCreateBadge = bottomNav.getOrCreateBadge(R.id.nav_bot_messege);
                        Log.e("num", orCreateBadge.getNumber() + "");
                        orCreateBadge.setVisible(true);
                        orCreateBadge.setNumber(orCreateBadge.getNumber() + 1);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}