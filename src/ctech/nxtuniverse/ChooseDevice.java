package ctech.nxtuniverse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ChooseDevice extends Activity {

	public static String EXTRA_DEVICE_ADDRESS = "device_address";

	private ArrayAdapter<String> mPairedDevicesArrayAdapter;
	private ArrayAdapter<String> mNewDevicesArrayAdapter;
	private BluetoothAdapter mBtAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.device_layout);

		// setResult(Activity.RESULT_CANCELED);

		Button scanButton = (Button) findViewById(R.id.button_scan);

		scanButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				doDiscovery();
				v.setVisibility(View.GONE);

			}
		});

		mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this,
				R.layout.device_name);
		mNewDevicesArrayAdapter = new ArrayAdapter<String>(this,
				R.layout.device_name);

		ListView pairedListView = (ListView) findViewById(R.id.paired_devices);

		pairedListView.setAdapter(mPairedDevicesArrayAdapter);
		pairedListView.setOnItemClickListener(mDeviceClickListener);

		ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
		newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
		newDevicesListView.setOnItemClickListener(mDeviceClickListener);

		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);

		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mReceiver, filter);

		mBtAdapter = BluetoothAdapter.getDefaultAdapter();

		Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

		boolean empty = true;

		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				if ((device.getBluetoothClass() != null)
						&& (device.getBluetoothClass().getDeviceClass() == BluetoothClass.Device.TOY_ROBOT)) {
					mPairedDevicesArrayAdapter.add(device.getName() + "\n"
							+ device.getAddress());
					empty = false;
				}
			}
		}
		if (!empty) {
			findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
			findViewById(R.id.no_devices).setVisibility(View.GONE);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (mBtAdapter != null) {
			mBtAdapter.cancelDiscovery();
		}

		// this.unregisterReceiver(mReceiver);
	}

	private void doDiscovery() {
		setProgressBarIndeterminateVisibility(true);
		setTitle("Scanning...");

		// findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}

		mBtAdapter.startDiscovery();

		mNewDevicesArrayAdapter.clear();
		findViewById(R.id.title_new_devices).setVisibility(View.GONE);
		if (mPairedDevicesArrayAdapter.getCount() == 0) {
			findViewById(R.id.no_devices).setVisibility(View.VISIBLE);
		}
	}

	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
			mBtAdapter.cancelDiscovery();

			String info = ((TextView) v).getText().toString();
			String address = info.substring(info.length() - 17);

//			Intent intent = new Intent();
//			intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

//			setResult(Activity.RESULT_OK, intent);

			// //////////

			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			
			BluetoothSocket socket;
			
			OutputStream outStream = MainActivity.outStream;
			InputStream inStream = MainActivity.inStream;
			
			BluetoothDevice nxt = adapter.getRemoteDevice(address);

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
				
				MainActivity.outStream.write(confirmationTone);
				
			} catch (IOException e) {
				e.printStackTrace();
			}

			// ////////

			finish();
		}
	};

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if ((device.getBondState() != BluetoothDevice.BOND_BONDED)
						&& (device.getBluetoothClass().getDeviceClass() == BluetoothClass.Device.TOY_ROBOT)) {
					mNewDevicesArrayAdapter.add(device.getName() + "\n"
							+ device.getAddress());
					findViewById(R.id.title_new_devices).setVisibility(
							View.VISIBLE);
					findViewById(R.id.no_devices).setVisibility(View.GONE);
				}
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {
				setProgressBarIndeterminateVisibility(false);
				setTitle("Select device");
				findViewById(R.id.button_scan).setVisibility(View.VISIBLE);
			}
		}
	};
}
