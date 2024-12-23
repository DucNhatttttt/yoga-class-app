package com.example.yoga_class_app_coursework.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.TimePickerDialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.yoga_class_app_coursework.MyDatabaseHelper;
import com.example.yoga_class_app_coursework.R;
import com.example.yoga_class_app_coursework.activities.MainActivity;
import com.example.yoga_class_app_coursework.models.Course;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class AddFragment extends Fragment {
    private View mView;
    FirebaseDatabase database;
    DatabaseReference coursesRef;
    EditText timeRangeEditText, capacity_edt, duration_edt, ppc_edt, des_edt;
    RadioGroup rg;
    RadioButton fy_rb;
    RadioButton ay_rb;
    RadioButton fay_rb;
    Button save_btn;
    TextView dow_selector_txt;
    String id, dayOfWeek, timeOfCourse, typeOfClass, description;
    int capacity, duration;
    float pricePerClass;
    MyDatabaseHelper myDB;
    ArrayList<Course> courses;
    private int startHour, startMinute, endHour, endMinute;
    private boolean[] selectedDays; // Mảng lưu trạng thái chọn ngày
    private ArrayList<String> selectedDaysList; // Danh sách ngày đã chọn
    private final String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_add, container, false);

        // Khởi tạo Firebase Database và đường dẫn lưu trữ
        database = FirebaseDatabase.getInstance();
        coursesRef = database.getReference("courses");

        // Các khai báo khác và thiết lập giao diện
        dow_selector_txt = mView.findViewById(R.id.dow_selector_txt);
        selectedDays = new boolean[daysOfWeek.length];
        selectedDaysList = new ArrayList<>();
        capacity_edt = mView.findViewById(R.id.capacity_edt);
        duration_edt = mView.findViewById(R.id.duration_edt);
        ppc_edt = mView.findViewById(R.id.ppc_edt);
        des_edt = mView.findViewById(R.id.des_edt);
        rg = mView.findViewById(R.id.rg);
        fy_rb = mView.findViewById(R.id.fy_rb);
        ay_rb = mView.findViewById(R.id.ay_rb);
        fay_rb = mView.findViewById(R.id.fay_rb);
        save_btn = mView.findViewById(R.id.save_btn);
        myDB = new MyDatabaseHelper(getActivity());
        courses = new ArrayList<>();

        timeRangeEditText = mView.findViewById(R.id.timeRangeEditText);

        // Khi nhấn vào EditText, sẽ mở TimePicker để chọn giờ bắt đầu và kết thúc
        timeRangeEditText.setOnClickListener(v -> showStartTimePicker());

        dow_selector_txt.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Select Days of the Week");
            builder.setMultiChoiceItems(daysOfWeek, selectedDays, (dialog, which, isChecked) -> {
                if (isChecked) {
                    if (!selectedDaysList.contains(daysOfWeek[which])) {
                        selectedDaysList.add(daysOfWeek[which]);
                    }
                } else {
                    selectedDaysList.remove(daysOfWeek[which]);
                }
            });

            builder.setPositiveButton("OK", (dialog, which) -> {
                if (!selectedDaysList.isEmpty()) {
                    dow_selector_txt.setText(String.join(", ", selectedDaysList));
                } else {
                    dow_selector_txt.setText("Click here to select days");
                }
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builder.create().show();
        });

        capacity_edt.setText("0");
        duration_edt.setText("0");
        ppc_edt.setText("0");

        save_btn.setOnClickListener(view -> {
            dayOfWeek = dow_selector_txt.getText().toString();
            capacity = Integer.parseInt(capacity_edt.getText().toString().trim());
            duration = Integer.parseInt(duration_edt.getText().toString().trim());
            pricePerClass = Float.parseFloat(ppc_edt.getText().toString().trim());
            description = des_edt.getText().toString().trim();
            timeOfCourse = timeRangeEditText.getText().toString().trim();

            // Kiểm tra điều kiện nhập dữ liệu
            if (duration == 0 || pricePerClass == 0 || timeOfCourse.equals("Click here to select time of course")
                    || capacity == 0 || dayOfWeek.equals("Click here to select days") || rg.getCheckedRadioButtonId() == -1) {
                displayFillAll();
            } else {
                int id_btn = rg.getCheckedRadioButtonId();
                RadioButton rb = mView.findViewById(id_btn);
                typeOfClass = rb.getText().toString();
                String courseId = coursesRef.push().getKey();
                Course course = new Course(courseId, dayOfWeek, timeOfCourse, capacity, duration, pricePerClass, typeOfClass, description);

                    // Add course to SQLite (using the same ID)
                myDB.addCourse(course);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);

            }
        });

        return mView;
    }

    public void displayFillAll() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Error")
                .setMessage("You need to fill all required fields!")
                .setNeutralButton("Close", null)
                .show();
    }

    private void showStartTimePicker() {
        // Lấy giờ hiện tại làm mặc định
        Calendar calendar = Calendar.getInstance();
        startHour = calendar.get(Calendar.HOUR_OF_DAY);
        startMinute = calendar.get(Calendar.MINUTE);

        // Tạo TimePickerDialog cho thời gian bắt đầu
        TimePickerDialog startTimePicker = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            startHour = hourOfDay;
            startMinute = minute;
            showEndTimePicker(); // Mở tiếp TimePicker cho giờ kết thúc
        }, startHour, startMinute, true);

        startTimePicker.setTitle("Select Start Time");
        startTimePicker.show();
    }

    private void showEndTimePicker() {
        // Lấy giờ hiện tại làm mặc định cho giờ kết thúc
        Calendar calendar = Calendar.getInstance();
        endHour = calendar.get(Calendar.HOUR_OF_DAY);
        endMinute = calendar.get(Calendar.MINUTE);

        // Tạo TimePickerDialog cho thời gian kết thúc
        TimePickerDialog endTimePicker = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            endHour = hourOfDay;
            endMinute = minute;
            updateTimeRange(); // Cập nhật thời gian vào EditText
        }, endHour, endMinute, true);

        endTimePicker.setTitle("Select End Time");
        endTimePicker.show();
    }

    private void updateTimeRange() {
        // Định dạng thời gian theo "HH:mm"
        String startTime = String.format("%02d:%02d", startHour, startMinute);
        String endTime = String.format("%02d:%02d", endHour, endMinute);
        String timeRange = startTime + " - " + endTime;

        // Hiển thị thời gian vào EditText
        timeRangeEditText.setText(timeRange);
    }
}
