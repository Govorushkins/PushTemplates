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
  public static final String APPLICATION_ID = "5A37F7AE-192F-8036-FFED-32A46313B400";
  public static final String ANDROID_API_KEY = "317382F0-36FD-33B2-FFC6-7B4C837AE400";
  public static final String REST_API_KEY = "A969376B-1CBE-79D7-FF57-D4856E7A4E00";
  public static final String SERVER_URL = "https://apitest.backendless.com";
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

