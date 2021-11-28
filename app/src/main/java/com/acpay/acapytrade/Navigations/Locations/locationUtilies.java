package com.acpay.acapytrade.Navigations.Locations;

import android.util.Log;

import com.acpay.acapytrade.Networking.HttpsTrustManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class locationUtilies {
    private static final String LOG_TAG = locationUtilies.class.getName();

    public locationUtilies() {
        super();
    }



    public static List<location> extractFeuterFromJason(String jason) {
        final List<location> list = new ArrayList<>();
       try {
           JSONObject jsonObject = new JSONObject(jason);
           JSONArray sa = jsonObject.names();
           for (int i = 0; i < sa.length(); i++) {
               JSONObject jsonArrayId = jsonObject.getJSONObject(sa.get(i).toString());
               location order = new location(jsonArrayId.getString("name"),
                       jsonArrayId.getString("latitude"),
                       jsonArrayId.getString("longlatitude"),
                       jsonArrayId.getString("date")
               );
               list.add(order);
           }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static URL getUrl(String uri) {
        URL url = null;
        try {
            url = new URL(uri);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String getHttpRequest(URL url) throws IOException {
        String jasonRespons = "";
        if (url == null) {
            return jasonRespons;
        }

        HttpsTrustManager.allowAllSSL();
        HttpURLConnection httpURLConnection = null;

        InputStream inputStream = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(45000);
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setRequestMethod("GET");
            HttpsTrustManager.allowAllSSL();
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jasonRespons = getStringFromInpurStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + httpURLConnection.getResponseCode());
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jasonRespons;
    }

    private static String getStringFromInpurStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
