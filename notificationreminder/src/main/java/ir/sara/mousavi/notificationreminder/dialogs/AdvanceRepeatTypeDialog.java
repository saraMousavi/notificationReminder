package ir.sara.mousavi.notificationreminder.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;

import ir.sara.mousavi.notificationreminder.R;

public class AdvanceRepeatTypeDialog extends Dialog {
    private NumberPicker intervalValue,intervalType;
    private Button intervalTypeBtn;
    private Context mContext;
    private String[] intervalTypeArray = new String[]{"Hour", "Day", "Week",
            "Month", "Year"};
    public AdvanceRepeatTypeDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    private void init() {
        setContentView(R.layout.dialog_advance_repeat_type);
        intervalValue = findViewById(R.id.intervalValue);
        intervalType = findViewById(R.id.intervalType);
        intervalTypeBtn = findViewById(R.id.intervalTypeBtn);
        intervalValue.setMinValue(2);
        intervalValue.setMaxValue(100);
        intervalType.setDisplayedValues(intervalTypeArray);

        intervalType.setMinValue(0);
        intervalType.setMaxValue(4);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        onClickEvents();
    }

    private void onClickEvents() {
        intervalTypeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences  = PreferenceManager
                        .getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("IntervalTime");
                editor.putInt("IntervalTime", intervalValue.getValue());
                editor.remove("IntervalType");
                editor.putString("IntervalType", intervalTypeArray[intervalType.getValue()]);
                editor.apply();
                dismiss();
            }
        });
    }
}
