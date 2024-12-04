// SettingsFragment.java
package com.mobdeve.s20.teves.hannah.mco;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class SettingsFragment extends Fragment {

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
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
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
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void loadSettings() {
        String day = sharedPreferences.getString("reminderDay", "Sunday");
        String time = sharedPreferences.getString("weeklyReminderTime", "12:00");
        weeklyReminderTime.setText(day + " - " + time);

        String server = sharedPreferences.getString("serverRegion", "America");
        serverRegion.setText(server);
    }
}
