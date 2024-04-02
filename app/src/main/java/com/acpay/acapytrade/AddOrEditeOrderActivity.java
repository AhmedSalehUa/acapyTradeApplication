package com.acpay.acapytrade;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.acpay.acapytrade.Networking.JasonReponser;
import com.acpay.acapytrade.Order.Order;
import com.acpay.acapytrade.Order.orderReponser;
import com.acpay.acapytrade.Order.progressReponser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddOrEditeOrderActivity extends AppCompatActivity {
    String actionType;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private Spinner userIdSpinner;
    private String userId = "0";
    private String userName = "";
    String TokenList = "";
    EditText place, location, date, time, matter, files, notes, fixtrype, dlivercost;
    EditText pickedDate, pickedTime, addProgTex;
    ImageView removeProg, addProg;
    String placeVal, locationVal, dateVal, timeVal, matterVal, filesVal, notesVal, fixtrypeVal, dlivercostVal;
    String Api;
    ListView progList;
    String idUpdated;
    Order updatedOrder;
    List<String> spinnerArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addorder_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Intent data = getIntent();
        idUpdated = data.getStringExtra("id");

        userIdSpinner = (Spinner) findViewById(R.id.spinner_gender);
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
        String api = "https://www.app.acapy-trade.com/tokens.php";
        final JasonReponser update = new JasonReponser();
        update.setFinish(false);
        update.execute(api);
        final Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                if (update.isFinish()) {
                    TokenList = update.getUserId();
                    Log.w(" ", TokenList);
                    setupSpinner();
                    Toast.makeText(AddOrEditeOrderActivity.this, "Ready", Toast.LENGTH_LONG).show();
                } else {
                    handler.postDelayed(this, 100);
                }
            }
        };
        handler.post(runnableCode);
        pickedDate.setEnabled(false);
        pickedTime.setEnabled(false);
        final ImageView datePicker = (ImageView) findViewById(R.id.addorder_date_add);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                long today = c.getTimeInMillis();
                final long oneDay = 24 * 60 * 60 * 1000L;
                Date previousDays = new Date(today - 1000);
                Date nextMonth = new Date(today + 30 * oneDay);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddOrEditeOrderActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                pickedDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(today);
                datePickerDialog.getDatePicker().setMaxDate(today * oneDay * 3);
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
        String api = "https://www.app.acapy-trade.com/orders.php?order=" + idUpdated;
        final orderReponser update = new orderReponser();
        update.setFinish(false);
        update.execute(api);
        final Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                if (update.isFinish()) {
                    String order = update.getUserId();
                    extractFeuterFromJason(order);

                } else {
                    handler.postDelayed(this, 100);
                }
            }
        };
        handler.post(runnableCode);

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
        spinnerArray.add("George");

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
                        Log.w("Ahmed", userId);
                    } else if (selection.equals("Mohamed")) {
                        userId = s[1];
                        userName = "Mohamed";
                        Log.w("Mohamed", userId);
                    } else if (selection.equals("Remon")) {
                        userId = s[2];
                        userName = "Remon";
                        Log.w("Remon", userId);
                    } else if (selection.equals("George")) {
                        userId = s[3];
                        userName = "George";
                        Log.w("George", userId);
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

                    setUPVariables();
                    setUpApi();
                    Log.w("addOrder", Api);
                    final JasonReponser update = new JasonReponser();
                    update.setFinish(false);
                    update.execute(Api);
                    final Handler handler = new Handler();
                    Runnable runnableCode = new Runnable() {
                        @Override
                        public void run() {
                            if (update.isFinish()) {
                                String res = update.getUserId();
                                if (!res.equals("0")) {
                                    String prodApi = "https://www.app.acapy-trade.com/addProgress.php?order=" + res;
                                    ArrayList<String> list = new ArrayList<>();
                                    for (int i = 0; i < progList.getAdapter().getCount(); i++) {
                                        String val = progList.getAdapter().getItem(i).toString();
                                        list.add(val);
                                        prodApi += "&prog[]=" + val;
                                    }
                                    final JasonReponser updateProgress = new JasonReponser();
                                    updateProgress.setFinish(false);
                                    updateProgress.execute(prodApi);
                                    final Handler handlerProg = new Handler();
                                    Runnable runnableCodeProg = new Runnable() {
                                        @Override
                                        public void run() {
                                            if (updateProgress.isFinish()) {
                                                String res = updateProgress.getUserId();
                                                if (!res.equals("0")) {
                                                    Toast.makeText(AddOrEditeOrderActivity.this, "Saved", Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(AddOrEditeOrderActivity.this, MainActivity.class));
                                                } else {
                                                    Toast.makeText(AddOrEditeOrderActivity.this, "not saved", Toast.LENGTH_LONG).show();
                                                }
                                            } else {
                                                handlerProg.postDelayed(this, 100);
                                            }
                                        }
                                    };
                                    handlerProg.post(runnableCodeProg);

                                } else {
                                    Toast.makeText(AddOrEditeOrderActivity.this, "not saved", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                handler.postDelayed(this, 100);
                            }
                        }
                    };
                    handler.post(runnableCode);
                } else if (actionType.equals("update")) {
                    setUPVariables();
                    setUpApi();
                    Api+="&orderNum="+idUpdated;
                    Log.w("addOrder", Api);
                    final JasonReponser update = new JasonReponser();
                    update.setFinish(false);
                    update.execute(Api+"&orderNum="+idUpdated);
                    final Handler handler = new Handler();
                    Runnable runnableCode = new Runnable() {
                        @Override
                        public void run() {
                            if (update.isFinish()) {
                                String res = update.getUserId();
                                if (!res.equals("0")) {
                                    String prodApi = "https://www.app.acapy-trade.com/addProgress.php?order=" + res;
                                    ArrayList<String> list = new ArrayList<>();
                                    for (int i = 0; i < progList.getAdapter().getCount(); i++) {
                                        String val = progList.getAdapter().getItem(i).toString();
                                        list.add(val);
                                        prodApi += "&prog[]=" + val;
                                    }
                                    final JasonReponser updateProgress = new JasonReponser();
                                    updateProgress.setFinish(false);
                                    updateProgress.execute(prodApi);
                                    final Handler handlerProg = new Handler();
                                    Runnable runnableCodeProg = new Runnable() {
                                        @Override
                                        public void run() {
                                            if (updateProgress.isFinish()) {
                                                String res = updateProgress.getUserId();
                                                if (!res.equals("0")) {
                                                    Toast.makeText(AddOrEditeOrderActivity.this, "Saved", Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(AddOrEditeOrderActivity.this, MainActivity.class));
                                                } else {
                                                    Toast.makeText(AddOrEditeOrderActivity.this, "not saved", Toast.LENGTH_LONG).show();
                                                }
                                            } else {
                                                handlerProg.postDelayed(this, 100);
                                            }
                                        }
                                    };
                                    handlerProg.post(runnableCodeProg);

                                } else {
                                    Toast.makeText(AddOrEditeOrderActivity.this, "not saved", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                handler.postDelayed(this, 100);
                            }
                        }
                    };
                    handler.post(runnableCode);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpApi() {
        Api = "https://www.app.acapy-trade.com/addOrders.php?uid=" + userId
                + "&place=" + placeVal + "&location=" + locationVal
                + "&date=" + dateVal + "&time=" + timeVal
                + "&mater=" + matterVal + "&file=" + filesVal
                + "&notes=" + notesVal + "&fixType=" + fixtrypeVal
                + "&amount=" + dlivercostVal + "&username=" + userName;
    }

    private void setUPVariables() {
        placeVal = place.getText().toString();
        locationVal = location.getText().toString();
        dateVal = date.getText().toString();
        timeVal = time.getText().toString();
        matterVal = matter.getText().toString();
        filesVal = files.getText().toString();
        notesVal = notes.getText().toString();
        fixtrypeVal = fixtrype.getText().toString();
        dlivercostVal = dlivercost.getText().toString();
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
                        jsonArrayId.getString("files"), jsonArrayId.getString("username")
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

        String api = "https://www.app.acapy-trade.com/progress.php?order=" + idUpdated;
        final progressReponser update = new progressReponser();
        update.setFinish(false);
        update.execute(api);
        final Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                if (update.isFinish()) {
                    String order = update.getUserId();
                    Log.e("sa",order);
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
                } else {
                    handler.postDelayed(this, 100);
                }
            }
        };
        handler.post(runnableCode);
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
}
