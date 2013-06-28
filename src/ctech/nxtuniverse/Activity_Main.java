package ctech.nxtuniverse;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

public class Activity_Main extends Activity {

	public static String version = "NXT Universe V 0.18 - Alpha";
	/*
	 * 1 - connection and beep 2 - first main activity screen 3 - ultrasonic
	 * sensor 4 - motor 5 - img bluetooth button 6 - getUltrasonicSensor() 7 -
	 * left shift motor position 8 - getMotorPosition() 9 - resetMotorPosition()
	 * 10 - NXTValue 11 - Dist pid 12 - Speed pid 13 - combined pid 14 - New
	 * NXTValue 15 - new main activity screen 16 - Navigate activity 17 - Robot
	 * class 18 - Tilt to navigate
	 */

	protected BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
	protected ImageView xImage, bluetooth;
	protected Button connect, disconnect, control, about;
	protected TextView versionDisplay;

	public static Robot robot1, robot2;

	// Robot 1 - "00:16:53:12:AA:37"
	// Robot 2 - "00:12:53:12:AA:D5"

	// /**
	// *
	// * @param num - Number of the robot
	// * @return - Returns the robot object. Returns Null if no robot of that
	// number is found.
	// */
	// public static Robot getRobot(int num) {
	// if (num == 1) {
	// return robot1;
	// } else if (num == 2) {
	// return robot2;
	// } else {
	// return null;
	// }
	// }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		robot1 = new Robot();

		// Images
		xImage = (ImageView) findViewById(R.id.imageView_xImage);
		bluetooth = (ImageView) findViewById(R.id.imageView_bluetooth);

		// Buttons
		connect = (Button) findViewById(R.id.button_connect);
		disconnect = (Button) findViewById(R.id.button_disconnect);
		control = (Button) findViewById(R.id.button_control);
		about = (Button) findViewById(R.id.button_about);

		// TextView
		versionDisplay = (TextView) findViewById(R.id.textView_version);
		versionDisplay.setText(version);

		// Sets alive/dead Bluetooth logo
		if (adapter.isEnabled()) { // If Bluetooth is on, set alive logo
			bluetooth.setImageResource(R.drawable.bluetooth_alive);
		} else { // Else, set dead logo
			bluetooth.setImageResource(R.drawable.bluetooth_dead);
		}

		// Assigning task to buttons
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

		connect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// XXX sync and unsync
				// XXX getters and setters
				// XXX 2 sideways seek bars in navigate one for speed and one
				// for dist.

				robot1 = new Robot();

				// robot1.setRightMotorPort(NXTValue.MOTOR_PORT_B);
				// robot1.setLeftMotorPort(NXTValue.MOTOR_PORT_A);
				robot1.setUltrasonicSensorPort(NXTValue.SENSOR_PORT_4);
				robot1.setMacAddress("00:16:53:12:AA:37");
				// robot1.setMacAddress("00:12:53:12:AA:D5");

				boolean connected = robot1.connect();
				if (connected) {
					xImage.setImageResource(R.drawable.connection_connected);
				} else {
					xImage.setImageResource(R.drawable.connection_disconnected);
				}

				robot1.write(NXTValue.CONFIRMATION_TONE);

				// robot2.outStream.write(confirmationTone);

				// boolean success;
				// BluetoothDevice nxt = adapter.getRemoteDevice(nxtMacAddress);
				// try {
				// // Establishing connection with the NXT device
				// socket = nxt.createRfcommSocketToServiceRecord(UUID
				// .fromString("00001101-0000-1000-8000-00805F9B34FB"));
				// socket.connect();
				//
				// // Note the preference after removing MainActivity
				// outStream = socket.getOutputStream();
				// inStream = socket.getInputStream();
				//
				// // Play the confirmation tone on the NXT device
				// byte[] confirmationTone = { 0x06, 0x00, (byte) 0x80, 0x03,
				// 0x0B, 0x02, (byte) 0xFA, 0x00 };
				// outStream.write(confirmationTone);
				// success = true;
				// } catch (IOException e) {
				// e.printStackTrace();
				// success = false;
				// }
				//
				// if (success) {
				// xImage.setImageResource(R.drawable.connection_connected);
				// } else {
				// xImage.setImageResource(R.drawable.connection_disconnected);
				// }
			}
		});

		// disconnect.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// boolean success;
		// try {
		// socket.close();
		// success = true;
		// } catch (IOException e) {
		// e.printStackTrace();
		// success = false;
		// }
		//
		// if (success) {
		// xImage.setImageResource(R.drawable.connection_disconnected);
		// } else {
		// xImage.setImageResource(R.drawable.connection_connected);
		// }
		// }
		// });

		control.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent startControl = new Intent("nxtuniverse.CONTROL");
				startActivity(startControl);
			}
		});

		about.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent startAbout = new Intent("nxtuniverse.ABOUT");
				startActivity(startAbout);
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
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		robot1.disconnect();
		robot2.disconnect();
		finish();
	}
}
