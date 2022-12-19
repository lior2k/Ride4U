package com.r4u.ride4u.Fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.r4u.ride4u.Objects.Post;
import com.r4u.ride4u.R;
import com.r4u.ride4u.UserActivities.Login;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


public class AddPostFragment extends Fragment {

    AutoCompleteTextView autoCompleteTextViewSrc;
    AutoCompleteTextView autoCompleteTextViewDest;
    ArrayAdapter<String> adapterSrcCities;
    ArrayAdapter<String> adapterDestCities;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance(Login.firebase_url).getReference();
    ArrayList<String> cities;

    DatePickerDialog datePickerDialog;
    Button dateButton;
    Button timeButton;
    int hour, minute;
    Button submitButton;

    HashMap<String, String> citiesAndPrices;

    private String source;
    private String destination;
    private String seats;
    private String description;
    private String date;
    private String time;
    private String cost;

    final int MAX_SEATS = 4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_post, container, false);
        initCitiesList();
        setupFromAndToAutoComplete(view);
        setupDateButton(view);
        setupTimeButton(view);
        setupSubmitButton(view);
        return view;
    }

    private void setupSubmitButton(View view) {
        submitButton = view.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(v -> {
            final TextInputLayout seatsView = view.findViewById(R.id.seats_number);
            final TextInputLayout descriptionView = view.findViewById(R.id.description);
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
                    cost = (source.equals("Ariel")) ? citiesAndPrices.get(destination) : citiesAndPrices.get(source);

                    if ((!source.equals("Ariel") && !destination.equals("Ariel"))) {
                        Toast.makeText(getActivity(), "Either source of destination has to be Ariel!", Toast.LENGTH_SHORT).show();
                    } else if (source.equals(destination)) {
                        Toast.makeText(getActivity(), "Source and Destination must be different!", Toast.LENGTH_SHORT).show();
                    } else if (Integer.parseInt(seats) > MAX_SEATS) {
                        Toast.makeText(getActivity(), "Max possible seats is 4", Toast.LENGTH_SHORT).show();
                    } else if (!checkValidTime()) {
                        Toast.makeText(getActivity(), "Time has passed", Toast.LENGTH_SHORT).show();
                    } else {
                        insertPostToDataBase();
                        Toast.makeText(getActivity(), "Posted successfully", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private boolean checkValidTime() {
        Calendar cal = Calendar.getInstance();
        int currHour = cal.get(Calendar.HOUR_OF_DAY);
        int currMinute = cal.get(Calendar.MINUTE);
        if (hour < currHour) {
            return false;
        } else return hour != currHour || minute >= currMinute;
    }

    private void insertPostToDataBase() {
        DatabaseReference newPostRef = databaseReference.child("posts").push();
        if (newPostRef.getKey() != null) {
            Post p = new Post(newPostRef.getKey(), Login.user.getId(), Login.user.getFirstname(), Login.user.getLastname(), seats, source, destination, time, date, cost, description);
            newPostRef.setValue(p);
            newPostRef.child("postID").removeValue();
        }
    }


    private void setupTimeButton(View view) {
        timeButton = view.findViewById(R.id.time_picker_button);
        timeButton.setOnClickListener(v -> {
            TimePickerDialog.OnTimeSetListener onTimeSetListener = (view2, hourOfDay, selectedMinute) -> {
                hour = hourOfDay;
                minute = selectedMinute;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            };
            int style = AlertDialog.THEME_HOLO_LIGHT;
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), style, onTimeSetListener, hour, minute, true);
            timePickerDialog.setTitle("Select Time");
            timePickerDialog.show();
        });
    }

    private void setupDateButton(View view) {
        initDatePicker();
        dateButton = view.findViewById(R.id.date_picker_button);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }

    private void setupFromAndToAutoComplete(View view) {
        autoCompleteTextViewSrc = view.findViewById(R.id.src);
        autoCompleteTextViewDest = view.findViewById(R.id.dest);

        adapterSrcCities = new ArrayAdapter<>(getContext(), R.layout.cities_list, cities);
        adapterDestCities = new ArrayAdapter<>(getContext(), R.layout.cities_list, cities);
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
        datePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMinDate(cal.getTimeInMillis());
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
        citiesAndPrices = new HashMap<>();
        databaseReference.child("cities").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapShot) {
                for(DataSnapshot snapshot : dataSnapShot.getChildren()) {
                    cities.add(snapshot.getKey());
                    citiesAndPrices.put(snapshot.getKey(), snapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getMessage());
            }
        });
    }
}