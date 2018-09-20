package com.testing.backendless_messaging_test.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.DeviceRegistration;
import com.backendless.Subscription;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.Message;
import com.backendless.messaging.PublishOptions;
import com.backendless.messaging.SubscriptionOptions;
import com.backendless.push.BackendlessBroadcastReceiver;
import com.testing.backendless_messaging_test.Defaults;
import com.testing.backendless_messaging_test.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MessagingTest extends Activity implements View.OnClickListener, View.OnLongClickListener
{

  public static final String SUBSCRIPTION_TAG = "SUBSCRIPTION";
  public static final String REGISTRATION_TAG = "REGISTRATION";
  public static final String COMMON_TAG = "MESSAGING_TEST_INFO";

  private static final String FIRST_CHANNEL_NAME = "first";
  private static final String SECOND_CHANNEL_NAME = "second";
  private static final String THIRD_CHANNEL_NAME = "third";

  // check boxes
  private static CheckBox firstChannelBox;
  private static CheckBox secondChannelBox;
  private static CheckBox thirdChannelBox;

  // buttons
  private static Button registerButton;
  private static Button unregisterButton;
  private static Button subscribeButton;
  private static Button unsubscribeButton;
  private static Button showStatusButton;
  private static Button messageHistory;
  private static Button sendMessageButton;

  private static Map<String, Subscription> subscriptionsMap = new HashMap<>();


  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView(R.layout.messaging_test);
    initUI();
  }

  private void initUI()
  {
    firstChannelBox = (CheckBox) findViewById( R.id.channel_1 );
    secondChannelBox = (CheckBox) findViewById( R.id.channel_2 );
    thirdChannelBox = (CheckBox) findViewById( R.id.channel_3 );

    registerButton = (Button) findViewById( R.id.register_btn );
    unregisterButton = (Button) findViewById( R.id.unregister_btn );
    subscribeButton = (Button) findViewById( R.id.subscribe_btn );
    unsubscribeButton = (Button) findViewById( R.id.unsubscribe_btn );
    showStatusButton = (Button) findViewById( R.id.show_status_btn );
    messageHistory = (Button) findViewById( R.id.message_history );
    sendMessageButton = (Button) findViewById( R.id.send_message_btn );

    registerButton.setOnClickListener( this );
    unregisterButton.setOnClickListener( this );
    subscribeButton.setOnClickListener( this );
    unsubscribeButton.setOnClickListener( this );
    showStatusButton.setOnClickListener( this );
    messageHistory.setOnClickListener(this);
    sendMessageButton.setOnClickListener( this );

    unsubscribeButton.setOnLongClickListener( this );


  }

  private void registerDevice()
  {
    Backendless.Messaging.registerDevice(Defaults.GCMSenderId, registrationCallback);
  }

  private void registerDevice(Date expiration)
  {
    Backendless.Messaging.registerDevice( Defaults.GCMSenderId, null, expiration, registrationCallback );
  }

  private void registerDevice( String channel )
  {
    Backendless.Messaging.registerDevice( Defaults.GCMSenderId, channel, registrationCallback );
  }

  private void registerDevice( List<String> channels )
  {
    Date expirationDate = new Date( System.currentTimeMillis() + 60 * 60 * 1000 );
    Backendless.Messaging.registerDevice( Defaults.GCMSenderId, channels, null, registrationCallback );
  }

//  private void subscribeDevice()
//  {
//    Backendless.Messaging.subscribe( pubSubHandler, subscriptionCallback );
//  }
//
//  private void subscribeDevice( String channel )
//  {
//    Backendless.Messaging.subscribe( channel, pubSubHandler, subscriptionCallback );
//  }
//
//  private void subscribeDevice( SubscriptionOptions options )
//  {
//    Backendless.Messaging.subscribe( pubSubHandler, options, subscriptionCallback );
//  }
//
//  private void subscribeDevice( String channel, SubscriptionOptions options )
//  {
//    Backendless.Messaging.subscribe( channel, pubSubHandler, options, subscriptionCallback );
//  }

  private AsyncCallback<Void> registrationCallback = new AsyncCallback<Void>()
  {
    @Override
    public void handleResponse( Void aVoid )
    {
      Defaults.logEvent( getApplicationContext(), REGISTRATION_TAG, "Device registered" );
    }

    @Override
    public void handleFault( BackendlessFault backendlessFault )
    {
      Defaults.logEvent( getApplicationContext(), REGISTRATION_TAG, backendlessFault );
    }
  };

  private AsyncCallback<String> fcmRegistrationCallback = new AsyncCallback<String>() {
    @Override
    public void handleResponse(String response) {
      Defaults.logEvent(getApplicationContext(), REGISTRATION_TAG, "Device registered. Response: " + response);
    }

    @Override
    public void handleFault(BackendlessFault fault) {
      Defaults.logEvent(getApplicationContext(), REGISTRATION_TAG, fault);
    }
  };

  private AsyncCallback<Subscription> subscriptionCallback = new AsyncCallback<Subscription>()
  {
    @Override
    public void handleResponse( Subscription subscription )
    {
      subscriptionsMap.put( subscription.getChannelName(), subscription );
      Defaults.logEvent( getApplicationContext(), SUBSCRIPTION_TAG, "Subscribed to " + subscription.getChannelName() + " channel" );
    }

    @Override
    public void handleFault( BackendlessFault backendlessFault )
    {
      Defaults.logEvent( getApplicationContext(), SUBSCRIPTION_TAG, backendlessFault );
    }
  };

  private AsyncCallback<List<Message>> pubSubHandler = new AsyncCallback<List<Message>>()
  {
    @Override
    public void handleResponse( List<Message> messages )
    {
      if( messages == null )
      {
//        Log.i( SUBSCRIPTION_TAG, "empty messages list" );
        Defaults.logEvent( getApplicationContext(), SUBSCRIPTION_TAG, "empty messages list" );
      }
      else
      {
        for( Message message : messages )
        {
//          Log.i( SUBSCRIPTION_TAG, "Length: " + message.getData().toString().length() + " " + message.getData().toString() );
//          Toast.makeText( , "Length: " + message.getData().toString().length() + " " + message.getData().toString(), Toast.LENGTH_LONG ).show();
          Defaults.logEvent( getApplicationContext(), SUBSCRIPTION_TAG, "Length: " + message.getData().toString().length() + " " + message.getData().toString() );
        }
      }
    }

    @Override
    public void handleFault( BackendlessFault backendlessFault )
    {
//      Log.e( SUBSCRIPTION_TAG, "PubSub error: " + backendlessFault.getMessage() );
      Defaults.logEvent( getApplicationContext(), SUBSCRIPTION_TAG, backendlessFault );
    }
  };

  private AsyncCallback<Void> unRegistrationCallback = new AsyncCallback<Void>()
  {
    @Override
    public void handleResponse( Void aVoid )
    {
//      Log.i( REGISTRATION_TAG, "Device unregistred" );
      Defaults.logEvent( getApplicationContext(), REGISTRATION_TAG, "Device unregistered" );
    }

    @Override
    public void handleFault( BackendlessFault backendlessFault )
    {
//      Log.e( REGISTRATION_TAG, "Unregistration failed. " + backendlessFault.getMessage() );
      Defaults.logEvent( getApplicationContext(), REGISTRATION_TAG, backendlessFault );
    }
  };

  private void unRegisterDevice()
  {
    Backendless.Messaging.unregisterDevice( unRegistrationCallback );

  }

  private void unSubscribeDevice( String channel )
  {
    if( subscriptionsMap.containsKey( channel ) )
    {
//      Backendless.Messaging.cancelSubscription( subscriptionsMap.get( channel ) );
      subscriptionsMap.get( channel ).cancelSubscription();
//      Log.i( SUBSCRIPTION_TAG, "unsubscribed from " + channel );
      Defaults.logEvent( getApplicationContext(), SUBSCRIPTION_TAG, "unsubscribed from " + channel );
      subscriptionsMap.remove( channel );
    }
    else
    {
      Log.e( SUBSCRIPTION_TAG, "Unsubscription failed - device is not subscribed" );
    }
  }

  @Override
  public void onClick( View view )
  {
    List<String> checkedChannels = getCheckedChannels();

    switch( view.getId() )
    {
      case R.id.register_btn:
        if( checkedChannels.size() == 0 )
        {
          registerDevice();
        }
        else if( checkedChannels.size() == 1 )
        {
          registerDevice( checkedChannels.get( 0 ) );
        }
        else
        {
          registerDevice( checkedChannels );
        }
        break;

      case R.id.unregister_btn:
        unRegisterDevice();
        break;

      case R.id.subscribe_btn:
        Intent intent = new Intent( this, ConfigureSubscription.class );
        startActivityForResult( intent, 1 );
        break;

      case R.id.unsubscribe_btn:
        if( checkedChannels.size() == 0 )
        {
          unSubscribeDevice( "default" );
        }
        else if( checkedChannels.size() == 1 )
        {
          unSubscribeDevice( checkedChannels.get( 0 ) );
        }
        else
        {
          Toast.makeText( this, "Multiple channels chosen", Toast.LENGTH_SHORT ).show();
        }
        break;

      case R.id.show_status_btn:

        Backendless.Messaging.getRegistrations( new AsyncCallback<DeviceRegistration>()
        {
          @Override
          public void handleResponse( DeviceRegistration response )
          {
            List<String> channels = response == null ? null : response.getChannels();
//            Log.i( COMMON_TAG, String.format( "\n*** CURRENT STATUS ***\n" +
//                                                      "Registered to: %s\n" +
//                                                      "Subscribed to: %s", channels, subscriptionsMap.keySet() ) );
            Defaults.logEvent( getApplicationContext(), COMMON_TAG, String.format( "\n*** CURRENT STATUS ***\n" +
                                                                                           "Registered to: %s\n" +
                                                                                           "Subscribed to: %s", channels, subscriptionsMap.keySet() ) );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            if( fault.getMessage().equals( "Unable to retrieve device. Invalid device ID." ) )
            {
//              Log.i( COMMON_TAG, String.format( "\n*** CURRENT STATUS ***\n" +
//                                                        "Registered to: %s\n" +
//                                                        "Subscribed to: %s", "[]", subscriptionsMap.keySet() ) );
              Defaults.logEvent( getApplicationContext(), COMMON_TAG, String.format( "\n*** CURRENT STATUS ***\n" +
                                                                                             "Registered to: %s\n" +
                                                                                             "Subscribed to: %s", "[]", subscriptionsMap.keySet() ) );
            }
            else
            {
//              Log.e( REGISTRATION_TAG, fault.getMessage() + fault.getDetail() );
              Defaults.logEvent( getApplicationContext(), REGISTRATION_TAG, fault );
            }
          }
        } );

        break;

      case R.id.message_history:
        Intent messageHistoryIntent = new Intent(this, MessageHistoryActivity.class);
        this.startActivity(messageHistoryIntent);
        break;

      case R.id.send_message_btn:

        Intent sendMessageIntent = new Intent( this, SendMessage.class );
        String channel = getCheckedChannels().size() > 0 ? getCheckedChannels().get( 0 ) : "default";
        sendMessageIntent.putExtra( "CHANNEL", channel );
        startActivity( sendMessageIntent );
        break;
    }
  }

  @Override
  protected void onActivityResult( int requestCode, int resultCode, Intent data )
  {
    List<String> checkedChannels = getCheckedChannels();

    if ( resultCode != RESULT_OK )
      return;

    if ( data == null )
      return;

    String selector = data.getStringExtra( Defaults.SELECTOR_TAG );
    String subtopic = data.getStringExtra( Defaults.SUBTOPIC_TAG );
    SubscriptionOptions options = null;

    if ( ( selector != null && selector.length() > 0 ) || ( subtopic != null && subtopic.length() > 0 ) )
    {
      options = new SubscriptionOptions();

      if ( selector != null && selector.length() > 0 )
        options.setSelector( selector );

      if ( subtopic != null && subtopic.length() > 0 )
        options.setSubtopic( subtopic );

    }

    if( checkedChannels.size() == 0 )
    {
      if ( options == null )
      {
        // subscribe to default without options
//        subscribeDevice();
      }
      else
      {
        // subscribe to default with options
//        subscribeDevice( options );
      }
    }
    else if( checkedChannels.size() == 1 )
    {
      if ( options == null )
      {
        // subscribe to selected channel with options
//        subscribeDevice( checkedChannels.get( 0 ) );
      }
      else
      {
        // subscribe to selected channel with options
//        subscribeDevice( checkedChannels.get( 0 ), options );
      }
    }
    else
    {
      Toast.makeText( this, "Multiple channels chosen", Toast.LENGTH_SHORT ).show();
    }
  }

  private List<String> getCheckedChannels()
  {
    List<String> checkedChannels = new ArrayList<>();
    if( firstChannelBox.isChecked() )
      checkedChannels.add( FIRST_CHANNEL_NAME );

    if( secondChannelBox.isChecked() )
      checkedChannels.add( SECOND_CHANNEL_NAME );

    if( thirdChannelBox.isChecked() )
      checkedChannels.add( THIRD_CHANNEL_NAME );

    return checkedChannels;
  }

  @Override
  public boolean onLongClick( View v )
  {
    switch( v.getId() )
    {
      case R.id.unsubscribe_btn:
        int count = subscriptionsMap.keySet().size();

        for ( Subscription s : subscriptionsMap.values() )
          s.cancelSubscription();

        subscriptionsMap.clear();
//        Log.i( SUBSCRIPTION_TAG, count + " subscriptions cancelled" );
        Defaults.logEvent( getApplicationContext(), SUBSCRIPTION_TAG, count + " subscriptions cancelled" );
        break;

      default:
        break;
    }

    return false;
  }
}
