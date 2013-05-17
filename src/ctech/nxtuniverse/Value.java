package ctech.nxtuniverse;

public enum Value {
/*! Message Codes.  These codes specify message type and specify if the command requires a return value or acknowledgement. */
    kNXTRet   (0x00), /*!< Command returns a value */
    kNXTNoRet ( 0x80), /*!< Command does not return a value */
    kNXTSysOP ( 0x01),  /*!< Command is a system operation (USB only) */

/*! Operation Codes.  This is a list of command operations.  Commands typically control sensors or servos), or request information. */
    kNXTStartProgram          ( 0x00), /*!< Start Program Op Code */
    kNXTStopProgram           ( 0x01), /*!< Stop Program Op Code */
    kNXTPlaySoundFile         ( 0x02), /*!< Play Sound File Op Code */
    kNXTPlayTone              ( 0x03), /*!< Play Tone Op Code */
    kNXTSetOutputState        ( 0x04), /*!< Set Output State Op Code */
    kNXTSetInputMode          ( 0x05), /*!< */
    kNXTGetOutputState        ( 0x06), /*!< */
    kNXTGetInputValues        ( 0x07), /*!< */
    kNXTResetScaledInputValue ( 0x08), /*!< */
    kNXTMessageWrite          ( 0x09), /*!< */
    kNXTResetMotorPosition    ( 0x0A), /*!< */
    kNXTGetBatteryLevel       ( 0x0B), /*!< */
    kNXTStopSoundPlayback     ( 0x0C), /*!< */
    kNXTKeepAlive             ( 0x0D), /*!< */
    kNXTLSGetStatus           ( 0x0E), /*!< */
    kNXTLSWrite               ( 0x0F), /*!< */
    kNXTLSRead                ( 0x10), /*!< */
    kNXTGetCurrentProgramName ( 0x11), /*!< */
    kNXTMessageRead           ( 0x13),  /*!< */

/*! Port Specifiers.  These enums specify sensor or motor ports. */
    kNXTSensor1  ( 0x00), /*!< Sensor Port 1 */
    kNXTSensor2  ( 0x01), /*!< Sensor Port 2 */
    kNXTSensor3  ( 0x02), /*!< Sensor Port 3 */
    kNXTSensor4  ( 0x03), /*!< Sensor Port 4), the serial port */

    kNXTMotorA   ( 0x00), /*!< Motor Port A */
    kNXTMotorB   ( 0x01), /*!< Motor Port B */
    kNXTMotorC   ( 0x02), /*!< Motor Port C */
    kNXTMotorAll ( 0xFF),  /*!< All Motors */

/*! Servo Modes.  These modes alter the behavior of servos. */
    kNXTCoast     ( 0x00), /*!< */
    kNXTMotorOn   ( 0x01), /*!< */
    kNXTBrake     ( 0x02), /*!< */
    kNXTRegulated ( 0x04),  /*!< */

/*! Servo Regulation Modes.  These regulation modes alter the behavior of servos. */
    kNXTRegulationModeIdle       ( 0x00), /*!< */
    kNXTRegulationModeMotorSpeed ( 0x01), /*!< */
    kNXTRegulationModeMotorSync  ( 0x02),  /*!< */

/*! Servo Run States.  These regulation modes alter the behavior of servos. */
    kNXTMotorRunStateIdle        ( 0x00), /*!< */
    kNXTMotorRunStateRampUp      ( 0x10), /*!< */
    kNXTMotorRunStateRunning     ( 0x20), /*!< */
    kNXTMotorRunStateRampDown    ( 0x40 ), /*!< */

/*! Sensor Types.  Specify sensor type and operation. */
    kNXTNoSensor            ( 0x00), /*!< */
    kNXTSwitch              ( 0x01), /*!< */
    kNXTTemperature         ( 0x02), /*!< */
    kNXTReflection          ( 0x03), /*!< */
    kNXTAngle               ( 0x04), /*!< */
    kNXTLightActive         ( 0x05), /*!< */
    kNXTLightInactive       ( 0x06), /*!< */
    kNXTSoundDB             ( 0x07), /*!< */
    kNXTSoundDBA            ( 0x08), /*!< */
    kNXTCustom              ( 0x09), /*!< */
    kNXTLowSpeed            ( 0x0A), /*!< */
    kNXTLowSpeed9V          ( 0x0B), /*!< */
    kNXTNoOfSensorTypes     ( 0x0C),  /*!< */

/*! Sensor Modes.  These modes control sensor operation. */
    kNXTRawMode             ( 0x00), /*!< */
    kNXTBooleanMode         ( 0x20), /*!< */
    kNXTTransitionCntMode   ( 0x40), /*!< */
    kNXTPeriodCounterMode   ( 0x60), /*!< */
    kNXTPCTFullScaleMode    ( 0x80), /*!< */
    kNXTCelciusMode         ( 0xA0), /*!< */
    kNXTFahrenheitMode      ( 0xC0), /*!< */
    kNXTAngleStepsMode      ( 0xE0), /*!< */
    kNXTSlopeMask           ( 0x1F), /*!< */
    kNXTModeMask            ( 0xE0 ), /*!< */

/*! Command Return Values.  Success and error codes returned by commands. */
    kNXTSuccess                 ( 0x00), /*!< */
    kNXTPendingCommunication    ( 0x20), /*!< */
    kNXTMailboxEmpty            ( 0x40), /*!< */
    kNXTNoMoreHandles           ( 0x81), /*!< */
    kNXTNoSpace                 ( 0x82), /*!< */
    kNXTNoMoreFiles             ( 0x83), /*!< */
    kNXTEndOfFileExpected       ( 0x84), /*!< */
    kNXTEndOfFile               ( 0x85), /*!< */
    kNXTNotALinearFile          ( 0x86), /*!< */
    kNXTFileNotFound            ( 0x87), /*!< */
    kNXTHandleAllReadyClosed    ( 0x88), /*!< */
    kNXTNoLinearSpace           ( 0x89), /*!< */
    kNXTUndefinedError          ( 0x8A), /*!< */
    kNXTFileIsBusy              ( 0x8B), /*!< */
    kNXTNoWriteBuffers          ( 0x8C), /*!< */
    kNXTAppendNotPossible       ( 0x8D), /*!< */
    kNXTFileIsFull              ( 0x8E), /*!< */
    kNXTFileExists              ( 0x8F), /*!< */
    kNXTModuleNotFound          ( 0x90), /*!< */
    kNXTOutOfBoundary           ( 0x91), /*!< */
    kNXTIllegalFileName         ( 0x92), /*!< */
    kNXTIllegalHandle           ( 0x93), /*!< */
    kNXTRequestFailed           ( 0xBD), /*!< */
    kNXTUnknownOpCode           ( 0xBE), /*!< */
    kNXTInsanePacket            ( 0xBF), /*!< */
    kNXTOutOfRange              ( 0xC0), /*!< */
    kNXTBusError                ( 0xDD), /*!< */
    kNXTCommunicationOverflow   ( 0xDE), /*!< */
    kNXTChanelInvalid           ( 0xDF), /*!< */
    kNXTChanelBusy              ( 0xE0), /*!< */
    kNXTNoActiveProgram         ( 0xEC), /*!< */
    kNXTIllegalSize             ( 0xED), /*!< */
    kNXTIllegalMailbox          ( 0xEE), /*!< */
    kNXTInvalidField            ( 0xEF), /*!< */
    kNXTBadInputOutput          ( 0xF0), /*!< */
    kNXTInsufficientMemmory     ( 0xFB); /*!< */

    private byte value;
    
    Value(int inItValue) {
    	value = (byte) inItValue;
    }

	public byte getValue () {
		return value;
	}
}
