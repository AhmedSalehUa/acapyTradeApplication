package com.acpay.acapytrade;

import static com.acpay.acapytrade.MainActivity.getAPIHEADER;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.acpay.acapytrade.DatabaseHelper.DatabaseHandler;
import com.acpay.acapytrade.Navigations.Messages.sendNotification.APIService;
import com.acpay.acapytrade.Navigations.Messages.sendNotification.Client;
import com.acpay.acapytrade.Navigations.Messages.sendNotification.Data;
import com.acpay.acapytrade.Navigations.Messages.sendNotification.Token;
import com.acpay.acapytrade.OrderOperations.Order;
import com.acpay.acapytrade.OrderOperations.progress.boxes;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddOrEditeOrderActivity extends AppCompatActivity {
    String actionType;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private Spinner userIdSpinner;
    private String userId = "0";
    private String userName = "";
    private String OrderUserName = "";
    String TokenList = "";
    EditText place, location, date, time, matter, files, notes, fixtrype, dlivercost;
    EditText pickedDate, pickedTime, addProgTex;
    TextView usernames;
    ImageView removeProg, addProg;
    String placeVal, locationVal, dateVal, timeVal, matterVal, filesVal, notesVal, fixtrypeVal, dlivercostVal;
    String Api;
    ListView progList;
    String idUpdated;
    Order updatedOrder;
    List<String> spinnerArray;
    List<String> usersList;
    boolean[] checkedColors;
    APIService apiService;
    Token token;

    DatabaseHandler db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addorder_activity);
        db = new DatabaseHandler(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        Intent data = getIntent();
        idUpdated = data.getStringExtra("id");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("orders");
        place = (EditText) findViewById(R.id.addorder_place);
        location = (EditText) findViewById(R.id.addorder_location);
        date = (EditText) findViewById(R.id.addorder_date);
        time = (EditText) findViewById(R.id.addorder_time);
        matter = (EditText) findViewById(R.id.addorder_matter);
        files = (EditText) findViewById(R.id.addorder_files);
        notes = (EditText) findViewById(R.id.addorder_notes);
        fixtrype = (EditText) findViewById(R.id.addorder_fix);
        dlivercost = (EditText) findViewById(R.id.addorder_dliverCost);
        pickedDate = (EditText) findViewById(R.id.addorder_date);
        pickedTime = (EditText) findViewById(R.id.addorder_time);
        addProgTex = (EditText) findViewById(R.id.addorder_progItem);
        progList = (ListView) findViewById(R.id.listOfProgress);
        usernames = (TextView) findViewById(R.id.usersname);
        addProg = (ImageView) findViewById(R.id.addorder_time_add_prog);
        removeProg = (ImageView) findViewById(R.id.addorder_time_remove_prog);
        if (idUpdated == null) {
            actionType = "add";
            getSupportActionBar().setTitle("Add New Order");
            addActionOnCreate();
        } else {
            actionType = "update";
            getSupportActionBar().setTitle("Edit Order");
            editeActionOnCreate();
        }
        String api = getAPIHEADER(AddOrEditeOrderActivity.this) + "/tokens.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        TokenList = response;
                        Toast.makeText(AddOrEditeOrderActivity.this, "Ready", Toast.LENGTH_LONG).show();
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

        final ImageView datePicker = (ImageView) findViewById(R.id.addorder_date_add);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddOrEditeOrderActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                pickedDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });
        final ImageView timePicker = (ImageView) findViewById(R.id.addorder_time_add);
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddOrEditeOrderActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                pickedTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

    }

    private void editeActionOnCreate() {
        String api = getAPIHEADER(AddOrEditeOrderActivity.this) + "/orders.php?order=" + idUpdated;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String order = response;
                        extractFeuterFromJason(order);
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

    private void addActionOnCreate() {
        final ArrayList<String> proglist = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, proglist);
        progList.setAdapter(arrayAdapter);
        addProg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayAdapter.add(addProgTex.getText().toString());
                addProgTex.setText("");
            }
        });
        progList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                addProgTex.setText(arrayAdapter.getItem(i));
            }
        });
        removeProg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayAdapter.remove(addProgTex.getText().toString());
            }
        });
    }

    private void setupSpinner() {
        spinnerArray = new ArrayList<String>();
        spinnerArray.add("Ahmed");
        spinnerArray.add("Mohamed");
        spinnerArray.add("Remon");
        spinnerArray.add("Barsom");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userIdSpinner.setAdapter(adapter);
        userIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                String[] s = TokenList.split(" ");
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals("Ahmed")) {
                        userId = s[0];
                        userName = "Ahmed";
                        OrderUserName = userId;

                    } else if (selection.equals("Mohamed")) {
                        userId = s[1];
                        userName = "Mohamed";
                        OrderUserName = userId;
                    } else if (selection.equals("Remon")) {
                        userId = s[2];
                        userName = "Remon";
                        OrderUserName = userId;

                    } else if (selection.equals("Barsom")) {
                        userId = s[3];
                        userName = "Barsom";
                        OrderUserName = userId;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                userId = "0"; // Unknown
            }
        });
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

                    if (setUPVariables()) {
                        setUpApi();
                        Log.w("addOrder", Api);
                        RequestQueue queue = Volley.newRequestQueue(this);
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        String res = response;
                                        if (!res.equals("0")) {
                                            String prodApi = getAPIHEADER(AddOrEditeOrderActivity.this) + "/addProgress.php?order=" + res;
                                            ArrayList<String> list = new ArrayList<>();
                                            for (int i = 0; i < progList.getAdapter().getCount(); i++) {
                                                String val = progList.getAdapter().getItem(i).toString();
                                                list.add(val);
                                                prodApi += "&prog[]=" + val;
                                            }
                                            RequestQueue queue = Volley.newRequestQueue(AddOrEditeOrderActivity.this);
                                            StringRequest stringRequest = new StringRequest(Request.Method.GET, prodApi,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            String res = response;
                                                            if (!res.equals("0")) {

                                                                for (int i = 0; i < checkedColors.length; i++) {
                                                                    boolean checked = checkedColors[i];
                                                                    if (checked) {
                                                                        Data data = new Data("new order", "تم اضافة اوردر خاص بك" + placeVal + " - " + locationVal, "order");
                                                                        SendNotification send = new SendNotification(AddOrEditeOrderActivity.this,  usersList.get(i) , data);
                                                                    }
                                                                }
                                                                Toast.makeText(AddOrEditeOrderActivity.this, "Saved", Toast.LENGTH_LONG).show();
                                                                startActivity(new Intent(AddOrEditeOrderActivity.this, MainActivity.class));
                                                            } else {
                                                                Toast.makeText(AddOrEditeOrderActivity.this, "not saved", Toast.LENGTH_LONG).show();
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


                                        } else {
                                            Toast.makeText(AddOrEditeOrderActivity.this, "not saved", Toast.LENGTH_LONG).show();
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
                } else if (actionType.equals("update")) {
                    if (setUPVariables()) {
                        setUpApi();
                        Api += "&orderNum=" + idUpdated;
                        Log.w("addOrder", Api);
                        RequestQueue queue = Volley.newRequestQueue(this);
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api + "&orderNum=" + idUpdated,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        String res = response;
                                        if (!res.equals("0")) {
                                            String prodApi = getAPIHEADER(AddOrEditeOrderActivity.this) + "/addProgress.php?order=" + res;
                                            ArrayList<String> list = new ArrayList<>();
                                            for (int i = 0; i < progList.getAdapter().getCount(); i++) {
                                                String val = progList.getAdapter().getItem(i).toString();
                                                list.add(val);
                                                prodApi += "&prog[]=" + val;
                                            }
                                            RequestQueue queue = Volley.newRequestQueue(AddOrEditeOrderActivity.this);
                                            StringRequest stringRequest = new StringRequest(Request.Method.GET, prodApi,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            String res = response;
                                                            if (!res.equals("0")) {
                                                                Toast.makeText(AddOrEditeOrderActivity.this, "Saved", Toast.LENGTH_LONG).show();

                                                                for (int i = 0; i < checkedColors.length; i++) {
                                                                    boolean checked = checkedColors[i];
                                                                    if (checked) {
                                                                        Data data = new Data("new order", "تم تعديل اوردر خاص بك (" + placeVal + " - " + locationVal + "(", "order");
                                                                        SendNotification send = new SendNotification(AddOrEditeOrderActivity.this,  usersList.get(i) , data);
                                                                    }
                                                                }

                                                                startActivity(new Intent(AddOrEditeOrderActivity.this, MainActivity.class));
                                                            } else {
                                                                Toast.makeText(AddOrEditeOrderActivity.this, "not saved", Toast.LENGTH_LONG).show();
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
                                        } else {
                                            Toast.makeText(AddOrEditeOrderActivity.this, "not saved", Toast.LENGTH_LONG).show();
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
        return super.onOptionsItemSelected(item);
    }

    private void setUpApi() {
        Api = getAPIHEADER(AddOrEditeOrderActivity.this) + "/addOrdersNew.php?"
                + "place=" + placeVal + "&location=" + locationVal
                + "&date=" + dateVal + "&time=" + timeVal
                + "&mater=" + matterVal + "&file=" + filesVal
                + "&notes=" + notesVal + "&fixType=" + fixtrypeVal
                + "&amount=" + dlivercostVal;
        for (int i = 0; i < checkedColors.length; i++) {
            boolean checked = checkedColors[i];
            if (checked) {
                Api += "&username[]=" + usersList.get(i);
                Api += "&uid[]=" + db.getTokens(usersList.get(i));
            }
        }
    }


    private boolean setUPVariables() {
        placeVal = place.getText().toString();
        locationVal = location.getText().toString();
        if (date.getText().toString().length() == 0) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddOrEditeOrderActivity.this);
            alertDialog.setTitle("خطا");
            alertDialog.setMessage("برجاء ادخال التاريخ");
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Toast.makeText(AddOrEditeOrderActivity.this, "canceled", Toast.LENGTH_SHORT).show();
                }
            });
            alertDialog.show();
            Log.e("log", "false");
            return false;
        } else {
            Log.e("log", String.valueOf(date.getText().toString().length()));
            dateVal = date.getText().toString();
        }

        timeVal = time.getText().toString();
        matterVal = matter.getText().toString();
        filesVal = files.getText().toString();
        notesVal = notes.getText().toString();
        fixtrypeVal = fixtrype.getText().toString();
        dlivercostVal = dlivercost.getText().toString();
        return true;
    }

    private void extractFeuterFromJason(String jason) {

        try {
            JSONObject jsonObject = new JSONObject(jason);
            JSONArray sa = jsonObject.names();
            for (int i = 0; i < sa.length(); i++) {
                JSONObject jsonArrayId = jsonObject.getJSONObject(sa.get(i).toString());
                updatedOrder = new Order(jsonArrayId.getString("order_num"),
                        jsonArrayId.getString("date"),
                        jsonArrayId.getString("time"),
                        jsonArrayId.getString("place"),
                        jsonArrayId.getString("location"),
                        jsonArrayId.getString("fixType"),
                        jsonArrayId.getString("num_of_matter"),
                        jsonArrayId.getString("dliverCost"),
                        jsonArrayId.getString("notes"),
                        jsonArrayId.getString("files"),
                        jsonArrayId.getString("username"), new ArrayList<boxes>()
                );
                place.setText(jsonArrayId.getString("place"));
                location.setText(jsonArrayId.getString("location"));
                date.setText(jsonArrayId.getString("date"));
                time.setText(jsonArrayId.getString("time"));
                matter.setText(jsonArrayId.getString("num_of_matter"));
                files.setText(jsonArrayId.getString("files"));
                notes.setText(jsonArrayId.getString("notes"));
                fixtrype.setText(jsonArrayId.getString("fixType"));
                dlivercost.setText(jsonArrayId.getString("dliverCost"));
                extractProgressFromJason();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void extractProgressFromJason() {

        String api = getAPIHEADER(AddOrEditeOrderActivity.this) + "/progress.php?order=" + idUpdated;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String order = response;
                        Log.e("sa", order);
                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddOrEditeOrderActivity.this, android.R.layout.simple_list_item_1, fetchBoxsJason(order));
                        progList.setAdapter(arrayAdapter);
                        addProg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                arrayAdapter.add(addProgTex.getText().toString());
                                addProgTex.setText("");
                            }
                        });
                        progList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                addProgTex.setText(arrayAdapter.getItem(i));
                            }
                        });
                        removeProg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                arrayAdapter.remove(addProgTex.getText().toString());
                            }
                        });
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

    private List<String> fetchBoxsJason(String boxesJasonResponse) {

        final List<String> boxes = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(boxesJasonResponse);
            JSONArray sa = jsonObject.names();
            Log.e("jsonObject", sa.length() + "");
            for (int i = 0; i < sa.length(); i++) {
                JSONObject jsonArrayId = jsonObject.getJSONObject(sa.get(i).toString());
                boxes.add(jsonArrayId.getString("progress_name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return boxes;
    }


//    private String getReciverName(String name) {
//        switch (name) {
//            case "Ahmed":
//                return "Ahmed Saleh";
//            case "Barsom":
//                return "Barsom";
//            case "Remon":
//                return "Remon";
//            case "Mohamed":
//                return "Mohamed Hammad";
//        }
//        return "";
//    }

    public void setUsersList(View view) {
        usernames.setText("");
        matter.setText("");
        AlertDialog.Builder builder = new AlertDialog.Builder(AddOrEditeOrderActivity.this);
        String[] colors = db.getTokens();

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

        // Set the positive/yes button click listener
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                for (int i = 0; i < checkedColors.length; i++) {
                    boolean checked = checkedColors[i];
                    if (checked) {
                        usernames.setText(usernames.getText() + " - " + usersList.get(i));
                        matter.setText(matter.getText() + " - " + usersList.get(i));
                    }
                }
            }
        });

        // Set the negative/no button click listener
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when click the negative button
            }
        });

        // Set the neutral/cancel button click listener
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when click the neutral button
            }
        });

        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();
    }

//    private String getId(String s) {
//        switch (s) {
//            case "Ahmed":
//                return "3grT34bqUdSG4WHVYFRuxDaIm1I3";
//            case "Mohamed":
//                return "aRisjHAS36R5qrvXgAO8fpNIiLE3";
//            case "Remon":
//                return "xHIE6dwSIwQB8rm3geTBzfJdN0r2";
//            case "Barsom":
//                return "poP0PGB5bpSoXYegmkTUOodDqjB2";
//        }
//        return "";
//    }

}
