package com.darrylday.teammanager;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import com.darrylday.teammanager.DB.DBHelper;
import com.darrylday.teammanager.TeamDetails.CreateOrEditActivity;
import com.darrylday.teammanager.TeamDetails.ViewTeamActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class TeamRotaActivity extends AppCompatActivity  {

    private ListView listView;
    private File m_imagePath;
    private Button m_share;
    DBHelper dbHelper;
    int personID;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_viewrota );

        dbHelper = new DBHelper( this );

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); // Code Reference: https://stackoverflow.com/questions/48117511/exposed-beyond-app-through-clipdata-item-geturi?rq=1
        StrictMode.setVmPolicy(builder.build());

        m_share = (Button)findViewById(R.id.sendRota);
        m_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = takeScreenshot();
                saveBitmap(bitmap);
                shareIt();
            }
        });
        final Cursor cursor = dbHelper.getAllPersons();
        String[] columns = new String[]{

                DBHelper.PERSON_COLUMN_FIRSTNAME,
                DBHelper.PERSON_COLUMN_SECONDNAME,
                DBHelper.PERSON_COLUMN_TEAM
        };

        int[] widgets = new int[]{
                R.id.personFirstName,
                R.id.personSecondName,
                personID
        };

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter( this, R.layout.rota_info,
                cursor, columns, widgets, 0 );

        listView = (ListView) findViewById( R.id.listView1 );
        View header = (View) getLayoutInflater().inflate( R.layout.header_view, null );
        listView.addHeaderView( header );
        listView.setAdapter( cursorAdapter );

    }

    public Bitmap takeScreenshot() {  //Method to take screenshot
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }
    public void saveBitmap(Bitmap bitmap) { //Method to save screenshot
        m_imagePath = new File( Environment.getExternalStorageDirectory() + "/screenshot.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(m_imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }
    private void shareIt() {  //method to send screenshot via email

        //Request permissions when using > 6.0 API - Code Reference: https://stackoverflow.com/questions/47349953/java-io-filenotfoundexception-storage-emulated-0-notes-fact50result-txt-open
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Do the file write
        } else {
            // Request permission from the user
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        final Cursor cursor = dbHelper.getAllPersons();
        ArrayList<String> teams = new ArrayList<String>();
        if (cursor != null ) {
            if  (cursor.moveToFirst()) {
                do {
                    String  dir = cursor.getString(cursor.getColumnIndex(dbHelper.PERSON_COLUMN_EMAIL));
                    teams.add(dir);
                }while (cursor.moveToNext());
            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Do the file write
        } else {
            // Request permission from the user
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }

        Uri uri = Uri.fromFile(m_imagePath);
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData( Uri.parse("mailto:" + teams));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Today's Team Status");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Please find current team's headcount status attached.");
        emailIntent.putExtra(Intent.EXTRA_STREAM,uri);
        startActivity(emailIntent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0:
                // Re-attempt file write
        }
    }
}
