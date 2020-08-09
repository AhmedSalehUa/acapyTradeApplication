package com.acpay.acapytrade.LeftNavigation.ip;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.acpay.acapytrade.R;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IpSearchFragment extends Fragment {
    TextView emptyView;
    TextView urrentIp;
    ListView ipList;
    ProgressBar listProgress;
    Button again;
    NetworkSniffTask task;
    List<String> names;
    ArrayAdapter<String> adapter;

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
        urrentIp = (TextView) rootview.findViewById(R.id.currentIp);
        ipList.setEmptyView(emptyView);
        ipList.setAdapter(adapter);

        task = new NetworkSniffTask();
        task.execute();
        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                urrentIp.setText("");
                task.cancel(true);

                NetworkSniffTask tasek = new NetworkSniffTask();
                tasek.execute();
            }
        });


        return rootview;
    }

    public class NetworkSniffTask extends AsyncTask<Void, NetworkSniffTask.MyTaskParams, List<String>> {
        private static final String TAG = "NetworkSniffTask";
        private WeakReference<Context> mContextRef;

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
                    String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
                    Log.e(TAG, "prefix: " + prefix);
                    list = new ArrayList<>();
                    for (int i = 0; i < 254; i++) {
                        MyTaskParams myTaskParams = new MyTaskParams();
                        String testIp = prefix + String.valueOf(i);
                        myTaskParams.setProg(i);
                        InetAddress name = InetAddress.getByName(testIp);
                        String hostName = name.getCanonicalHostName();
                        if (name.isReachable(1000)) {
                            list.add(hostName);
                            myTaskParams.setValue(hostName);
                            publishProgress(myTaskParams);
                            Log.e(TAG, "Host:" + hostName);
                        } else {
                            publishProgress(myTaskParams);
                        }
                    }

                }else {
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
            if (values[0].getValue() != null) {
                names.add(values[0].getValue());
                adapter.add(values[0].getValue());
                adapter.notifyDataSetChanged();
            }
            urrentIp.setText(values[0].getProg() + "");

        }

        @Override
        protected void onPostExecute(List aVoid) {
            super.onPostExecute(aVoid);
            listProgress.setVisibility(View.GONE);
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
