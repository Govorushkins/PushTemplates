package com.testing.backendless_messaging_test;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.exceptions.BackendlessFault;

// key: AIzaSyCjZ_k_T03tbilN8446SC4reSgOu7qwmmo
// gcm: 652990554361
// fcm key: AAAAuo4KgrI:APA91bEowpgQuaxpB2QS9UEvXi5wKX0JUe4EN-lQcNP-I7aZXegtynkTCR5ykSpyb54AsCu09nokQzVaFDK7fmoZxPaB9RXp_tWu7wAOqZYn--z_2FaxsbR180psqrOGQBuC7hTtjeyt
// fcm sender: 801246970546

public class Defaults
{
  public static AppMode applicationMode;

  // production section
  public static final String APPLICATION_ID = "2583DDF3-9D18-859D-FF08-10CB7E073B00";
  public static final String ANDROID_API_KEY = "D03B9AF6-3B9F-716A-FFF3-0E1D5D6ABD00";
  public static final String REST_API_KEY = "235AA146-2EF4-22E7-FF2C-313AA859C900";
  public static final String SERVER_URL = "http://10.0.1.14:9000";
/*
  public static final String APPLICATION_ID = "67D0DFF2-C1BA-CE27-FFE5-ACFB006D0C00";
  public static final String ANDROID_API_KEY = "643B7C70-1C0F-B02C-FF9A-EE1F2BD11800";
  public static final String REST_API_KEY = "A7CC80B0-1D8A-B6E9-FF4E-EABF751C6F00";
  public static final String SERVER_URL = "https://api.backendless.com";
*/
  public static final String GCMSenderId = "801246970546";

  public static final String SELECTOR_TAG = "SELECTOR";
  public static final String SUBTOPIC_TAG = "SUBTOPIC";

  private static boolean isInitialised = false;

  public static void initApp( Context context )
  {
    if( !isInitialised )
    {
      Backendless.setUrl( SERVER_URL );
      Backendless.initApp( context, APPLICATION_ID, ANDROID_API_KEY );
      isInitialised = true;
    }
  }

  public static void logEvent( final Context context, final String tag, final String message )
  {
    Log.i( tag, message );
    showToaster( context, message );
  }

  public static void logEvent( final Context context, final String tag, final BackendlessFault fault )
  {
    StringBuilder message = new StringBuilder();
    message.append( "\nMessage: " )
            .append( fault.getMessage() )
            .append( "\nCode: " )
            .append( fault.getCode() )
            .append( "\nDetail: " )
            .append( fault.getDetail() );

    Log.e( tag, message.toString() );

    Toast.makeText( context, message.toString(), Toast.LENGTH_LONG ).show();
  }

  private static void showToaster( final Context context, final String message )
  {
    Toast.makeText( context, message, Toast.LENGTH_LONG ).show();
  }

}

