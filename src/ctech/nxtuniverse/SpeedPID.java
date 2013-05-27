package ctech.nxtuniverse;

public class SpeedPID {


	
	static double kp = 5;
	static double ki = 0;
	static double kd = 0;

	public static double integral = 0;
	public static double oldError = 0;

	static double error;
	static double derivative;
	static double actualSpeed;

	static double output;

	private int wantSpeed;
	
	SpeedPID(int wantPosition){
		this.setWantPosition(wantPosition);
		this.active=true;
	}

	volatile boolean active = true;

	public void run() {

		byte[] motor = Control.motorDataStructure;
		long startTime = System.currentTimeMillis();

		while (active) {
//			startTime = System.currentTimeMillis();

//			actualSpeed = Control.readDistance();

			// DistancePID calculation
			error = wantSpeed - actualSpeed;
			integral += error*timeElapsed;

			if (integral > 100) {
				integral = 100;
			} else if (integral < -100) {
				integral = -100;
			}

			derivative = (error - oldError)/timeElapsed;
			// DistancePID calculation ends

			output = ((kp * error) + (ki * integral) + (kd * derivative));

			if (output > 100) {
				output = 100;
			} else if (output < -100) {
				output = -100;
			}

			motor[5] = (byte) -output;
			motor[19] = (byte) -output;
			Control.write(motor);

			oldError = error;

			
			
//			long timeElapsed = System.currentTimeMillis() - startTime;
//
//			if (timeElapsed < 250) {
//				try {
//					Thread.sleep(250 - timeElapsed);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
		}

		motor[5] = 0;
		motor[19] = 0;
		Control.write(motor);

	}
	
	public void setWantPosition (int wantPosition) {
		// NXT's body in front of the sensor is 13 cm
		if (wantPosition <= 13){
			this.wantSpeed = 13;
		} else {
			this.wantSpeed = wantPosition;
		}
	}

	public void kill() {
		active = false;
	}

}