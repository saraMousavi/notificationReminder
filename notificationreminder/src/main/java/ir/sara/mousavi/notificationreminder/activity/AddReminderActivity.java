package ir.sara.mousavi.notificationreminder.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

import ir.sara.mousavi.notificationreminder.R;
import ir.sara.mousavi.notificationreminder.db.DataBaseHelper;
import ir.sara.mousavi.notificationreminder.db.holder.Reminder;
import ir.sara.mousavi.notificationreminder.dialogs.AdvanceRepeatTypeDialog;
import ir.sara.mousavi.notificationreminder.utils.calender.DatePickerDialog;
import ir.sara.mousavi.notificationreminder.utils.calender.PersianCalendar;
import ir.sara.mousavi.notificationreminder.utils.calender.TimePickerDialog;
import ir.sara.mousavi.notificationreminder.utils.enums.ReminderType;

public class AddReminderActivity extends AppCompatActivity
        implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {
    TextView reminderTime, reminderTimeValue;
    String reminderTimeText;
    Spinner repeatTypeSpinner;
    Button insertReminderBtn;
    EditText reminderTitle;
    DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        onClickEvents();
    }

    private void onClickEvents() {
        reminderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersianCalendar persianCalendar = new PersianCalendar();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        AddReminderActivity.this,
                        persianCalendar.getPersianYear(),
                        persianCalendar.getPersianMonth(),
                        persianCalendar.getPersianDay()
                );
                datePickerDialog.show(getSupportFragmentManager(), "startDatepickerdialog");
            }
        });
        repeatTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 6) {
                    AdvanceRepeatTypeDialog advanceRepeatTypeDialog = new AdvanceRepeatTypeDialog(AddReminderActivity.this);
                    advanceRepeatTypeDialog.show();
                    advanceRepeatTypeDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        insertReminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertReminder();
            }
        });
    }

    private void insertReminder() {
        Reminder reminder = new Reminder(reminderTitle.getText().toString(), reminderTimeValue.getText().toString(), "daily", 1);
        db.insertReminder(reminder);
        setResult(RESULT_OK);
        finish();
    }

    private void init() {
        setContentView(R.layout.activity_add_reminder);
        reminderTime = findViewById(R.id.reminderTime);
        reminderTimeValue = findViewById(R.id.reminderTimeValue);
        repeatTypeSpinner = findViewById(R.id.repeatType);
        insertReminderBtn = findViewById(R.id.insertReminderBtn);
        reminderTitle = findViewById(R.id.reminderTitle);
        db = new DataBaseHelper(AddReminderActivity.this);
        ArrayList<String> remindTimeArray = new ArrayList<>();
        remindTimeArray.add("Don't Repeat");
        remindTimeArray.add("Hourly");
        remindTimeArray.add("Daily");
        remindTimeArray.add("Weekly");
        remindTimeArray.add("Monthly");
        remindTimeArray.add("Yearly");
        remindTimeArray.add("Advance");
        ArrayAdapter<String> remindTimeAdapter = new ArrayAdapter<String>(AddReminderActivity.this,
                android.R.layout.simple_spinner_dropdown_item, remindTimeArray);
        repeatTypeSpinner.setAdapter(remindTimeAdapter);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        reminderTimeText = year + "/" + monthOfYear + "/" + dayOfMonth;
        Calendar now = Calendar.getInstance();
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                AddReminderActivity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show(getSupportFragmentManager(), "startTimePickerDialog");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute) {
        reminderTimeText += "," + hourOfDay + ":" + minute;
        reminderTimeValue.setText(reminderTimeText);
    }
}