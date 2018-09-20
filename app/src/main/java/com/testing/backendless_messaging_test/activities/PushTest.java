package com.testing.backendless_messaging_test.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.DeliveryOptions;
import com.backendless.messaging.MessageStatus;
import com.backendless.messaging.PublishOptions;
import com.backendless.messaging.PublishPolicyEnum;
import com.backendless.messaging.PushBroadcastMask;
import com.testing.backendless_messaging_test.AppMode;
import com.testing.backendless_messaging_test.Defaults;
import com.testing.backendless_messaging_test.R;

public class PushTest extends Activity {

    private Button startTestButton;
    private Button sendSinglePushButton;
    private Button goToMainMenuButton;
    private EditText delayField;
    private EditText messQuantityField;
    private EditText numOfThreadsField;

    private static final Long defaultDelay = 1000l;
    private static final int defaultNumOfMessages = 50;
    private static final int DEFAULT_NUM_OF_THREADS = 1;
    private static final DeliveryOptions deliveryOptions = new DeliveryOptions();
    private static final String LOG_TAG = "PushTestActivity";

    static {
        deliveryOptions.setPublishPolicy(PublishPolicyEnum.PUSH);
        deliveryOptions.setPushBroadcast(PushBroadcastMask.ANDROID);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_load);
        Defaults.applicationMode = AppMode.PUSH_LOAD;
        initUI();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Defaults.applicationMode = AppMode.DEFAULT;
    }

    private void initUI() {
        startTestButton = (Button) findViewById(R.id.start_test_button);
        sendSinglePushButton = (Button) findViewById(R.id.send_single_button);
        delayField = (EditText) findViewById(R.id.delay);
        messQuantityField = (EditText) findViewById(R.id.num_of_notifications);
        goToMainMenuButton = (Button) findViewById(R.id.go_to_main_menu);
        numOfThreadsField = (EditText) findViewById(R.id.num_of_threads);

        startTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartTestButtonClicked();
            }
        });

        sendSinglePushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendSinglePushButtonClicked();
            }
        });

        goToMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoToMainMenuButtonClick();
            }
        });
    }

    private void onStartTestButtonClicked() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Long delay;
                int numOfMessages;
                int numOfThreads;

                String numOfMessagesText = messQuantityField.getText().toString();
                String delayText = delayField.getText().toString();
                String numOfThreadsText = numOfThreadsField.getText().toString();

                if (!delayText.isEmpty()) {
                    delay = Long.parseLong(delayText);
                } else {
//          Toast.makeText( MyActivity.this, "Delay is set to " + DEFAULT_DELAY + " ms", Toast.LENGTH_SHORT ).show();
                    delay = defaultDelay;
                }

                if (!numOfMessagesText.isEmpty()) {
                    numOfMessages = Integer.parseInt(numOfMessagesText);
                } else {
//          Toast.makeText( MyActivity.this, "Number of messages is " + defaultNumOfMessages, Toast.LENGTH_SHORT ).show();
                    numOfMessages = defaultNumOfMessages;
                }

                if (!numOfThreadsText.isEmpty()) {
                    numOfThreads = Integer.parseInt(numOfThreadsText);
                } else {
                    numOfThreads = DEFAULT_NUM_OF_THREADS;
                }

                sendMultiplyPushes(numOfThreads, numOfMessages, delay);

            }

        }).start();

        Toast.makeText(PushTest.this, "Test started", Toast.LENGTH_SHORT).show();
    }

    private void onSendSinglePushButtonClicked() {
        sendSinglePush();

        Toast.makeText(PushTest.this, "Push sent", Toast.LENGTH_SHORT).show();
    }

    private void sendMultiplyPushes(final int numOfThreads, final int numberOfPushes, final Long delay) {
        for (int i = 0; i < numOfThreads; i++) {
            final int threadId = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < numberOfPushes; i++) {

                        try {
                            Thread.sleep(delay);
                        } catch (InterruptedException e) {
                            Log.e(LOG_TAG, e.getMessage());
                        }

                        PublishOptions publishOptions = new PublishOptions();
                        publishOptions.putHeader(PublishOptions.ANDROID_CONTENT_TEXT_TAG, "" + System.currentTimeMillis());
                        publishOptions.putHeader(PublishOptions.ANDROID_CONTENT_TITLE_TAG, "MessageId: " + i);
                        publishOptions.putHeader(PublishOptions.ANDROID_TICKER_TEXT_TAG, "ThreadId: " + threadId);
                        Backendless.Messaging.publish("default", "subtopic", publishOptions, deliveryOptions, new AsyncCallback<MessageStatus>() {
                            @Override
                            public void handleResponse(MessageStatus messageStatus) {
                                Log.i(LOG_TAG, "Message status: " + messageStatus);
                            }

                            @Override
                            public void handleFault(BackendlessFault backendlessFault) {
                                Log.i(LOG_TAG, "Publish failed: " + backendlessFault.toString());
                            }
                        });
                    }
                }
            }).start();
        }
    }

    private void sendSinglePush() {
        PublishOptions publishOptions = new PublishOptions();
        publishOptions.putHeader(PublishOptions.ANDROID_CONTENT_TEXT_TAG, "" + System.currentTimeMillis());
        publishOptions.putHeader(PublishOptions.ANDROID_CONTENT_TITLE_TAG, "Single message");

        Backendless.Messaging.publish("default", "subtopic", publishOptions, deliveryOptions, new AsyncCallback<MessageStatus>() {
            @Override
            public void handleResponse(MessageStatus messageStatus) {
                System.out.println("Single message published");
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                System.out.println("Message failed: " + backendlessFault.toString());
            }
        });
    }

    private void onGoToMainMenuButtonClick() {
        startActivity(new Intent(PushTest.this, MainMenu.class));
    }
}
