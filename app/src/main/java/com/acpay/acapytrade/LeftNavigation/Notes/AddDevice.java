package com.acpay.acapytrade.LeftNavigation.Notes;

import static com.acpay.acapytrade.MainActivity.getAPIHEADER;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.acpay.acapytrade.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.muddzdev.styleabletoast.StyleableToast;

public class AddDevice extends AppCompatActivity {
    public AddDevice() {
        super();
    }

    String id;
    EditText name, type, model, details, ip, port, username, password, email, emailpass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_adddevice);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        name = (EditText) findViewById(R.id.deivceName);
        type = (EditText) findViewById(R.id.deivceType);
        model = (EditText) findViewById(R.id.deivceModel);
        details = (EditText) findViewById(R.id.deivceDetails);
        ip = (EditText) findViewById(R.id.deivceIp);
        port = (EditText) findViewById(R.id.deivcePort);
        username = (EditText) findViewById(R.id.deivceUsername);
        password = (EditText) findViewById(R.id.deivcePassword);
        email = (EditText) findViewById(R.id.deivceEmail);
        emailpass = (EditText) findViewById(R.id.deivceEmailPassword);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                RequestQueue queue = Volley.newRequestQueue(AddDevice.this);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, getApi(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                String res =response;
                                if (!res.equals("0")) {
                                    new StyleableToast.Builder(AddDevice.this).text("Saved").iconStart(R.drawable.ic_save).backgroundColor(getResources().getColor(android.R.color.holo_green_light)).show();
                                    onBackPressed();
                                } else {
                                    new StyleableToast.Builder(AddDevice.this).text("Not Saved!").iconStart(R.drawable.falied).backgroundColor(getResources().getColor(android.R.color.holo_red_dark)).show();
                                    onBackPressed();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("onResponse", error.toString());
                    }
                });
                stringRequest.setShouldCache(false);
                stringRequest.setShouldRetryConnectionErrors(true);
                stringRequest.setShouldRetryServerErrors(true);
                queue.add(stringRequest);

        }
        return true;
    }

    private String getApi() {
        String Api = getAPIHEADER(AddDevice.this)+"/addNotesDetails.php?id=" + id
                + "&name=" + name.getText().toString() + "&type=" + type.getText().toString()
                + "&model=" + model.getText().toString() + "&details=" + details.getText().toString()
                + "&ip=" + ip.getText().toString() + "&username=" + username.getText().toString()
                + "&port=" + port.getText().toString() + "&email=" + email.getText().toString()
                + "&demailpass=" + emailpass.getText().toString() + "&password=" + password.getText().toString();
        return Api;
    }
}
