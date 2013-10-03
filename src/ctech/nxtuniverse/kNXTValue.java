
package ctech.nxtuniverse;

public class kNXTValue {
	// You're welcome Dr. Cooper =) - Arshi A.
	
	// Command returns a value
	public static byte kNXTRet = 0x00;

	// Command does not return a value
	public static byte kNXTNoRet = (byte) 0x80;

	// Command is a system operation (USB only)
	public static byte kNXTSysOP = (byte) 0x01;

	// Operation Codes. This is a list of command operations. Commands
	// typically control sensors or servos), or request information.
	public static byte kNXTStartProgram = (0x00); // Start Program Op Code
	public static byte kNXTStopProgram = (0x01); // Stop Program Op Code
	public static byte kNXTPlaySoundFile = (0x02); // Play Sound File Op Code
	public static byte kNXTPlayTone = (0x03); // Play Tone Op Code
	public static byte kNXTSetOutputState = (0x04); // Set Output State Op
	public static byte kNXTSetInputMode = (0x05);
	public static byte kNXTGetOutputState = (0x06);
	public static byte kNXTGetInputValues = (0x07);
	public static byte kNXTResetScaledInputValue = (0x08);
	public static byte kNXTMessageWrite = (0x09);
	public static byte kNXTResetMotorPosition = (0x0A);
	public static byte kNXTGetBatteryLevel = (0x0B);
	public static byte kNXTStopSoundPlayback = (0x0C);
	public static byte kNXTKeepAlive = (0x0D);
	public static byte kNXTLSGetStatus = (0x0E);
	public static byte kNXTLSWrite = (0x0F);
	public static byte kNXTLSRead = (0x10);
	public static byte kNXTGetCurrentProgramName = (0x11);
	public static byte kNXTMessageRead = (0x13);

	// Port Specifiers. These enums specify sensor or motor ports.
	public static byte kNXTSensor1 = (0x00); // Sensor Port 1
	public static byte kNXTSensor2 = (0x01); // Sensor Port 2
	public static byte kNXTSensor3 = (0x02); // Sensor Port 3
	public static byte kNXTSensor4 = (0x03); // Sensor Port 4), the serial port

	public static byte kNXTMotorA = (0x00); // Motor Port A
	public static byte kNXTMotorB = (0x01); // Motor Port B
	public static byte kNXTMotorC = (0x02); // Motor Port C
	public static byte kNXTMotorAll = (byte) 0xFF; // All Motors

	// Servo Modes. These modes alter the behavior of servos.
	public static byte kNXTCoast = (0x00);
	public static byte kNXTMotorOn = (0x01);
	public static byte kNXTBrake = (0x02);
	public static byte kNXTRegulated = (0x04);

	// Servo Regulation Modes. These regulation modes alter the behavior of
	// servos.
	public static byte kNXTRegulationModeIdle = (0x00);
	public static byte kNXTRegulationModeMotorSpeed = (0x01);
	public static byte kNXTRegulationModeMotorSync = (0x02);

	// Servo Run States. These regulation modes alter the behavior of servos.
	public static byte kNXTMotorRunStateIdle = (0x00);
	public static byte kNXTMotorRunStateRampUp = (0x10);
	public static byte kNXTMotorRunStateRunning = (0x20);
	public static byte kNXTMotorRunStateRampDown = (0x40);

	// Sensor Types. Specify sensor type and operation.
	public static byte kNXTNoSensor = (0x00);
	public static byte kNXTSwitch = (0x01);
	public static byte kNXTTemperature = (0x02);
	public static byte kNXTReflection = (0x03);
	public static byte kNXTAngle = (0x04);
	public static byte kNXTLightActive = (0x05);
	public static byte kNXTLightInactive = (0x06);
	public static byte kNXTSoundDB = (0x07);
	public static byte kNXTSoundDBA = (0x08);
	public static byte kNXTCustom = (0x09);
	public static byte kNXTLowSpeed = (0x0A);
	public static byte kNXTLowSpeed9V = (0x0B);
	public static byte kNXTNoOfSensorTypes = (0x0C);

	// Sensor Modes. These modes control sensor operation.
	public static byte kNXTRawMode = (0x00);
	public static byte kNXTBooleanMode = (0x20);
	public static byte kNXTTransitionCntMode = (0x40);
	public static byte kNXTPeriodCounterMode = (0x60);
	public static byte kNXTPCTFullScaleMode = (byte) (0x80);
	public static byte kNXTCelciusMode = (byte) (0xA0);
	public static byte kNXTFahrenheitMode = (byte) (0xC0);
	public static byte kNXTAngleStepsMode = (byte) (0xE0);
	public static byte kNXTSlopeMask = (0x1F);
	public static byte kNXTModeMask = (byte) (0xE0);

	// Command Return Values. Success and error codes returned by commands.
	public static byte kNXTSuccess = (0x00);
	public static byte kNXTPendingCommunication = (0x20);
	public static byte kNXTMailboxEmpty = (0x40);
	public static byte kNXTNoMoreHandles = (byte) (0x81);
	public static byte kNXTNoSpace = (byte) (0x82);
	public static byte kNXTNoMoreFiles = (byte) (0x83);
	public static byte kNXTEndOfFileExpected = (byte) (0x84);
	public static byte kNXTEndOfFile = (byte) (0x85);
	public static byte kNXTNotALinearFile = (byte) (0x86);
	public static byte kNXTFileNotFound = (byte) (0x87);
	public static byte kNXTHandleAllReadyClosed = (byte) (0x88);
	public static byte kNXTNoLinearSpace = (byte) (0x89);
	public static byte kNXTUndefinedError = (byte) (0x8A);
	public static byte kNXTFileIsBusy = (byte) (0x8B);
	public static byte kNXTNoWriteBuffers = (byte) (0x8C);
	public static byte kNXTAppendNotPossible = (byte) (0x8D);
	public static byte kNXTFileIsFull = (byte) (0x8E);
	public static byte kNXTFileExists = (byte) (0x8F);
	public static byte kNXTModuleNotFound = (byte) (0x90);
	public static byte kNXTOutOfBoundary = (byte) (0x91);
	public static byte kNXTIllegalFileName = (byte) (0x92);
	public static byte kNXTIllegalHandle = (byte) (0x93);
	public static byte kNXTRequestFailed = (byte) (0xBD);
	public static byte kNXTUnknownOpCode = (byte) (0xBE);
	public static byte kNXTInsanePacket = (byte) (0xBF);
	public static byte kNXTOutOfRange = (byte) (0xC0);
	public static byte kNXTBusError = (byte) (0xDD);
	public static byte kNXTCommunicationOverflow = (byte) (0xDE);
	public static byte kNXTChanelInvalid = (byte) (0xDF);
	public static byte kNXTChanelBusy = (byte) (0xE0);
	public static byte kNXTNoActiveProgram = (byte) (0xEC);
	public static byte kNXTIllegalSize = (byte) (0xED);
	public static byte kNXTIllegalMailbox = (byte) (0xEE);
	public static byte kNXTInvalidField = (byte) (0xEF);
	public static byte kNXTBadInputOutput = (byte) (0xF0);
	public static byte kNXTInsufficientMemmory = (byte) (0xFB);

}