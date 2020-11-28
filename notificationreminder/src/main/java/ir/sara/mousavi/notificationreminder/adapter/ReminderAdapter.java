package ir.sara.mousavi.notificationreminder.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.sara.mousavi.notificationreminder.R;
import ir.sara.mousavi.notificationreminder.activity.MainActivity;
import ir.sara.mousavi.notificationreminder.db.DataBaseHelper;
import ir.sara.mousavi.notificationreminder.db.holder.Reminder;
import ir.sara.mousavi.notificationreminder.receiver.AlarmReceiver;

public class ReminderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Reminder> mReminders;
    private DataBaseHelper db;
    private Context mContext;

    public ReminderAdapter(Context context, ArrayList<Reminder> mReminders) {
        this.mReminders = mReminders;
        db = new DataBaseHelper(context);
        mContext = context;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView reminderTitle, reminderTime, reminderIntervalItem;
        private ImageView deleteReminder;


        public ItemViewHolder(View itemView) {
            super(itemView);
            reminderTitle = itemView.findViewById(R.id.reminderTitleItem);
            reminderTime = itemView.findViewById(R.id.reminderTimeItem);
            deleteReminder = itemView.findViewById(R.id.deleteReminder);
            reminderIntervalItem = itemView.findViewById(R.id.reminderIntervalItem);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View contactView = inflater.inflate(R.layout.reminder_item_list, parent, false);

        return new ItemViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder viewHolderItem = (ItemViewHolder) holder;
        Reminder mReminder = mReminders.get(position);
        viewHolderItem.reminderTitle.setText(mReminders.get(position).getReminderTitle());
        viewHolderItem.reminderTime.setText(mReminders.get(position).getReminderTime());
        if(mReminder.getReminderIntervalTime() == 1){
            viewHolderItem.reminderIntervalItem.setText(mReminder.getReminderIntervalType());
        } else if(mReminders.get(position).getReminderIntervalTime() > 1) {
            viewHolderItem.reminderIntervalItem.
                    setText(mContext.getString(R.string.each) + mReminder.getReminderIntervalTime()
                    + mReminder.getReminderIntervalType());
        }
        viewHolderItem.deleteReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteReminder(mReminders.get(position));
                new AlarmReceiver().cancelAlarm(mContext, mReminders.get(position).getReminderId());
                ((Activity) mContext).finish();
                ((Activity) mContext).overridePendingTransition(0, 0);
                mContext.startActivity(new Intent(mContext, MainActivity.class));
                ((Activity) mContext).overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mReminders.size();
    }
}
