package com.darrylday.teammanager.Graphics;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.darrylday.teammanager.*;
import com.darrylday.teammanager.DB.DBHelper;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class TeamChartActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{

    private static String TAG = "TeamChartActivity";
    private PieChart teamPieChart;
    private DBHelper dbHelper;
    int [] m_yDataTeam = new int [3];
    int [] m_yDataRole = new int [3];
    int [] m_yDataGender = new int [2];
    private Spinner m_spinnerData;
    private String m_getSpinnerData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_team_piechart );
        Log.d( TAG, "onCreate: starting Chart" );

        m_spinnerData = findViewById(R.id.spinnerData);
        String[] data_array = getResources().getStringArray( R.array.data_array);
        ArrayList<String> datalist = new ArrayList<String>( Arrays.asList(data_array));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TeamChartActivity.this, android.R.layout.simple_spinner_dropdown_item, datalist);
        m_spinnerData.setAdapter(adapter);
        m_spinnerData.setOnItemSelectedListener(this);
    }

    private void addDataSetTeam(PieChart teamPieChart) {   //Method to get X values (Teams) and Y values (number of employees per Team)
        Log.d( TAG, "addDataSet start" );
        dbHelper = new DBHelper( this );

        ArrayList<PieEntry> yVals = new ArrayList<PieEntry>();
        for (int i = 0; i < m_yDataTeam.length; i++) {
            m_yDataTeam[0] = Collections.frequency( dbHelper.queryYDataTeam(), "Team Blue" );
            m_yDataTeam[1] = Collections.frequency( dbHelper.queryYDataTeam(), "Team Green" );
            m_yDataTeam[2] = Collections.frequency( dbHelper.queryYDataTeam(), "Team Red" );
            yVals.add( new PieEntry( m_yDataTeam[i] ) );
            // https://stackoverflow.com/questions/505928/how-to-count-the-number-of-occurrences-of-an-element-in-a-list
            }

       ArrayList<String> xVals = new ArrayList<String>();
       for (int i = 0; i < dbHelper.queryXDataTeam().size(); i++) {
           xVals.add( dbHelper.queryXDataTeam().get( i ) );
            }

        PieDataSet pieDataSet = new PieDataSet( yVals, "Number of Members per Team" );
        pieDataSet.setSliceSpace( 2 );
        pieDataSet.setValueTextColor(Color.WHITE );
        pieDataSet.setValueTextSize( 20 );

        ArrayList<Integer> colors = new ArrayList<>( );
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        colors.add(Color.RED);

        pieDataSet.setColors(colors);
        teamPieChart.setCenterText( "Team Allocations" );
        PieData pieData = new PieData( pieDataSet );
        teamPieChart.setData( pieData );
        teamPieChart.invalidate();
        }

    private void addDataSetGender(PieChart teamPieChart) { //Method to get X values (Gender) and Y values (number of males and females)
        Log.d( TAG, "addDataSet start" );
        dbHelper = new DBHelper( this );

        ArrayList<PieEntry> yVals = new ArrayList<PieEntry>();
        for (int i = 0; i < m_yDataGender.length; i++) {
            m_yDataGender[0] = Collections.frequency( dbHelper.queryYDataGender(), "Male" );
            m_yDataGender[1] = Collections.frequency( dbHelper.queryYDataGender(), "Female" );
            yVals.add( new PieEntry(m_yDataGender[i] ) );
        }

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < dbHelper.queryXDataGender().size(); i++) {
            xVals.add( dbHelper.queryXDataGender().get( i ) );
        }

        PieDataSet pieDataSet = new PieDataSet( yVals, "Male vs Female Ratio" );
        pieDataSet.setSliceSpace( 2 );
        pieDataSet.setValueTextColor(Color.WHITE );
        pieDataSet.setValueTextSize( 20 );

        ArrayList<Integer> colors = new ArrayList<>( );
        colors.add(Color.BLUE);
        colors.add(Color.RED);

        pieDataSet.setColors(colors);
        teamPieChart.setCenterText( "Gender Balance" );
        PieData pieData = new PieData( pieDataSet );
        teamPieChart.setData( pieData );
        teamPieChart.invalidate();
        }

    private void addDataSetRole(PieChart teamPieChart) { //Method to get X values (Roles) and Y values (number of roles in total employee list)
        Log.d( TAG, "addDataSet start" );
        dbHelper = new DBHelper( this );

        ArrayList<PieEntry> yVals = new ArrayList<PieEntry>();
        for (int i = 0; i < m_yDataRole.length; i++) {
            m_yDataRole[0] = Collections.frequency( dbHelper.queryYDataRoles(), "Supervisor" );
            m_yDataRole[1] = Collections.frequency( dbHelper.queryYDataRoles(), "Engineer" );
            m_yDataRole[2] = Collections.frequency( dbHelper.queryYDataRoles(), "Technician" );
            yVals.add( new PieEntry(m_yDataRole[i] ) );
        }

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < dbHelper.queryXDataRoles().size(); i++) {
            xVals.add( dbHelper.queryXDataRoles().get( i ) );
        }

        PieDataSet pieDataSet = new PieDataSet( yVals, "Team Roles - Supervisor, Engineer, Technician" );
        pieDataSet.setSliceSpace( 2 );
        pieDataSet.setValueTextColor(Color.WHITE );
        pieDataSet.setValueTextSize( 20 );

        ArrayList<Integer> colors = new ArrayList<>( );
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);

        pieDataSet.setColors(colors);
        teamPieChart.setCenterText( "Team Roles" );
        PieData pieData = new PieData( pieDataSet );
        teamPieChart.setData( pieData );
        teamPieChart.invalidate();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {  //Method using switch to pick graph to display based on spinner selection
        m_getSpinnerData = parent.getItemAtPosition(position).toString();
        switch (m_getSpinnerData) {
            case  "Team Allocation":
                teamPieChart = (PieChart) findViewById( R.id.piechartTeam );
                teamPieChart.setUsePercentValues( false );
                teamPieChart.getDescription().setEnabled( false);
                teamPieChart.setExtraOffsets( 5, 10, 5, 5 );
                teamPieChart.setDragDecelerationFrictionCoef( 0.95f );
                teamPieChart.setDrawHoleEnabled( true );
                teamPieChart.setHoleColor( Color.WHITE );
                teamPieChart.setTransparentCircleRadius( 40f );
                teamPieChart.setCenterTextSize(20);
                teamPieChart.setDrawEntryLabels( true );

                addDataSetTeam(teamPieChart);
                break;

            case "Gender Balance":
                teamPieChart = (PieChart) findViewById( R.id.piechartTeam );
                teamPieChart.setUsePercentValues( false );
                teamPieChart.getDescription().setEnabled( false);
                teamPieChart.setExtraOffsets( 5, 10, 5, 5 );
                teamPieChart.setDragDecelerationFrictionCoef( 0.95f );
                teamPieChart.setDrawHoleEnabled( true );
                teamPieChart.setHoleColor( Color.WHITE );
                teamPieChart.setTransparentCircleRadius( 40f );
                teamPieChart.setCenterTextSize(20);
                teamPieChart.setDrawEntryLabels( true );

                addDataSetGender(teamPieChart);
                break;

            case "Roles in Team":
                 teamPieChart = (PieChart) findViewById( R.id.piechartTeam );
                 teamPieChart.setUsePercentValues( false );
                 teamPieChart.getDescription().setEnabled( false);
                 teamPieChart.setExtraOffsets( 5, 10, 5, 5 );
                 teamPieChart.setDragDecelerationFrictionCoef( 0.95f );
                 teamPieChart.setDrawHoleEnabled( true );
                 teamPieChart.setHoleColor( Color.WHITE );
                 teamPieChart.setTransparentCircleRadius( 40f );
                 teamPieChart.setCenterTextSize(20);
                 teamPieChart.setDrawEntryLabels( true );

                 addDataSetRole(teamPieChart);
                 break;
            }
        }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    return;
    }
}
