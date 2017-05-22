package com.example.danw8.listaplac;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.LinkedList;

public class Hours extends AppCompatActivity {
    private int ID;
    private DatabaseHelper myDB;
    private LinkedList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            ID = extras.getInt("ID");
        setContentView(R.layout.activity_hours);
        myDB = new DatabaseHelper(this);
        data = myDB.getData("SELECT imie,nazwisko FROM pracownicy WHERE id = " + ID);
        this.setTitle(data.get(0));

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarViewHours);
        TextView textView = (TextView) findViewById(R.id.textViewHours);
        EditText editText = (EditText) findViewById(R.id.editTextHours);
        Button button = (Button) findViewById(R.id.buttonAddChangeHours);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

            }
        });
    }
}
