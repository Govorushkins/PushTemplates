package com.testing.backendless_messaging_test.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.messaging.PublishOptions;
import com.backendless.push.BackendlessBroadcastReceiver;
import com.backendless.push.BackendlessPushService;
import com.testing.backendless_messaging_test.R;
import com.testing.backendless_messaging_test.receivers.PushReceiver;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MessageHistoryActivity extends AppCompatActivity {

    // buttons
    private static Button refreshButton;
    private static Button deleteAllButton;
    private static TableLayout resultTable;
    private SwipeRefreshLayout swipe_container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_history);

        Intent intent = getIntent();
        if (intent.getAction() != null)
            handleIntent(intent);

        refreshButton = findViewById(R.id.button_refresh);
        deleteAllButton = findViewById(R.id.button_deleteall);
        resultTable = findViewById(R.id.table_resultList);

        addHeaders();
        addData();

        refreshButton.setOnClickListener(v -> {
            resultTable.removeAllViews();
            addHeaders();
            addData();
        });

        deleteAllButton.setOnClickListener(v -> {
            MessageRecordDao.deleteAll(this);
            resultTable.removeAllViews();
        });

        swipe_container = findViewById(R.id.swipe_container);
        swipe_container.setOnRefreshListener(() -> {
            new Handler().postDelayed( () -> swipe_container.setRefreshing(false), 1000);
            resultTable.removeAllViews();
            addHeaders();
            addData();
        });
    }

    private void handleIntent(Intent intent) {
        final String action = intent.getAction();
        final String templateName = intent.getStringExtra(PublishOptions.TEMPLATE_NAME);
        final String messageText = intent.getStringExtra(PublishOptions.MESSAGE_TAG);
        Log.i("MessagingTest", "templateName:" + Objects.toString(templateName) + "; action:" + action + "; messageId:" + messageText + "; templateName:" + templateName);

        MessageRecord msgRecord = new MessageRecord();
        msgRecord.setAction(action);
        msgRecord.setTemplateName(templateName);
        msgRecord.setMessageText(messageText);

        hideNotification(templateName);

        if (action.equals("A.action2")) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> Toast.makeText(this, "User choice: " + intent.getAction(), Toast.LENGTH_LONG).show());
        }

        if (action.equals("A.action1")) {
            Bundle remoteInput = RemoteInput.getResultsFromIntent(getIntent());
            String reply = (String) remoteInput.getCharSequence("inline_reply");
            Log.i("MessagingTest", "Reply: " + reply);
            msgRecord.setUserReply(reply);

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> Toast.makeText(this, "User choice: " + intent.getAction() + "\nUser response: " + reply, Toast.LENGTH_LONG).show());
        }

        msgRecord.setDateTime(new Date());
        MessageRecordDao.saveToDB(getApplicationContext(), msgRecord);
    }


    private void hideNotification(String tag) {
        Log.i("MessagingTest", "hideNotification - " + tag );
        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from( getApplicationContext() );
        notificationManager.cancel(1);

        // For update
        /*
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setDefaults(Notification.DEFAULT_ALL);

            notificationManager.notify(notificationId, builder.build());
        */
    }

    private void addHeaders() {
        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        for (String colName : MessageRecord.columns) {
            TextView colHeader = new TextView(this);
            colHeader.setText(colName);
            colHeader.setTextColor(Color.DKGRAY);
            colHeader.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            colHeader.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            colHeader.setPadding(5, 5, 5, 5);
            headerRow.addView(colHeader);  // Adding textView to tablerow.
        }
        resultTable.addView(headerRow);
    }

    private void addData() {
        List<MessageRecord> messages = MessageRecordDao.readAllFromDB(getApplicationContext());

        for (MessageRecord msgRec : messages) {
            TableRow valueRow = new TableRow(this);
            valueRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            for (String colName : MessageRecord.columns)
            {
                ContentValues values = msgRec.convertToContentValues();
                TextView colWithData = new TextView(this);
                colWithData.setText(values.getAsString(colName));
                colWithData.setTextColor(Color.DKGRAY);
                colWithData.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                colWithData.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                colWithData.setPadding(3, 3, 3, 3);
                valueRow.addView(colWithData);
            }
            resultTable.addView(valueRow);
        }

    }
}
