package com.r4u.ride4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class AddPost extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextViewSrc;
    AutoCompleteTextView autoCompleteTextViewDest;
    ArrayAdapter<String> adapterSrcCities;
    ArrayAdapter<String> adapterDestCities;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://ride4u-3a773-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    ArrayList<String> cities;

    DatePickerDialog datePickerDialog;
    Button dateButton;
    Button timeButton;
    int hour, minute;
    Button submitButton;

    private String source;
    private String destination;
    private String seats;
    private String description;
    private String date;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        initCitiesList();
        setupFromAndToAutoComplete();
        setupDateButton();
        setupTimeButton();
        setupSubmitButton();
    }

    private void setupSubmitButton() {
        submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextInputLayout seatsView = findViewById(R.id.seats_number);
                final TextInputLayout descriptionView = findViewById(R.id.description);
                final EditText seatsTxt = seatsView.getEditText();
                final EditText descriptionTxt = descriptionView.getEditText();
                if (descriptionTxt != null && seatsTxt != null) {
                    if (autoCompleteTextViewSrc.getText().length() > 0 && autoCompleteTextViewDest.getText().length() > 0 && seatsTxt.getText().length() > 0
                            && descriptionTxt.getText().length() > 0 && dateButton.getText().length() > 0 && timeButton.getText().length() > 0) {

                        source = autoCompleteTextViewSrc.getText().toString();
                        destination = autoCompleteTextViewDest.getText().toString();
                        seats = seatsTxt.getText().toString();
                        description = descriptionTxt.getText().toString();
                        date = dateButton.getText().toString();
                        time = timeButton.getText().toString();

                        if ((!source.equals("Ariel") && !destination.equals("Ariel"))) {
                            Toast.makeText(AddPost.this, "Either source of destination has to be Ariel!", Toast.LENGTH_SHORT).show();
                        } else if (source.equals(destination)) {
                            Toast.makeText(AddPost.this, "Source and Destination must be different!", Toast.LENGTH_SHORT).show();
                        } else {
                            insertPostToDataBase();
                            finish();
                        }
                    }
                    else {
                        Toast.makeText(AddPost.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void insertPostToDataBase() {
        DatabaseReference newPostRef = databaseReference.child("posts").push();
        if (newPostRef.getKey() != null) {
            newPostRef.setValue(new Post(newPostRef.getKey(), Login.user.getId(), Login.user.getFirstname(), Login.user.getLastname(), seats, source, destination, time, date, description));
            newPostRef.child("postID").removeValue();
        }
    }

    private void setupTimeButton() {
        timeButton = findViewById(R.id.time_picker_button);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int selectedMinute) {
                        hour = hourOfDay;
                        minute = selectedMinute;
                        timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                    }
                };
                int style = AlertDialog.THEME_HOLO_LIGHT;
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddPost.this, style, onTimeSetListener, hour, minute, true);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }
        });
    }

    private void setupDateButton() {
        initDatePicker();
        dateButton = findViewById(R.id.date_picker_button);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }

    private void setupFromAndToAutoComplete() {
        autoCompleteTextViewSrc = findViewById(R.id.src);
        autoCompleteTextViewDest = findViewById(R.id.dest);

        adapterSrcCities = new ArrayAdapter<>(this, R.layout.cities_list, cities);
        adapterDestCities = new ArrayAdapter<>(this, R.layout.cities_list, cities);
        autoCompleteTextViewSrc.setAdapter(adapterSrcCities);
        autoCompleteTextViewDest.setAdapter(adapterDestCities);
    }


    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = makeDateString(dayOfMonth, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int dayOfMonth, int month, int year) {
        return dayOfMonth + " " + getMonthFormat(month) + " " + year;
    }

    private String getMonthFormat(int month) {
        String [] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        return months[month];
    }

    private void initCitiesList() {
        cities = new ArrayList<>();
        databaseReference.child("cities").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapShot) {
                for(DataSnapshot snapshot : dataSnapShot.getChildren()) {
                    cities.add(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getMessage());
            }
        });
    }
}
