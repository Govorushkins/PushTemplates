package com.testing.backendless_messaging_test.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.testing.backendless_messaging_test.AppMode;
import com.testing.backendless_messaging_test.Defaults;
import com.testing.backendless_messaging_test.R;

/**
 * Created by alex on 2/12/16.
 */
public class MainMenu extends Activity
{
  private Button userTestButton;
  private Button messagingTestButton;
  private Button fenceTestButton;
  private Button pushLoadButton;

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_menu);

    Defaults.initApp(getApplicationContext());
    Defaults.applicationMode = AppMode.DEFAULT;

    initUI();
  }

  private void initUI()
  {
    userTestButton = (Button) findViewById( R.id.user_test_button );
    messagingTestButton = (Button) findViewById( R.id.messaging_test_button );
    fenceTestButton = (Button) findViewById( R.id.fence_test_button );
    pushLoadButton = (Button) findViewById( R.id.push_load_button );

    userTestButton.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        Intent intent = new Intent( getApplicationContext(), UserTest.class );
//        BackendlessUser user = new BackendlessUser();
//        user.setEmail( "test1@test.com" );
//        user.setPassword( "xxx" );
//        intent.putExtra( "test_user", user );
        startActivity( intent );
      }
    } );

    messagingTestButton.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        Intent intent = new Intent( getApplicationContext(), MessagingTest.class );
        startActivity( intent );
      }
    } );

    fenceTestButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent( getApplicationContext(), FenceTest.class );
        startActivity( intent );
      }
    });

    pushLoadButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent( getApplicationContext(), PushTest.class );
        startActivity( intent );
      }
    });
  }

}
