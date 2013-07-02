// Version Log
// 1 - Connecting Bluetooth and making the beep tone
// 2 - First MainActivity and Control
// 3 - Getting data from ultrasonic sensor
// 4 - Functioning Motor and its data structure
// 5 - Image enabling Bluetooth
// 6 - Ultrasonic sensor's method
// 7 - Motor position
// 8 - Motor position's method
// 9 - Reset motor's position and its method
// 10 - NXTValue class
// 11 - Distance PID
// 12 - Speed PID
// 13 - Combined PID
// 14 - Improved NXTValue class
// 15 - New look for MainActivity
// 16 - Navigating activity
// 17 - Robot class
// 18 - Tilt to navigate activity
// 19 - Communication and robot class bugs fix

package ctech.nxtuniverse;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class Activity_Main extends Activity {

	public static final String VERSION = "NXT Universe V 0.19 Alpha";

	private BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
	private ImageView xImage, bluetooth;
	private Button connect, disconnect;
	private TextView versionDisplay;

	private SharedPreferences pref;
	private SharedPreferences.Editor prefEdit;

	public static Robot[] robot = new Robot[1];

	// Robot 1 - "00:16:53:12:AA:37"
	// Robot 2 - "00:12:53:12:AA:D5"

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Version run tracking
		pref = this.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
		prefEdit = pref.edit();
		String storedVersion = pref.getString("Version", "");
		String currentVersion = VERSION;
		int currentRun = 1;
		if (storedVersion.equals(currentVersion)) {
			currentRun = pref.getInt("Run", -1) + 1;
			Log.i("Run", currentRun + "");
			prefEdit.putInt("Run", currentRun);
		} else {
			prefEdit.putString("Version", VERSION);
			prefEdit.putInt("Run", 1);
		}
		prefEdit.commit();

		// Version display TextView
		versionDisplay = (TextView) findViewById(R.id.textView_version);
		versionDisplay.setText(VERSION + " Run: " + currentRun);

		// Initializing robots
		for (int i = 0; i < robot.length; i++) {
			robot[i] = new Robot();
		}

		// xImage
		xImage = (ImageView) findViewById(R.id.imageView_xImage);
		xImage.setOnClickListener(new View.OnClickListener() {

			int counter = 0;

			@Override
			public void onClick(View v) {
				counter++;
				if (counter == 3) {
					Intent startControl = new Intent("nxtuniverse.CONTROL");
					startActivity(startControl);
					counter = 0;
				}
			}
		});

		// Bluetooth image
		bluetooth = (ImageView) findViewById(R.id.imageView_bluetooth);

		// Sets alive/dead Bluetooth logo
		if (adapter.isEnabled()) { // If Bluetooth is on, set alive logo
			bluetooth.setImageResource(R.drawable.bluetooth_alive);
		} else { // Else, set dead logo
			bluetooth.setImageResource(R.drawable.bluetooth_dead);
		}

		bluetooth.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// To synchronize Bluetooth of the Android device and the image
				// boolean enabling = false;

				if (adapter.isEnabled()) {
					adapter.disable();
					while (adapter.isEnabled()) {
						// Empty while loop.
						// Purpose: wait until Bluetooth on the Android device
						// is disabled.
					}
					bluetooth.setImageResource(R.drawable.bluetooth_dead);
				} else {
					// enabling = true;
					adapter.enable();
					while (!adapter.isEnabled()) {
						// Empty while loop.
						// Purpose: wait until Bluetooth on the Android device
						// is enabled.
					}
					bluetooth.setImageResource(R.drawable.bluetooth_alive);
				}
			}
		});

		// Buttons
		connect = (Button) findViewById(R.id.button_connect);
		disconnect = (Button) findViewById(R.id.button_disconnect);

		connect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				robot[0] = new Robot();

				robot[0].setMacAddress("00:16:53:12:AA:37");
				robot[0].setRightMotorPort(NXTValue.MOTOR_PORT_B);
				robot[0].setLeftMotorPort(NXTValue.MOTOR_PORT_C);
				// robot1.setMacAddress("00:12:53:12:AA:D5");

				boolean connected = robot[0].connect();
				if (connected) {
					xImage.setImageResource(R.drawable.connection_connected);
					robot[0].write(NXTValue.CONFIRMATION_TONE);
				} else {
					xImage.setImageResource(R.drawable.connection_disconnected);
				}
			}
		});

		disconnect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean success = robot[0].disconnect();

				if (success) {
					xImage.setImageResource(R.drawable.connection_disconnected);
				} else {
					xImage.setImageResource(R.drawable.connection_connected);
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		if (item.getItemId() == R.id.menu_main_navigate) {
			Intent startNavigate = new Intent("nxtuniverse.NAVIGATE");
			startActivity(startNavigate);
		} else if (item.getItemId() == R.id.menu_main_tilt_navigate) {
			Intent startTiltNavigate = new Intent("nxtuniverse.TILT_NAVIGATE");
			startActivity(startTiltNavigate);
		} else if (item.getItemId() == R.id.menu_main_about) {
			Intent startAbout = new Intent("nxtuniverse.ABOUT");
			startActivity(startAbout);
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		for (int i = 0; i < robot.length; i++) {
			robot[i].disconnect();
		}
		finish();
	}
}