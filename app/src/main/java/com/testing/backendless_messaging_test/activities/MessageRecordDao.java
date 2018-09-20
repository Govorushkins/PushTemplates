package com.testing.backendless_messaging_test.activities;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MessageRecordDao {
    private MessageRecordDao(Context context) {

    }

    public static void saveToDB(Context context, MessageRecord msgRec) {
        SQLiteDatabase database = new SampleSQLiteDBHelper(context).getWritableDatabase();
        long newRowId = database.insert(MessageRecord.MESSAGE_RECORD_TABLE_NAME, null, msgRec.convertToContentValues());

    }

    public static void deleteAll(Context context) {
        SQLiteDatabase database = new SampleSQLiteDBHelper(context).getWritableDatabase();
        database.delete(MessageRecord.MESSAGE_RECORD_TABLE_NAME, null, null );
    }

    public static List<MessageRecord> readAllFromDB(Context context) {
        SQLiteDatabase database = new SampleSQLiteDBHelper(context).getReadableDatabase();

        Cursor cursor = database.query(
                MessageRecord.MESSAGE_RECORD_TABLE_NAME,   // The table to query
                null,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // don't sort
        );

        ArrayList<MessageRecord> msgRecords = new ArrayList<>();

        while (cursor.moveToNext()) {
            MessageRecord rec = new MessageRecord();
            rec.setId(cursor.getInt(0));
            rec.setAction(cursor.getString(1));
            rec.setTemplateName(cursor.getString(2));
            rec.setMessageId(cursor.getInt(3));
            rec.setMessageText(cursor.getString(4));
            rec.setUserReply(cursor.getString(5));
            rec.setDateTime(new Date(cursor.getLong(6)));
            msgRecords.add(rec);
        }
        cursor.close();

        return msgRecords;
    }
}
