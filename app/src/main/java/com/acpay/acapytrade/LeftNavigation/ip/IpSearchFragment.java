package com.acpay.acapytrade.LeftNavigation.ip;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.acpay.acapytrade.MainActivity;
import com.acpay.acapytrade.R;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class IpSearchFragment extends Fragment {
    TextView emptyView;
    ListView ipList;
    ProgressBar listProgress;
    Button again, preFixChange;
    NetworkSniffTask task;
    List<String> names;
    ArrayAdapter<String> adapter;
    String prefix = "192.168.1.";

    public IpSearchFragment() {
        super();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.ipsearch_activity, container, false);
        ipList = (ListView) rootview.findViewById(R.id.ipList);
        adapter = new ipAdapter(getContext(), new ArrayList<String>());
        listProgress = (ProgressBar) rootview.findViewById(R.id.ipListProgress);
        emptyView = (TextView) rootview.findViewById(R.id.ipListEmpty);
        again = (Button) rootview.findViewById(R.id.searchAgain);
        preFixChange = (Button) rootview.findViewById(R.id.pre);

        ipList.setEmptyView(emptyView);
        ipList.setAdapter(adapter);
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                task = new NetworkSniffTask();
                task.execute();
            } else {
                listProgress.setVisibility(View.GONE);
                emptyView.setText("you connect from mobile data");
            }
        }

        preFixChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("تغير الip");
                LinearLayout container = new LinearLayout(getContext());
                container.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(20, 20, 20, 20);
                final EditText input = new EditText(getContext());
                input.setLayoutParams(lp);
                input.setGravity(Gravity.TOP | Gravity.LEFT);
                input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                input.setLines(1);
                input.setMaxLines(1);
                input.setText(prefix);
                container.addView(input, lp);

                alertDialog.setView(container);
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        prefix = input.getText().toString();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(getContext(), "canceled", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });
        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                if (task!=null)
                task.cancel(true);
                ConnectivityManager cm =
                        (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                if (isConnected) {
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        listProgress.setVisibility(View.VISIBLE);
                        task = new NetworkSniffTask();
                        task.execute();
                    } else {
                        emptyView.setText("you connect from mobile data");
                    }
                }
            }
        });
        ipList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String ip = adapter.getItem(i);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + ip));
                startActivity(browserIntent);
            }
        });
        setHasOptionsMenu(true);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("IP Search");
        return rootview;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    public class NetworkSniffTask extends AsyncTask<Void, NetworkSniffTask.MyTaskParams, List<String>> {
        private static final String TAG = "NetworkSniffTask";
        private WeakReference<Context> mContextRef;
        String ip;
        List<String> list;

        public NetworkSniffTask() {
            mContextRef = new WeakReference<>(getContext());

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            this.cancel(true);
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            Log.e(TAG, "Let's sniff the network");
            try {
                Context context = mContextRef.get();
                if (context != null) {

                    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo connectionInfo = wm.getConnectionInfo();
                    int ipAddress = connectionInfo.getIpAddress();
                    String ipString = Formatter.formatIpAddress(ipAddress);

                    Log.e(TAG, "activeNetwork: " + String.valueOf(activeNetwork));
                    Log.e(TAG, "ipString: " + String.valueOf(ipString));
                    Log.e(TAG, "prefix: " + prefix);
                    list = new ArrayList<>();
                    for (int i = 0; i < 254; i++) {
                        ip = String.valueOf(i);
                        if (isCancelled()) {
                            break;
                        }
                        final int finalI = i;
                        new Thread(new Runnable() {
                            public void run() {
                                MyTaskParams myTaskParams = new MyTaskParams();
                                String testIp = prefix + finalI;
                                myTaskParams.setProg(finalI);
                                InetAddress name = null;
                                try {
                                    name = InetAddress.getByName(testIp);
                                } catch (UnknownHostException e) {
                                    e.printStackTrace();
                                }
                                String hostName = name.getCanonicalHostName();
                                String hoName = name.getHostAddress();
                                try {
                                    if (name.isReachable(3000)) {
                                        list.add(hoName);
                                        myTaskParams.setValue(hostName);
                                        publishProgress(myTaskParams);
                                        Log.e(TAG, "Host:" + hostName);
                                    } else {
                                        publishProgress(myTaskParams);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }

                } else {
                    Log.e(TAG, "null");
                }
            } catch (Throwable t) {
                Log.e(TAG, "Well that's not good.", t);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            names = new ArrayList<String>();
            adapter.clear();
            ipList.setAdapter(adapter);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(MyTaskParams... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(List aVoid) {
            super.onPostExecute(aVoid);
            listProgress.setVisibility(View.GONE);
            emptyView.setText("no ip found");
            adapter.clear();
            adapter.addAll(list);
            adapter.notifyDataSetChanged();
            //  adapter.addAll(list);
        }

        private class MyTaskParams {
            int prog;
            String value;

            public int getProg() {
                return prog;
            }

            public void setProg(int prog) {
                this.prog = prog;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            MyTaskParams() {

            }
        }

    }
}
