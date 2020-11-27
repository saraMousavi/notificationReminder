package ir.sara.mousavi.reminderwithnotification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);
        try {
           Intent intent = new Intent(MainActivity.this, Class.forName("ir.sara.mousavi.notificationreminder.activity.MainActivity"));
           finish();
           startActivity(intent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}