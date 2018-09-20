package com.testing.backendless_messaging_test.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.testing.backendless_messaging_test.Defaults;
import com.testing.backendless_messaging_test.R;

/**
 * Created by alex on 1/18/16.
 */
public class ConfigureSubscription extends Activity implements View.OnClickListener
{

  private EditText selectorField;
  private EditText subtopicField;
  private Button ok_button;

  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.configure_subscription );

    initUI();
  }

  private void initUI()
  {
    selectorField = (EditText)findViewById( R.id.selector_field );
    subtopicField = (EditText)findViewById( R.id.subtopic_field );
    ok_button = (Button)findViewById( R.id.subscription_conf_btn );

    ok_button.setOnClickListener( this );
  }

  @Override
  public void onClick( View v )
  {
    Intent intent = new Intent();
    intent.putExtra( Defaults.SUBTOPIC_TAG, subtopicField.getText().toString() );
    intent.putExtra( Defaults.SELECTOR_TAG, selectorField.getText().toString() );
    setResult( RESULT_OK, intent );
    finish();
  }
}
