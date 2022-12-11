package com.example.formularacing;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class admin_view extends AppCompatActivity {
    dataAccess dal =new dataAccess();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);

        //when workplan button clicked
        Button workplanButton = (Button) findViewById(R.id.weekly_workplan);
        workplanButton.setOnClickListener((view)->openWorkplanActivity());

        //when my shifts button clicked
        Button shiftsButton = (Button) findViewById(R.id.myShifts);
        shiftsButton.setOnClickListener((view)-> showShifts());

        //when admin want change client's appointments
        Button manageButton = (Button) findViewById(R.id.ManageAppointments);
        manageButton.setOnClickListener((view -> changeAppointments()));
    }

    private void changeAppointments() {
        Intent intent = new Intent(admin_view.this, manageAppointments.class);
        admin_view.this.startActivity(intent);
    }

    private void showShifts() {
        Task task=dal.adminGetShifts();
        List<String> shiftList = new ArrayList<>();
        while (!task.isComplete()){}
        HashMap<String, ArrayList<String>> appointmentCreatorList = (HashMap<String, ArrayList<String>>) ((DataSnapshot) task.getResult()).getValue();
        // Create a TreeSet object and add the dates to it (it sort the dates in the correct order)
        TreeSet<String> sortedDates = new TreeSet<>();
        sortedDates.addAll(appointmentCreatorList.keySet());
        // Iterate over the set of keys
        Iterator<String> iterator = sortedDates.iterator();
        while (iterator.hasNext()) {
            // Get the current key
            String key = iterator.next();

            // Parse the date string using the specified format
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = null;
            try {
                date = dateFormat.parse(key);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Create a calendar object with the parsed date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            // Get the day of the week from the calendar
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            // Convert the day of the week to a string
            String dayString="Saturday";
            switch (dayOfWeek) {
                case Calendar.SUNDAY:
                    dayString = "Sunday";
                    break;
                case Calendar.MONDAY:
                    dayString = "Monday";
                    break;
                case Calendar.TUESDAY:
                    dayString = "Tuesday";
                    break;
                case Calendar.WEDNESDAY:
                    dayString = "Wednesday";
                    break;
                case Calendar.THURSDAY:
                    dayString = "Thursday";
                    break;
                case Calendar.FRIDAY:
                    dayString = "Friday";
                    break;
            }
            key=key+" - " + dayString;
            shiftList.add(key);
        }
        // TODO Display shiftList!!

        AlertDialog alertDialog = new AlertDialog.Builder(admin_view.this).
                setTitle("Shifts").
                setMessage("Sunday\n" +
                            "monday").
                setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.show();
        //need add here function that get the the work days of employee
    }

    public void openWorkplanActivity(){
        Intent intent = new Intent(admin_view.this, workplanActivity.class);
        admin_view.this.startActivity(intent);
    }
}