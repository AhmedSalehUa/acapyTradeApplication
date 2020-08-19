package com.acpay.acapytrade.OrderOperations.progress;

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

public class costsAdapter extends ArrayAdapter<costs> {
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView =  ((Activity) getContext()).getLayoutInflater().inflate(R.layout.activity_transitions_details_items_content, parent, false);

        }
        costs items =getItem(position);
//
//        TextView text=(TextView)convertView.findViewById(R.id.cost_amount);
//        text.setText(items.getAmount());
//
//        TextView note=(TextView)convertView.findViewById(R.id.cost_details);
//        note.setText(items.getDetails());
//
//        TextView co=(TextView)convertView.findViewById(R.id.cost_date);
//        co.setText(items.getDate());
        return convertView;
    }

    public costsAdapter(@NonNull Context context, @NonNull List<costs> list) {
        super(context, 0, list);
    }
}
