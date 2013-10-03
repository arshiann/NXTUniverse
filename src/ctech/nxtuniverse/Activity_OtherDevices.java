/*
 * 
 */

package ctech.nxtuniverse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Activity_OtherDevices extends Activity {

	private TextView hint;
	private Button scan;
	private ProgressBar scanInProgress;

	private ListView listView;
	private ArrayList<HashMap<String, String>> deviceList = new ArrayList<HashMap<String, String>>();
	private MyAdapter arrayAdapter;

	private BluetoothAdapter btAdapter = Activity_Main.btAdapter;

	private final String MSG_SCAN = "Search for device(s)";
	private final String MSG_SCANNING = "Scanning";
	private final String MSG_DEVICES = "Devices";
	private final String MSG_SELECT_DEVICE = "Select device";
	private final String MSG_NO_DEVICE_FOUND = "No device found";
	private final String MSG_CONNECT_ERROR = "Could not connect";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_other_devices);

		listView = (ListView) findViewById(R.id.otherdevices_listView);
		scan = (Button) findViewById(R.id.otherdevices_scan);
		hint = (TextView) findViewById(R.id.otherdevices_textView_hint);
		scanInProgress = (ProgressBar) findViewById(R.id.otherdevice_progressBar_scan);

		hint.setText(MSG_DEVICES);
		scan.setText(MSG_SCAN);
		scanInProgress.setVisibility(View.INVISIBLE);

		arrayAdapter = new MyAdapter(this, deviceList);

		addPairedDevices();
		updateListView();

		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(broadcastReceiver, filter);

		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(broadcastReceiver, filter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			String tag = "itemClickListener";

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.i(tag, deviceList.get(arg2).get("name"));
				Log.i(tag, deviceList.get(arg2).get("address"));
				Activity_Main.robot[0].setMacAddress(deviceList.get(arg2).get(
						"address"));
				if (Activity_Main.robot[0].connect()) {
					Activity_Main.connectionStatus = Activity_Main.CONNECTION_CONNECTED;
					Activity_Main.robot[0].write(NXTValue.CONFIRMATION_TONE);
					finish();
				} else {
					hint.setText(MSG_CONNECT_ERROR);
				}
			}
		});

		scan.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				scan.setText(MSG_SCANNING);
				scan.setEnabled(false);
				scanInProgress.setVisibility(View.VISIBLE);

				if (btAdapter.isDiscovering()) {
					btAdapter.cancelDiscovery();
				}

				deviceList.clear();
				addPairedDevices();
				btAdapter.startDiscovery();

			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		btAdapter.cancelDiscovery();
		Log.i("onDestroy", "Canceling discovery");

		unregisterReceiver(broadcastReceiver);
		Log.i("onDestroy", "Unregistering reciver");
	}

	// //////////////////////////////////////////////////////////////////
	public class MyAdapter extends BaseAdapter {

		ArrayList<HashMap<String, String>> array;
		LayoutInflater inflater;
		Context context;

		public MyAdapter(Context context,
				ArrayList<HashMap<String, String>> array) {
			this.array = array;
			this.context = context;
			inflater = LayoutInflater.from(context);
		}

		public void update(ArrayList<HashMap<String, String>> array) {
			this.array = array;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return array.size();
		}

		@Override
		public HashMap<String, String> getItem(int arg0) {
			return array.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertedView, ViewGroup parent) {

			// Scope the view holder
			ViewHolder viewHolder;

			if (convertedView == null) { // If the view does not exist,

				convertedView = inflater.inflate(
						R.layout.listview_other_devices, parent, false);

				// create a new one
				viewHolder = new ViewHolder();

				// Get inner views by id
				viewHolder.name = (TextView) convertedView
						.findViewById(R.id.listview_other_devices_textView_name);
				viewHolder.address = (TextView) convertedView
						.findViewById(R.id.listview_other_devices_textView_address);
				viewHolder.icon = (ImageView) convertedView
						.findViewById(R.id.listview_other_devices_imageView_icon);

				// set the converted with the view holder
				convertedView.setTag(viewHolder);
			} else {

				// else (view exist)
				viewHolder = (ViewHolder) convertedView.getTag();
				// reuse the view for better performance
			}

			// setting values
			viewHolder.name.setText(array.get(position).get("name"));
			viewHolder.address.setText(array.get(position).get("address"));

			// set icon visible if isRobot is true, else invisible
			viewHolder.icon.setVisibility(array.get(position).get("isRobot")
					.equals("true") ? View.VISIBLE : View.INVISIBLE);

			return convertedView;
		}

		private class ViewHolder {
			TextView name;
			TextView address;
			ImageView icon;
		}
	}

	// //////////////////////////////////////////////////////////////////

	private void addPairedDevices() {
		Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
		if (pairedDevices.size() > 0) {

			for (BluetoothDevice device : pairedDevices) {

				HashMap<String, String> temp = new HashMap<String, String>();
				temp.put("name", device.getName().replaceAll("\n", ""));
				temp.put("address", device.getAddress());
				if ((device.getBluetoothClass() != null)
						&& (device.getBluetoothClass().getDeviceClass() == BluetoothClass.Device.TOY_ROBOT)) {
					temp.put("isRobot", "true");
				} else {
					temp.put("isRobot", "false");
				}
				deviceList.add(temp);
			}
		}
	}

	private void updateListView() {
		listView.setAdapter(arrayAdapter);
	}

	// Create a BroadcastReceiver for ACTION_FOUND
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		String tag = "broadcastReceiver";

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(tag, "onRecive");
			String action = intent.getAction();
			
			Log.i(tag, "intent.getAction()");
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				Log.i(tag, "action_found");
				
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				
				HashMap<String, String> temp = new HashMap<String, String>();
				temp.put("name", device.getName());
				temp.put("address", device.getAddress());
				
				if ((device.getBondState() != BluetoothDevice.BOND_BONDED)
						&& (device.getBluetoothClass().getDeviceClass() == BluetoothClass.Device.TOY_ROBOT)) {
					temp.put("isRobot", "true");
				} else {
					temp.put("isRobot", "false");
				}
				
				deviceList.add(temp);
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {
				
				Log.i(tag, "ACTION_DISCOVERY_FINISHED");
				
				updateListView();

				scanInProgress.setVisibility(View.INVISIBLE);
				scan.setText(MSG_SCAN);
				scan.setEnabled(true);

				if (deviceList.size() > 0) {
					hint.setText(MSG_SELECT_DEVICE);
				} else {
					hint.setText(MSG_NO_DEVICE_FOUND);
				}
			}
		}
	};
}