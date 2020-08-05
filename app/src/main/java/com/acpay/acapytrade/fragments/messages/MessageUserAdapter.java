package com.acpay.acapytrade.fragments.messages;

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

public class MessageUserAdapter extends ArrayAdapter<User> {
    public MessageUserAdapter(Context context, int resource, List<User> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.message_activity_item, parent, false);
        }
        User user = getItem(position);
        String na = user.getUsername();
        TextView userId=(TextView)convertView.findViewById(R.id.message_user);
        userId.setText(na.subSequence(0,1));

        TextView username=(TextView)convertView.findViewById(R.id.message_user_name);
        username.setText(na);
        return convertView;
    }
}
