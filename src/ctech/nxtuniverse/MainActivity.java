package ctech.nxtuniverse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;

public class MainActivity extends Activity {

	// Buttons
	private Button connect, disconnect, control, about;

	// Images
	private ImageView xImage, bluetooth;

	// Bluetooth I/O
	public static BluetoothAdapter adapter = BluetoothAdapter
			.getDefaultAdapter();
	public static BluetoothSocket socket;
	public static OutputStream outStream;
	public static InputStream inStream;

	// MAC address of the NXT device
	String nxtMacAddress = "00:16:53:12:AA:37";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Images
		xImage = (ImageView) findViewById(R.id.xImage);
		bluetooth = (ImageView) findViewById(R.id.image_bluetooth);

		// Buttons
		connect = (Button) findViewById(R.id.connect);
		disconnect = (Button) findViewById(R.id.disconnect);
		control = (Button) findViewById(R.id.control);
		about = (Button) findViewById(R.id.about);

		// Sets alive/dead Bluetooth logo
		if (adapter.isEnabled()) { // If Bluetooth is on, set alive logo
			bluetooth.setImageResource(R.drawable.bluetooth_alive);
		} else { // Else, set dead logo
			bluetooth.setImageResource(R.drawable.bluetooth_dead);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

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
				boolean success;
				BluetoothDevice nxt = adapter.getRemoteDevice(nxtMacAddress);
				try {
					// Establishing connection with the NXT device
					socket = nxt.createRfcommSocketToServiceRecord(UUID
							.fromString("00001101-0000-1000-8000-00805F9B34FB"));
					socket.connect();

					// Note the preference after removing MainActivity
					outStream = socket.getOutputStream();
					inStream = socket.getInputStream();

					// Play the confirmation tone on the NXT device
					byte[] confirmationTone = { 0x06, 0x00, (byte) 0x80, 0x03,
							0x0B, 0x02, (byte) 0xFA, 0x00 };
					outStream.write(confirmationTone);
					success = true;
				} catch (IOException e) {
					e.printStackTrace();
					success = false;
				}

				if (success) {
					xImage.setImageResource(R.drawable.connection_connected);
				} else {
					xImage.setImageResource(R.drawable.connection_disconnected);
				}
			}
		});

		disconnect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean success;
				try {
					socket.close();
					success = true;
				} catch (IOException e) {
					e.printStackTrace();
					success = false;
				}

				if (success) {
					xImage.setImageResource(R.drawable.connection_disconnected);
				} else {
					xImage.setImageResource(R.drawable.connection_connected);
				}
			}
		});

		control.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent startControl = new Intent(
						"android.intent.action.CONTROL");
				startActivity(startControl);
			}
		});

		about.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent startAbout = new Intent("android.intent.action.ABOUT");
				startActivity(startAbout);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finish();
	}
}
