package ctech.nxtuniverse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ToggleButton;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
//import android.view.Menu;
import android.content.Intent;

public class MainActivity extends Activity {

	// Buttons and images
	Button connect, disconnect, control, about, scanDevice;
	// ToggleButton bluetooth;
	ImageView xImage, bluetooth;

	// Declaring
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
		// bluetooth = (ToggleButton) findViewById(R.id.bluetooth);
		connect = (Button) findViewById(R.id.connect);
		disconnect = (Button) findViewById(R.id.disconnect);
		control = (Button) findViewById(R.id.control);
		about = (Button) findViewById(R.id.about);
		scanDevice = (Button) findViewById(R.id.scan);

		// XXX new bluetooth apparatus
		if (adapter.isEnabled()) {
			bluetooth.setImageResource(R.drawable.bluetooth_alive);
		} else {
			bluetooth.setImageResource(R.drawable.bluetooth_dead);
		}

		// Assigning task to buttons
		bluetooth.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean enabling = false;
				if (adapter.isEnabled()) {
					adapter.disable();
					bluetooth.setImageResource(R.drawable.bluetooth_dead);
				} else {
					enabling = true;
					adapter.enable();
				}

				if (enabling) {
					while (!adapter.isEnabled()) {
					}
					bluetooth.setImageResource(R.drawable.bluetooth_alive);
				}
			}
		});

		scanDevice.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent startScan = new Intent("android.intent.action.SCAN");
				startActivity(startScan);
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
					outStream = MainActivity.socket.getOutputStream();
					inStream = MainActivity.socket.getInputStream();

					// Play the

					byte[] confirmationTone = { 0x06, 0x00, (byte) 0x80, 0x03,
							0x0B, 0x02, (byte) 0xFA, 0x00 };
					outStream.write(confirmationTone);
					success = true;
				} catch (IOException e) {
					e.printStackTrace();
					success = false;
				}

				if (success) {
					xImage.setImageResource(R.drawable.green);
				} else {
					xImage.setImageResource(R.drawable.red);
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
					xImage.setImageResource(R.drawable.red);
				} else {
					xImage.setImageResource(R.drawable.green);
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

		// bluetooth.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// localAdapter.enable();
		// }
		// });

		// bluetooth
		// .setOnCheckedChangeListener(new
		// CompoundButton.OnCheckedChangeListener() {
		// public void onChecketacodChanged(CompoundButton buttonView, boolean
		// isOn) {
		//
		// if (isOn) {
		// adapter.enable();
		// } else {
		// adapter.disable();
		// }
		// }
		// });

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
