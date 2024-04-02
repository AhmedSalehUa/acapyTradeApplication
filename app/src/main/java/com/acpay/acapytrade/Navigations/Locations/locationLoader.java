package com.acpay.acapytrade.Navigations.Locations;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.acpay.acapytrade.Order.OrderLoader;

import java.util.List;

public class locationLoader extends AsyncTaskLoader<List<location>> {

    private static final String LOG_TAG = OrderLoader.class.getName();

    /** Query URL */
    private String mUrl;

    public locationLoader(Context context, String url) {
        super(context);
        mUrl = url;
        Log.w("from loader " , mUrl);
    }
    @Nullable
    @Override
    public List<location> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        Log.w("network",mUrl);
        List<location> locations = locationUtilies.fetchData(mUrl);
        return locations;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
