package com.acpay.acapytrade.LeftNavigation.ip;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acpay.acapytrade.R;

import java.util.List;

public class ipAdapter extends ArrayAdapter {
    List<String> list;
    public ipAdapter(@NonNull Context context, List<String> list) {
        super(context, 0, list);
        this.list=list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rootView;
        if (convertView == null) {
            rootView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.ipsearch_item, parent, false);
        }else {
            rootView = convertView;
        }
        TextView ip = rootView.findViewById(R.id.ip);
        ip.setText(list.get(position));

        return rootView;
    }
}
