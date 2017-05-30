package com.example.danw8.listaplac;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;

/**
 * Created by danw8 on 12.03.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String databaseName = "data.db";
    //public static final String pracownicy
    public DatabaseHelper(Context context) {
        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS pracownicy (id INTEGER PRIMARY KEY AUTOINCREMENT, imie TEXT, nazwisko TEXT, stawka REAL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS place (data TEXT, godziny INT, pracownik_id INT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.onCreate(db);
    }

    public boolean insertWorker(String imie, String nazwisko)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put("imie", imie);
        data.put("nazwisko", nazwisko);
        data.put("stawka", 0);
        long result = db.insert("pracownicy", null, data);
        return result == -1 ? false : true;
    }

    public boolean insertHours(int id, String dateTime, int godziny)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put("data", dateTime);
        data.put("godziny", godziny);
        data.put("pracownik_id", id);
        long result = db.insert("place", null, data);
        return result == -1 ? false : true;
    }

    public void ClearDB()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM pracownicy");
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='pracownicy';");
        db.execSQL("DELETE FROM place");
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='place';");
    }

    public void execSql(String sql)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
    }

    public int getSingleInt(String sql)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(sql, null);
        int tmp = 0;
        if (res.getCount() != 0)
            while (res.moveToNext()) {
                tmp = res.getInt(0);
            }
        res.close();
        return tmp;
    }

    public float getSingleFloat(String sql)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(sql, null);
        float tmp = 0;
        if (res.getCount() != 0) {
            res.moveToFirst();
            tmp = res.getFloat(0);
        }
        res.close();
        return tmp;
    }


    public LinkedList<String> getData(String sql)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(sql, null);
        LinkedList<String> list = new LinkedList<String>();
        if (res.getCount() != 0)
            while (res.moveToNext()) {
                String tmp = "";
                for (int i = 0; i < res.getColumnCount(); i++)
                    tmp = tmp + res.getString(i) + " ";
                list.add(tmp);
            }
        res.close();
        return list;
    }



}
