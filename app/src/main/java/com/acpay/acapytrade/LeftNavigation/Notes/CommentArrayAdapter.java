package com.acpay.acapytrade.LeftNavigation.Notes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acpay.acapytrade.Cordinations.ECCardContentListItemAdapter;
import com.acpay.acapytrade.R;

import java.util.List;

@SuppressLint({"SetTextI18n", "InflateParams"})
public class CommentArrayAdapter extends ECCardContentListItemAdapter<NotesPlacesDetails> {

    public CommentArrayAdapter(@NonNull Context context, @NonNull List<NotesPlacesDetails> objects) {
        super(context, R.layout.activity_notes_details, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());

            rowView = inflater.inflate(R.layout.activity_notes_details, null);
            // configure view holder
            viewHolder = new ViewHolder();
            viewHolder.deviceName = (TextView) rowView.findViewById(R.id.deviceName);
            viewHolder.deviceType = (TextView) rowView.findViewById(R.id.deviceType);
            viewHolder.deviceModel = (TextView) rowView.findViewById(R.id.deviceModel);
            viewHolder.deviceDetails = (TextView) rowView.findViewById(R.id.deviceDetails);
            viewHolder.deviceIp = (TextView) rowView.findViewById(R.id.deviceContentIP);
            viewHolder.devicePort = (TextView) rowView.findViewById(R.id.deviceContentPort);
            viewHolder.deviceUsername = (TextView) rowView.findViewById(R.id.deviceContentUser);
            viewHolder.devicePassword = (TextView) rowView.findViewById(R.id.deviceContentPass);
            viewHolder.deviceEmail = (TextView) rowView.findViewById(R.id.deviceContentEmail);
            viewHolder.deviceEmailPass = (TextView) rowView.findViewById(R.id.deviceContentEmailPass);
            viewHolder.shareDevice = (TextView) rowView.findViewById(R.id.shareDevice);
            viewHolder.deleteDevice = (TextView) rowView.findViewById(R.id.deleteDevice);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        final NotesPlacesDetails objectItem = getItem(position);
        if (objectItem != null) {
            viewHolder.deviceType.setText(objectItem.getDeviceType());
            viewHolder.deviceModel.setText(objectItem.getDeviceModel());
            viewHolder.deviceName.setText(objectItem.getDeviceName());
            viewHolder.deviceDetails.setText(objectItem.getDeviceDetails());
            viewHolder.deviceIp.setText(objectItem.getDeviceIp());
            viewHolder.devicePort.setText(objectItem.getDevicePort());
            viewHolder.deviceUsername.setText(objectItem.getDeviceUsername());
            viewHolder.devicePassword.setText(objectItem.getDevicePasswoed());
            viewHolder.deviceEmail.setText(objectItem.getDeviceEmail());
            viewHolder.deviceEmailPass.setText(objectItem.getDeviceEmailPass());
            viewHolder.shareDevice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(getContext(),"shareDevice" +  objectItem.getId(),Toast.LENGTH_SHORT).show();
                }
            });
            viewHolder.deleteDevice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(),"deleteDevice" +  objectItem.getId(),Toast.LENGTH_SHORT).show();
                }
            });
        }


        return rowView;
    }

    static class ViewHolder {
        TextView deviceName;
        TextView deviceType;
        TextView deviceModel;
        TextView deviceDetails;

        TextView deviceIp;
        TextView devicePort;
        TextView deviceUsername;
        TextView devicePassword;
        TextView deviceEmail;
        TextView deviceEmailPass;

        TextView shareDevice;
        TextView deleteDevice;
    }

}
