package com.mobdeve.s20.teves.hannah.mco;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.TimeZone;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";
    private TextView weeklyReminderTime;
    private TextView serverRegion;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        sharedPreferences = getContext().getSharedPreferences("SettingsPrefs", Context.MODE_PRIVATE);

        // Weekly Reminder Time
        View weeklyReminderTimeBlock = view.findViewById(R.id.weeklyReminderTimeBlock);
        ImageView weeklyReminderIcon = weeklyReminderTimeBlock.findViewById(R.id.settingIcon);
        TextView weeklyReminderTitle = weeklyReminderTimeBlock.findViewById(R.id.settingTitle);
        weeklyReminderTime = weeklyReminderTimeBlock.findViewById(R.id.settingParameter);

        weeklyReminderIcon.setImageResource(R.drawable.ic_clock);
        weeklyReminderTitle.setText("Weekly Reminder");
        weeklyReminderTimeBlock.setOnClickListener(v -> showEditReminderDialog());

        // Server Region
        View serverRegionBlock = view.findViewById(R.id.serverRegionBlock);
        ImageView serverRegionIcon = serverRegionBlock.findViewById(R.id.settingIcon);
        TextView serverRegionTitle = serverRegionBlock.findViewById(R.id.settingTitle);
        serverRegion = serverRegionBlock.findViewById(R.id.settingParameter);

        serverRegionIcon.setImageResource(R.drawable.ic_map);
        serverRegionTitle.setText("Server Region");
        serverRegionBlock.setOnClickListener(v -> showEditServerDialog());

        // Load saved settings
        loadSettings();

        return view;
    }

    private void showEditReminderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit Weekly Reminder");
        builder.setMessage("(Set your reminder to do your weeklies)");

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_reminder, null);
        builder.setView(dialogView);

        Spinner daySpinner = dialogView.findViewById(R.id.daySpinner);
        TimePicker timePicker = dialogView.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String day = daySpinner.getSelectedItem().toString();
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
            String time = String.format("%02d:%02d", hour, minute);
            weeklyReminderTime.setText(day + " - " + time);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("reminderDay", day);
            editor.putString("weeklyReminderTime", time);
            editor.apply();

            setWeeklyReminder(day, hour, minute);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void setWeeklyReminder(String day, int hour, int minute) {
        Log.d(TAG, "setWeeklyReminder: Setting reminder for " + day + " at " + hour + ":" + minute);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        int dayOfWeek = getDayOfWeek(day);
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), TaskAlarmReceiver.class);
        intent.putExtra("TASK_NAME", "It's time to do your weeklies!");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), dayOfWeek, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
        Log.d(TAG, "setWeeklyReminder: Alarm set for " + calendar.getTime());
    }

    private int getDayOfWeek(String day) {
        switch (day.toLowerCase()) {
            case "sunday": return Calendar.SUNDAY;
            case "monday": return Calendar.MONDAY;
            case "tuesday": return Calendar.TUESDAY;
            case "wednesday": return Calendar.WEDNESDAY;
            case "thursday": return Calendar.THURSDAY;
            case "friday": return Calendar.FRIDAY;
            case "saturday": return Calendar.SATURDAY;
            default: return Calendar.MONDAY;
        }
    }

    private void showEditServerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Server Region");
        builder.setMessage("(Determine the server time for your reminders)");

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_server, null);
        builder.setView(dialogView);

        Spinner serverSpinner = dialogView.findViewById(R.id.serverSpinner);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String server = serverSpinner.getSelectedItem().toString();

            // Save the server region in SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("serverRegion", server);
            editor.apply();

            serverRegion.setText(server);

            // Set server reset notification
            setServerResetNotification(server);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void setServerResetNotification(String serverRegion) {
        Log.d(TAG, "setServerResetNotification: Setting server reset notification for region " + serverRegion);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 4);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        TimeZone timeZone;
        switch (serverRegion.toLowerCase()) {
            case "asia":
            case "tw, hk, mo":
                timeZone = TimeZone.getTimeZone("GMT+8");
                break;
            case "europe":
                timeZone = TimeZone.getTimeZone("GMT+1");
                break;
            case "america":
                timeZone = TimeZone.getTimeZone("GMT-5");
                break;
            default:
                timeZone = TimeZone.getDefault();
                break;
        }

        calendar.setTimeZone(timeZone);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), TaskAlarmReceiver.class);
        intent.putExtra("TASK_NAME", "The server reset!");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
        Log.d(TAG, "setServerResetNotification: Alarm set for " + calendar.getTime());
    }

    private void loadSettings() {
        String day = sharedPreferences.getString("reminderDay", "Sunday");
        String time = sharedPreferences.getString("weeklyReminderTime", "12:00");
        weeklyReminderTime.setText(day + " - " + time);

        String server = sharedPreferences.getString("serverRegion", "America");
        serverRegion.setText(server);
    }
}