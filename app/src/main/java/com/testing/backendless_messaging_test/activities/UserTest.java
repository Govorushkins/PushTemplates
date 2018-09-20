package com.testing.backendless_messaging_test.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.testing.backendless_messaging_test.AppMode;
import com.testing.backendless_messaging_test.Defaults;
import com.testing.backendless_messaging_test.R;

/**
 * Created by alex on 2/12/16.
 */
public class UserTest extends Activity
{

  private static final String LOG_TAG = "USER_SERVICE_TEST";
  private static final String STAY_LOGGED_IN_FLAG_KEY = "stayLoggedIn";

  private EditText emailLoginEditText;
  private EditText passwordEditText;
  private CheckBox stayLoggedInCheckBox;
  private Button loginButton;
  private Button registerUserButton;
  private Button logoutButton;
  private Button checkLoggedInUserButton;

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView(R.layout.user_test);
    Defaults.applicationMode = AppMode.DEFAULT;

    initUI( savedInstanceState );
  }

  private void initUI( Bundle savedState )
  {

//    Intent intent = getIntent();
//    try
//    {
//      Object obj = intent.getExtras().get( "test_user" );
//      BackendlessUser receivedUser = (BackendlessUser) obj;
//    }
//    catch( Exception e )
//    {
//      e.printStackTrace();
//    }

    emailLoginEditText = (EditText) findViewById( R.id.email_login_edit_text );
    passwordEditText = (EditText) findViewById( R.id.password_edit_text );

    stayLoggedInCheckBox = (CheckBox) findViewById( R.id.stay_logged_in_checkbox );

    if ( savedState != null && savedState.getBoolean( STAY_LOGGED_IN_FLAG_KEY, false ) )
      stayLoggedInCheckBox.setChecked( true );

    loginButton = (Button) findViewById( R.id.login_button );
    logoutButton = (Button) findViewById( R.id.logout_button );
    registerUserButton = (Button) findViewById( R.id.register_user_button );
    checkLoggedInUserButton = (Button) findViewById( R.id.check_user_login_button );

    loginButton.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        String login = emailLoginEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        boolean stayLoggedIn = stayLoggedInCheckBox.isChecked();

        login( login, password, stayLoggedIn );
      }
    } );

    logoutButton.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        logout();
      }
    } );

    registerUserButton.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        register( prepareBackendlessUser() );
      }
    } );

    checkLoggedInUserButton.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        checkUserLogin();
      }
    } );

    checkLoggedInUserButton.setOnLongClickListener( new View.OnLongClickListener()
    {
      @Override
      public boolean onLongClick( View v )
      {
        checkCurrentUser();
        return false;
      }
    } );
  }

  @Override
  public void onSaveInstanceState( Bundle outState )
  {
    super.onSaveInstanceState( outState );

    if( stayLoggedInCheckBox != null )
      outState.putBoolean( STAY_LOGGED_IN_FLAG_KEY, stayLoggedInCheckBox.isChecked() );
  }

  private void register( final BackendlessUser user )
  {
    Backendless.UserService.register( user, new AsyncCallback<BackendlessUser>()
    {
      @Override
      public void handleResponse( BackendlessUser response )
      {
        Defaults.logEvent( getApplicationContext(), LOG_TAG, "User is registered. Email: " + response.getEmail() );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        Defaults.logEvent( getApplicationContext(), LOG_TAG, fault );
      }
    } );
  }

  private void login( final String login, final String password, final boolean stayLoggedIn )
  {
    Backendless.UserService.login( login, password, new AsyncCallback<BackendlessUser>()
    {
      @Override
      public void handleResponse( BackendlessUser response )
      {
        Defaults.logEvent( getApplicationContext(), LOG_TAG, "User " + response.getEmail() + " is logged in" );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        Defaults.logEvent( getApplicationContext(), LOG_TAG, fault );
      }
    }, stayLoggedIn );
  }

  private void logout()
  {
    Backendless.UserService.logout( new AsyncCallback<Void>()
    {
      @Override
      public void handleResponse( Void response )
      {
        Defaults.logEvent( getApplicationContext(), LOG_TAG, "User is logged out" );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        Defaults.logEvent( getApplicationContext(), LOG_TAG, fault );
      }
    } );
  }

  private void checkUserLogin()
  {
    Backendless.UserService.isValidLogin( new AsyncCallback<Boolean>()
    {
      @Override
      public void handleResponse( Boolean response )
      {
        Defaults.logEvent( getApplicationContext(), LOG_TAG, response ? "User is logged in" : "User is NOT logged in" );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        Defaults.logEvent( getApplicationContext(), LOG_TAG, fault );
      }
    } );
  }

  private void checkCurrentUser()
  {
    BackendlessUser user = Backendless.UserService.CurrentUser();

    Defaults.logEvent( getApplicationContext(), LOG_TAG, user == null ? "No current user" : "Current user email: " + user.getEmail() );
  }

  // TODO: implement user creation method - should take values from UI and return user ready for registration
  private BackendlessUser prepareBackendlessUser()
  {
    String email = emailLoginEditText.getText().toString();
    String password = passwordEditText.getText().toString();

    BackendlessUser user = new BackendlessUser();
    user.setEmail( email );
    user.setPassword( password );

    return user;
  }

}
