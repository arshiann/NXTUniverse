/*
 * SpeedPID Controller - A Proportional-Integral-Derivative Controller
 * 
 * A PID controller calculates an "error" value as the difference between
 * a measured process variable and a desired setpoint. The controller
 * attempts to minimize the error by adjusting the process control inputs.
 * 
 * For further reading: http://en.wikipedia.org/wiki/PID_controller
 */

package ctech.nxtuniverse;

import android.util.Log;

public class SpeedPID extends Thread {

	/**
	 * Maintain a certain speed.
	 * 
	 * @param wantSpeed
	 *            - Speed to maintain (measured in cm/s)
	 */
	SpeedPID(int wantSpeed) {
		// Setting wantSpeed
		setWantSpeed(wantSpeed);

		// Thread's life control
		this.active = true;
	}

	// PID Controller's constants
	double kp = 3;
	double ki = 0;
	double kd = 0;

	private double wantSpeed = 3; // Measured in cm/s

	// To control thread's life cycle
	private volatile boolean active = false;

	// XXX add pause and resume methods

	public void run() {

		// Scope of variables
		double speedError = 0;
		double integral = 0;
		double derivative = 0;

		double motorRotationCount;

		// double oldSpeedError = 0;
		double oldMotorRotationCount = 0;

		int output = 0;
		int outputLimit = 100;
		// XXX Un-comment this when integral is in use
		// int integralLimit = 100;

		long time;
		long oldTime = 0;

		byte[] motor = Control.motorData;
		long startTime;

		do {
			// Marking the start time of the cycle (the loop)
			startTime = System.currentTimeMillis();

			// Get value form NXT
			int[] tempMotorRotationCount = Control.getMotorPosition();

			// Setting right motor value
			motorRotationCount = tempMotorRotationCount[0];
			Log.i("Motor rotation", String.valueOf(motorRotationCount));
			Log.i("Old motor rotation", String.valueOf(oldMotorRotationCount));

			// To measure the speed
			time = System.currentTimeMillis();

			// Converting arbitrary rotation count to cm/s
			double motorSpeed = ((motorRotationCount - oldMotorRotationCount) / 25)
					/ ((double) (time - oldTime) / 1000);
			Log.i("Speed", String.valueOf(motorSpeed));

			// /////////////////// SpeedPID calculation /////////////////////
			speedError = wantSpeed - motorSpeed;
			Log.i("Proportional", String.valueOf(speedError));

			// integral += speedError * (time - oldTime) ;
			// Log.i("Integral", String.valueOf(integral));
			//
			// if (integral > integralLimit) {
			// integral = integralLimit;
			// } else if (integral < -integralLimit) {
			// integral = -integralLimit;
			// }
			//
			// derivative = (speedError - oldSpeedError) / (time - oldTime);
			// Log.i("Derivative", String.valueOf(derivative));
			// ///////////////// SpeedPID calculation ends //////////////////

			// Calculating the final output value
			output += (int) ((kp * speedError) + (ki * integral) + (kd * derivative));
			Log.i("Output", String.valueOf(output));

			// Limiting final output power
			if (output > outputLimit) {
				output = outputLimit;
			} else if (output < -outputLimit) {
				output = -outputLimit;
			}

			// Updating the motor's data
			motor[5] = (byte) output;
			motor[19] = (byte) output;

			// Writing data to the NXT device
			Control.write(motor);

			// Setting old values
			// oldSpeedError = speedError;
			oldTime = time;
			oldMotorRotationCount = motorRotationCount;

			// Calculating the time elapse.
			// The time to complete the cycle (loop).
			long timeElapsed = System.currentTimeMillis() - startTime;

			// If the cycle is completed under 250 milliseconds
			if (timeElapsed < 250) {
				try {
					// Spend rest of the time waiting
					Thread.sleep(250 - timeElapsed);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// Purpose of waiting until 250 milliseconds:
				// PID controller needs to work like a clock.
			}
		} while (active);

		// When user stops the thread,
		motor[5] = (byte) 0;
		motor[19] = (byte) 0;
		Control.write(motor);

	}

	public void setWantSpeed(double wantSpeed) {
		this.wantSpeed = wantSpeed;
	}

	// XXX add "It is recommended to use this method in "onDestroy" method." in
	// the doc after adding pause and resume method
	/**
	 * Stops the PID controller completely.
	 */
	public void kill() {
		active = false;
	}

}