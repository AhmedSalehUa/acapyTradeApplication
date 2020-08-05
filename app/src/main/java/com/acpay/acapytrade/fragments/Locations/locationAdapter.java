package com.acpay.acapytrade.fragments.Locations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.acpay.acapytrade.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class locationAdapter extends ArrayAdapter<location> {

    public locationAdapter(@NonNull Context context, @NonNull List<location> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.location_activity_list_items, parent, false);
        }
        location location = getItem(position);

        TextView userName = (TextView)listItemView.findViewById(R.id.UserName);
        String fullUserNAme = location.getUser();
        String fristChar = fullUserNAme.substring(0,1);
        userName.setText(fristChar);

        TextView latitude = (TextView)listItemView.findViewById(R.id.latitude);
        latitude.setText(location.getLatitude());

        TextView Longlatitude = (TextView)listItemView.findViewById(R.id.longtitude);
        Longlatitude.setText(location.getLonglatitude());

        TextView date = (TextView)listItemView.findViewById(R.id.Date);
        TextView time = (TextView)listItemView.findViewById(R.id.Time);

        String fullDate =location.getDate();
        String[] s = fullDate.split(" ");
        date.setText(s[0]);

        time.setText(s[1]);

        return listItemView;
    }
}
