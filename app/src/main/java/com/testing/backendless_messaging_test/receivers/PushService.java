package com.testing.backendless_messaging_test.receivers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.util.Log;
import android.widget.Toast;

import com.backendless.messaging.AndroidPushTemplate;
import com.backendless.messaging.PublishOptions;
import com.backendless.push.BackendlessBroadcastReceiver;
import com.backendless.push.BackendlessPushService;
import com.backendless.push.PushTemplateHelper;
import com.testing.backendless_messaging_test.R;
import com.testing.backendless_messaging_test.activities.MessagingTest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;



public class PushService extends BackendlessPushService
{

  private static final AtomicInteger ints = new AtomicInteger(  );

  @Override
  public void onError( Context context, String message )
  {
    super.onError( context, message );
  }

  @Override
  public boolean onMessage( Context context, Intent intent )
  {
    return handleAsDefault(context, intent);
  }

  @Override
  public void onUnregistered( Context context, Boolean unregistered )
  {
    super.onUnregistered( context, unregistered );
  }

  @Override
  public void onRegistered( Context context, String registrationId )
  {
    super.onRegistered( context, registrationId );
  }


  private boolean handleAsDefault(final Context context, Intent intent) {
    final String message = intent.getStringExtra(PublishOptions.MESSAGE_TAG);
    final String templateName  = intent.getStringExtra(PublishOptions.TEMPLATE_NAME);

    String header = intent.getStringExtra("header1");


    Log.i(MessagingTest.REGISTRATION_TAG, "Push message received. Message: " + message);

    Handler handler = new Handler(Looper.getMainLooper());
    handler.post(() -> Toast.makeText(context, "Push message received. Message: " + message, Toast.LENGTH_LONG).show());
    return false;
  }

}
