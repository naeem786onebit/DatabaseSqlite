package com.example.databasehandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "usersdb";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "users";

    // Column Names
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_DOB = "dob";

    // Constructor
    public DbHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " ("
                + KEY_EMAIL + " TEXT PRIMARY KEY,"
                + KEY_USERNAME + " TEXT,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_DOB + " TEXT)";
        db.execSQL(CREATE_TABLE_QUERY);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newUserVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }


    public void addUser(String email, String username, String password, String dob) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, email);
        values.put(KEY_USERNAME, username);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_DOB, dob);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {KEY_EMAIL};

        String selection = KEY_EMAIL + " = ? AND " + KEY_PASSWORD + " = ?";

        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }


    public Cursor getUserData(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(TABLE_NAME, null, KEY_EMAIL + "=?", new String[]{email}, null, null, null);
    }


    public int updateUser(String email, String newUsername, String newPassword, String newDob) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, newUsername);
        values.put(KEY_PASSWORD, newPassword);
        values.put(KEY_DOB, newDob);


        return db.update(TABLE_NAME, values, KEY_EMAIL + " = ?", new String[]{email});
    }
}

