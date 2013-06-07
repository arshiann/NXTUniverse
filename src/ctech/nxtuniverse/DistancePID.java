package ctech.nxtuniverse;

public class DistancePID extends Thread {
	
	DistancePID(int wantPosition) {
		setWantPosition(wantPosition);
		this.active = true;
	}
	
	// Remove static if necessary
	static double kp = 5;
	static double ki = 0;
	static double kd = 0;

	private int wantPosition; // Measured in cm
	private volatile boolean active = false; // XXX change it back to true

	public void run() {

		double distanceError = 0;
		double integral = 0;
		double derivative = 0;

		double actualPosition;

		double oldError = 0;

		double output = 0;

		byte[] motor = Control.motorDataStructure;
		// long startTime = System.currentTimeMillis();
		long startTime;

		while (active) {
			startTime = System.currentTimeMillis();

			actualPosition = Control.readDistance();

			// DistancePID calculation
			distanceError = wantPosition - actualPosition;
			integral += distanceError;

			if (integral > 100) {
				integral = 100;
			} else if (integral < -100) {
				integral = -100;
			}

			derivative = (distanceError - oldError);
			// DistancePID calculation ends

			output = ((kp * distanceError) + (ki * integral) + (kd * derivative));

			if (output > 100) {
				output = 100;
			} else if (output < -100) {
				output = -100;
			}

			motor[5] = (byte) -output;
			motor[19] = (byte) -output;
			Control.write(motor);

			oldError = distanceError;

			long timeElapsed = System.currentTimeMillis() - startTime;

			if (timeElapsed < 250) {
				try {
					Thread.sleep(250 - timeElapsed);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		motor[5] = 0;
		motor[19] = 0;
		Control.write(motor);

	}

	public void setWantPosition(int wantPosition) {
		// NXT's body in front of the sensor is 13 cm
		if (wantPosition <= 13) {
			this.wantPosition = 13;
		} else {
			this.wantPosition = wantPosition;
		}
	}

	public void kill() {
		active = false;
	}

}