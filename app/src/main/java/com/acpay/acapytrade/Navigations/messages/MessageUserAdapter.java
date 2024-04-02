package com.acpay.acapytrade.Navigations.messages;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acpay.acapytrade.R;
import com.acpay.acapytrade.User;

import java.util.List;

public class MessageUserAdapter extends ArrayAdapter<MessageUsers> {
    public MessageUserAdapter(Context context, int resource, List<MessageUsers> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.message_activity_item_users, parent, false);
        }
        MessageUsers user = getItem(position);
        String na = user.getName();
        TextView userId=(TextView)convertView.findViewById(R.id.message_user);
        userId.setText(na.subSequence(0,1));

        TextView username=(TextView)convertView.findViewById(R.id.message_user_name);
        username.setText(user.getName());
        return convertView;
    }
}
