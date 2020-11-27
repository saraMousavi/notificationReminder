package ir.sara.mousavi.notificationreminder.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.AlarmManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import ir.sara.mousavi.notificationreminder.R;
import ir.sara.mousavi.notificationreminder.adapter.ReminderAdapter;
import ir.sara.mousavi.notificationreminder.db.DataBaseHelper;
import ir.sara.mousavi.notificationreminder.receiver.AlarmReceiver;
import ir.sara.mousavi.notificationreminder.receiver.AutoStartServicesReceiver;
import ir.sara.mousavi.notificationreminder.service.AlarmService;

public class MainActivity extends AppCompatActivity {

    private Calendar mCalendar;
    private Button addReminderBtn;
    private RecyclerView reminderList;
    private ReminderAdapter reminderAdapter;
    private DataBaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        mCalendar = Calendar.getInstance();
        BroadcastReceiver broadcastReceiver = new AlarmReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.EXTRA_NO_CONNECTIVITY);
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        this.registerReceiver(broadcastReceiver, filter);

        BroadcastReceiver autoAlarmReceiver = new AutoStartServicesReceiver();
        IntentFilter autoFilter = new IntentFilter(ConnectivityManager.EXTRA_NO_CONNECTIVITY);
        autoFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
        autoFilter.addAction(Intent.ACTION_LOCKED_BOOT_COMPLETED);
        this.registerReceiver(autoAlarmReceiver, autoFilter);

        addReminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, AddReminderActivity.class), 100);
//                new AlarmReceiver().setAlarm(getApplicationContext(), mCalendar, 1, 5000, btn.getText().toString());
            }
        });
    }

    private void init() {
        setContentView(R.layout.activity_notif);
        db = new DataBaseHelper(MainActivity.this);
        reminderList = findViewById(R.id.reminderList);
        addReminderBtn = findViewById(R.id.addReminderBtn);
        reminderAdapter = new ReminderAdapter(MainActivity.this, db.getAllReminders());
        reminderList.setAdapter(reminderAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK){
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }
    }
}