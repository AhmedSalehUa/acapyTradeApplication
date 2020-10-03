package com.acpay.acapytrade.LeftNavigation.Notes;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.acpay.acapytrade.Networking.JasonReponser;
import com.acpay.acapytrade.R;
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

    AddResponser update;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (actionType.equals("add")) {
                    update = new AddResponser();
                    update.setFinish(false);
                    update.execute("https://www.app.acapy-trade.com/addNotesPlace.php?name=" + name.getText().toString() + "&location=" + location.getText().toString() + "&details=" + details.getText().toString());
                    final Handler handler = new Handler();
                    Runnable runnableCode = new Runnable() {
                        @Override
                        public void run() {
                            if (update.isFinish()) {
                                String res = update.getUserId();
                                if (!res.equals("0")) {
                                    new StyleableToast.Builder(AddPlace.this).text("Saved").iconStart(R.drawable.ic_save).backgroundColor(getResources().getColor(android.R.color.holo_green_light)).show();
                                    onBackPressed();
                                } else {
                                    new StyleableToast.Builder(AddPlace.this).text("Not Saved!").iconStart(R.drawable.falied).backgroundColor(getResources().getColor(android.R.color.holo_red_dark)).show();
                                    onBackPressed();
                                }
                            } else {
                                handler.postDelayed(this, 100);
                            }
                        }
                    };
                    handler.post(runnableCode);
                } else if (actionType.equals("update")) {
                    update = new AddResponser();
                    update.setFinish(false);
                    update.execute("https://www.app.acapy-trade.com/updateNotesPlace.php?id="+idUpdated+"&name=" + name.getText().toString() + "&location=" + location.getText().toString() + "&details=" + details.getText().toString());
                    final Handler handler = new Handler();
                    Runnable runnableCode = new Runnable() {
                        @Override
                        public void run() {
                            if (update.isFinish()) {
                                String res = update.getUserId();
                                if (!res.equals("0")) {
                                    new StyleableToast.Builder(AddPlace.this).text("Saved").iconStart(R.drawable.ic_save).backgroundColor(getResources().getColor(android.R.color.holo_green_light)).show();
                                    onBackPressed();
                                } else {
                                    new StyleableToast.Builder(AddPlace.this).text("Not Saved!").iconStart(R.drawable.falied).backgroundColor(getResources().getColor(android.R.color.holo_red_dark)).show();
                                    onBackPressed();
                                }
                            } else {
                                handler.postDelayed(this, 100);
                            }
                        }
                    };
                    handler.post(runnableCode);
                }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
