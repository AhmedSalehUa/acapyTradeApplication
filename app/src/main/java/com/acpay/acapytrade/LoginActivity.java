package com.acpay.acapytrade;

import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD
=======
import android.util.Log;
>>>>>>> 4d8adbf7f1e4ccbed605560ec2c90d1b8bdba1f1

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
<<<<<<< HEAD
import com.google.android.gms.tasks.OnSuccessListener;
=======
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
>>>>>>> 4d8adbf7f1e4ccbed605560ec2c90d1b8bdba1f1
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
<<<<<<< HEAD
=======
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
>>>>>>> 4d8adbf7f1e4ccbed605560ec2c90d1b8bdba1f1

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static final int RC_SIGN_IN = 1;
    String token;
    Tokens tokens;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mDatabaseReference = mFirebaseDatabase.getReference().child("users");

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("main", "getInstanceId failed", task.getException());
                            return;
                        }


                        token = task.getResult().getToken();
                        tokens = new Tokens(token);


                        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
                            @Override
                            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                final FirebaseUser user = firebaseAuth.getCurrentUser();
                                if (user != null) {
                                    mDatabaseReference = mFirebaseDatabase.getReference().child("users").child(user.getDisplayName());
                                    mDatabaseReference.removeValue();
                                    User Fireuser = new User(user.getDisplayName(), user.getUid(), "offline");
                                    mDatabaseReference.push().setValue(Fireuser);
                                    if (user.getDisplayName().equals("Ahmed Saleh")) {

                                    } else {
                                        DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference().child("tokens").child(user.getDisplayName());
                                        mDatabaseReference.removeValue();
                                        mDatabaseReference.push().setValue(tokens);
                                    }
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                                    startActivity(intent);
                                } else {
                                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.PhoneBuilder().build());
                                    startActivityForResult(
                                            AuthUI.getInstance()
                                                    .createSignInIntentBuilder()
                                                    .setAvailableProviders(providers).setIsSmartLockEnabled(false)
                                                    .build(),
                                            RC_SIGN_IN);
                                }
                            }
                        };

                        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("token", token);
                        // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    protected void onStart() {

        super.onStart();
    }


}
