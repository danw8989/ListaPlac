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
        db.execSQL("CREATE TABLE IF NOT EXISTS pracownicy (id INTEGER PRIMARY KEY AUTOINCREMENT, imie TEXT, nazwisko TEXT)");
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
        long result = db.insert("pracownicy", null, data);
        return result == -1 ? false : true;
    }

    public void ClearDB()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM pracownicy");
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='pracownicy';");
    }

    public LinkedList<String> getData(String sql)
    {
        SQLiteDatabase db = this.getWritableDatabase();
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