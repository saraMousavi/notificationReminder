package ir.sara.mousavi.notificationreminder.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

import ir.sara.mousavi.notificationreminder.R;
import ir.sara.mousavi.notificationreminder.adapter.ReminderAdapter;
import ir.sara.mousavi.notificationreminder.db.DataBaseHelper;
import ir.sara.mousavi.notificationreminder.receiver.AlarmReceiver;
import ir.sara.mousavi.notificationreminder.receiver.AutoStartServicesReceiver;

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
            }
        });
    }

    private void init() {
        setContentView(R.layout.activity_notification_main);
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