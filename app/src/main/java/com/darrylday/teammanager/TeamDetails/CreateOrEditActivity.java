package com.darrylday.teammanager.TeamDetails;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.darrylday.teammanager.DB.DBHelper;
import com.darrylday.teammanager.DatePickerFragment;
import com.darrylday.teammanager.MainActivity;
import com.darrylday.teammanager.R;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class CreateOrEditActivity extends AppCompatActivity implements View.OnClickListener, DatePickerFragment.DateDialogListener  {

    // DB object
    private DBHelper dbHelper ;

    private static final String DIALOG_DATE = "DialogDate";
    private int personID;

    // Form variables
    EditText m_firstnameEditText;
    EditText m_secondnameEditText;
    EditText m_emailEditText;
    EditText m_hireDateEditText;
    ImageView m_calendarImage;

    //Radio and Spinner variables
    private RadioGroup m_radioGroup;
    private RadioButton m_maleRadioButton, m_femaleRadioButton, m_rbGender;
    String m_getSpinnerRoleData, m_getSpinnerTeamData; //variable to hold spinner data
    Spinner m_spinnerRole, m_spinnerTeam;

    // Button variables
    Button m_saveButton;
    LinearLayout m_buttonLayout;
    Button m_editButton, m_deleteButton;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        personID = getIntent().getIntExtra( ViewTeamActivity.KEY_EXTRA_CONTACT_ID, 0 );

        setContentView( R.layout.activity_createedit);
        m_firstnameEditText = (EditText) findViewById( R.id.edit_text_first_name );
        m_secondnameEditText = (EditText) findViewById( R.id.edit_text_last_name);
        m_emailEditText = (EditText) findViewById( R.id.edit_text_email);
        m_hireDateEditText = (EditText) findViewById(R.id.edit_text_hire_date);
        m_calendarImage = (ImageView) findViewById(R.id.image_view_hire_date);

        m_saveButton = (Button) findViewById( R.id.saveButton );
        m_saveButton.setOnClickListener( this );
        m_buttonLayout = (LinearLayout) findViewById( R.id.buttonLayout );
        m_editButton = (Button) findViewById( R.id.editButton );
        m_editButton.setOnClickListener( this );
        m_deleteButton = (Button) findViewById( R.id.deleteButton );
        m_deleteButton.setOnClickListener( this );

        m_radioGroup = (RadioGroup) findViewById( R.id.radio_gender);    //Code reference How to store and retrieve sqlite database using spinner and radio button in android - https://www.youtube.com/watch?v=LHMGamTPEpE
        m_maleRadioButton = (RadioButton) findViewById( R.id.radio_male);
        m_femaleRadioButton =(RadioButton) findViewById( R.id.radio_female);

        //Spinner for selecting Role
        m_spinnerRole = findViewById(R.id.spinner);
        String[] role_array = getResources().getStringArray( R.array.role_array);
        ArrayList<String> rolelist = new ArrayList<String>( Arrays.asList( role_array ));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateOrEditActivity.this, android.R.layout.simple_spinner_dropdown_item, rolelist);
        m_spinnerRole.setAdapter( adapter );

        m_spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {    //Code reference - https://stackoverflow.com/questions/15676908/how-to-store-the-selected-value-of-a-spinner-in-sqlite-database
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                m_getSpinnerRoleData = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }});

        //Spinner for selecting Team
        m_spinnerTeam = findViewById(R.id.spinner_team);
        String[] team_array = getResources().getStringArray( R.array.team_array);
        ArrayList<String> teamlist = new ArrayList<String>( Arrays.asList( team_array ));
        ArrayAdapter<String> adapterTeam = new ArrayAdapter<String>(CreateOrEditActivity.this, android.R.layout.simple_spinner_dropdown_item, teamlist);
        m_spinnerTeam.setAdapter( adapterTeam );

        m_spinnerTeam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {  //Method for saving selected spinner item
                m_getSpinnerTeamData = parent.getItemAtPosition(pos).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }});

        m_calendarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Method for Datepicker fragment
                FragmentManager manager = getSupportFragmentManager();
                DatePickerFragment dialog = new DatePickerFragment();
                dialog.show(manager, DIALOG_DATE);
            }
        });

        dbHelper = new DBHelper( this );

        //For case when database already populated on activity start
        if (personID > 0) {
            m_saveButton.setVisibility( View.GONE );
            m_buttonLayout.setVisibility( View.VISIBLE );

            Cursor rs = dbHelper.getPerson( personID );
            rs.moveToFirst();
            String personFirstName = rs.getString( rs.getColumnIndex( DBHelper.PERSON_COLUMN_FIRSTNAME ) );
            String personSecondName = rs.getString( rs.getColumnIndex( DBHelper.PERSON_COLUMN_SECONDNAME ) );
            String personEmail = rs.getString( rs.getColumnIndex( DBHelper.PERSON_COLUMN_EMAIL ) );
            String personGender = rs.getString( rs.getColumnIndex( DBHelper.PERSON_COLUMN_GENDER ) );
            String personHire = rs.getString( rs.getColumnIndex( DBHelper.PERSON_COLUMN_HIREDATE ) );
            String personRole = rs.getString( rs.getColumnIndex( DBHelper.PERSON_COLUMN_ROLE ) );
            String personTeam = rs.getString( rs.getColumnIndex( DBHelper.PERSON_COLUMN_TEAM ) );

            if (!rs.isClosed()) {
                rs.close();
            }
            m_firstnameEditText.setText( personFirstName );
            m_firstnameEditText.setFocusable( false );
            m_firstnameEditText.setClickable( false );

            m_secondnameEditText.setText( personSecondName );
            m_secondnameEditText.setFocusable( false );
            m_secondnameEditText.setClickable( false );

            m_emailEditText.setText( personEmail );
            m_emailEditText.setFocusable( false );
            m_emailEditText.setClickable( false );

            m_hireDateEditText.setText( personHire );
            m_hireDateEditText.setFocusable( false );
            m_hireDateEditText.setClickable( false );

            int selectionPosition= adapter.getPosition(personRole); //Code reference: https://stackoverflow.com/questions/2390102/how-to-set-selected-item-of-spinner-by-value-not-by-position
            m_spinnerRole.setSelection(selectionPosition);
            m_spinnerRole.setEnabled(false);

            int selectionTeamPosition= adapterTeam.getPosition(personTeam);
            m_spinnerTeam.setSelection(selectionTeamPosition);
            m_spinnerTeam.setEnabled(false);

            if (personGender.equals( "Male" ))
                m_maleRadioButton.setChecked( true );
            if (personGender.equals( "Female" )){
                m_femaleRadioButton.setChecked( true );
            }
            m_maleRadioButton.setClickable( false );
            m_femaleRadioButton.setClickable( false );
        }
    }

//For case to add or edit or delete
    @Override
    public void onClick(View view) {  //method for on click of each button state
        switch (view.getId()) {
            case R.id.saveButton:
                ChangePerson();
                return;
            case R.id.editButton:
                m_saveButton.setVisibility(View.VISIBLE);
                m_buttonLayout.setVisibility(View.GONE);

                m_firstnameEditText.setEnabled(true);
                m_firstnameEditText.setFocusableInTouchMode(true);
                m_firstnameEditText.setClickable(true);

                m_secondnameEditText.setEnabled(true);
                m_secondnameEditText.setFocusableInTouchMode(true);
                m_secondnameEditText.setClickable(true);

                m_emailEditText.setEnabled(true);
                m_emailEditText.setFocusableInTouchMode(true);
                m_emailEditText.setClickable(true);

                m_hireDateEditText.setEnabled(true);
                m_hireDateEditText.setFocusableInTouchMode(true);
                m_hireDateEditText.setClickable(true);

                m_spinnerRole.setEnabled( true );
                m_spinnerTeam.setEnabled( true );

                return;

            case R.id.deleteButton:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.deletePerson)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbHelper.deletePerson(personID);
                                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ViewTeamActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog d = builder.create();
                d.setTitle("Delete Person?");
                d.show();
                return;
        }
    }

    public void ChangePerson() {  //method for edit or adding new person
        int id = m_radioGroup.getCheckedRadioButtonId();
        if(id != -1) {  // find the radiobutton by returned id
            m_rbGender = (RadioButton) findViewById(id);

        }
        if(personID > 0) {
            if(dbHelper.updatePerson(personID, m_firstnameEditText.getText().toString(),m_secondnameEditText.getText().toString(), m_emailEditText.getText().toString(),
                    m_rbGender.getText().toString(),m_hireDateEditText.getText().toString(),m_getSpinnerRoleData.toString(),m_getSpinnerTeamData.toString() )) {
                Toast.makeText(getApplicationContext(), "Person Update Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ViewTeamActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else {
                Toast.makeText(getApplicationContext(), "Person Update Failed", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if(dbHelper.insertPerson(m_firstnameEditText.getText().toString(),m_secondnameEditText.getText().toString(),m_emailEditText.getText().toString(),
                    m_rbGender.getText().toString(),m_hireDateEditText.getText().toString(),m_getSpinnerRoleData.toString(),m_getSpinnerTeamData.toString() )) {
                Toast.makeText(getApplicationContext(), "Person Inserted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Could not Insert person", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(getApplicationContext(), ViewTeamActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void onFinishDialog(Date date) {  //Method to return date selection from datepicker
        m_hireDateEditText.setText(formatDate(date));
    }
    public String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String hireDate = sdf.format(date);
        return hireDate;
    }
}

