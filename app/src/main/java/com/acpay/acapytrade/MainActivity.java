package com.acpay.acapytrade;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.acpay.acapytrade.LeftNavigation.CostFragment;
import com.acpay.acapytrade.LeftNavigation.ip.IpSearchFragment;
import com.acpay.acapytrade.Navigations.HomeFragment;
import com.acpay.acapytrade.Navigations.Locations.LocationFragment;
import com.acpay.acapytrade.Navigations.messages.MessegeChildsFragment;
import com.acpay.acapytrade.Navigations.messages.MessegeFragment;
import com.acpay.acapytrade.Navigations.OrderFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    NavigationView navigationView;
    private DrawerLayout drawer;
    FirebaseUser user;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Dexter.withActivity(this).withPermissions(Arrays.asList(Manifest.permission.FOREGROUND_SERVICE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_FINE_LOCATION)).withListener(new MultiplePermissionsListener() {
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
        BadgeDrawable bage = bottomNav.getOrCreateBadge(R.id.nav_bot_messege);
        bage.setVisible(true);
        bage.setNumber(5);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("main", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("token", token);

                    }
                });
        Intent noty = this.getIntent();
        String type = noty.getStringExtra("type");
        if (type != null) {
            if (type.equals("notification")) {
                bottomNav.setSelectedItemId(R.id.nav_bot_messege);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MessegeChildsFragment(noty.getStringExtra("sender"))).commit();
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


    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.pending_request:
                Intent intent = new Intent(MainActivity.this, PendingRequests.class);
                startActivity(intent);
                return true;
            case R.id.deleted_order:

                Intent intent2 = new Intent(MainActivity.this, DeletedActivity.class);
                startActivity(intent2);
                return true;
            case R.id.done_orders:
                Intent intent3 = new Intent(MainActivity.this, FinishedActivity.class);
                startActivity(intent3);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.lef_nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                bottomNav.setVisibility(View.VISIBLE);
                bottomNav.setSelectedItemId(R.id.nav_bot_order);
                break;
            case R.id.lef_nav_cost:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CostFragment()).commit();
                bottomNav.setVisibility(View.INVISIBLE);
                break;
            case R.id.lef_nav_ip:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new IpSearchFragment()).commit();
                bottomNav.setVisibility(View.INVISIBLE);
                break;
            case R.id.lef_nav_notes:
                Snackbar.make(findViewById(android.R.id.content), "Soon", BaseTransientBottomBar.LENGTH_SHORT).show();
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotesFragment()).commit();
//                bottomNav.setVisibility(View.INVISIBLE);
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void status(String status) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getDisplayName().equals("Ahmed Saleh")) {

        } else {
            String token = this.getIntent().getStringExtra("token");
            DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getDisplayName());
            mDatabaseReference.removeValue();
            User Fireuser = new User(firebaseUser.getDisplayName(), firebaseUser.getUid(), token, status);
            mDatabaseReference.push().setValue(Fireuser);
        }

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
}