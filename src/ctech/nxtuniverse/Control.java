package ctech.nxtuniverse;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Thread.State;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Control extends Activity {

	// Global objects
	// Buttons
	static Button readDistance, getMotorStatus, resetMotorCount;
	static Button test, test2;
	static Button forward, backward, left, right;

	// Text displays
	static TextView display1, display2, display3, display4, display5;

	// Value classes to access NXT direct command codes
	static CustomValue Value = new CustomValue();
	static kNXTValue kNXTvalue = new kNXTValue();

	// Communication - In and Out Stream
	public static OutputStream outStream = MainActivity.outStream;
	public static InputStream inStream = MainActivity.inStream;

	// Ports on NXT device - Define ports
	static byte rightMotorPort = Value.getMotorA();
	static byte leftMotorPort = Value.getMotorB();
	static byte ultrasonicSensorPort = Value.getSensor4();

	static byte[] motorDataStructure = new byte[28];
	{
		// right motor
		motorDataStructure[0] = 0x0c; // length (from byte 2 to 13 inclusive)
		motorDataStructure[1] = 0x00; // start from byte 2
		motorDataStructure[2] = (byte) 0x80; // return type
		motorDataStructure[3] = 0x04; // setOutputState
		motorDataStructure[4] = rightMotorPort; // port
		motorDataStructure[5] = (byte) 0; // power
		motorDataStructure[6] = 0x07; // XXX unknown predefined value
		motorDataStructure[7] = 0x02; // motor regulation
		motorDataStructure[8] = (byte) 0; // turn ratio
		// ratio between power supply
		motorDataStructure[9] = Value.getMotorRunStateRunning();
		motorDataStructure[10] = 0x00;// taco limit
		motorDataStructure[11] = 0x00;// taco limit
		motorDataStructure[12] = 0x00;// taco limit
		motorDataStructure[13] = 0x00;// taco limit

		// left motor
		motorDataStructure[14] = 0x0c; // length (from byte 16 to 27 inclusive)
		motorDataStructure[15] = 0x00; // start from byte 2
		motorDataStructure[16] = (byte) 0x80; // return type
		motorDataStructure[17] = 0x04; // setOutputState
		motorDataStructure[18] = leftMotorPort; // port
		motorDataStructure[19] = (byte) 0; // power
		motorDataStructure[20] = 0x07; // XXX unknown predefined value
		motorDataStructure[21] = 0x02; // motor regulation
		motorDataStructure[22] = (byte) 0; // turn ratio
		motorDataStructure[23] = Value.getMotorRunStateRunning();
		motorDataStructure[24] = 0x00;// taco limit
		motorDataStructure[25] = 0x00;// taco limit
		motorDataStructure[26] = 0x00;// taco limit
		motorDataStructure[27] = 0x00;// taco limit

	}
	
	static PID pid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control);

		// onCreate - Linking layout XML to current java file
		// Text display
		display1 = (TextView) findViewById(R.id.display1);
		display2 = (TextView) findViewById(R.id.display2);
		display3 = (TextView) findViewById(R.id.display3);
		display4 = (TextView) findViewById(R.id.display4);
		display5 = (TextView) findViewById(R.id.display5);

		// Distance listener button
		readDistance = (Button) findViewById(R.id.distanceListener);

		// Motor count buttons
		getMotorStatus = (Button) findViewById(R.id.motorCount);
		resetMotorCount = (Button) findViewById(R.id.resetCount);

		// Primary navigation buttons
		forward = (Button) findViewById(R.id.forward);
		backward = (Button) findViewById(R.id.backward);
		left = (Button) findViewById(R.id.left);
		right = (Button) findViewById(R.id.right);

		// Test buttons
		test = (Button) findViewById(R.id.test);
		test2 = (Button) findViewById(R.id.test2);
		
		

		// Setting up sensors
		try {
			// Ultrasonic sensor
			byte[] ultrasonicSetupCommand = { 0x05, 0x00,
					Value.getNullReturn(), Value.getSetInputMode(),
					ultrasonicSensorPort, Value.getLowSpeed9V(),
					Value.getRawMode() };
			write(ultrasonicSetupCommand);
			write(Value.setContinuous);
			// End of ultrasonic sensor setup
		} catch (Exception e) {
			e.printStackTrace();
			display5.setText("Setup error");
		}
		// End of sensors setup

		// Assigning task(s) to buttons
		resetMotorCount.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				resetMotorCount();
			}
		});

		getMotorStatus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				display1.setText(intArrayToString(readMotorRotationCount())
						+ " rotation");
			}
		});

		forward.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					move(Value.getGoForward());
				} else if (action == MotionEvent.ACTION_UP) {
					move(Value.getStop());
				}
				return true;
			}
		});

		backward.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					move(Value.getGoBackward());
				} else if (action == MotionEvent.ACTION_UP) {
					move(Value.getStop());
				}
				return true;
			}
		});

		right.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					move(Value.getTurnRight());
				} else if (action == MotionEvent.ACTION_UP) {
					move(Value.getStop());
				}
				return true;
			}
		});

		left.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					move(Value.getTurnLeft());
				} else if (action == MotionEvent.ACTION_UP) {
					move(Value.getStop());
				}
				return true;
			}
		});

		readDistance.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				display1.setText(readDistance() + " cm");
			}
		});

		test2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				pid.kill();

			}
		});

		
		test.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				pid = new PID ();
				pid.setWantPosition(50);
				pid.start();

			}
		});

	}
	
	

	// End of onCreate

	// Methods


	public static void pid() {

	}

	public static int[] readMotorRotationCount() {
		byte[] rightMotorCommand = { 0x03, 0x00, Value.getReturnStatus(), 0x06,
				rightMotorPort };
		byte[] leftMotorCommand = { 0x03, 0x00, Value.getReturnStatus(), 0x06,
				leftMotorPort };

		write(rightMotorCommand);
		write(leftMotorCommand);

		int[] input4 = read();
		int[] input5 = read();

		int[] value = new int[2];

		try {
			// right motor
			value[0] = input4[19] + (input4[20] << 8) + (input4[21] << 16)
					+ (input4[22] << 24);
			// left motor
			value[1] = input5[19] + (input5[20] << 8) + (input5[21] << 16)
					+ (input5[22] << 24);
		} catch (Exception e) {
			value[0] = 255;
			value[1] = 255;
		}

		return value;

		// left motor
		// int value2 = input5[19] + (input5[20] << 8) + (input5[21] << 16)
		// + (input5[22] << 24);

		// //////////////////////////////////////////////////////////////
		// byte[] rightMotorCommand = { Value.getReturnStatus(), 0x06,
		// rightMotorPort };
		// byte[] leftMotorCommand = { Value.getReturnStatus(), 0x06,
		// leftMotorPort };
		//
		// byte[] rightMotorCommandingData = getData(rightMotorCommand);
		// byte[] leftMotorCommandingData = getData(leftMotorCommand);
		//
		// write(rightMotorCommandingData);
		// sleep(100);
		// write(leftMotorCommandingData);
		//
		// int[] input4 = read();
		// sleep(100);
		// int[] input5 = read();
		//
		// // right motor
		// int value = input4[19] + (input4[20] << 8) + (input4[21] << 16)
		// + (input4[22] << 24);
		// display2.setText(value + " suc 17-20");
		// int value2 = input4[15] + (input4[16] << 8)
		// + (input4[17] << 16) + (input4[18] << 24);
		// display3.setText(value2 + " suc 13-16");
		// int value3 = input4[23] + (input4[24] << 8)
		// + (input4[25] << 16) + (input4[26] << 24);
		// display4.setText(value3 + " suc 21-24");
		// int value5 = input5[23] + (input5[24] << 8)
		// + (input5[25] << 16) + (input5[26] << 24);
		// display5.setText(value5 + " suc 21-24");
		// //////////////////////////////////////////////////////////////
	}

	/**
	 * Reset motor count
	 */
	public static void resetMotorCount() {
		byte[] rightMotorCommand = { 0x04, 0x00, Value.getNullReturn(), 0x0A,
				rightMotorPort, 1 }; // 1 is boolean true in byte
		byte[] leftMotorCommand = { 0x04, 0x00, Value.getNullReturn(), 0x0A,
				leftMotorPort, 1 };

		write(rightMotorCommand);
		write(leftMotorCommand);
	}

	/**
	 * Reads the distance from NXT ultrasonic sensor
	 * 
	 * @return Distance in cm
	 */
	public static int readDistance() {
		// Data and scope
		byte[] askStatusOnPort3 = { 0x03, 0x00, 0x00, 0x0E, 0x03 };
		byte[] readByteZero = { 0x07, 0x00, 0x00, 0x0F, 0x03, 0x02, 0x01, 0x02,
				0x42 };
		final byte[] readLS = { 0x03, 0x00, 0x00, 0x10, 0x03 };

		@SuppressWarnings("unused")
		int[] input1;
		int[] input2;
		int[] input3;

		// Read write
		try {
			write(readByteZero);
			sleep(100);
			// Clearing NXT buffer for further reading
			input1 = read();

			// Wait until there is something to read
			do {
				write(askStatusOnPort3);
				input2 = read();
			} while (input2[5] == 0);

			// Ask NXT to reply with data
			write(readLS);

			// Reading data form NXT
			input3 = read();

			// Return the distance (cm)
			return input3[6];
		} catch (Exception e) {
			e.printStackTrace();
			return 255;
		}
	}

	/**
	 * 
	 * @param direction
	 *            - Refer to the Custom Value class
	 */
	public static void move(int direction) {

		int power = 100;

		byte[] motor = motorDataStructure;

		if (direction == Value.getGoForward()) {
			motor[5] = (byte) power;
			motor[19] = (byte) power;
		} else if (direction == Value.getGoBackward()) {
			motor[5] = (byte) -power;
			motor[19] = (byte) -power;
		} else if (direction == Value.getTurnRight()) {
			motor[5] = (byte) -power;
			motor[7] = 0x00;
			motor[19] = (byte) power;
			motor[21] = 0x00;
		} else if (direction == Value.getTurnLeft()) {
			motor[5] = (byte) power;
			motor[7] = 0x00;
			motor[19] = (byte) -power;
			motor[21] = 0x00;
		} else {
			motor[5] = (byte) 0;
			motor[19] = (byte) 0;
		}
		write(motor);
	}

	/**
	 * Converts int array to string
	 * 
	 * @param array
	 *            - The int array that will be converted into string.
	 * @return The string version of the array.
	 */
	public static String intArrayToString(int[] array) {
		String stringArray = "";
		for (int i = 0; i < array.length; i++) {
			stringArray += array[i] + " ";
		}
		return stringArray;
	}

	/**
	 * Wait before execution.
	 * 
	 * @param time
	 *            - Time in millisecond.
	 */
	public static void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
			e.printStackTrace();
			display5.setText("Wait error");
		}
	}

	/**
	 * Read any size of data from the NXT buffer.
	 * 
	 * @return Array of data [int] read from the NXT buffer. Returns 255 if
	 *         buffer is empty or an error has occurred.
	 */
	public static int[] read() {
		int[] message;
		try {
			int size = inStream.read();
			message = new int[size + 2];
			message[0] = size;
			for (int i = 0; i < size + 1; i++) {
				message[i + 1] = inStream.read();
			}
			return message;
		} catch (Exception e) {
			e.printStackTrace();
			message = new int[1];
			message[0] = 255;
			display5.setText("Empty NXT buffer");
			return message;
		}
	}

	/**
	 * Write to the connected NXT device
	 * 
	 * @param data
	 *            - Array of data that will be written on the NXT device
	 */
	public static void write(byte[] data) {

		try {
			outStream.write(data);
			outStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
			display5.setText("Connection lost");
		}
	}

	// /////////////////////////////////////////////////////////////////////////

	@Override
	protected void onPause() {
		super.onDestroy();
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
	}
}