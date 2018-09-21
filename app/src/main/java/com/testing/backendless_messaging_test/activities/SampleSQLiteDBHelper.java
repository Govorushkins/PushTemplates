package com.testing.backendless_messaging_test.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SampleSQLiteDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "messaging";

    public SampleSQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + MessageRecord.MESSAGE_RECORD_TABLE_NAME + " (" +
                MessageRecord.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MessageRecord.COLUMN_ACTION + " TEXT, " +
                MessageRecord.COLUMN_TEMPLATE_NAME + " TEXT, " +
                MessageRecord.COLUMN_MESSAGE_ID + " INTEGER, " +
                MessageRecord.COLUMN_MESSAGE_TEXT + " TEXT, " +
                MessageRecord.COLUMN_USER_REPLY + " TEXT," +
                MessageRecord.COLUMN_DATE_TIME + " NUMERIC" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MessageRecord.MESSAGE_RECORD_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}

