package ctech.nxtuniverse;

import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Control extends Activity {

	// Buttons
	protected Button readDistance, getMotorPosition, resetMotorPosition;
	protected Button test, test2, test3, test4;
	protected Button forward, backward, left, right;

	// Text Views
	public static TextView display1, display2, display3, display4, display5;

	// Bluetooth Communication - In and Out Streams
	static OutputStream outStream = MainActivity.outStream;
	static InputStream inStream = MainActivity.inStream;

	// Defining ports of NXT device
	static byte rightMotorPort = NXTValue.MOTOR_PORT_A;
	static byte leftMotorPort = NXTValue.MOTOR_PORT_B;
	static byte ultrasonicSensorPort = NXTValue.SENSOR_PORT_4;

	// Motor data
	static byte[] motorData = NXTValue.MOTOR_DATA;

	// PID Controllers
	static DistancePID distancePid;
	static SpeedPID speedPid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control);

		// Text Views
		display1 = (TextView) findViewById(R.id.display1);
		display2 = (TextView) findViewById(R.id.display2);
		display3 = (TextView) findViewById(R.id.display3);
		display4 = (TextView) findViewById(R.id.display4);
		display5 = (TextView) findViewById(R.id.display5);

		// Distance listener button
		readDistance = (Button) findViewById(R.id.distanceListener);

		// Motor related buttons
		getMotorPosition = (Button) findViewById(R.id.motorCount);
		resetMotorPosition = (Button) findViewById(R.id.resetCount);

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

		// Setting up sensors
		try {
			// Ultrasonic sensor
			byte[] ultrasonicSetupCommand = { 0x05, 0x00, NXTValue.RETURN_VOID,
					NXTValue.SET_INPUT_MODE, ultrasonicSensorPort,
					NXTValue.SENSOR_TYPE_LOW_SPEED_9V, NXTValue.SENSOR_MODE_RAW };
			write(ultrasonicSetupCommand);
			write(NXTValue.SET_CONTINUOUS);
		} catch (Exception e) {
			e.printStackTrace();
			display5.setText("Setup error");
		}
		// End of sensors setup
	}

	// End of onCreate

	@Override
	protected void onStart() {
		super.onStart();

		// Defining task(s) to buttons
		resetMotorPosition.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				resetMotorPosition();
			}
		});

		getMotorPosition.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				display1.setText(intArrayToString(getMotorPosition())
						+ " rotation");
			}
		});

		forward.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) { // button is touched
					move(NXTValue.GO_FORWARD);
				} else if (action == MotionEvent.ACTION_UP) { // is not touched
					move(NXTValue.STOP);
				}
				return true;
			}
		});

		backward.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) { // button is touched
					move(NXTValue.GO_BACKWARD);
				} else if (action == MotionEvent.ACTION_UP) { // is not touched
					move(NXTValue.STOP);
				}
				return true;
			}
		});

		right.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) { // button is touched
					move(NXTValue.TURN_RIGHT);
				} else if (action == MotionEvent.ACTION_UP) { // is not touched
					move(NXTValue.STOP);
				}
				return true;
			}
		});

		left.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) { // button is touched
					move(NXTValue.TURN_LEFT);
				} else if (action == MotionEvent.ACTION_UP) { // is not touched
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
					speedPid = new SpeedPID(3);
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

	// End of onStart

	// Methods
	/**
	 * Returns a byte array with the right motor's position at index 0 and the
	 * left motor's position at index 1.
	 * 
	 * @return Byte array [right motor's position, left motor's position]
	 */
	public static int[] getMotorPosition() {
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
			// Returning 255 in case of an error
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
	 * Resets both right and left motor's position
	 */
	public static void resetMotorPosition() {
		byte[] dataForRightMotor = { 0x04, 0x00, NXTValue.RETURN_VOID, 0x0A,
				rightMotorPort, 1 }; // 1 is boolean true in byte
		byte[] dataForleftMotor = { 0x04, 0x00, NXTValue.RETURN_VOID, 0x0A,
				leftMotorPort, 1 };

		write(dataForRightMotor);
		write(dataForleftMotor);
	}

	/**
	 * This method will first request NXT for ultrasonic sensor's data. Then,
	 * this method will return the distance (measured in cm)
	 * 
	 * @return int - Distance in cm
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

			// Request NXT to reply with ultrasonic sensor's data
			write(readLS);

			// Reading data form NXT
			input3 = read();

			// Return the distance (cm)
			return input3[6];
		} catch (Exception e) {
			e.printStackTrace();

			// In case of an error, return 255
			return 255;
		}
	}

	/**
	 * Simple navigating method. Describe the direction in parameter using
	 * constants from NXTValue class.
	 * 
	 * @param Direction
	 *            - Constants from NXTValue class
	 */
	public static void move(int direction) {

		int power = 100;

		byte[] motor = motorData;

		// Updating data based on what user wants
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
		
		// Writing the data to NXT device
		write(motor);
	}

	/**
	 * Converts int array to a string.
	 * 
	 * @param Int
	 *            array - The int array that will be converted into string.
	 * @return The string representation of the array.
	 */
	public static String intArrayToString(int[] array) {
		String stringRepresentationOfTheArray = "";
		
		// Concatenating
		for (int i = 0; i < array.length; i++) {
			stringRepresentationOfTheArray += array[i] + ", ";
		}
		return stringRepresentationOfTheArray;
	}

	/**
	 * Wait before execution.
	 * 
	 * @param Time
	 *            - Time in millisecond.
	 */
	public static void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
			e.printStackTrace();
			display5.setText("Wait error"); // XXX use alert dialog
		}
	}

	/**
	 * Reads and returns (in form of an int array) any size of data from the NXT
	 * buffer.
	 * 
	 * @return Int array - The message that is read from the NXT buffer. Returns
	 *         255 if buffer is empty or an error has occurred.
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
			// display5.setText("Empty NXT buffer"); // XXX use alert dialog
			return message;
		}
	}

	/**
	 * Writes data to the NXT device.
	 * 
	 * @param Data
	 *            - Array of data that will be sent to the NXT device
	 */
	public static void write(byte[] data) {

		try {
			outStream.write(data);
			outStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// display5.setText("Connection lost"); // XXX use alert dialog
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
	}
}