package com.acpay.acapytrade.Navigations.messages;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.acpay.acapytrade.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {
    String userName;

    public MessageAdapter(Context context, int resource, List<Message> objects, String userName) {
        super(context, resource, objects);
        this.userName = userName;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.message_activity_item, parent, false);
        }

        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.photoImageView);
        TextView messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.dateTextView);
        TextView timeTextView = (TextView) convertView.findViewById(R.id.timeTextView);

        Message message = getItem(position);

        boolean isPhoto = message.getPhotoUrl() != null;
        if (isPhoto) {
            messageTextView.setVisibility(View.GONE);
            photoImageView.setVisibility(View.VISIBLE);
            Glide.with(photoImageView.getContext())
                    .load(message.getPhotoUrl())
                    .into(photoImageView);
        } else {
            messageTextView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            messageTextView.setText(message.getText());
        }
        authorTextView.setText("Me");
        dateTextView.setText(message.getDate());
        timeTextView.setText(message.getTime());
        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.massege_container);
        if (message.getName().equals(userName)) {
            linearLayout.setBackground(getContext().getResources().getDrawable(R.drawable.shape_bg_outgoing_bubble));
            authorTextView.setTextColor(getContext().getResources().getColor(R.color.massegeMeSender));
            dateTextView.setTextColor(getContext().getResources().getColor(R.color.massegeMeSender));
            timeTextView.setTextColor(getContext().getResources().getColor(R.color.massegeMeSender));
            linearLayout.setGravity(Gravity.RIGHT);

        } else {
            linearLayout.setBackground(getContext().getResources().getDrawable(R.drawable.shape_bg_incoming_bubble));
            authorTextView.setTextColor(getContext().getResources().getColor(R.color.massegeToSender));
            dateTextView.setTextColor(getContext().getResources().getColor(R.color.massegeToSender));
            timeTextView.setTextColor(getContext().getResources().getColor(R.color.massegeToSender));

            linearLayout.setGravity(Gravity.LEFT);

        }
        return convertView;
    }
}
