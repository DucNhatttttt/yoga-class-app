package com.example.yoga_class_app_coursework.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yoga_class_app_coursework.ClassAdapter;
import com.example.yoga_class_app_coursework.MyDatabaseHelper;
import com.example.yoga_class_app_coursework.R;
import com.example.yoga_class_app_coursework.models.Class;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ClassActivity extends AppCompatActivity {
    FloatingActionButton add_fab;
    MyDatabaseHelper myDB;
    RecyclerView cl_rv;
    SearchView name_class_sv;
    ClassAdapter classAdapter;
    ArrayList<Class> classes;
    Button back_btn;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        add_fab = findViewById(R.id.add_fab);
        cl_rv = findViewById(R.id.cl_rv);
        back_btn = findViewById(R.id.back_ob_btn);
        name_class_sv = findViewById(R.id.name_class_sv);
        back_btn.setOnClickListener(view -> finish());


        myDB = new MyDatabaseHelper(this);
        classes = new ArrayList<>();


        storeDataInArrays();
        getAndSetIntentData();
        ArrayList<Class> classList = new ArrayList<>();
        for (Class cl : classes){
            if (cl.getCourse_id().matches(id)){
                classList.add(cl);
            }else {
                ArrayList<Class> emptyList = new ArrayList<>();
                classes = emptyList;
            }
        }
        classes = classList;

        classAdapter = new ClassAdapter(this, classes);
        //getAndSetIntentData();
        cl_rv.setAdapter(classAdapter);
        cl_rv.setLayoutManager(new LinearLayoutManager(this));

        name_class_sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                classAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                classAdapter.getFilter().filter(newText);
                return false;
            }
        });

        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassActivity.this, AddClassActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });




    }


    void getAndSetIntentData() {
        if (getIntent().hasExtra("id")) {

            // Geting Data from Intent
            id = getIntent().getStringExtra("id");


        }else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    private void storeDataInArrays() {
        Cursor cursor = myDB.readAllDataClass();


        if (cursor.getCount() == 0) {
            Toast.makeText(this, "NO DATA", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String teacher = cursor.getString(1);
                String date = cursor.getString(2);
                String comment = cursor.getString(3);
                String course_id = cursor.getString(4);
                Class aClass = new Class(id, teacher, date, comment, course_id);
                classes.add(aClass);
            }
        }
    }

}