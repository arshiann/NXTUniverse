/*
 * DistancePID Controller - A Proportional-Integral-Derivative Controller
 * 
 * A PID controller calculates an "error" value as the difference between
 * a measured process variable and a desired setpoint. The controller
 * attempts to minimize the error by adjusting the process control inputs.
 * 
 * For further reading: http://en.wikipedia.org/wiki/PID_controller
 */

package ctech.nxtuniverse;

import android.util.Log;

public class DistancePID extends Thread {

	/**
	 * Maintain a certain distance from a physical object.
	 * 
	 * @param wantPosition
	 *            - Distance to maintain
	 */
	DistancePID(int wantPosition) {
		// Setting wantPosition
		setWantPosition(wantPosition);

		// Thread's life control
		active = true;
	}

	// PID Controller's constants
	private double kp = 5;
	private double ki = 0;
	private double kd = 0;

	private int wantPosition; // Measured in cm

	// To control thread's life cycle
	private volatile boolean active = false;

	// XXX add pause and resume methods

	public void run() {

		// Scope of variables
		double distanceError = 0;
		double integral = 0;
		double derivative = 0;

		double actualPosition;
		double oldError = 0;

		// XXX Output should be Int. Might be the cause of negative output
		double output = 0;
		int outputLimit = 100;
		int integralLimit = 100;

		byte[] motor = Control.motorData;
		long startTime;

		// PID loop
		do {
			// Marking the start time of the cycle (the loop)
			startTime = System.currentTimeMillis();

			// Get distance
			actualPosition = Control.readDistance();
			Log.i("Actual Position", String.valueOf(actualPosition));

			// //////////////// DistancePID calculation //////////////////
			distanceError = wantPosition - actualPosition;
			Log.i("Proportional", String.valueOf(distanceError));
			
			integral += distanceError;
			Log.i("Integral", String.valueOf(integral));

			// Limiting the integral
			if (integral > integralLimit) {
				integral = integralLimit;
			} else if (integral < -integralLimit) {
				integral = -integralLimit;
			}

			derivative = (distanceError - oldError);
			Log.i("Derivative", String.valueOf(derivative));
			// ////////////// DistancePID calculation ends ///////////////

			// Calculating the final output
			output = ((kp * distanceError) + (ki * integral) + (kd * derivative));
			Log.i("Output", String.valueOf(output));

			// Limiting the final output
			if (output > outputLimit) {
				output = outputLimit;
			} else if (output < -outputLimit) {
				output = -outputLimit;
			}

			// Updating the motor's data (byte array)
			motor[5] = (byte) -output;
			motor[19] = (byte) -output;

			// Writing data to the NXT device
			Control.write(motor);

			// Setting old values
			oldError = distanceError;

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
			}
			// Purpose of waiting until 250 milliseconds:
			// PID controller needs to work like a clock.
		} while (active);

		// When user stops the thread,
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

	// XXX add "It is recommended to use this method in "onDestroy" method." in
	// the doc after adding pause and resume method
	/**
	 * Stops the PID controller completely.
	 */
	public void kill() {
		active = false;
	}

}