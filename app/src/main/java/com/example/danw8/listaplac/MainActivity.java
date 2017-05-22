package com.example.danw8.listaplac;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ListView myList;
    private LinkedList<String> arr;
    private ArrayAdapter<String> adapter;
    private AlertDialog alertDialog;

    private DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDB = new DatabaseHelper(this);

        Button connButton = (Button) findViewById(R.id.connectButton);

        // Budowanie okienka dialogowego dodającego pracownika;
        connButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                final View nView = getLayoutInflater().inflate(R.layout.dialog_workerdata, null);
                final EditText mName = (EditText) nView.findViewById(R.id.dialog_name);
                final EditText mSurname = (EditText) nView.findViewById(R.id.dialog_surname);
                Button mDialogAddButton = (Button) nView.findViewById(R.id.dialogAddButton);

                mDialogAddButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!(mName.getText().toString().isEmpty()) && !(mSurname.getText().toString().isEmpty())) {
                            myDB.insertWorker(mName.getText().toString(), mSurname.getText().toString());
                            Toast.makeText(MainActivity.this, "Dodano!", Toast.LENGTH_SHORT).show();
                            UpdateList();
                            alertDialog.dismiss();

                        } else
                            Toast.makeText(MainActivity.this, "Uzupełnij dane", Toast.LENGTH_SHORT).show();
                    }
                });
                mBuilder.setView(nView);
                alertDialog = mBuilder.create();
                alertDialog.show();

            }
        });

        arr = myDB.getData("SELECT * FROM pracownicy");
        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, arr);
        myList = (ListView) findViewById(R.id.mobile_list);
        myList.setAdapter(adapter);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getBaseContext(), Hours.class);
                i.putExtra("ID", position+1);
                startActivity(i);
            }
        });
        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
                // TODO: Usuwanie/Edycja Pracownika/ContextMenu.
            }
        });
    }

    private void UpdateList() {
        arr = myDB.getData("SELECT * FROM pracownicy");
        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, arr);
        myList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear) {
            myDB.ClearDB();
            UpdateList();
        }

        return super.onOptionsItemSelected(item);
    }


}
