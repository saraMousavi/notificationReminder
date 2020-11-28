package ir.sara.mousavi.notificationreminder.activity;

import android.app.AlarmManager;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

import ir.sara.mousavi.notificationreminder.R;
import ir.sara.mousavi.notificationreminder.db.DataBaseHelper;
import ir.sara.mousavi.notificationreminder.db.holder.Reminder;
import ir.sara.mousavi.notificationreminder.dialogs.AdvanceRepeatTypeDialog;
import ir.sara.mousavi.notificationreminder.receiver.AlarmReceiver;
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
    private Calendar mCalendar;
    private int mYear, mMonth, mHour, mMinute, mDay;

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
        if(reminderTitle.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), getString(R.string.enterReminderTile), Toast.LENGTH_LONG).show();
            return;
        }
        if(reminderTimeText == null){
            Toast.makeText(getApplicationContext(), getString(R.string.enterReminderTime), Toast.LENGTH_LONG).show();
            return;
        }
        int intervalTime = 0;
        String intervalType = "";
        long intervalDuration = 0;
        SharedPreferences sharedPreferences  = PreferenceManager
                .getDefaultSharedPreferences(AddReminderActivity.this);
        switch (repeatTypeSpinner.getSelectedItemPosition()){
            case 1:
                intervalTime = 1;
                intervalType = "hourly";
                intervalDuration = AlarmManager.INTERVAL_HOUR;
                break;
            case 2:
                intervalTime = 1;
                intervalType = "daily";
                intervalDuration = AlarmManager.INTERVAL_DAY;
                break;
            case 3:
                intervalTime = 1;
                intervalType = "weekly";
                intervalDuration = 7 * AlarmManager.INTERVAL_DAY;
                break;
            case 4:
                intervalTime = 1;
                intervalType = "monthly";
                intervalDuration = 30 * AlarmManager.INTERVAL_DAY;
                break;
            case 5:
                intervalTime = 1;
                intervalType = "yearly";
                intervalDuration = 365 * AlarmManager.INTERVAL_DAY;
                break;
            case 6:
                intervalTime = sharedPreferences.getInt("IntervalTime", 0);
                intervalType = sharedPreferences.getString("IntervalType", "");
                switch (intervalType){
                    case "Hour":
                        intervalDuration = intervalTime * AlarmManager.INTERVAL_HOUR;
                        break;
                    case "Day":
                        intervalDuration = intervalTime * AlarmManager.INTERVAL_DAY;
                        break;
                    case "Week":
                        intervalDuration = 7 * intervalTime * AlarmManager.INTERVAL_DAY;
                        break;
                    case "Month":
                        intervalDuration = 30 * intervalTime * AlarmManager.INTERVAL_DAY;
                        break;
                    case "Year":
                        intervalDuration = 365 * intervalTime * AlarmManager.INTERVAL_DAY;
                        break;
                }
                break;
        }
        Reminder reminder = new Reminder(reminderTitle.getText().toString(), reminderTimeValue.getText().toString(),
                intervalType, intervalTime);
        int ID = (int) db.insertReminder(reminder);
        mCalendar.set(Calendar.MONTH, --mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, 0);
        new AlarmReceiver().setAlarm(getApplicationContext(), mCalendar, ID, reminder.getReminderTitle(),
                (repeatTypeSpinner.getSelectedItemPosition() == 0) ? 0 : 1,  intervalDuration);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("IntervalTime");
        editor.remove("IntervalType");
        editor.apply();
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
        mCalendar = Calendar.getInstance();
        ArrayAdapter<String> remindTimeAdapter = new ArrayAdapter<String>(AddReminderActivity.this,
                android.R.layout.simple_spinner_dropdown_item, remindTimeArray);
        repeatTypeSpinner.setAdapter(remindTimeAdapter);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mYear = year;
        mMonth = monthOfYear + 1;
        mDay = dayOfMonth;
        reminderTimeText = year + "/" + mMonth + "/" + dayOfMonth;
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
        mHour = hourOfDay;
        mMinute = minute;
        reminderTimeText += "," + hourOfDay + ":" + minute;
        reminderTimeValue.setText(reminderTimeText);
    }
}