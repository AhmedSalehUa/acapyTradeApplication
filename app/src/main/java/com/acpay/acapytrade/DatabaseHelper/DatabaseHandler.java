package com.acpay.acapytrade.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.acpay.acapytrade.LeftNavigation.Transitions.Names.TransitionsName;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "acapy";

    private static final String MAINTAINCE_TABLE = "Maintaince";
    private static final String MAINTAINCE_ID = "id";
    private static final String MAINTAINCE_NAME = "name";
    private static final String MAINTAINCE_TOKEN = "token";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String Create_Users_Data = "CREATE TABLE " + MAINTAINCE_TABLE + " (" +
                MAINTAINCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MAINTAINCE_NAME + " TEXT," +
                MAINTAINCE_TOKEN + " TEXT" +
                ")";
        db.execSQL(Create_Users_Data);

        ContentValues Ahmed = new ContentValues();
        Ahmed.put(MAINTAINCE_NAME, "Ahmed Saleh");
        Ahmed.put(MAINTAINCE_TOKEN, "3grT34bqUdSG4WHVYFRuxDaIm1I3");
        db.insert(MAINTAINCE_TABLE, null, Ahmed);

        ContentValues Mohamed = new ContentValues();
        Mohamed.put(MAINTAINCE_NAME, "Mohamed Hammad");
        Mohamed.put(MAINTAINCE_TOKEN, "aRisjHAS36R5qrvXgAO8fpNIiLE3");
        db.insert(MAINTAINCE_TABLE, null, Mohamed);

        ContentValues Remon = new ContentValues();
        Remon.put(MAINTAINCE_NAME, "Remon");
        Remon.put(MAINTAINCE_TOKEN, "xHIE6dwSIwQB8rm3geTBzfJdN0r2");
        db.insert(MAINTAINCE_TABLE, null, Remon);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + MAINTAINCE_TABLE);
        onCreate(db);
    }

    public List<TransitionsName> getNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + MAINTAINCE_TABLE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<TransitionsName> list = new ArrayList<TransitionsName>();
        if (cursor.moveToFirst()) {
            do {
                TransitionsName contact = new TransitionsName();
                contact.setName(cursor.getString(1));
                list.add(contact);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public String[] getTokens() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + MAINTAINCE_TABLE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        String list = "";
        String separator = ",";
        if (cursor.moveToFirst()) {
            do {

                list += cursor.getString(1) + separator;

            } while (cursor.moveToNext());
        }
        String[] split = list.split(separator);
        return split;
    }

    public String getTokens(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  " + MAINTAINCE_TOKEN + " FROM " + MAINTAINCE_TABLE + " WHERE " + MAINTAINCE_NAME + "= '" + name + "'";
        Cursor cursor = db.query(MAINTAINCE_TABLE, new String[]{MAINTAINCE_TOKEN}, MAINTAINCE_NAME + " = '" + name + "'", null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor.getString(0);
    }

    public void addUser(String name, String token) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues user = new ContentValues();
        user.put(MAINTAINCE_NAME, name);
        user.put(MAINTAINCE_TOKEN, token);
        db.insert(MAINTAINCE_TABLE, null, user);
    }

    public void deleteUser(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MAINTAINCE_TABLE,MAINTAINCE_NAME +"=?",new String[]{name});

    }
}
