package ctech.nxtuniverse;

import android.util.Log;

public class SpeedPID extends Thread {

	SpeedPID() {
//		setWantSpeed(wantSpeed);
		this.active = true;
	}

	// Remove static if necessary
	static double kp = 3;
	static double ki = 0;
	static double kd = 0;

	private double wantSpeed = 3; // Measured in cm/s
	private volatile boolean active = false; // XXX change it to true

	public void run() {

		double speedError = 0;
		double integral = 0;
		double derivative = 0;

		double motorRotationCount;

		// double oldSpeedError = 0;
		double oldMotorRotationCount = 0;

		int output = 0;

		long time;
		long oldTime = 0;

		byte[] motor = Control.motorDataStructure;
		long startTime;

		while (active) {
			startTime = System.currentTimeMillis();
			// get value form NXT
			int[] tempMotorRotationCount = Control.getMotorRotationCount();

			// setting right motor value
			motorRotationCount = tempMotorRotationCount[0];
			Log.i("motor count", String.valueOf(motorRotationCount));
			Log.i("old motor count", String.valueOf(oldMotorRotationCount));
//			Control.resetMotorCount();

			time = System.currentTimeMillis();

			// Converting rotation to cm/s

			double motorSpeed = ((motorRotationCount - oldMotorRotationCount) / 25)
					/ ((double) (time - oldTime) / 1000);
			Log.i("speed", String.valueOf(motorSpeed));

			// PID Calculation
			speedError = wantSpeed - motorSpeed;

			// integral += speedError * (time - oldTime) ; //
			//
			// if (integral > 100) {
			// integral = 100;
			// } else if (integral < -100) {
			// integral = -100;
			// }
			//
			// derivative = (speedError - oldSpeedError) / (time - oldTime); //

			// End of PID Calculation

			// Value in cm/s
			output += (int) ((kp * speedError) + (ki * integral) + (kd * derivative));

			Log.i("output power", String.valueOf(output));

			// Limiting final output power
			if (output > 100) {
				output = 100;
			} else if (output < -100) {
				output = -100;
			}

			motor[5] = (byte) output;
			motor[19] = (byte) output;
			Control.write(motor);

			// oldSpeedError = speedError;
			oldTime = time;
			oldMotorRotationCount = motorRotationCount;

			long timeElapsed = System.currentTimeMillis() - startTime;

			if (timeElapsed < 250) {
				try {
					Thread.sleep(250 - timeElapsed);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		motor[5] = (byte) 0;
		motor[19] = (byte) 0;
		Control.write(motor);

	}

	public void setWantSpeed(double wantSpeed) {
		this.wantSpeed = wantSpeed;
	}

	public void kill() {
		active = false;
	}

}