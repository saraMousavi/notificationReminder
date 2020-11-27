package ir.sara.mousavi.notificationreminder.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import ir.sara.mousavi.notificationreminder.R;

public class NotificationMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_message);
        Bundle bundle = getIntent().getExtras();
        TextView tvMessage = findViewById(R.id.tvMessage);
        tvMessage.setText(bundle.getString("message"));
    }
}