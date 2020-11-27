package ir.sara.mousavi.notificationreminder.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;

import ir.sara.mousavi.notificationreminder.R;

public class AdvanceRepeatTypeDialog extends Dialog {
    NumberPicker intervalValue,intervalType;
    Button intervalTypeBtn;
    public AdvanceRepeatTypeDialog(@NonNull Context context) {
        super(context);
    }

    private void init() {
        setContentView(R.layout.dialog_advance_repeat_type);
        intervalValue = findViewById(R.id.intervalValue);
        intervalType = findViewById(R.id.intervalType);
        intervalTypeBtn = findViewById(R.id.intervalTypeBtn);
        intervalValue.setMinValue(1);
        intervalValue.setMaxValue(100);
        String[] intervalTypeArray = new String[]{"Hour", "Day", "Week",
                "Month", "Year"};
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
                dismiss();
            }
        });
    }
}
