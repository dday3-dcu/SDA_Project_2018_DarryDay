package com.darrylday.teammanager.TeamDetails;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.darrylday.teammanager.DB.DBHelper;
import com.darrylday.teammanager.R;

public class ViewTeamActivity extends AppCompatActivity {

    public final static String KEY_EXTRA_CONTACT_ID = "KEY_EXTRA_CONTACT_ID";
    private ListView listView;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewteam);

        Button button = (Button) findViewById(R.id.addNew);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewTeamActivity.this, CreateOrEditActivity.class);
                intent.putExtra(KEY_EXTRA_CONTACT_ID, 0);
                startActivity(intent);
            }
        });
        dbHelper = new DBHelper(this);

        final Cursor cursor = dbHelper.getAllPersons();
        String [] columns = new String[] {
                DBHelper.PERSON_COLUMN_ID,
                DBHelper.PERSON_COLUMN_FIRSTNAME,
                DBHelper.PERSON_COLUMN_SECONDNAME,
                DBHelper.PERSON_COLUMN_TEAM
        };
        int [] widgets = new int[] {
                R.id.personID,
                R.id.personFirstName,
                R.id.personSecondName,
                R.id.personTeam
        };

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.person_info,
                cursor, columns, widgets, 0);
        listView = (ListView)findViewById(R.id.listView1);
        View header = (View) getLayoutInflater().inflate( R.layout.header_view_team, null );
        listView.addHeaderView( header );
        listView.setAdapter(cursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView listView, View view,
                                    int position, long id) {
                Cursor itemCursor = (Cursor) ViewTeamActivity.this.listView.getItemAtPosition(position);
                int personID = itemCursor.getInt(itemCursor.getColumnIndex(DBHelper.PERSON_COLUMN_ID));
                Intent intent = new Intent(getApplicationContext(), CreateOrEditActivity.class);
                intent.putExtra(KEY_EXTRA_CONTACT_ID, personID);
                startActivity(intent);
            }
        });

    }}
