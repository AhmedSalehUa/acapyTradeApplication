package com.acpay.acapytrade.LeftNavigation.Notes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acpay.acapytrade.AddOrEditeOrderActivity;
import com.acpay.acapytrade.Cordinations.ECCardContentListItemAdapter;
import com.acpay.acapytrade.MainActivity;
import com.acpay.acapytrade.Navigations.Messages.Message;
import com.acpay.acapytrade.Navigations.Messages.sendNotification.Data;
import com.acpay.acapytrade.R;
import com.acpay.acapytrade.SendNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@SuppressLint({"SetTextI18n", "InflateParams"})
public class CommentArrayAdapter extends ECCardContentListItemAdapter<NotesPlacesDetails> {

    Context a;

    public CommentArrayAdapter(@NonNull Context context, @NonNull List<NotesPlacesDetails> objects) {
        super(context, R.layout.activity_notes_details, objects);
        a = context;

    }

    NotesPlacesDetails objectItem;

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

        objectItem = getItem(position);
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

                    setUsersList();
                }
            });
            viewHolder.deleteDevice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String api = "https://www.app.acapy-trade.com/deleteNotesDetails.php?id=" + objectItem.getId();
                    final NotesResponser update = new NotesResponser();
                    update.setFinish(false);
                    update.execute(api);
                    final Handler handler = new Handler();
                    Runnable runnableCode = new Runnable() {
                        @SuppressLint("NewApi")
                        @Override
                        public void run() {
                            if (update.isFinish()) {
                                if (update.getUserId().contains("1")) {
                                    Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                    a.startActivity(new Intent(a,MainActivity.class));
                                } else {
                                }
                            } else {
                                handler.postDelayed(this, 100);
                            }
                        }
                    };
                    handler.postDelayed(runnableCode, 1000);
                    handler.post(runnableCode);
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

    List<String> usersList;
    boolean[] checkedColors;
    String ReciverName = "";

    public void setUsersList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(a);
        String[] colors = new String[]{
                "Ahmed Saleh",
                "Mohamed Hammad",
                "Remon",
                "George Elgndy"
        };

        checkedColors = new boolean[]{
                false,
                false,
                false,
                false
        };
        usersList = Arrays.asList(colors);

        builder.setMultiChoiceItems(colors, checkedColors, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checkedColors[which] = isChecked;

                String currentItem = usersList.get(which);
            }
        });

        // Specify the dialog is not cancelable
        builder.setCancelable(false);

        // Set a title for alert dialog
        builder.setTitle("Order For");


        // Set the negative/no button click listener
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < checkedColors.length; i++) {
                    boolean checked = checkedColors[i];
                    if (checked) {
                        ReciverName = usersList.get(i);
                    }
                }
                String message = "Device Name: " + objectItem.getDeviceName() + "\n"
                        + "Device Details: " + objectItem.getDeviceDetails() + "\n"
                        + "Device Model: " + objectItem.getDeviceModel() + "\n"
                        + "Device Type: " + objectItem.getDeviceType() + "\n"
                        + "Device Ip: " + objectItem.getDeviceIp() + "\n"
                        + "Device Port: " + objectItem.getDevicePort() + "\n"
                        + "Device Username: " + objectItem.getDeviceUsername() + "\n"
                        + "Device Passwoed: " + objectItem.getDevicePasswoed() + "\n"
                        + "Device Email: " + objectItem.getDeviceEmail() + "\n"
                        + "Device EmailPass: " + objectItem.getDeviceEmailPass();
                String DateNow = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
                String TimeNow = new SimpleDateFormat("hh:mm", Locale.ENGLISH).format(new Date());
                String sender=FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                Message friendlyMessage = new Message(message, sender, null, DateNow, TimeNow, false);
                FirebaseDatabase.getInstance().getReference().child("messages").child(ReciverName).push().setValue(friendlyMessage);
                Data data =new Data( sender, message,"message");
                SendNotification send = new SendNotification(getContext(),ReciverName,data);

            }
        });

        // Set the neutral/cancel button click listener
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
