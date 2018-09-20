package com.testing.backendless_messaging_test.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.DeliveryOptions;
import com.backendless.messaging.MessageStatus;
import com.backendless.messaging.PublishOptions;
import com.backendless.messaging.PublishPolicyEnum;
import com.backendless.messaging.PushBroadcastMask;
import com.testing.backendless_messaging_test.Defaults;
import com.testing.backendless_messaging_test.R;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alex on 1/19/16.
 */
public class SendMessage extends Activity implements View.OnClickListener {

	// TODO: pushSingleCast
	// TODO: repeatEvery + repeatExpiresAt
	// TODO: pushBroadcast
	// TODO: publishAt

	private static final String TAG = "MESSAGING";

	private EditText messageField;
	private EditText subtopicField;
	private EditText cityField;
	private EditText stateField;
	private EditText publisherIdField;
	private Button sendMessageButton;
	private Switch osSwitcher;
	private RadioGroup publishPolicyGroup;
	private EditText publishAt_date;
	private EditText publishAt_time;
	private EditText repeatEvery;
	private EditText repeatExpiresAt;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-Y");
	private SimpleDateFormat timeFormat = new SimpleDateFormat("H:m:s");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configure_message);
		initUI();
	}

	private void initUI() {
		messageField = (EditText) findViewById(R.id.message_text);
		subtopicField = (EditText) findViewById(R.id.subtopic_field);
		cityField = (EditText) findViewById(R.id.city_field);
		stateField = (EditText) findViewById(R.id.state_field);
		publisherIdField = (EditText) findViewById(R.id.publisher_id_field);
		sendMessageButton = (Button) findViewById(R.id.send_message_btn);
		publishPolicyGroup = (RadioGroup) findViewById(R.id.publishPolicyGroup);

		publishAt_date = (EditText)findViewById(R.id.PublishAt_date_editText);
		publishAt_time = (EditText)findViewById(R.id.PublishAt_time_editText);
		repeatEvery = (EditText)findViewById(R.id.RepeatEvery_editText);
		repeatExpiresAt = (EditText)findViewById(R.id.RepeatExpiresAt_editText);

		Date currentDate = new Date();
		publishAt_date.setText(dateFormat.format(currentDate));
		publishAt_time.setText(timeFormat.format(currentDate));


		osSwitcher = (Switch) findViewById(R.id.os_switcher);

		osSwitcher.setTextColor(getResources().getColor(R.color.green));

		sendMessageButton.setOnClickListener(this);
		osSwitcher.setOnClickListener(this);





	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.send_message_btn:

				Object receivedChannel = getIntent().getExtras().get("CHANNEL");
				String channel = (receivedChannel != null && receivedChannel.toString().length() > 0) ? receivedChannel.toString() : null;
				String message = messageField.getText().toString();
				PublishOptions publishOptions = preparePublishOptions();
				DeliveryOptions deliveryOptions = prepareDeliveryOptions();

				if (!osSwitcher.isChecked()) {
					sendMessage(channel, message, deliveryOptions, publishOptions);
				} else {
					sendMessageRest(channel, message, deliveryOptions, publishOptions);
				}

				finish();
				break;

			case R.id.os_switcher:
				if (osSwitcher.isChecked()) {
					osSwitcher.setText("REST");
					osSwitcher.setTextColor(getResources().getColor(R.color.red));
				} else {
					osSwitcher.setText("Android");
					osSwitcher.setTextColor(getResources().getColor(R.color.green));
				}
				break;

			default:
				finish();
				break;
		}
	}

	private AsyncCallback<MessageStatus> messageCallback = new AsyncCallback<MessageStatus>() {
		@Override
		public void handleResponse(MessageStatus response) {
//      Log.i( TAG, "Message sent: " + response.getMessageId() );
			Defaults.logEvent(getApplicationContext(), TAG, "Message sent: " + response.getMessageId());
		}

		@Override
		public void handleFault(BackendlessFault fault) {
//      Log.i( TAG, String.format( "Failed. Code: %s Message: %s Detail: %s ", fault.getCode(), fault.getMessage(), fault.getDetail() ) );
			Defaults.logEvent(getApplicationContext(), TAG, fault);
		}
	};

	private void sendMessage(String channel, Object message, DeliveryOptions deliveryOptions,
							 PublishOptions publishOptions) {
		Backendless.Messaging.publish(channel == null ? "default" : channel, message, publishOptions, deliveryOptions, messageCallback);
	}

	private void sendMessageRest(String channel, Object message, DeliveryOptions deliveryOptions,
								 PublishOptions publishOptions) {

		final OkHttpClient client = new OkHttpClient();
		MediaType type = MediaType.parse("application/json; charset=utf-8");

		try {
			String url = Defaults.SERVER_URL + "/" + Defaults.APPLICATION_ID + "/" + Defaults.REST_API_KEY + "/messaging/" + ((channel != null && channel.length() > 0) ? channel : "default");
			String jsonBody = getRequestBody(message, deliveryOptions, publishOptions);
			RequestBody requestBody = RequestBody.create(type, jsonBody);

			final Request request = new Request.Builder()
					.url(url)
					.addHeader("Content-Type", "application/json")
					.post(requestBody)
					.build();

			new AsyncTask<String, String, String>() {
				@Override
				protected String doInBackground(String... params) {
					try {
						Response response = client.newCall(request).execute();
//            Log.i( "MESSAGING", "Response code: " + response.code() + "\nResponse body: " + response.body().string() );
						Defaults.logEvent(getApplicationContext(), TAG, "Response code: " + response.code() + "\nResponse body: " + response.body().string());
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			}.execute("Stub");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getRequestBody(Object message, DeliveryOptions deliveryOptions,
								  PublishOptions publishOptions) throws JSONException {
		JSONObject body = new JSONObject();

		body.put("message", message);
		body.put("publishPolicy", deliveryOptions.getPublishPolicy().toString());

		if (publishOptions != null) {
			if (publishOptions.getHeaders() != null && publishOptions.getHeaders().size() > 0)
				body.put("headers", new JSONObject(publishOptions.getHeaders()));

			if (publishOptions.getPublisherId() != null && publishOptions.getPublisherId().length() > 0)
				body.put("publisherId", publishOptions.getPublisherId());

			if (publishOptions.getSubtopic() != null && publishOptions.getSubtopic().length() > 0)
				body.put("subtopic", publishOptions.getSubtopic());
		}

		return body.toString();
	}

	private DeliveryOptions prepareDeliveryOptions() {
		DeliveryOptions options = new DeliveryOptions();
		options.setPublishPolicy(getSelectedPublishPolicy());
		options.setPushBroadcast(PushBroadcastMask.ALL);
//    options.setPushSinglecast( null );

/*
		Date date = null;
		try {
			Date d1 = dateFormat.parse(publishAt_date.getText().toString());
			Date d2 = timeFormat.parse(publishAt_time.getText().toString());
			date = new Date (d1.getTime()+d2.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
*/
//		options.setPublishAt(date);
//		options.setRepeatEvery(Long.getLong(publishAt_date.getText().toString()));
//		options.setRepeatExpiresAt(null);
		return options;
	}

	private PublishOptions preparePublishOptions() {
		String subtopic = subtopicField.getText().toString();
		String city = cityField.getText().toString();
		String state = stateField.getText().toString();
		String publisherId = publisherIdField.getText().toString();

		PublishOptions options = new PublishOptions();
		Map<String, String> headers = new HashMap<>();

		if (city.length() > 0)
			headers.put("city", city);

		if (state.length() > 0)
			headers.put("state", state);

		if (subtopic.length() > 0)
			options.setSubtopic(subtopic);

		if (publisherId.length() > 0)
			options.setPublisherId(publisherId);

		options.setHeaders(headers);

		return options;
	}

	private PublishPolicyEnum getSelectedPublishPolicy() {
		switch (publishPolicyGroup.getCheckedRadioButtonId()) {
			case R.id.push_btn:
				return PublishPolicyEnum.PUSH;

			case R.id.pubsub_btn:
				return PublishPolicyEnum.PUBSUB;

			case R.id.both_btn:
			default:
				return PublishPolicyEnum.BOTH;
		}
	}
}
