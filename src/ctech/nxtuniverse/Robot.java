package ctech.nxtuniverse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class Robot {

	// Bluetooth IO
	private BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
	private BluetoothSocket socket;
	private OutputStream outStream;
	private InputStream inStream;

	// NXT ports

	private String macAddress;
	private final byte rightMotorPort = 0x00;
	private final byte leftMotorPort = 0x01;
	private byte ultrasonicSensorPort;

	private byte[] motorData = {
			// Right motor
			0x0c, // Length (from byte 2 to 13 inclusive)
			0x00, // Start from byte 2
			(byte) NXTValue.RETURN_VOID, // Return type
			0x04, // setOutputState
			rightMotorPort, // Port
			(byte) 0, // Power
			0x07, // Mode byte (unknown value)
			0x00,// NXTValue.MOTOR_REGULATION_SYNC, // Motor regulation
			(byte) 0, // Turn ratio
			NXTValue.MOTOR_RUNSTATE_RUNNING, // Run state
			0x00, // Tacho limit
			0x00, // Tacho limit
			0x00, // Tacho limit
			0x00, // Tacho limit

			// Left motor
			0x0c, // Length (from byte 2 to 13 inclusive)
			0x00, // Start from byte 2
			(byte) NXTValue.RETURN_VOID, // Return type
			NXTValue.SET_OUTPUT_STATE, // Set Output State
			leftMotorPort, // Port
			(byte) 0, // Power
			0x07, // Mode byte (unknown value)
			0x00, // NXTValue.MOTOR_REGULATION_SYNC, // Motor regulation
			(byte) 0, // Turn ratio
			NXTValue.MOTOR_RUNSTATE_RUNNING, // Run state
			0x00, // Tacho limit
			0x00, // Tacho limit
			0x00, // Tacho limit
			0x00 // Tacho limit
	};

	public void setMotorData(byte[] motorData) {
		this.motorData = motorData;
	}

	public byte[] getMotorData() {
		return motorData;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	// public void setRightMotorPort(byte port) {
	// rightMotorPort = port;
	// }
	//
	// public void setLeftMotorPort(byte port) {
	// this.leftMotorPort = port;
	// }

	public void setUltrasonicSensorPort(byte port) {
		this.ultrasonicSensorPort = port;
	}

	public byte getRightMotorPort() {
		return rightMotorPort;
	}

	public byte getLeftMotorPort() {
		return leftMotorPort;
	}

	public byte getUltrasonicSensorPort() {
		return ultrasonicSensorPort;
	}

	public OutputStream getOutStream() {
		return outStream;
	}

	public InputStream getInStream() {
		return inStream;
	}

	/**
	 * Connect to a NXT device
	 * 
	 * @return - True if connection has been established.
	 */
	public boolean connect() {
		boolean connected = false;
		BluetoothDevice nxt = adapter.getRemoteDevice(macAddress);
		try {
			// Establishing connection with the NXT device
			socket = nxt.createRfcommSocketToServiceRecord(UUID
					.fromString("00001101-0000-1000-8000-00805F9B34FB"));
			socket.connect();

			// Note the preference after removing MainActivity
			outStream = socket.getOutputStream();
			inStream = socket.getInputStream();

			// Play the confirmation tone on the NXT device
			byte[] confirmationTone = { 0x06, 0x00, (byte) 0x80, 0x03, 0x0B,
					0x02, (byte) 0xFA, 0x00 };
			outStream.write(confirmationTone);

			// Setting up sensors
			try {
				// Ultrasonic sensor
				byte[] ultrasonicSetupCommand = { 0x05, 0x00,
						NXTValue.RETURN_VOID, NXTValue.SET_INPUT_MODE,
						ultrasonicSensorPort,
						NXTValue.SENSOR_TYPE_LOW_SPEED_9V,
						NXTValue.SENSOR_MODE_RAW };
				write(ultrasonicSetupCommand);
				write(NXTValue.SET_CONTINUOUS);
			} catch (Exception e) {
				e.printStackTrace();
				// display5.setText("Setup error"); // XXX
			}
			// End of sensors setup
			connected = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return connected;
	}

	/**
	 * Disconnects the Robot's communication socket.
	 * 
	 * @return - True if connection terminates successfully
	 */
	public boolean disconnect() {
		boolean disconnected = false;
		try {
			socket.close();
			disconnected = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return disconnected;
	}

	/**
	 * This method will first request NXT for ultrasonic sensor's data. Then,
	 * this method will return the distance (measured in cm)
	 * 
	 * @return int - Distance in cm
	 */
	public int getUltrasonicSensorValue() {
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
			Thread.sleep(100);

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
	 * Makes the robot move.
	 * 
	 * @param direction
	 *            - Reffer to NXTValue Class
	 * @param power
	 *            - form -100 to 100
	 */
	public void move(int direction, int power) {
		// int power = 100;

		if (power > 100) {
			power = 100;
		} else if (power < -100) {
			power = -100;
		}

		// Updating data based on what user wants
		if (direction == NXTValue.GO_FORWARD) {
			motorData[5] = (byte) power;
			motorData[19] = (byte) power;
		} else if (direction == NXTValue.GO_BACKWARD) {
			motorData[5] = (byte) -power;
			motorData[19] = (byte) -power;
		} else if (direction == NXTValue.TURN_RIGHT) {
			motorData[5] = (byte) -power;
			motorData[7] = 0x00;
			motorData[19] = (byte) power;
			motorData[21] = 0x00;
		} else if (direction == NXTValue.TURN_LEFT) {
			motorData[5] = (byte) power;
			motorData[7] = 0x00;
			motorData[19] = (byte) -power;
			motorData[21] = 0x00;
		} else {
			motorData[5] = (byte) 0;
			motorData[19] = (byte) 0;
		}

		// Writing the data to NXT device
		write(motorData);
	}

	/**
	 * Returns a byte array with the right motor's position at index 0 and the
	 * left motor's position at index 1.
	 * 
	 * @return Byte array [right motor's position, left motor's position]
	 */
	public int[] getMotorPosition() {
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
	public void resetMotorPosition() {
		byte[] dataForRightMotor = { 0x04, 0x00, NXTValue.RETURN_VOID, 0x0A,
				rightMotorPort, 1 }; // 1 is boolean true in byte
		byte[] dataForleftMotor = { 0x04, 0x00, NXTValue.RETURN_VOID, 0x0A,
				leftMotorPort, 1 };

		write(dataForRightMotor);
		write(dataForleftMotor);
	}

	/**
	 * Reads and returns (in form of an int array) any size of data from the NXT
	 * buffer.
	 * 
	 * @return Int array - The message that is read from the NXT buffer. Returns
	 *         255 if buffer is empty or an error has occurred.
	 */
	public int[] read() {
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
	public void write(byte[] data) {
		try {
			outStream.write(data);
			outStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// display5.setText("Connection lost"); // XXX use alert dialog
		}
	}

}
