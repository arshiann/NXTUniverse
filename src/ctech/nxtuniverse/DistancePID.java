package ctech.nxtuniverse;

import android.util.Log;

public class DistancePID extends Thread {

	DistancePID(int wantPosition) {
		setWantPosition(wantPosition);
		this.active = true;
	}

	private double kp = 5;
	private double ki = 0;
	private double kd = 0;

	private int wantPosition; // Measured in cm
	private volatile boolean active = false;

	public void run() {

		double distanceError = 0;
		double integral = 0;
		double derivative = 0;

		double actualPosition;

		double oldError = 0;

		//XXX Output should be Int
		double output = 0;
		double outputLimit = 100;
		
		byte[] motor = Control.motorData;
		// long startTime = System.currentTimeMillis();
		long startTime;

		while (active) {
			startTime = System.currentTimeMillis();

			actualPosition = Control.readDistance();
			Log.i("Actual Position", String.valueOf(actualPosition));

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
			Log.i("Output", String.valueOf(output));
			
			if (output > outputLimit) {
				output = outputLimit;
			} else if (output < -outputLimit) {
				output = -outputLimit;
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