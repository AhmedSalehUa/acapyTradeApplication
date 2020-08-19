package com.acpay.acapytrade.LeftNavigation.Transitions.Names;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acpay.acapytrade.LeftNavigation.Transitions.Transitions;
import com.acpay.acapytrade.R;

import java.util.List;

public class TransitionsNameAdapter extends ArrayAdapter<TransitionsName> {
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView =  ((Activity) getContext()).getLayoutInflater().inflate(R.layout.activity_transitions_names_item, parent, false);

        }
        TransitionsName items =getItem(position);
        String fullUserNAme = items.getName();
        TextView Name=(TextView)convertView.findViewById(R.id.tarnsition_name);
        Name.setText(fullUserNAme);

        TextView chara=(TextView)convertView.findViewById(R.id.tarnsition_name_char);

        String fristChar = fullUserNAme.substring(0,1);
        chara.setText(fristChar);

        return convertView;
    }

    public TransitionsNameAdapter(@NonNull Context context, @NonNull List<TransitionsName> list) {
        super(context, 0, list);
    }
}
