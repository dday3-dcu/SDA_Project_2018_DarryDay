package com.darrylday.teammanager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import java.util.ArrayList;

import com.darrylday.teammanager.DB.DBHelper;
import com.darrylday.teammanager.Graphics.TeamChartActivity;
import com.darrylday.teammanager.TeamDetails.ViewTeamActivity;

public class MainActivity extends AppCompatActivity{

    private CardView m_goViewTeam, m_goViewStats,m_goTeamRota, m_goTeamMeeting;
    DBHelper dbHelper;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_goViewTeam = (CardView) findViewById(R.id.TeamActionsView);
        m_goViewStats = (CardView) findViewById( R.id.TeamStatsView);
        m_goTeamRota =  (CardView)findViewById( R.id.TeamCalenderView);
        m_goTeamMeeting =  (CardView)findViewById( R.id.TeamMeetingView);
        dbHelper = new DBHelper(this);

        m_goViewTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ViewTeamActivity.class);
                startActivity(i);
            }
        });
        m_goViewStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TeamChartActivity.class);
                startActivity(i);
            }
        });

        m_goTeamRota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TeamRotaActivity.class);
                startActivity(i);
            }
        });

        m_goTeamMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Cursor cursor = dbHelper.getAllPersons();
                ArrayList<String> teams = new ArrayList<String>();
                if (cursor != null ) {
                    if  (cursor.moveToFirst()) {
                        do {
                            String  dir = cursor.getString(cursor.getColumnIndex(dbHelper.PERSON_COLUMN_EMAIL));     //Get emails from DB and add to Arraylist
                            teams.add(dir);
                        }while (cursor.moveToNext());
                    }
                };
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData( Uri.parse("mailto:" + teams));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Team Meeting");
                startActivity(emailIntent);
            }
        });
    }
}
