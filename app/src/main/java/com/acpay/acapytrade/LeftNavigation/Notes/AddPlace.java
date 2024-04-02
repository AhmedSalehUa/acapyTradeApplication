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

public class AddPlace extends AppCompatActivity {
    public AddPlace() {
        super();
    }

    String actionType;
    EditText name, location, details;
    String idUpdated;
    Intent data;
    NotesPlaces notesPlaces;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_addplace);
        data = getIntent();
        idUpdated = data.getStringExtra("id");
        name = (EditText) findViewById(R.id.addPlace_name);
        location = (EditText) findViewById(R.id.addPlace_location);
        details = (EditText) findViewById(R.id.addPlace_details);
        if (idUpdated == null) {
            actionType = "add";
            getSupportActionBar().setTitle("Add New Place");
        } else {
            actionType = "update";
            getSupportActionBar().setTitle("Edit Place");
            name.setText(data.getStringExtra("name"));
            location.setText(data.getStringExtra("location"));
            details.setText(data.getStringExtra("details"));
        }
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
                if (actionType.equals("add")) {
                    String api = getAPIHEADER(AddPlace.this) + "/addNotesPlace.php?name=" + name.getText().toString() + "&location=" + location.getText().toString() + "&details=" + details.getText().toString();
                    RequestQueue queue = Volley.newRequestQueue(AddPlace.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    String res = response;
                                    if (!res.equals("0")) {
                                        new StyleableToast.Builder(AddPlace.this).text("Saved").iconStart(R.drawable.ic_save).backgroundColor(getResources().getColor(android.R.color.holo_green_light)).show();
                                        onBackPressed();
                                    } else {
                                        new StyleableToast.Builder(AddPlace.this).text("Not Saved!").iconStart(R.drawable.falied).backgroundColor(getResources().getColor(android.R.color.holo_red_dark)).show();
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

                } else if (actionType.equals("update")) {

                    String api = getAPIHEADER(AddPlace.this) + "/updateNotesPlace.php?id=" + idUpdated + "&name=" + name.getText().toString() + "&location=" + location.getText().toString() + "&details=" + details.getText().toString();
                    RequestQueue queue = Volley.newRequestQueue(AddPlace.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    String res = response;
                                    if (!res.equals("0")) {
                                        new StyleableToast.Builder(AddPlace.this).text("Saved").iconStart(R.drawable.ic_save).backgroundColor(getResources().getColor(android.R.color.holo_green_light)).show();
                                        onBackPressed();
                                    } else {
                                        new StyleableToast.Builder(AddPlace.this).text("Not Saved!").iconStart(R.drawable.falied).backgroundColor(getResources().getColor(android.R.color.holo_red_dark)).show();
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
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
