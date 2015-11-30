package com.ozsaat.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.UUID;

public class CrimeFragment extends Fragment {

    private Crime crime;
    private EditText titleField;
    private Button dateButton;
    private CheckBox solvedCheckBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID)getActivity().getIntent().getSerializableExtra( CrimeActivity.EXTRA_CRIME_ID );
        crime = CrimeLab.get( getActivity() ).getCrime( crimeId );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);

        titleField = (EditText)view.findViewById(R.id.crime_title);
        titleField.setText( crime.getTitle() );
        titleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                crime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        dateButton = (Button)view.findViewById(R.id.crime_date);
        dateButton.setText(DateFormat.format("EEEE, dd MMM, yyyy", crime.getDate()).toString());
//        dateButton.setText(crime.getDate().toString());
        dateButton.setEnabled(false);

        solvedCheckBox = (CheckBox)view.findViewById(R.id.crime_solved);
        solvedCheckBox.setChecked( crime.isSolved() );
        solvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Set crime's solved property
                crime.setSolved(isChecked);
            }
        });

        return view;
    }
}
