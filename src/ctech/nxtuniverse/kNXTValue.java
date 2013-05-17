package ctech.nxtuniverse;

public class kNXTValue {
	/*
	 * ! Message Codes. These codes specify message type and specify if the
	 * command requires a return value or acknowledgement.
	 */
	private byte kNXTRet = (byte) 0x00; /* !< Command returns a value */
	private byte kNXTNoRet = (byte) 0x80; /* !< Command does not return a value */
	private byte kNXTSysOP = (byte) 0x01; /*
										 * !< Command is a system operation (USB
										 * only)
										 */

	/*
	 * ! Operation Codes. This is a list of command operations. Commands
	 * typically control sensors or servos); or request information.
	 */
	private byte kNXTStartProgram = (byte) 0x00; /* !< Start Program Op Code */
	private byte kNXTStopProgram = (byte) 0x01; /* !< Stop Program Op Code */
	private byte kNXTPlaySoundFile = (byte) 0x02; /* !< Play Sound File Op Code */
	private byte kNXTPlayTone = (byte) 0x03; /* !< Play Tone Op Code */
	private byte kNXTSetOutputState = (byte) 0x04; /*
													 * !< Set Output State Op
													 * Code
													 */
	private byte kNXTSetInputMode = (byte) 0x05;
	private byte kNXTGetOutputState = (byte) 0x06;
	private byte kNXTGetInputValues = (byte) 0x07;
	private byte kNXTResetScaledInputValue = (byte) 0x08;
	private byte kNXTMessageWrite = (byte) 0x09;
	private byte kNXTResetMotorPosition = (byte) 0x0A;
	private byte kNXTGetBatteryLevel = (byte) 0x0B;
	private byte kNXTStopSoundPlayback = (byte) 0x0C;
	private byte kNXTKeepAlive = (byte) 0x0D;
	private byte kNXTLSGetStatus = (byte) 0x0E;
	private byte kNXTLSWrite = (byte) 0x0F;
	private byte kNXTLSRead = (byte) 0x10;
	private byte kNXTGetCurrentProgramName = (byte) 0x11;
	private byte kNXTMessageRead = (byte) 0x13;

	/* ! Port Specifiers. These enums specify sensor or motor ports. */
	private byte kNXTSensor1 = (byte) 0x00; /* !< Sensor Port 1 */
	private byte kNXTSensor2 = (byte) 0x01; /* !< Sensor Port 2 */
	private byte kNXTSensor3 = (byte) 0x02; /* !< Sensor Port 3 */
	private byte kNXTSensor4 = (byte) 0x03; /*
											 * !< Sensor Port 4); the serial
											 * port
											 */

	private byte kNXTMotorA = (byte) 0x00; /* !< Motor Port A */
	private byte kNXTMotorB = (byte) 0x01; /* !< Motor Port B */
	private byte kNXTMotorC = (byte) 0x02; /* !< Motor Port C */
	private byte kNXTMotorAll = (byte) 0xFF; /* !< All Motors */

	/* ! Servo Modes. These modes alter the behavior of servos. */
	private byte kNXTCoast = (byte) 0x00;
	private byte kNXTMotorOn = (byte) 0x01;
	private byte kNXTBrake = (byte) 0x02;
	private byte kNXTRegulated = (byte) 0x04;

	/*
	 * ! Servo Regulation Modes. These regulation modes alter the behavior of
	 * servos.
	 */
	private byte kNXTRegulationModeIdle = (byte) 0x00;
	private byte kNXTRegulationModeMotorSpeed = (byte) 0x01;
	private byte kNXTRegulationModeMotorSync = (byte) 0x02;

	/* ! Servo Run States. These regulation modes alter the behavior of servos. */
	private byte kNXTMotorRunStateIdle = (byte) 0x00;
	private byte kNXTMotorRunStateRampUp = (byte) 0x10;
	private byte kNXTMotorRunStateRunning = (byte) 0x20;
	private byte kNXTMotorRunStateRampDown = (byte) 0x40;

	/* ! Sensor Types. Specify sensor type and operation. */
	private byte kNXTNoSensor = (byte) 0x00;
	private byte kNXTSwitch = (byte) 0x01;
	private byte kNXTTemperature = (byte) 0x02;
	private byte kNXTReflection = (byte) 0x03;
	private byte kNXTAngle = (byte) 0x04;
	private byte kNXTLightActive = (byte) 0x05;
	private byte kNXTLightInactive = (byte) 0x06;
	private byte kNXTSoundDB = (byte) 0x07;
	private byte kNXTSoundDBA = (byte) 0x08;
	private byte kNXTCustom = (byte) 0x09;
	private byte kNXTLowSpeed = (byte) 0x0A;
	private byte kNXTLowSpeed9V = (byte) 0x0B;
	private byte kNXTNoOfSensorTypes = (byte) 0x0C;

	/* ! Sensor Modes. These modes control sensor operation. */
	private byte kNXTRawMode = (byte) 0x00;
	private byte kNXTBooleanMode = (byte) 0x20;
	private byte kNXTTransitionCntMode = (byte) 0x40;
	private byte kNXTPeriodCounterMode = (byte) 0x60;
	private byte kNXTPCTFullScaleMode = (byte) 0x80;
	private byte kNXTCelciusMode = (byte) 0xA0;
	private byte kNXTFahrenheitMode = (byte) 0xC0;
	private byte kNXTAngleStepsMode = (byte) 0xE0;
	private byte kNXTSlopeMask = (byte) 0x1F;
	private byte kNXTModeMask = (byte) 0xE0;

	/* ! Command Return Values. Success and error codes returned by commands. */
	private byte kNXTSuccess = (byte) 0x00;
	private byte kNXTPendingCommunication = (byte) 0x20;
	private byte kNXTMailboxEmpty = (byte) 0x40;
	private byte kNXTNoMoreHandles = (byte) 0x81;
	private byte kNXTNoSpace = (byte) 0x82;
	private byte kNXTNoMoreFiles = (byte) 0x83;
	private byte kNXTEndOfFileExpected = (byte) 0x84;
	private byte kNXTEndOfFile = (byte) 0x85;
	private byte kNXTNotALinearFile = (byte) 0x86;
	private byte kNXTFileNotFound = (byte) 0x87;
	private byte kNXTHandleAllReadyClosed = (byte) 0x88;
	private byte kNXTNoLinearSpace = (byte) 0x89;
	private byte kNXTUndefinedError = (byte) 0x8A;
	private byte kNXTFileIsBusy = (byte) 0x8B;
	private byte kNXTNoWriteBuffers = (byte) 0x8C;
	private byte kNXTAppendNotPossible = (byte) 0x8D;
	private byte kNXTFileIsFull = (byte) 0x8E;
	private byte kNXTFileExists = (byte) 0x8F;
	private byte kNXTModuleNotFound = (byte) 0x90;
	private byte kNXTOutOfBoundary = (byte) 0x91;
	private byte kNXTIllegalFileName = (byte) 0x92;
	private byte kNXTIllegalHandle = (byte) 0x93;
	private byte kNXTRequestFailed = (byte) 0xBD;
	private byte kNXTUnknownOpCode = (byte) 0xBE;
	private byte kNXTInsanePacket = (byte) 0xBF;
	private byte kNXTOutOfRange = (byte) 0xC0;
	private byte kNXTBusError = (byte) 0xDD;
	private byte kNXTCommunicationOverflow = (byte) 0xDE;
	private byte kNXTChanelInvalid = (byte) 0xDF;
	private byte kNXTChanelBusy = (byte) 0xE0;
	private byte kNXTNoActiveProgram = (byte) 0xEC;
	private byte kNXTIllegalSize = (byte) 0xED;
	private byte kNXTIllegalMailbox = (byte) 0xEE;
	private byte kNXTInvalidField = (byte) 0xEF;
	private byte kNXTBadInputOutput = (byte) 0xF0;
	private byte kNXTInsufficientMemmory = (byte) 0xFB;

	public byte getkNXTRet() {
		return kNXTRet;
	}

	public byte getkNXTNoRet() {
		return kNXTNoRet;
	}

	public byte getkNXTSysOP() {
		return kNXTSysOP;
	}

	public byte getkNXTStartProgram() {
		return kNXTStartProgram;
	}

	public byte getkNXTStopProgram() {
		return kNXTStopProgram;
	}

	public byte getkNXTPlaySoundFile() {
		return kNXTPlaySoundFile;
	}

	public byte getkNXTPlayTone() {
		return kNXTPlayTone;
	}

	public byte getkNXTSetOutputState() {
		return kNXTSetOutputState;
	}

	public byte getkNXTSetInputMode() {
		return kNXTSetInputMode;
	}

	public byte getkNXTGetOutputState() {
		return kNXTGetOutputState;
	}

	public byte getkNXTGetInputValues() {
		return kNXTGetInputValues;
	}

	public byte getkNXTResetScaledInputValue() {
		return kNXTResetScaledInputValue;
	}

	public byte getkNXTMessageWrite() {
		return kNXTMessageWrite;
	}

	public byte getkNXTResetMotorPosition() {
		return kNXTResetMotorPosition;
	}

	public byte getkNXTGetBatteryLevel() {
		return kNXTGetBatteryLevel;
	}

	public byte getkNXTStopSoundPlayback() {
		return kNXTStopSoundPlayback;
	}

	public byte getkNXTKeepAlive() {
		return kNXTKeepAlive;
	}

	public byte getkNXTLSGetStatus() {
		return kNXTLSGetStatus;
	}

	public byte getkNXTLSWrite() {
		return kNXTLSWrite;
	}

	public byte getkNXTLSRead() {
		return kNXTLSRead;
	}

	public byte getkNXTGetCurrentProgramName() {
		return kNXTGetCurrentProgramName;
	}

	public byte getkNXTMessageRead() {
		return kNXTMessageRead;
	}

	public byte getkNXTSensor1() {
		return kNXTSensor1;
	}

	public byte getkNXTSensor2() {
		return kNXTSensor2;
	}

	public byte getkNXTSensor3() {
		return kNXTSensor3;
	}

	public byte getkNXTSensor4() {
		return kNXTSensor4;
	}

	public byte getkNXTMotorA() {
		return kNXTMotorA;
	}

	public byte getkNXTMotorB() {
		return kNXTMotorB;
	}

	public byte getkNXTMotorC() {
		return kNXTMotorC;
	}

	public byte getkNXTMotorAll() {
		return kNXTMotorAll;
	}

	public byte getkNXTCoast() {
		return kNXTCoast;
	}

	public byte getkNXTMotorOn() {
		return kNXTMotorOn;
	}

	public byte getkNXTBrake() {
		return kNXTBrake;
	}

	public byte getkNXTRegulated() {
		return kNXTRegulated;
	}

	public byte getkNXTRegulationModeIdle() {
		return kNXTRegulationModeIdle;
	}

	public byte getkNXTRegulationModeMotorSpeed() {
		return kNXTRegulationModeMotorSpeed;
	}

	public byte getkNXTRegulationModeMotorSync() {
		return kNXTRegulationModeMotorSync;
	}

	public byte getkNXTMotorRunStateIdle() {
		return kNXTMotorRunStateIdle;
	}

	public byte getkNXTMotorRunStateRampUp() {
		return kNXTMotorRunStateRampUp;
	}

	public byte getkNXTMotorRunStateRunning() {
		return kNXTMotorRunStateRunning;
	}

	public byte getkNXTMotorRunStateRampDown() {
		return kNXTMotorRunStateRampDown;
	}

	public byte getkNXTNoSensor() {
		return kNXTNoSensor;
	}

	public byte getkNXTSwitch() {
		return kNXTSwitch;
	}

	public byte getkNXTTemperature() {
		return kNXTTemperature;
	}

	public byte getkNXTReflection() {
		return kNXTReflection;
	}

	public byte getkNXTAngle() {
		return kNXTAngle;
	}

	public byte getkNXTLightActive() {
		return kNXTLightActive;
	}

	public byte getkNXTLightInactive() {
		return kNXTLightInactive;
	}

	public byte getkNXTSoundDB() {
		return kNXTSoundDB;
	}

	public byte getkNXTSoundDBA() {
		return kNXTSoundDBA;
	}

	public byte getkNXTCustom() {
		return kNXTCustom;
	}

	public byte getkNXTLowSpeed() {
		return kNXTLowSpeed;
	}

	public byte getkNXTLowSpeed9V() {
		return kNXTLowSpeed9V;
	}

	public byte getkNXTNoOfSensorTypes() {
		return kNXTNoOfSensorTypes;
	}

	public byte getkNXTRawMode() {
		return kNXTRawMode;
	}

	public byte getkNXTBooleanMode() {
		return kNXTBooleanMode;
	}

	public byte getkNXTTransitionCntMode() {
		return kNXTTransitionCntMode;
	}

	public byte getkNXTPeriodCounterMode() {
		return kNXTPeriodCounterMode;
	}

	public byte getkNXTPCTFullScaleMode() {
		return kNXTPCTFullScaleMode;
	}

	public byte getkNXTCelciusMode() {
		return kNXTCelciusMode;
	}

	public byte getkNXTFahrenheitMode() {
		return kNXTFahrenheitMode;
	}

	public byte getkNXTAngleStepsMode() {
		return kNXTAngleStepsMode;
	}

	public byte getkNXTSlopeMask() {
		return kNXTSlopeMask;
	}

	public byte getkNXTModeMask() {
		return kNXTModeMask;
	}

	public byte getkNXTSuccess() {
		return kNXTSuccess;
	}

	public byte getkNXTPendingCommunication() {
		return kNXTPendingCommunication;
	}

	public byte getkNXTMailboxEmpty() {
		return kNXTMailboxEmpty;
	}

	public byte getkNXTNoMoreHandles() {
		return kNXTNoMoreHandles;
	}

	public byte getkNXTNoSpace() {
		return kNXTNoSpace;
	}

	public byte getkNXTNoMoreFiles() {
		return kNXTNoMoreFiles;
	}

	public byte getkNXTEndOfFileExpected() {
		return kNXTEndOfFileExpected;
	}

	public byte getkNXTEndOfFile() {
		return kNXTEndOfFile;
	}

	public byte getkNXTNotALinearFile() {
		return kNXTNotALinearFile;
	}

	public byte getkNXTFileNotFound() {
		return kNXTFileNotFound;
	}

	public byte getkNXTHandleAllReadyClosed() {
		return kNXTHandleAllReadyClosed;
	}

	public byte getkNXTNoLinearSpace() {
		return kNXTNoLinearSpace;
	}

	public byte getkNXTUndefinedError() {
		return kNXTUndefinedError;
	}

	public byte getkNXTFileIsBusy() {
		return kNXTFileIsBusy;
	}

	public byte getkNXTNoWriteBuffers() {
		return kNXTNoWriteBuffers;
	}

	public byte getkNXTAppendNotPossible() {
		return kNXTAppendNotPossible;
	}

	public byte getkNXTFileIsFull() {
		return kNXTFileIsFull;
	}

	public byte getkNXTFileExists() {
		return kNXTFileExists;
	}

	public byte getkNXTModuleNotFound() {
		return kNXTModuleNotFound;
	}

	public byte getkNXTOutOfBoundary() {
		return kNXTOutOfBoundary;
	}

	public byte getkNXTIllegalFileName() {
		return kNXTIllegalFileName;
	}

	public byte getkNXTIllegalHandle() {
		return kNXTIllegalHandle;
	}

	public byte getkNXTRequestFailed() {
		return kNXTRequestFailed;
	}

	public byte getkNXTUnknownOpCode() {
		return kNXTUnknownOpCode;
	}

	public byte getkNXTInsanePacket() {
		return kNXTInsanePacket;
	}

	public byte getkNXTOutOfRange() {
		return kNXTOutOfRange;
	}

	public byte getkNXTBusError() {
		return kNXTBusError;
	}

	public byte getkNXTCommunicationOverflow() {
		return kNXTCommunicationOverflow;
	}

	public byte getkNXTChanelInvalid() {
		return kNXTChanelInvalid;
	}

	public byte getkNXTChanelBusy() {
		return kNXTChanelBusy;
	}

	public byte getkNXTNoActiveProgram() {
		return kNXTNoActiveProgram;
	}

	public byte getkNXTIllegalSize() {
		return kNXTIllegalSize;
	}

	public byte getkNXTIllegalMailbox() {
		return kNXTIllegalMailbox;
	}

	public byte getkNXTInvalidField() {
		return kNXTInvalidField;
	}

	public byte getkNXTBadInputOutput() {
		return kNXTBadInputOutput;
	}

	public byte getkNXTInsufficientMemmory() {
		return kNXTInsufficientMemmory;
	}

}

// public enum Value {
// /*! Message Codes. These codes specify message type and specify if the
// command requires a return value or acknowledgement. */
// kNXTRet (0x00), /*!< Command returns a value */
// kNXTNoRet ( 0x80), /*!< Command does not return a value */
// kNXTSysOP ( 0x01), /*!< Command is a system operation (USB only) */
//
// /*! Operation Codes. This is a list of command operations. Commands typically
// control sensors or servos), or request information. */
// kNXTStartProgram ( 0x00), /*!< Start Program Op Code */
// kNXTStopProgram ( 0x01), /*!< Stop Program Op Code */
// kNXTPlaySoundFile ( 0x02), /*!< Play Sound File Op Code */
// kNXTPlayTone ( 0x03), /*!< Play Tone Op Code */
// kNXTSetOutputState ( 0x04), /*!< Set Output State Op Code */
// kNXTSetInputMode ( 0x05), /*!< */
// kNXTGetOutputState ( 0x06), /*!< */
// kNXTGetInputValues ( 0x07), /*!< */
// kNXTResetScaledInputValue ( 0x08), /*!< */
// kNXTMessageWrite ( 0x09), /*!< */
// kNXTResetMotorPosition ( 0x0A), /*!< */
// kNXTGetBatteryLevel ( 0x0B), /*!< */
// kNXTStopSoundPlayback ( 0x0C), /*!< */
// kNXTKeepAlive ( 0x0D), /*!< */
// kNXTLSGetStatus ( 0x0E), /*!< */
// kNXTLSWrite ( 0x0F), /*!< */
// kNXTLSRead ( 0x10), /*!< */
// kNXTGetCurrentProgramName ( 0x11), /*!< */
// kNXTMessageRead ( 0x13), /*!< */
//
// /*! Port Specifiers. These enums specify sensor or motor ports. */
// kNXTSensor1 ( 0x00), /*!< Sensor Port 1 */
// kNXTSensor2 ( 0x01), /*!< Sensor Port 2 */
// kNXTSensor3 ( 0x02), /*!< Sensor Port 3 */
// kNXTSensor4 ( 0x03), /*!< Sensor Port 4), the serial port */
//
// kNXTMotorA ( 0x00), /*!< Motor Port A */
// kNXTMotorB ( 0x01), /*!< Motor Port B */
// kNXTMotorC ( 0x02), /*!< Motor Port C */
// kNXTMotorAll ( 0xFF), /*!< All Motors */
//
// /*! Servo Modes. These modes alter the behavior of servos. */
// kNXTCoast ( 0x00), /*!< */
// kNXTMotorOn ( 0x01), /*!< */
// kNXTBrake ( 0x02), /*!< */
// kNXTRegulated ( 0x04), /*!< */
//
// /*! Servo Regulation Modes. These regulation modes alter the behavior of
// servos. */
// kNXTRegulationModeIdle ( 0x00), /*!< */
// kNXTRegulationModeMotorSpeed ( 0x01), /*!< */
// kNXTRegulationModeMotorSync ( 0x02), /*!< */
//
// /*! Servo Run States. These regulation modes alter the behavior of servos. */
// kNXTMotorRunStateIdle ( 0x00), /*!< */
// kNXTMotorRunStateRampUp ( 0x10), /*!< */
// kNXTMotorRunStateRunning ( 0x20), /*!< */
// kNXTMotorRunStateRampDown ( 0x40 ), /*!< */
//
// /*! Sensor Types. Specify sensor type and operation. */
// kNXTNoSensor ( 0x00), /*!< */
// kNXTSwitch ( 0x01), /*!< */
// kNXTTemperature ( 0x02), /*!< */
// kNXTReflection ( 0x03), /*!< */
// kNXTAngle ( 0x04), /*!< */
// kNXTLightActive ( 0x05), /*!< */
// kNXTLightInactive ( 0x06), /*!< */
// kNXTSoundDB ( 0x07), /*!< */
// kNXTSoundDBA ( 0x08), /*!< */
// kNXTCustom ( 0x09), /*!< */
// kNXTLowSpeed ( 0x0A), /*!< */
// kNXTLowSpeed9V ( 0x0B), /*!< */
// kNXTNoOfSensorTypes ( 0x0C), /*!< */
//
// /*! Sensor Modes. These modes control sensor operation. */
// kNXTRawMode ( 0x00), /*!< */
// kNXTBooleanMode ( 0x20), /*!< */
// kNXTTransitionCntMode ( 0x40), /*!< */
// kNXTPeriodCounterMode ( 0x60), /*!< */
// kNXTPCTFullScaleMode ( 0x80), /*!< */
// kNXTCelciusMode ( 0xA0), /*!< */
// kNXTFahrenheitMode ( 0xC0), /*!< */
// kNXTAngleStepsMode ( 0xE0), /*!< */
// kNXTSlopeMask ( 0x1F), /*!< */
// kNXTModeMask ( 0xE0 ), /*!< */
//
// /*! Command Return Values. Success and error codes returned by commands. */
// kNXTSuccess ( 0x00), /*!< */
// kNXTPendingCommunication ( 0x20), /*!< */
// kNXTMailboxEmpty ( 0x40), /*!< */
// kNXTNoMoreHandles ( 0x81), /*!< */
// kNXTNoSpace ( 0x82), /*!< */
// kNXTNoMoreFiles ( 0x83), /*!< */
// kNXTEndOfFileExpected ( 0x84), /*!< */
// kNXTEndOfFile ( 0x85), /*!< */
// kNXTNotALinearFile ( 0x86), /*!< */
// kNXTFileNotFound ( 0x87), /*!< */
// kNXTHandleAllReadyClosed ( 0x88), /*!< */
// kNXTNoLinearSpace ( 0x89), /*!< */
// kNXTUndefinedError ( 0x8A), /*!< */
// kNXTFileIsBusy ( 0x8B), /*!< */
// kNXTNoWriteBuffers ( 0x8C), /*!< */
// kNXTAppendNotPossible ( 0x8D), /*!< */
// kNXTFileIsFull ( 0x8E), /*!< */
// kNXTFileExists ( 0x8F), /*!< */
// kNXTModuleNotFound ( 0x90), /*!< */
// kNXTOutOfBoundary ( 0x91), /*!< */
// kNXTIllegalFileName ( 0x92), /*!< */
// kNXTIllegalHandle ( 0x93), /*!< */
// kNXTRequestFailed ( 0xBD), /*!< */
// kNXTUnknownOpCode ( 0xBE), /*!< */
// kNXTInsanePacket ( 0xBF), /*!< */
// kNXTOutOfRange ( 0xC0), /*!< */
// kNXTBusError ( 0xDD), /*!< */
// kNXTCommunicationOverflow ( 0xDE), /*!< */
// kNXTChanelInvalid ( 0xDF), /*!< */
// kNXTChanelBusy ( 0xE0), /*!< */
// kNXTNoActiveProgram ( 0xEC), /*!< */
// kNXTIllegalSize ( 0xED), /*!< */
// kNXTIllegalMailbox ( 0xEE), /*!< */
// kNXTInvalidField ( 0xEF), /*!< */
// kNXTBadInputOutput ( 0xF0), /*!< */
// kNXTInsufficientMemmory ( 0xFB); /*!< */
//
// private byte value;
//
// Value(int inItValue) {
// value = (byte) inItValue;
// }
//
// public byte getValue () {
// return value;
// }
// }
