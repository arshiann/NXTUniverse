package ctech.nxtuniverse;

public class NXTValue {

	// Command return type
	/**
	 * NXT device will return status
	 */
	public static final byte RETURN_STATUS = (byte) 0x00;
	/**
	 * NXT device will not return anything
	 */
	public static final byte RETURN_VOID = (byte) 0x80;

	// Operational Codes.
	// Typically control sensors, cerveux or request information.
	/** @deprecated */
	public static final byte START_PROGRAM = (byte) 0x00;
	/** @deprecated */
	public static final byte STOP_PROGRAM = (byte) 0x01;
	public static final byte PLAY_SOUND_FILE = (byte) 0x02;
	public static final byte PLAY_TONE = (byte) 0x03;
	public static final byte SET_OUTPUT_STATE = (byte) 0x04;
	public static final byte SET_INPUT_MODE = (byte) 0x05;
	public static final byte GET_OUTPUT_STATE = (byte) 0x06;
	public static final byte GET_INPUT_VALUES = (byte) 0x07;
	public static final byte RESET_SCALED_INPUT_VALUE = (byte) 0x08;
	public static final byte MESSAGE_WRITE = (byte) 0x09;
	public static final byte RESET_MOTOR_POSITION = (byte) 0x0A;
	public static final byte GET_BATTERY_LEVEL = (byte) 0x0B;
	public static final byte STOP_SOUND_PLAYBACK = (byte) 0x0C;
	public static final byte KEEP_ALIVE = (byte) 0x0D;
	public static final byte LS_GET_STATUS = (byte) 0x0E;
	public static final byte LS_WRITE = (byte) 0x0F;
	public static final byte LS_READ = (byte) 0x10;
	/** @deprecated */
	public static final byte GET_CURRENT_PROGRAM_NAME = (byte) 0x11;
	public static final byte MESSAGE_READ = (byte) 0x13;

	// //////////////////////////// MOTORS //////////////////////////////
	// Output ports (for motors)
	public static final byte MOTOR_PORT_A = (byte) 0x00;
	public static final byte MOTOR_PORT_B = (byte) 0x01;
	public static final byte MOTOR_PORT_C = (byte) 0x02;
	/** @deprecated */
	public static final byte motorAll = (byte) 0xFF;

	// Modes. To alter how the cerveux utilize motors.
	public static final byte MOTOR_COAST = (byte) 0x00;
	public static final byte MOTOR_ON = (byte) 0x01;
	public static final byte MOTOR_BRAKE = (byte) 0x02;
	public static final byte MOTOR_REGULATED = (byte) 0x04;

	// Regulation Modes. Alters how the cerveux regulate motors.
	public static final byte MOTOR_REGULATION_IDLE = (byte) 0x00;
	public static final byte MOTOR_REGULATION_SPEED = (byte) 0x01;
	public static final byte MOTOR_REGULATION_SYNC = (byte) 0x02;

	// Run States.
	public static final byte MOTOR_RUNSTATE_IDLE = (byte) 0x00;
	public static final byte MOTOR_RUNSTATE_RAMPUP = (byte) 0x10;
	public static final byte MOTOR_RUNSTATE_RUNNING = (byte) 0x20;
	public static final byte MOTOR_RUNSTATE_RAMPDOWN = (byte) 0x40;

	// //////////////////////////// SENSORS //////////////////////////////
	// Input ports (for sensors)
	public static final byte SENSOR_PORT_1 = (byte) 0x00;
	public static final byte SENSOR_PORT_2 = (byte) 0x01;
	public static final byte SENSOR_PORT_3 = (byte) 0x02;
	public static final byte SENSOR_PORT_4 = (byte) 0x03;

	// Sensor Types. Specify sensor type and operation.
	/** @deprecated */
	public static final byte SENSOR_TYPE_NO_SENSOR = (byte) 0x00;
	public static final byte SENSOR_TYPE_LIMIT_SWITCH = (byte) 0x01;
	public static final byte SENSOR_TYPE_TEMPERATURE = (byte) 0x02;
	public static final byte SENSOR_TYPE_ULTRASONIC_SENSOR = (byte) 0x03;
	public static final byte SENSOR_TYPE_ANGLE = (byte) 0x04;
	public static final byte SENSOR_TYPE_LIGHT_ACTIVE = (byte) 0x05;
	public static final byte SENSOR_TYPE_LIGHT_INACTIVE = (byte) 0x06;
	public static final byte SENSOR_TYPE_SOUND_DB = (byte) 0x07;
	public static final byte SENSOR_TYPE_SOUND_DBA = (byte) 0x08;
	public static final byte SENSOR_TYPE_CUSTOM = (byte) 0x09;
	public static final byte SENSOR_TYPE_LOW_SPEED = (byte) 0x0A;
	public static final byte SENSOR_TYPE_LOW_SPEED_9V = (byte) 0x0B;
	// Deprecated
	// public static final byte SENSOR_TYPE_NO_OF_SENSOR_TYPES = (byte) 0x0C;

	// Sensor Modes. These modes control how the sensor will collect data.
	public static final byte SENSOR_MODE_RAW = (byte) 0x00;
	public static final byte SENSOR_MODE_BOOLEAN = (byte) 0x20;
	public static final byte SENSOR_MODE_TRANSITION_CNT = (byte) 0x40;
	public static final byte SENSOR_MODE_PERIOD_COUNTER = (byte) 0x60;
	public static final byte SENSOR_MODE_PCT_FULL_SCALE = (byte) 0x80;
	public static final byte SENSOR_MODE_CELCIUS = (byte) 0xA0;
	public static final byte SENSOR_MODE_FAHRENHEIT = (byte) 0xC0;
	public static final byte SENSOR_MODE_ANGLE_STEPS = (byte) 0xE0;
	public static final byte SENSOR_MODE_SLOPE_MASK = (byte) 0x1F;
	public static final byte SENSOR_MODE_MASK = (byte) 0xE0;

	// ////////////////////////// RETURNED CODES //////////////////////////////
	// NXT's Return Values. Success and error codes returned by NXT.
	public static final byte RETURNED_SUCCESS = (byte) 0x00;
	public static final byte RETURNED_PENDING_COMMUNICATION = (byte) 0x20;
	public static final byte RETURNED_MAILBOX_EMPTY = (byte) 0x40;
	public static final byte RETURNED_NO_MORE_HANDLES = (byte) 0x81;
	public static final byte RETURNED_NO_SPACE = (byte) 0x82;
	public static final byte RETURNED_NO_MORE_FILES = (byte) 0x83;
	public static final byte RETURNED_END_OF_FILE_EXPECTED = (byte) 0x84;
	public static final byte RETURNED_END_OF_FILE = (byte) 0x85;
	public static final byte RETURNED_NOT_A_LINEAR_FILE = (byte) 0x86;
	public static final byte RETURNED_FILE_NOT_FOUND = (byte) 0x87;
	public static final byte RETURNED_HANDLE_ALL_READY_CLOSED = (byte) 0x88;
	public static final byte RETURNED_NO_LINEAR_SPACE = (byte) 0x89;
	public static final byte RETURNED_UNDEFINED_ERROR = (byte) 0x8A;
	public static final byte RETURNED_FILE_IS_BUSY = (byte) 0x8B;
	public static final byte RETURNED_NO_WRITE_BUFFERS = (byte) 0x8C;
	public static final byte RETURNED_APPEND_NOT_POSSIBLE = (byte) 0x8D;
	public static final byte RETURNED_FILE_IS_FULL = (byte) 0x8E;
	public static final byte RETURNED_FILE_EXISTS = (byte) 0x8F;
	public static final byte RETURNED_MODULE_NOT_FOUND = (byte) 0x90;
	public static final byte RETURNED_OUT_OF_BOUNDARY = (byte) 0x91;
	public static final byte RETURNED_ILLEGAL_FILE_NAME = (byte) 0x92;
	public static final byte RETURNED_ILLEGAL_HANDLE = (byte) 0x93;
	public static final byte RETURNED_REQUEST_FAILED = (byte) 0xBD;
	public static final byte RETURNED_UNKNOWN_OP_CODE = (byte) 0xBE;
	public static final byte RETURNED_INSANE_PACKET = (byte) 0xBF;
	public static final byte RETURNED_OUT_OF_RANGE = (byte) 0xC0;
	public static final byte RETURNED_BUS_ERROR = (byte) 0xDD;
	public static final byte RETURNED_COMMUNICATION_OVERFLOW = (byte) 0xDE;
	public static final byte RETURNED_CHANEL_INVALID = (byte) 0xDF;
	public static final byte RETURNED_CHANEL_BUSY = (byte) 0xE0;
	public static final byte RETURNED_CO_ACTIVE_PROGRAM = (byte) 0xEC;
	public static final byte RETURNED_ILLEGAL_SIZE = (byte) 0xED;
	public static final byte RETURNED_ILLEGAL_MAILBOX = (byte) 0xEE;
	public static final byte RETURNED_INVALID_FIELD = (byte) 0xEF;
	public static final byte RETURNED_BAD_INPUT_OUTPUT = (byte) 0xF0;
	public static final byte RETURNED_INSUFFICIENT_MEMMORY = (byte) 0xFB;

	// Extra
	// Motor direction
	public static final int DIRECTION_STOP = 0;
	public static final int DIRECTION_FORWARD = 1;
	public static final int DIRECTION_BACKWARD = 2;
	public static final int DIRECTION_RIGHT = 3;
	public static final int DIRECTION_LEFT = 4;

	public static final int PID_MODE_DISTANCE = 1;
	public static final int PID_MODE_SPEED = 2;

	/**
	 * Connection confirmation beep tone played on NXT device when Android
	 * device is successfully connected
	 */
	public static final byte[] CONFIRMATION_TONE = { 0x06, 0x00, (byte) 0x80,
			0x03, 0x0B, 0x02, (byte) 0xFA, 0x00 };

	// byte[] setPortMode = { 0x05, 0x00, (byte) 0x80, 0x05, 0x03, 0x0B, 0x00 };

	// public static final byte[] ASK_STATUS_ON_PORT_4 = { 0x03, 0x00, 0x00,
	// 0x0E,
	// 0x03 }; // port 4 -> 0x03

	// public static final byte[] READ_BYTE_ZERO = { 0x07, 0x00, 0x00, 0x0F,
	// 0x03,
	// 0x02, 0x01, 0x02, 0x42 };
	//
	// /**
	// * Byte array of data for two motors (right and left)
	// */
	// public static byte[] MOTOR_DATA = {
	// // Right motor
	// 0x0c, // Length (from byte 2 to 12 inclusive)
	// 0x00, // Start from byte 2
	// (byte) RETURN_VOID, // Return type
	// 0x04, // setOutputState
	// Control.rightMotorPort, // Port
	// (byte) 0, // Power
	// 0x07, // Mode byte (unknown value)
	// MOTOR_REGULATION_SYNC, // Motor regulation
	// (byte) 0, // Turn ratio
	// MOTOR_RUNSTATE_RUNNING, // Run state
	// 0x00, // Tacho limit
	// 0x00, // Tacho limit
	// 0x00, // Tacho limit
	// 0x00, // Tacho limit
	//
	// // Left motor
	// 0x0c, // Length (from byte 2 to 12 inclusive)
	// 0x00, // Start from byte 2
	// (byte) RETURN_VOID, // Return type
	// SET_OUTPUT_STATE, // Set Output State
	// Control.leftMotorPort, // Port
	// (byte) 0, // Power
	// 0x07, // Mode byte (unknown value)
	// MOTOR_REGULATION_SYNC, // Motor regulation
	// (byte) 0, // Turn ratio
	// MOTOR_RUNSTATE_RUNNING, // Run state
	// 0x00, // Tacho limit
	// 0x00, // Tacho limit
	// 0x00, // Tacho limit
	// 0x00 // Tacho limit
	// };
}
