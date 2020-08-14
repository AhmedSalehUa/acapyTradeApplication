package com.acpay.acapytrade.Navigations.messages;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acpay.acapytrade.R;
import com.acpay.acapytrade.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.protobuf.StringValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MessageUserAdapter extends ArrayAdapter<MessageUsers> {
    String theLastMessage;
    TextView last_msg;
    String lastChild;
    Context context;
    public MessageUserAdapter(Context context, int resource, List<MessageUsers> objects) {
        super(context, resource, objects);
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.message_activity_item_users, parent, false);
        }
        MessageUsers user = getItem(position);
        String na = user.getName();
        TextView userId = (TextView) convertView.findViewById(R.id.message_user);
        userId.setText(na.subSequence(0, 1));

        TextView username = (TextView) convertView.findViewById(R.id.message_user_name);
        username.setText(user.getName());

        LinearLayout linearLayout = (LinearLayout)convertView.findViewById(R.id.userLatout);
        last_msg = (TextView) convertView.findViewById(R.id.last_msg);
        getLastMessege(na, last_msg,linearLayout);


        return convertView;
    }

    Message respon;

    private void getLastMessege(final String user, final TextView last_msg,final LinearLayout linearLayout) {
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("messages").child(user);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    respon = snapshot.getValue(Message.class);

                }
                if (respon.getPhotoUrl() != null) {
                    theLastMessage = "photo";
                } else {
                    theLastMessage = respon.getText();
                }
                if (!respon.getName().equals(firebaseUser.getDisplayName())) {
                    if (respon.isSeen()) {

                    } else {
                        linearLayout.setBackgroundColor(getContext().getResources().getColor(android.R.color.holo_blue_light));
                    }
                }
                switch (theLastMessage) {
                    case "default":
                        last_msg.setText("No Message");
                        break;

                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
