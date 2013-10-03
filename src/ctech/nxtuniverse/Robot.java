package ctech.nxtuniverse;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class Robot {

	// Bluetooth
	private static BluetoothAdapter adapter = BluetoothAdapter
			.getDefaultAdapter(); // XXX static adapter
	private BluetoothSocket socket;
	private OutputStream outStream;
	private InputStream inStream;

	// NXT ports - Default values unless overwritten via setters
	private String macAddress;
	// private byte otherMotorPort = 0x00;
	private byte rightMotorPort = 0x01;
	private byte leftMotorPort = 0x02;
	/**
	 * Mounting the Ultrasonic sensor on other ports causes the app to crash
	 */
	private final byte ultrasonicSensorPort = NXTValue.SENSOR_PORT_4;

	// Constants
	/**
	 * To convert units (rotation in degree and cm)
	 */
	public final double DEG_PER_CM = 20.46;

	// Other variavles
	private byte[] motorData = {
			// Right motor
			0x0c, // Length (from byte 2 to 12 inclusive)
			0x00, // Start from byte 2
			(byte) NXTValue.RETURN_VOID, // Return type
			0x04, // setOutputState
			rightMotorPort, // Port
			(byte) 0, // Power
			0x07, // Mode byte (unknown value)
			NXTValue.MOTOR_REGULATION_IDLE, // Motor regulation
			(byte) 0, // Turn ratio
			NXTValue.MOTOR_RUNSTATE_RUNNING, // Run state
			0x00, // Tacho limit
			0x00, // Tacho limit
			0x00, // Tacho limit
			0x00, // Tacho limit

			// Left motor
			0x0c, // Length (from byte 2 to 12 inclusive)
			0x00, // Start from byte 2
			(byte) NXTValue.RETURN_VOID, // Return type
			NXTValue.SET_OUTPUT_STATE, // Set Output State
			leftMotorPort, // Port
			(byte) 0, // Power
			0x07, // Mode byte (unknown value)
			NXTValue.MOTOR_REGULATION_IDLE, // Motor regulation
			(byte) 0, // Turn ratio
			NXTValue.MOTOR_RUNSTATE_RUNNING, // Run state
			0x00, // Tacho limit
			0x00, // Tacho limit
			0x00, // Tacho limit
			0x00 // Tacho limit
	};

	public byte getRightMotorPort() {
		return rightMotorPort;
	}

	public void setRightMotorPort(byte rightMotorPort) {
		this.rightMotorPort = rightMotorPort;
		this.motorData[4] = rightMotorPort;
	}

	public byte getLeftMotorPort() {
		return leftMotorPort;
	}

	public void setLeftMotorPort(byte leftMotorPort) {
		this.leftMotorPort = leftMotorPort;
		this.motorData[18] = leftMotorPort;
	}

	public byte getUltrasonicSensorPort() {
		return ultrasonicSensorPort;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public byte[] getMotorData() {
		return motorData;
	}

	public void setMotorData(byte[] motorData) {
		this.motorData = motorData;
	}

	/**
	 * Connect to a NXT device
	 * 
	 * @return True if connection has been established.
	 */
	public boolean connect() {
		String tag = "connect()";

		if (macAddress == null) {
			Log.i(tag,
					"macAddress is not specified. Use setMacAddress(address);");
			return false;
		}

		BluetoothDevice nxt = adapter.getRemoteDevice(macAddress);
		try {
			// Establishing connection with the NXT device
			socket = nxt.createRfcommSocketToServiceRecord(UUID
					.fromString("00001101-0000-1000-8000-00805F9B34FB"));
			socket.connect();
			// Getting inStream and outStream
			outStream = socket.getOutputStream();
			inStream = socket.getInputStream();
			Log.i(tag, "Sucessfully connected to NXT!");

			// Setting up sensors - Ultrasonic sensor
			// Declaring port and sensor type
			byte[] setInputMode = { 0x05, 0x00, NXTValue.RETURN_VOID,
					NXTValue.SET_INPUT_MODE, ultrasonicSensorPort,
					NXTValue.SENSOR_TYPE_LOW_SPEED_9V, NXTValue.SENSOR_MODE_RAW };
			write(setInputMode);
			Log.i(tag, "Port setup successful");

			// Setting Ultrasonic sensor (on i2c bus) on continuous measurement
			int[] read1;
			do {
				byte[] setContinuous = { 0x08, 0x00, (byte) 0x00, 0x0F, 0x03,
						0x03, 0x00, 0x02, 0x41, 0x02 };
				write(setContinuous);
				read1 = read();
				Log.i(tag, Activity_Control.intArrayToString(read1));
			} while (read1[4] != 0);
			Log.i(tag, "SetContinuous successful");
			// End of sensors setup
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Disconnects the Robot's communication socket.
	 * 
	 * @return True if connection terminates successfully
	 */
	public boolean disconnect() {
		boolean disconnected = false;
		try {
			socket.close();
			disconnected = true;
		} catch (Exception e) {
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
	public int getDistanceFromUltrasonicSensor() {

		String tag = "getDistanceFromUltrasonicSensor()";

		int[] read1 = new int[1];
		try {
			// Scope of read2, outside the loop
			int[] read2;
			do {
				Log.i(tag, "Reading byte zero");
				byte[] readByteZero = { 0x07, 0x00, 0x00, 0x0F, 0x03, 0x02,
						0x01, 0x02, 0x42 };
				write(readByteZero);
				read1 = read();
				Log.i(tag + " - read1",
						Activity_Control.intArrayToString(read1));

				Log.i(tag, "Polling port 4 (0x03)"); // Polling port 4
														// (0x03)
				byte[] askStatusOnPort4 = { 0x03, 0x00, 0x00, 0x0E, 0x03 };
				write(askStatusOnPort4);
				read2 = read();
				Log.i(tag + " - read2",
						Activity_Control.intArrayToString(read2));
			} while (read1[4] != 0 && read2[5] != 1);
			Log.i(tag, "Byte zero is read successfully");
			Log.i(tag, "Polling port 4 (0x03) success");

			// Reading data
			Log.i(tag, "Read UlSonic data");
			final byte[] readLS = { 0x03, 0x00, 0x00, 0x10, 0x03 };
			write(readLS);
			int[] read3 = read();
			Log.i(tag + " - read3", Activity_Control.intArrayToString(read3));
			if (read3[6] == 255) {
				Log.i(tag, "Physical object is out of UlSonic's range");
			}
			Log.i(tag, "UlSonic data reading successful");
			return read3[6];
		} catch (Exception e) {
			if (read1[0] == 255) {
				Log.i(tag, "Lost connection");
			}
			return 255;
		}
	}

	/**
	 * Makes the robot move.
	 * 
	 * @param direction
	 *            Reffer to NXTValue Class
	 * @param power
	 *            form -100 to 100
	 */
	public void move(int direction, int power) {
		// int power = 100;

		if (power > 100) {
			power = 100;
		} else if (power < -100) {
			power = -100;
		}

		// Updating data based on what user wants
		if (direction == NXTValue.DIRECTION_FORWARD) {
			motorData[5] = (byte) power;
			// motorData[7] = 0x02;
			motorData[19] = (byte) power;
			// motorData[21] = 0x02;
		} else if (direction == NXTValue.DIRECTION_BACKWARD) {
			motorData[5] = (byte) -power;
			// motorData[7] = 0x02;
			motorData[19] = (byte) -power;
			// motorData[21] = 0x02;
		} else if (direction == NXTValue.DIRECTION_RIGHT) {
			motorData[5] = (byte) -power;
			// motorData[7] = 0x00;
			motorData[19] = (byte) power;
			// motorData[21] = 0x00;
		} else if (direction == NXTValue.DIRECTION_LEFT) {
			motorData[5] = (byte) power;
			// motorData[7] = 0x00;
			motorData[19] = (byte) -power;
			// motorData[21] = 0x00;
		} else {
			motorData[5] = (byte) 0;
			motorData[19] = (byte) 0;
		}

		// Writing the data to NXT device
		write(motorData);
	}

	/**
	 * Returns a byte array with motor's rotation (degree)
	 * 
	 * @return Byte array [right, left]
	 */
	public int[] getMotorRotation() {
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

	}

	/**
	 * Resets both right and left motor's position
	 */
	public void resetMotorRotation() {
		byte[] dataForRightMotor = { 0x04, 0x00, NXTValue.RETURN_VOID, 0x0A,
				rightMotorPort, 1 }; // 1 is boolean true in byte
		byte[] dataForleftMotor = { 0x04, 0x00, NXTValue.RETURN_VOID, 0x0A,
				leftMotorPort, 1 };

		write(dataForRightMotor);
		write(dataForleftMotor);
	}

	public double rotationToCm(int rotation) {
		return rotation / DEG_PER_CM;
	}

	public double cmToRotation(double cm) {
		return cm * DEG_PER_CM;
	}

	/**
	 * @deprecated
	 * @return
	 */
	public int[] evaluateDegPerCm() {
		resetMotorRotation();
		int start = getDistanceFromUltrasonicSensor();
		int at;
		move(NXTValue.DIRECTION_BACKWARD, 10);
		do {

			at = getDistanceFromUltrasonicSensor();
		} while (at <= start + 1);
		move(NXTValue.DIRECTION_STOP, 0);
		int[] rotation = getMotorRotation();

		return rotation;
	}

	// ////////////////////////////////////////////////////////////////////////////
	// XXX Map navigation

	public static final int SIDE_BOTH = 0;
	public static final int SIDE_RIGHT = 1;
	public static final int SIDE_LEFT = 2;

	private PositionPID pid;

	/**
	 * Starts the PositionPID (Thread). Use {@code stopPositionPID()} when
	 * finished
	 */
	public void startPositionPID() {
		pid = new PositionPID();
		pid.start();
	}

	/**
	 * Stops the PositionPID (Thread)
	 */
	public void stopPositionPID() {
		pid.active = false;

	}

	/**
	 * Start the PID with {@code startPositionPID()} before using this method to
	 * add.
	 * 
	 * @param side
	 *            Robot.SIDE_RIGHT, Robot.SIDE_LEFT or Robot.SIDE_BOTH
	 * @param amount
	 *            how many degree
	 * @return false if PID is not started
	 */
	public boolean addToPositionPID(int side, int amount) {
		if (pid != null) {
			if (side == SIDE_LEFT) {
				pid.wantDeg_left += amount;
			} else if (side == SIDE_RIGHT) {
				pid.wantDeg_right += amount;
			} else if (side == SIDE_BOTH) {
				pid.wantDeg_left += amount;
				pid.wantDeg_right += amount;
			} else {
				Log.i("addToPositionPID()",
						"Invalid side. use Robot.SIDE_RIGHT or Robot.SIDE_LEFT");
			}
			return true;
		} else {
			Log.i("addToPositionPID()", "");
			return false;
		}
	}

	private class PositionPID extends Thread {
		public PositionPID() {
			active = true;
		}

		private int wantDeg_left;
		private int wantDeg_right;
		private byte[] motorData;

		private boolean active = false;

		public void run() {

			resetMotorRotation();

			motorData = getMotorData();

			int[] motorPosition;
			int rightPosition;
			int leftPosition;
			int rightError;
			int leftError;
			int mulConst = 1;
			int rightPower;
			int leftPower;

			int limit = 100;

			// motorData[7] = 0x00;
			// motorData[21] = 0x00;

			do {

				motorPosition = getMotorRotation();

				rightPosition = motorPosition[0];
				Log.i("Pos_Right", rightPosition + "");
				leftPosition = motorPosition[1];
				Log.i("Pos_Left", leftPosition + "");

				rightError = wantDeg_right - rightPosition;
				Log.i("Err_Right", rightError + "");
				leftError = wantDeg_left - leftPosition;
				Log.i("Err_Left", leftError + "");

				rightPower = mulConst * rightError;
				Log.i("right Power", rightPower + "");

				leftPower = mulConst * leftError;
				Log.i("left Power", leftPower + "");

				if (rightPower > limit) {
					rightPower = limit;
				} else if (rightPower < -limit) {
					rightPower = -limit;
				}

				if (leftPower > limit) {
					leftPower = limit;
				} else if (leftPower < -limit) {
					leftPower = -limit;
				}

				motorData[5] = (byte) rightPower;
				motorData[19] = (byte) leftPower;
				write(motorData);

			} while (active);

			move(NXTValue.DIRECTION_STOP, 0);
		}

	}

	// ////////////////////////////////////////////////////////////////////////////

	/**
	 * Reads and returns (int array) any size of data from the NXT buffer.
	 * 
	 * @return int[] - The message that is read from the NXT buffer. Returns 255
	 *         if buffer is empty or an error has occurred.
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
			Log.i("read()", "Error!");
			e.printStackTrace();
			message = new int[1];
			message[0] = 255;
			return message;
		}
	}

	/**
	 * Write data to the NXT device.
	 * 
	 * @param data
	 *            - Array of data that will be sent to the NXT device
	 */
	public void write(byte[] data) {
		try {
			outStream.write(data);
			outStream.flush();
		} catch (Exception e) {
			Log.i("write()", "Error!");
			e.printStackTrace();
		}
	}

}
