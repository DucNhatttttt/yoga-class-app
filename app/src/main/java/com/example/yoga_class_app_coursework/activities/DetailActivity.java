package com.example.yoga_class_app_coursework.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yoga_class_app_coursework.R;

public class DetailActivity extends AppCompatActivity {
    String id, dayOfWeek, timeOfCourse, typeOfClass, description;
    int capacity, duration;
    float pricePerClass;
    Button back_btn;
    TextView timeoc_d_content, capacity_d_content, duration_d_content, ppc_d_content, dow_d_content,
            typeoc_d_content, des_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        timeoc_d_content = findViewById(R.id.timeoc_d_content);
        capacity_d_content = findViewById(R.id.capacity_d_content);
        duration_d_content = findViewById(R.id.duration_d_content);
        ppc_d_content = findViewById(R.id.ppc_d_content);
        dow_d_content = findViewById(R.id.dow_d_content);
        typeoc_d_content = findViewById(R.id.typeoc_d_content);
        des_content = findViewById(R.id.des_d_content);
        back_btn = findViewById(R.id.back_ob_btn);

        getAndSetIntentData();

        back_btn.setOnClickListener(view -> finish());



    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("dayOfWeek") && getIntent().hasExtra("timeOfCourse")
                && getIntent().hasExtra("capacity") && getIntent().hasExtra("duration") && getIntent().hasExtra("pricePerClass")
                && getIntent().hasExtra("typeOfClass") && getIntent().hasExtra("description")) {
            // Geting Data from Intent
            id = getIntent().getStringExtra("id");
            dayOfWeek = getIntent().getStringExtra("dayOfWeek");
            timeOfCourse = getIntent().getStringExtra("timeOfCourse");
            capacity = getIntent().getIntExtra("capacity", 0);
            duration = getIntent().getIntExtra("duration", 0);
            pricePerClass = getIntent().getFloatExtra("pricePerClass", 0);
            typeOfClass = getIntent().getStringExtra("typeOfClass");
            description = getIntent().getStringExtra("description");
            // Setting Intent Data
            timeoc_d_content.setText(timeOfCourse);
            capacity_d_content.setText(capacity+"");
            duration_d_content.setText(duration+"");
            ppc_d_content.setText(pricePerClass+"");
            des_content.setText(description);
            if (typeOfClass.matches("Flow Yoga")) {
                typeoc_d_content.setText("Flow Yoga");
            } else if (typeOfClass.matches("Aerial Yoga")) {
                typeoc_d_content.setText("Aerial Yoga");
            } else if (typeOfClass.matches("Family Yoga")) {
                typeoc_d_content.setText("Family Yoga");
            }
            if (dayOfWeek.matches("Monday")) {
                dow_d_content.setText("Monday");
            } else if (dayOfWeek.matches("Tuesday")) {
                dow_d_content.setText("Tuesday");
            } else if (dayOfWeek.matches("Wednesday")) {
                dow_d_content.setText("Wednesday");
            } else if (dayOfWeek.matches("Thursday")) {
                dow_d_content.setText("Thursday");
            } else if (dayOfWeek.matches("Friday")) {
                dow_d_content.setText("Friday");
            } else if (dayOfWeek.matches("Saturday")) {
                dow_d_content.setText("Saturday");
            } else if (dayOfWeek.matches("Sunday")) {
                dow_d_content.setText("Sunday");
            }


        }else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }
}