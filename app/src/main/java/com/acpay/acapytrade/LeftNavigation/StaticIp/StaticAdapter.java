package com.acpay.acapytrade.LeftNavigation.StaticIp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acpay.acapytrade.Navigations.Locations.location;
import com.acpay.acapytrade.R;

import java.util.List;

public class StaticAdapter extends ArrayAdapter<Static> {

    public StaticAdapter(@NonNull Context context, @NonNull List<Static> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.static_ip_items, parent, false);
        }
        Static Static = getItem(position);

        TextView Place = listItemView.findViewById(R.id.static_ip_place);
        Place.setText(Static.getPlace());

        TextView Ip = listItemView.findViewById(R.id.static_ip_ip);
        Ip.setText(Static.getIp());

        return listItemView;
    }
}
