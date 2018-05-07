package com.example.craig.camera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Medications extends AppCompatActivity {

    ListView listview;
    Button Addbutton;
    EditText GetValue;
    String[] Medications = new String[] {//created a sample array to show functionality of the view layout
            "Atorvastatin",
            "Levothyroxine",
            "Lisinopril",
            "Omeprazole",
            "Metformin",
            "Amlodipine",
            "Simvastatin",
            "Losartan",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medications);
//setting up the list view as well as the add button and text input
        listview = (ListView) findViewById(R.id.listView1);
        Addbutton = (Button) findViewById(R.id.button1);
        GetValue = (EditText) findViewById(R.id.editText1);
//add new item to current array list
        final List< String > ListElementsArrayList = new ArrayList < String >
                (Arrays.asList(Medications));


        final ArrayAdapter < String > adapter = new ArrayAdapter < String >
                (Medications.this, android.R.layout.simple_list_item_1,
                        ListElementsArrayList);

        listview.setAdapter(adapter);

        Addbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ListElementsArrayList.add(GetValue.getText().toString());
                adapter.notifyDataSetChanged();
            }
        });
    }
}
