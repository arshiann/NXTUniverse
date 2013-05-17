package ctech.nxtuniverse;

public class CustomValue {

	//
	// public static Control Control;

	// Command return type
	private final byte returnStatus = (byte) 0x00;
	private final byte nullReturn = (byte) 0x80;

	// Operation Codes.
	// Typically control sensors, cerveux or request information.
	// private final byte startProgram = (byte) 0x00;
	// private final byte stopProgram = (byte) 0x01;
	private final byte playSoundFile = (byte) 0x02;
	private final byte playTone = (byte) 0x03;
	private final byte setOutputState = (byte) 0x04;
	private final byte setInputMode = (byte) 0x05;
	private final byte getOutputState = (byte) 0x06;
	private final byte getInputValues = (byte) 0x07;
	private final byte resetScaledInputValue = (byte) 0x08;
	private final byte messageWrite = (byte) 0x09;
	private final byte resetMotorPosition = (byte) 0x0A;
	private final byte getBatteryLevel = (byte) 0x0B;
	private final byte stopSoundPlayback = (byte) 0x0C;
	private final byte keepAlive = (byte) 0x0D;
	private final byte LSGetStatus = (byte) 0x0E;
	private final byte LSWrite = (byte) 0x0F;
	private final byte LSRead = (byte) 0x10;
	private final byte getCurrentProgramName = (byte) 0x11;
	private final byte messageRead = (byte) 0x13;

	// Port Specifiers. Specify sensor or motor ports.
	// Input - For sensors
	private final byte sensor1 = (byte) 0x00;
	private final byte sensor2 = (byte) 0x01;
	private final byte sensor3 = (byte) 0x02;
	private final byte sensor4 = (byte) 0x03;

	// Output - For motors
	private final byte motorA = (byte) 0x00;
	private final byte motorB = (byte) 0x01;
	private final byte motorC = (byte) 0x02;
	private final byte motorAll = (byte) 0xFF;

	// Cerveux Modes. These modes alter the behavior of cerveux.
	private final byte coast = (byte) 0x00;
	private final byte motorOn = (byte) 0x01;
	private final byte brake = (byte) 0x02;
	private final byte regulated = (byte) 0x04;

	// Cerveux Regulation Modes
	// These regulation modes alter the behavior of Cerveux.
	private final byte regulationModeIdle = (byte) 0x00;
	private final byte regulationModeMotorSpeed = (byte) 0x01;
	private final byte regulationModeMotorSync = (byte) 0x02;

	// Cerveux Run States. These regulation modes alter the behavior of Cerveux.
	private final byte motorRunStateIdle = (byte) 0x00;
	private final byte motorRunStateRampUp = (byte) 0x10;
	private final byte motorRunStateRunning = (byte) 0x20;
	private final byte motorRunStateRampDown = (byte) 0x40;

	// Sensor Types. Specify sensor type and operation.
	private final byte noSensor = (byte) 0x00;
	private final byte LimitSwitch = (byte) 0x01;
	private final byte temperature = (byte) 0x02;
	private final byte ultrasonicSensor = (byte) 0x03;
	private final byte angle = (byte) 0x04;
	private final byte lightActive = (byte) 0x05;
	private final byte lightInactive = (byte) 0x06;
	private final byte soundDB = (byte) 0x07;
	private final byte soundDBA = (byte) 0x08;
	private final byte custom = (byte) 0x09;
	private final byte lowSpeed = (byte) 0x0A;
	private final byte lowSpeed9V = (byte) 0x0B;
	private final byte noOfSensorTypes = (byte) 0x0C;

	// Sensor Modes. These modes control sensor operation.
	private final byte rawMode = (byte) 0x00;
	private final byte booleanMode = (byte) 0x20;
	private final byte rransitionCntMode = (byte) 0x40;
	private final byte periodCounterMode = (byte) 0x60;
	private final byte PCTFullScaleMode = (byte) 0x80;
	private final byte celciusMode = (byte) 0xA0;
	private final byte fahrenheitMode = (byte) 0xC0;
	private final byte angleStepsMode = (byte) 0xE0;
	private final byte slopeMask = (byte) 0x1F;
	private final byte modeMask = (byte) 0xE0;

	// Command Return Values. Success and error codes returned by commands.
	private final byte success = (byte) 0x00;
	private final byte pendingCommunication = (byte) 0x20;
	private final byte mailboxEmpty = (byte) 0x40;
	private final byte noMoreHandles = (byte) 0x81;
	private final byte noSpace = (byte) 0x82;
	private final byte noMoreFiles = (byte) 0x83;
	private final byte endOfFileExpected = (byte) 0x84;
	private final byte endOfFile = (byte) 0x85;
	private final byte notALinearFile = (byte) 0x86;
	private final byte fileNotFound = (byte) 0x87;
	private final byte handleAllReadyClosed = (byte) 0x88;
	private final byte noLinearSpace = (byte) 0x89;
	private final byte undefinedError = (byte) 0x8A;
	private final byte fileIsBusy = (byte) 0x8B;
	private final byte noWriteBuffers = (byte) 0x8C;
	private final byte appendNotPossible = (byte) 0x8D;
	private final byte fileIsFull = (byte) 0x8E;
	private final byte fileExists = (byte) 0x8F;
	private final byte moduleNotFound = (byte) 0x90;
	private final byte outOfBoundary = (byte) 0x91;
	private final byte illegalFileName = (byte) 0x92;
	private final byte illegalHandle = (byte) 0x93;
	private final byte requestFailed = (byte) 0xBD;
	private final byte unknownOpCode = (byte) 0xBE;
	private final byte insanePacket = (byte) 0xBF;
	private final byte outOfRange = (byte) 0xC0;
	private final byte busError = (byte) 0xDD;
	private final byte communicationOverflow = (byte) 0xDE;
	private final byte chanelInvalid = (byte) 0xDF;
	private final byte chanelBusy = (byte) 0xE0;
	private final byte coActiveProgram = (byte) 0xEC;
	private final byte illegalSize = (byte) 0xED;
	private final byte illegalMailbox = (byte) 0xEE;
	private final byte invalidField = (byte) 0xEF;
	private final byte badInputOutput = (byte) 0xF0;
	private final byte insufficientMemmory = (byte) 0xFB;



	// Arshi's entry
	// Motor direction
	private final int stop = 0;
	private final int goForward = 1;
	private final int goBackward = 2;
	private final int turnRight = 3;
	private final int turnLeft = 4;

	// Connection confirmation beep tone played when Android is connected to the NXT device
	final byte[] confirmationTone = { 0x06, 0x00, (byte) 0x80, 0x03, 0x0B,
			0x02, (byte) 0xFA, 0x00 };

	// byte[] setPortMode = { 0x05, 0x00, (byte) 0x80, 0x05, 0x03, 0x0B, 0x00 };
	final byte[] setContinuous = { 0x08, 0x00, (byte) 0x80, // 0x00 to
															// 0x08
			0x0F, 0x03, 0x03, 0x00, 0x02, 0x41, 0x02 };

	final byte[] askStatusOnPort3 = { 0x03, 0x00, 0x00, 0x0E, 0x03 };
	final byte[] readLS = { 0x03, 0x00, 0x00, 0x10, 0x03 };
	final byte[] readByteZero = { 0x07, 0x00, 0x00, 0x0F, 0x03, 0x02, 0x01,
			0x02, 0x42 };

	public int getStop() {
		return stop;
	}

	public int getGoForward() {
		return goForward;
	}

	public int getGoBackward() {
		return goBackward;
	}

	public int getTurnRight() {
		return turnRight;
	}

	public int getTurnLeft() {
		return turnLeft;
	}

	// End of Arshi's entry

	public byte getReturnStatus() {
		return returnStatus;
	}

	public byte getNullReturn() {
		return nullReturn;
	}

	public byte getPlaySoundFile() {
		return playSoundFile;
	}

	public byte getPlayTone() {
		return playTone;
	}

	public byte getSetOutputState() {
		return setOutputState;
	}

	public byte getSetInputMode() {
		return setInputMode;
	}

	public byte getGetOutputState() {
		return getOutputState;
	}

	public byte getGetInputValues() {
		return getInputValues;
	}

	public byte getResetScaledInputValue() {
		return resetScaledInputValue;
	}

	public byte getMessageWrite() {
		return messageWrite;
	}

	public byte getResetMotorPosition() {
		return resetMotorPosition;
	}

	public byte getGetBatteryLevel() {
		return getBatteryLevel;
	}

	public byte getStopSoundPlayback() {
		return stopSoundPlayback;
	}

	public byte getKeepAlive() {
		return keepAlive;
	}

	public byte getLSGetStatus() {
		return LSGetStatus;
	}

	public byte getLSWrite() {
		return LSWrite;
	}

	public byte getLSRead() {
		return LSRead;
	}

	public byte getGetCurrentProgramName() {
		return getCurrentProgramName;
	}

	public byte getMessageRead() {
		return messageRead;
	}

	public byte getSensor1() {
		return sensor1;
	}

	public byte getSensor2() {
		return sensor2;
	}

	public byte getSensor3() {
		return sensor3;
	}

	public byte getSensor4() {
		return sensor4;
	}

	public byte getMotorA() {
		return motorA;
	}

	public byte getMotorB() {
		return motorB;
	}

	public byte getMotorC() {
		return motorC;
	}

	public byte getMotorAll() {
		return motorAll;
	}

	public byte getCoast() {
		return coast;
	}

	public byte getMotorOn() {
		return motorOn;
	}

	public byte getBrake() {
		return brake;
	}

	public byte getRegulated() {
		return regulated;
	}

	public byte getRegulationModeIdle() {
		return regulationModeIdle;
	}

	public byte getRegulationModeMotorSpeed() {
		return regulationModeMotorSpeed;
	}

	public byte getRegulationModeMotorSync() {
		return regulationModeMotorSync;
	}

	public byte getMotorRunStateIdle() {
		return motorRunStateIdle;
	}

	public byte getMotorRunStateRampUp() {
		return motorRunStateRampUp;
	}

	public byte getMotorRunStateRunning() {
		return motorRunStateRunning;
	}

	public byte getMotorRunStateRampDown() {
		return motorRunStateRampDown;
	}

	public byte getNoSensor() {
		return noSensor;
	}

	public byte getLimitSwitch() {
		return LimitSwitch;
	}

	public byte getTemperature() {
		return temperature;
	}

	public byte getUltrasonicSensor() {
		return ultrasonicSensor;
	}

	public byte getAngle() {
		return angle;
	}

	public byte getLightActive() {
		return lightActive;
	}

	public byte getLightInactive() {
		return lightInactive;
	}

	public byte getSoundDB() {
		return soundDB;
	}

	public byte getSoundDBA() {
		return soundDBA;
	}

	public byte getCustom() {
		return custom;
	}

	public byte getLowSpeed() {
		return lowSpeed;
	}

	public byte getLowSpeed9V() {
		return lowSpeed9V;
	}

	public byte getNoOfSensorTypes() {
		return noOfSensorTypes;
	}

	public byte getRawMode() {
		return rawMode;
	}

	public byte getBooleanMode() {
		return booleanMode;
	}

	public byte getRransitionCntMode() {
		return rransitionCntMode;
	}

	public byte getPeriodCounterMode() {
		return periodCounterMode;
	}

	public byte getPCTFullScaleMode() {
		return PCTFullScaleMode;
	}

	public byte getCelciusMode() {
		return celciusMode;
	}

	public byte getFahrenheitMode() {
		return fahrenheitMode;
	}

	public byte getAngleStepsMode() {
		return angleStepsMode;
	}

	public byte getSlopeMask() {
		return slopeMask;
	}

	public byte getModeMask() {
		return modeMask;
	}

	public byte getSuccess() {
		return success;
	}

	public byte getPendingCommunication() {
		return pendingCommunication;
	}

	public byte getMailboxEmpty() {
		return mailboxEmpty;
	}

	public byte getNoMoreHandles() {
		return noMoreHandles;
	}

	public byte getNoSpace() {
		return noSpace;
	}

	public byte getNoMoreFiles() {
		return noMoreFiles;
	}

	public byte getEndOfFileExpected() {
		return endOfFileExpected;
	}

	public byte getEndOfFile() {
		return endOfFile;
	}

	public byte getNotALinearFile() {
		return notALinearFile;
	}

	public byte getFileNotFound() {
		return fileNotFound;
	}

	public byte getHandleAllReadyClosed() {
		return handleAllReadyClosed;
	}

	public byte getNoLinearSpace() {
		return noLinearSpace;
	}

	public byte getUndefinedError() {
		return undefinedError;
	}

	public byte getFileIsBusy() {
		return fileIsBusy;
	}

	public byte getNoWriteBuffers() {
		return noWriteBuffers;
	}

	public byte getAppendNotPossible() {
		return appendNotPossible;
	}

	public byte getFileIsFull() {
		return fileIsFull;
	}

	public byte getFileExists() {
		return fileExists;
	}

	public byte getModuleNotFound() {
		return moduleNotFound;
	}

	public byte getOutOfBoundary() {
		return outOfBoundary;
	}

	public byte getIllegalFileName() {
		return illegalFileName;
	}

	public byte getIllegalHandle() {
		return illegalHandle;
	}

	public byte getRequestFailed() {
		return requestFailed;
	}

	public byte getUnknownOpCode() {
		return unknownOpCode;
	}

	public byte getInsanePacket() {
		return insanePacket;
	}

	public byte getOutOfRange() {
		return outOfRange;
	}

	public byte getBusError() {
		return busError;
	}

	public byte getCommunicationOverflow() {
		return communicationOverflow;
	}

	public byte getChanelInvalid() {
		return chanelInvalid;
	}

	public byte getChanelBusy() {
		return chanelBusy;
	}

	public byte getCoActiveProgram() {
		return coActiveProgram;
	}

	public byte getIllegalSize() {
		return illegalSize;
	}

	public byte getIllegalMailbox() {
		return illegalMailbox;
	}

	public byte getInvalidField() {
		return invalidField;
	}

	public byte getBadInputOutput() {
		return badInputOutput;
	}

	public byte getInsufficientMemmory() {
		return insufficientMemmory;
	}

}