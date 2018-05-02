package com.darrylday.teammanager.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "employee.db";  //Define DB: Tables and Columns as Constants
    private static final int DATABASE_VERSION = 1;
    public static final String PERSON_TABLE_NAME = "employee";
    public static final String PERSON_COLUMN_ID = "_id";
    public static final String PERSON_COLUMN_FIRSTNAME = "firstname";
    public static final String PERSON_COLUMN_SECONDNAME = "secondname";
    public static final String PERSON_COLUMN_EMAIL = "email";
    public static final String PERSON_COLUMN_GENDER = "gender";
    public static final String PERSON_COLUMN_HIREDATE = "hiredate";
    public static final String PERSON_COLUMN_ROLE = "role";
    public static final String PERSON_COLUMN_TEAM = "team";

    public DBHelper(Context context) {      //Constructor: handles creation/update of DB. version starts at 1
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }
    private static String TAG = "DBActivity";

    @Override
    public void onCreate(SQLiteDatabase db) {  //Method whenever a new DB is created
        db.execSQL( "CREATE TABLE " + PERSON_TABLE_NAME + "(" +
                PERSON_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                PERSON_COLUMN_FIRSTNAME + " TEXT, " +
                PERSON_COLUMN_SECONDNAME + " TEXT, " +
                PERSON_COLUMN_EMAIL + " TEXT, " +
                PERSON_COLUMN_GENDER + " TEXT, " +
                PERSON_COLUMN_HIREDATE + " NUMERIC, " +
                PERSON_COLUMN_ROLE + " TEXT, " +
                PERSON_COLUMN_TEAM + " TEAM)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //Method whenever the DB needs updated
        db.execSQL( "DROP TABLE IF EXISTS " + PERSON_TABLE_NAME );
        onCreate( db );
    }

    public boolean insertPerson(String firstname, String secondname, String email, String gender, String hiredate, String role, String team) {  //Method to Insert new person
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put( PERSON_COLUMN_FIRSTNAME, firstname );
        contentValues.put( PERSON_COLUMN_SECONDNAME, secondname );
        contentValues.put( PERSON_COLUMN_EMAIL, email );
        contentValues.put( PERSON_COLUMN_GENDER, gender );
        contentValues.put( PERSON_COLUMN_HIREDATE, hiredate );
        contentValues.put( PERSON_COLUMN_ROLE, role );
        contentValues.put( PERSON_COLUMN_TEAM, team );

        db.insert( PERSON_TABLE_NAME, null, contentValues );
        return true;
    }

    public boolean updatePerson(Integer id, String firstname, String secondname, String email, String gender, String hiredate, String role, String team) { //Method to Update Person
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put( PERSON_COLUMN_FIRSTNAME, firstname );
        contentValues.put( PERSON_COLUMN_SECONDNAME, secondname );
        contentValues.put( PERSON_COLUMN_GENDER, gender );
        contentValues.put( PERSON_COLUMN_EMAIL, email );
        contentValues.put( PERSON_COLUMN_HIREDATE, hiredate );
        contentValues.put( PERSON_COLUMN_ROLE, role );
        contentValues.put( PERSON_COLUMN_TEAM, team );
        db.update( PERSON_TABLE_NAME, contentValues, PERSON_COLUMN_ID + " = ? ", new String[]{Integer.toString( id )} );
        return true;
    }

    public Cursor getPerson(int id) {   //Method to retrieve single person
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + PERSON_TABLE_NAME + " WHERE " +
                PERSON_COLUMN_ID + "=?", new String[]{Integer.toString( id )} );
        return res;
    }

    public Cursor getAllPersons() { //Method to retrieve all persons
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + PERSON_TABLE_NAME, null );
        return res;
    }

    public Integer deletePerson(Integer id) { //Method to delete person
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete( PERSON_TABLE_NAME,
                PERSON_COLUMN_ID + " = ? ",
                new String[]{Integer.toString( id )} );
    }

    public ArrayList<String> queryYDataTeam() {  //Method to get all teams from all teams
        ArrayList<String> yNewData = new ArrayList<String>();
        String query = "select team from employee order by team asc";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( query, null );
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            yNewData.add( cursor.getString( cursor.getColumnIndex( PERSON_COLUMN_TEAM ) ) );
        }
        cursor.close();
        return yNewData;
    }

    public ArrayList<String> queryXDataTeam() {      //Method to get count of team occurrences for employees // Code Reference: https://stackoverflow.com/questions/7654978/using-count-to-find-the-number-of-occurrences
        ArrayList<String> xNewData = new ArrayList<String>();
        String query = "select team, count(*) from employee group by team";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( query, null );
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            xNewData.add( cursor.getString( cursor.getColumnIndex( PERSON_COLUMN_TEAM ) ) );
        }
        cursor.close();
        return xNewData;
    }

    public ArrayList<String> queryYDataGender() { //Method to get all genders from all teams
        ArrayList<String> yNewData = new ArrayList<String>();
        String query = "select gender from employee order by gender asc";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( query, null );
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            yNewData.add( cursor.getString( cursor.getColumnIndex( PERSON_COLUMN_GENDER ) ) );
        }
        cursor.close();
        return yNewData;
    }

    public ArrayList<String> queryXDataGender() {       //Method to get count of gender occurrences for employees
        ArrayList<String> xNewData = new ArrayList<String>();
        String query = "select gender, count(*) from employee group by gender";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( query, null );
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            xNewData.add( cursor.getString( cursor.getColumnIndex( PERSON_COLUMN_GENDER) ) );
        }
        cursor.close();
        return xNewData;
    }

    public ArrayList<String> queryYDataRoles() { //Method to get all roles from all teams
        ArrayList<String> yNewData = new ArrayList<String>();
        String query = "select role from employee order by role asc";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( query, null );
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            yNewData.add( cursor.getString( cursor.getColumnIndex( PERSON_COLUMN_ROLE ) ) );
        }
        cursor.close();
        return yNewData;
    }

    public ArrayList<String> queryXDataRoles() {   //Method to get count of roles occurrences for employees
        ArrayList<String> xNewData = new ArrayList<String>();
        String query = "select role, count(*) from employee group by role";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( query, null );
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            xNewData.add( cursor.getString( cursor.getColumnIndex( PERSON_COLUMN_ROLE) ) );
        }
        cursor.close();
        return xNewData;
    }
}

