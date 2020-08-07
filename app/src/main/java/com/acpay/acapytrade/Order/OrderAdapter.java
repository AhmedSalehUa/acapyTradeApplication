package com.acpay.acapytrade.Order;

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

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class OrderAdapter extends ArrayAdapter<Order> {
    TextView orderNum;
    TextView UserName;
    TextView userName;
    TextView date;
    TextView time;
    TextView place;
    TextView location;
    TextView dliverCost;
    TextView fixType;
    TextView classMatter;


    public OrderAdapter(@NonNull Context context, List<Order> orders) {
        super(context, 0, orders);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rootview;
        if (convertView == null) {
            rootview = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.orders_activity_list_header, parent, false);
        } else {
            rootview = convertView;
        }
        Order item = getItem(position);
        orderNum = rootview.findViewById(R.id.title_orderNum);
        orderNum.setText(item.getOrderNum());

        UserName=(TextView)rootview.findViewById(R.id.title_username);
        UserName.setText(item.getUserName());

        time = rootview.findViewById(R.id.title_time_label);
        time.setText(item.getTime());

        date = rootview.findViewById(R.id.title_date_label);
        date.setText(item.getDate());

        place = rootview.findViewById(R.id.title_place);
        place.setText(item.getPlace());

        location = rootview.findViewById(R.id.title_location);
        location.setText(item.getLocation());

        dliverCost = rootview.findViewById(R.id.title_dliverCost);
        dliverCost.setText(item.getDliverCost());

        classMatter = rootview.findViewById(R.id.title_matter);
        classMatter.setText(item.getClassMatter());

        fixType = rootview.findViewById(R.id.title_fixType);
        fixType.setText(item.getFixType());

        return rootview;
    }
}
