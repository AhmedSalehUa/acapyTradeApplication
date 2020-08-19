package com.acpay.acapytrade.Navigations.Messages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.acpay.acapytrade.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessegeFragment extends Fragment {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;


    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;
    private ChildEventListener mChildEventListener;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private static final String TAG = "MessegeFragment";

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    String theLastMessage;
    private ListView mUsersListView;
    private MessageUserAdapter mUsersMessageAdapter;


    private static final int RC_PHOTO_PICKER = 2;
    public static final int RC_SIGN_IN = 1;

    public MessegeFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.message_activity_users, container, false);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("messages");
        mChatPhotosStorageReference = mFirebaseStorage.getReference().child("chat_photo");
        FirebaseUser user = mFirebaseAuth.getCurrentUser();

        mUsersListView = (ListView) rootView.findViewById(R.id.usersListView);


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    setSiginigInIntilization(user.getDisplayName(), user.getUid());
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
        List<MessageUsers> friendlyMessages = new ArrayList<>();
        mUsersMessageAdapter = new MessageUserAdapter(getContext(), R.layout.message_activity_item_users, friendlyMessages);
        mUsersListView.setAdapter(mUsersMessageAdapter);

        mUsersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MessageUsers user = mUsersMessageAdapter.getItem(i);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MessegeChildsFragment(user.getName())).commit();
            }
        });
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Contacts");
        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }


    private void setSiginigInIntilization(String displayName, String id) {
        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        DatabaseReference itemsRef = mFirebaseDatabase.getReference().child("messages");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUsersMessageAdapter.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    MessageUsers user = new MessageUsers(ds.getKey());
                    mUsersMessageAdapter.add(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        itemsRef.addListenerForSingleValueEvent(eventListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

}