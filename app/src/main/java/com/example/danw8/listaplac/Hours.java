package com.example.danw8.listaplac;

import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

public class Hours extends AppCompatActivity {
    private int ID;
    private DatabaseHelper myDB;
    private LinkedList<String> data;
    private Calendar currDate;

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

        final CalendarView calendarView = (CalendarView) findViewById(R.id.calendarViewHours);
        final TextView textView = (TextView) findViewById(R.id.textViewHours);
        final EditText editText = (EditText) findViewById(R.id.editTextHours);
        Button button = (Button) findViewById(R.id.buttonAddChangeHours);
        Button payoutButton = (Button) findViewById(R.id.payoutButton);
        currDate = Calendar.getInstance();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                currDate.set(year,month,dayOfMonth);
                data =  myDB.getData("SELECT data, godziny From place Where pracownik_id = " +ID + " and data = '" + new SimpleDateFormat("MM/dd/yyyy").format(currDate.getTime()) + "'");
                if (data.size() != 0) {
                    int godziny = Integer.parseInt(data.get(0).split(" ")[1]);
                    textView.setText("Pracował " + godziny + " godzin");
                }
                else
                    textView.setText("Nie Pracował");

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(editText.getText().toString())) {
                    data = myDB.getData("SELECT data From place Where pracownik_id = " + ID + " and data = '" + new SimpleDateFormat("MM/dd/yyyy").format(currDate.getTime()) + "'");
                    if (data.size() == 0)
                        myDB.insertHours(ID, new SimpleDateFormat("MM/dd/yyyy").format(currDate.getTime()), Integer.parseInt(editText.getText().toString()));
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Hours.this);
                        builder.setTitle("Chcesz nadpisać godziny?");
                        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                myDB.execSql("DELETE FROM place WHERE data = '" + new SimpleDateFormat("MM/dd/yyyy").format(currDate.getTime()) + "'");
                                myDB.insertHours(ID, new SimpleDateFormat("MM/dd/yyyy").format(currDate.getTime()), Integer.parseInt(editText.getText().toString()));
                            }
                        });

                        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
                else
                    editText.setError("Musisz najpierw wpisać godziny!");
                    //MsgBox("ok");
            }
        });

        final float stawka = myDB.getSingleFloat("SELECT stawka FROM pracownicy WHERE id = " + ID);

        payoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Hours.this);
                builder.setTitle("Wypłata!");
                int godziny = 0;

                String date = new SimpleDateFormat("MM/yyyy").format(currDate.getTime());
                date = new StringBuilder(date).insert(3, "%/").toString();
                data = myDB.getData("SELECT godziny From place Where pracownik_id = " + ID + " and data LIKE '" + date + "'");

                for (String i :data) {
                    godziny+= Integer.parseInt(i.split(" ")[0]);
                }
                //float payout =
                builder.setMessage(Hours.this.getTitle() + " zarobił " + String.format("%1$.2f zł", godziny*stawka));
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



        if (stawka == 0.0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(Hours.this);
            builder.setTitle("Podaj stawkę godzinową dla nowego pracownika");
            final EditText input = new EditText(Hours.this);
            input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            builder.setView(input);
            builder.setPositiveButton("Ustaw", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    float tmp = Float.parseFloat(input.getText().toString());
                    myDB.execSql("UPDATE pracownicy SET stawka = " + tmp + " WHERE id = " + ID);
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }
    public void MsgBox(String msg)
    {

    }
}
