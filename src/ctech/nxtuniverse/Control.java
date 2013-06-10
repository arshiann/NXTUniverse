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
	protected Button readDistance, getMotorStatus, resetMotorCount;
	protected Button test, test2, test3, test4;
	protected Button forward, backward, left, right;

	// Text displays
	public static TextView display1, display2, display3, display4, display5;

	// Value classes to access NXT direct command codes
//	static CustomValue Value = new CustomValue();

	// Bluetooth Communication - In and Out Stream
	static OutputStream outStream = MainActivity.outStream;
	static InputStream inStream = MainActivity.inStream;

	// Ports on NXT device - Define ports
	static byte rightMotorPort = NXTValue.PORT_MOTOR_A;
	static byte leftMotorPort = NXTValue.PORT_MOTOR_B;
	static byte ultrasonicSensorPort = NXTValue.PORT_SENSOR_4;

	static byte[] motorData = new byte[28];

	static DistancePID distancePid;
	static SpeedPID speedPid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control);

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
		test3 = (Button) findViewById(R.id.button_test3);
		test4 = (Button) findViewById(R.id.button_test4);

//		// Motor data structure
//		// Right motor
//		motorData[0] = 0x0c; // Length (from byte 2 to 13 inclusive)
//		motorData[1] = 0x00; // Start from byte 2
//		motorData[2] = (byte) 0x80; // Return type (void)
//		motorData[3] = 0x04; // setOutputState
//		motorData[4] = rightMotorPort; // Port
//		motorData[5] = (byte) 0; // Power
//		motorData[6] = 0x07; // Mode byte (unknown value)
//		motorData[7] = 0x02; // Motor regulation (sync)
//		motorData[8] = (byte) 0; // Turn ratio
//		motorData[9] = Value.getMotorRunStateRunning(); // Run state
//		motorData[10] = 0x00; // Tacho limit
//		motorData[11] = 0x00; // Tacho limit
//		motorData[12] = 0x00; // Tacho limit
//		motorData[13] = 0x00; // Tacho limit
//
//		// Left motor
//		motorData[14] = 0x0c; // Length (from byte 2 to 13 inclusive)
//		motorData[15] = 0x00; // Start from byte 2
//		motorData[16] = (byte) 0x80; // Return type (void)
//		motorData[17] = 0x04; // setOutputState
//		motorData[18] = leftMotorPort; // Port
//		motorData[19] = (byte) 0; // Power
//		motorData[20] = 0x07; // Mode byte (unknown value)
//		motorData[21] = 0x02; // Motor regulation (sync)
//		motorData[22] = (byte) 0; // Turn ratio
//		motorData[23] = Value.getMotorRunStateRunning(); // Run state
//		motorData[24] = 0x00; // Tacho limit
//		motorData[25] = 0x00; // Tacho limit
//		motorData[26] = 0x00; // Tacho limit
//		motorData[27] = 0x00; // Tacho limit

		// Setting up sensors
		try {
			// Ultrasonic sensor
			byte[] ultrasonicSetupCommand = { 0x05, 0x00,
					NXTValue.RETURN_VOID, NXTValue.SET_INPUT_MODE,
					ultrasonicSensorPort, NXTValue.SENSOR_TYPE_LOW_SPEED_9V,
					NXTValue.SENSOR_MODE_RAW };
			write(ultrasonicSetupCommand);
			write(NXTValue.SET_CONTINUOUS);
			// End of ultrasonic sensor setup
		} catch (Exception e) {
			e.printStackTrace();
			display5.setText("Setup error");
		}
		// End of sensors setup

		// Defining task(s) to buttons
		resetMotorCount.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				resetMotorCount();
			}
		});

		getMotorStatus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				display1.setText(intArrayToString(getMotorRotationCount())
						+ " rotation");
			}
		});

		forward.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					move(NXTValue.GO_FORWARD);
				} else if (action == MotionEvent.ACTION_UP) {
					move(NXTValue.STOP);
				}
				return true;
			}
		});

		backward.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					move(NXTValue.GO_BACKWARD);
				} else if (action == MotionEvent.ACTION_UP) {
					move(NXTValue.STOP);
				}
				return true;
			}
		});

		right.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					move(NXTValue.TURN_RIGHT);
				} else if (action == MotionEvent.ACTION_UP) {
					move(NXTValue.STOP);
				}
				return true;
			}
		});

		left.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					move(NXTValue.TURN_LEFT);
				} else if (action == MotionEvent.ACTION_UP) {
					move(NXTValue.STOP);
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

		test.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (distancePid == null) {
					distancePid = new DistancePID(50);
					// distancePid.setWantPosition(50);
					distancePid.start();
					test.setText("Running");
				} else {
					distancePid.kill();
					test.setText("Stopped");
				}

			}
		});

		test2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (speedPid == null) {
					speedPid = new SpeedPID();
					speedPid.setWantSpeed(3);
					speedPid.start();
					sleep(5000);
					speedPid.setWantSpeed(15);
				} else {
					speedPid.kill();
				}

			}
		});

		test3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		test4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

	}

	// End of onCreate

	// Methods

	public static int[] getMotorRotationCount() {
		byte[] rightMotorCommand = { 0x03, 0x00, NXTValue.RETURN_STATUS, 0x06,
				rightMotorPort };
		byte[] leftMotorCommand = { 0x03, 0x00, NXTValue.RETURN_STATUS, 0x06,
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
		byte[] rightMotorCommand = { 0x04, 0x00, NXTValue.RETURN_VOID, 0x0A,
				rightMotorPort, 1 }; // 1 is boolean true in byte
		byte[] leftMotorCommand = { 0x04, 0x00, NXTValue.RETURN_VOID, 0x0A,
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

		byte[] motor = motorData;

		if (direction == NXTValue.GO_FORWARD) {
			motor[5] = (byte) power;
			motor[19] = (byte) power;
		} else if (direction == NXTValue.GO_BACKWARD) {
			motor[5] = (byte) -power;
			motor[19] = (byte) -power;
		} else if (direction == NXTValue.TURN_RIGHT) {
			motor[5] = (byte) -power;
			motor[7] = 0x00;
			motor[19] = (byte) power;
			motor[21] = 0x00;
		} else if (direction == NXTValue.TURN_LEFT) {
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
			// display5.setText("Connection lost");
		}
	}

	// /////////////////////////////////////////////////////////////////////////

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
	}
}