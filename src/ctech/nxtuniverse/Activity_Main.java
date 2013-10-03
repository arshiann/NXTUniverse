/* Version Log
 * 
 * ============================ ALPHA ============================
 * 0.21 - Introduction of the joysitck
 * 0.20 - mainActivity layout better organized in terms of coding
 *      - onStart, prompt user to enable Bluetooth if it is off.
 *      - hidden control activity - tap the "X" 3 times
 *      - OtherDevices Activity - to connect to other NXT device
 *        - getPaired device - device that are already paired to
 *          the phone
 *        - Search for device - search for new devices
 *        - connect to a new device
 *      - NXT ultrasonic logo beside NXT Device
 * 0.19 - Communication and robot class bugs fix
 * 0.18 - Tilt to navigate function and activity
 * 0.17 - Robot class
 * 0.16 - Navigating activity
 * 0.15 - New look for MainActivity
 * 0.14 - Improved NXTValue class. No visible binary
 *        value in main classes
 * 0.13 - Combined PID
 * 0.12 - Speed PID
 * 0.11 - Distance PID
 * 0.10 - NXTValue class. Start of the disappearance
 *        of binary values in main classes. 
 * 0.9 - Reset motor's position and a method for it
 * 0.8 - Motor position's method
 * 0.7 - Motor position (degree of roration)
 * 0.6 - Ultrasonic sensor's method
 * 0.5 - Image enabling Bluetooth
 * 0.4 - Writing data to operate motors on NXT device
 * 0.3 - Reading data from ultrasonic sensor successfully
 * 0.2 - First MainActivity and ControlActivity
 * 0.1 - Connecting to a NXT device and making the beep tone
 * 
 */

/*
 * XXX - Task list
 * - Map navigation
 *   - Grid navigation
 *   - turns
 *   - auto calibrate rotation/cm
 *   - speed pid for both wheels and not just right one
 *   - identify physical object and mark them on map
 *   - avoid known physical objects and look out for unknown physical object
 *   - navigate maze and remember shortest path
 * 
 * - Add pearson logo on about page and list of members.
 */

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
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;

public class Activity_Main extends Activity {

	public static final String VERSION = "NXT Universe V 0.21 Alpha";

	// Views
	private ImageView xImage;
	private Button connect, disconnect, scanDevice;
	private TextView versionDisplay;

	protected static BluetoothAdapter btAdapter = BluetoothAdapter
			.getDefaultAdapter();
	private static boolean turnedBtOn;

	private SharedPreferences pref;
	private SharedPreferences.Editor prefEdit;

	protected static Robot[] robot = new Robot[1];

	// Robot 1 - "00:16:53:12:AA:37"
	// Robot 2 - "00:12:53:12:AA:D5"

	protected static int connectionStatus = 0;
	protected static final int CONNECTION_DEFAULT = 0;
	protected static final int CONNECTION_DISCONNECTED = 1;
	protected static final int CONNECTION_CONNECTED = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Views
		xImage = (ImageView) findViewById(R.id.main_imageView_xImage);
		connect = (Button) findViewById(R.id.main_button_connect);
		disconnect = (Button) findViewById(R.id.main_button_disconnect);
		versionDisplay = (TextView) findViewById(R.id.main_textView_version);
		scanDevice = (Button) findViewById(R.id.main_button_otherDevice);

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
		versionDisplay.setText(VERSION + " - Run: " + currentRun);

		// Initializing robots
		for (int i = 0; i < robot.length; i++) {
			robot[i] = new Robot();
		}

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

		connect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				robot[0] = new Robot();

				robot[0].setMacAddress("00:16:53:12:AA:37");
				robot[0].setRightMotorPort(NXTValue.MOTOR_PORT_B);
				robot[0].setLeftMotorPort(NXTValue.MOTOR_PORT_C);
				// robot1.setMacAddress("00:12:53:12:AA:D5");

				if (robot[0].connect()) {
					connectionStatus = CONNECTION_CONNECTED;
					robot[0].write(NXTValue.CONFIRMATION_TONE);
				} else {
					connectionStatus = CONNECTION_DISCONNECTED;
				}
				updateConnectionImage();
			}
		});

		disconnect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				robot[0].disconnect();
				connectionStatus = CONNECTION_DISCONNECTED;
				updateConnectionImage();
			}
		});

		scanDevice.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent startOtherDevice = new Intent(
						"nxtuniverse.OTHER_DEVICES");
				startActivity(startOtherDevice);
			}
		});

		// {
		// robot[0] = new Robot();
		//
		// robot[0].setMacAddress("00:16:53:12:AA:37");
		// robot[0].setRightMotorPort(NXTValue.MOTOR_PORT_B);
		// robot[0].setLeftMotorPort(NXTValue.MOTOR_PORT_C);
		// // robot1.setMacAddress("00:12:53:12:AA:D5");
		//
		// boolean connected = robot[0].connect();
		// if (connected) {
		// robot[0].write(NXTValue.CONFIRMATION_TONE);
		// }
		//
		// Intent startControl = new Intent("nxtuniverse.CONTROL");
		// startActivity(startControl);
		// }

	}

	@Override
	protected void onResume() {
		super.onResume();

		updateConnectionImage();

		if (btAdapter == null) {
			AlertDialog.Builder alert = new Builder(this);
			alert.setTitle("Bluetooth");
			alert.setMessage("Bluetooth hardware not found");

			alert.setNegativeButton("Exit the app", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			alert.create().show();
		} else {
			if (!btAdapter.isEnabled()) { // Bluetooth is off
				turnedBtOn = true;
				Intent enableBtIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, 0);
			}
		}
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
//		} else if (item.getItemId() == R.id.menu_main_compass) {
////			Intent startCompass = new Intent("nxtuniverse.COMPASS");
////			startActivity(startCompass);
		} else if (item.getItemId() == R.id.menu_main_joystick) {
//			Intent startJoystick = new Intent(this, Activity_Joystick.class);
//			startActivity(startJoystick);
			Intent startJoystick = new Intent("nxtuniverse.JOYSTICK");
			startActivity(startJoystick);
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		for (int i = 0; i < robot.length; i++) {
			robot[i].disconnect();
		}
		if (turnedBtOn){
			BluetoothAdapter.getDefaultAdapter().disable();
		}
		finish();
	}

	private void updateConnectionImage() {
		if (connectionStatus == CONNECTION_DEFAULT) {
			xImage.setImageResource(R.drawable.ic_launcher);
		} else if (connectionStatus == CONNECTION_CONNECTED) {
			xImage.setImageResource(R.drawable.connection_connected);
		} else if (connectionStatus == CONNECTION_DISCONNECTED) {
			xImage.setImageResource(R.drawable.connection_disconnected);
		}
	}

}
