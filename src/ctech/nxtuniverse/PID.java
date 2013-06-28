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

public class PID extends Thread {

	/**
	 * PID constructor 
	 * @param robot - Robot
	 * @param mode - Refer to NXTValue class
	 * @param target - Distance(cm) or Speed(cm/s)
	 */
	PID(Robot robot, int mode, int target) {
		if (mode == 1) {
			setWantPosition(target);
			active = true;
		} else if (mode == 2) {
			setWantSpeed(target);
			active = true;
		}
		this.mode = mode;
		this.robot1 = robot;
	}

	Robot robot1;
	
	int mode;

	// PID Controller's constants
	private double distanceKp = 5;
	private double distanceKi = 0;
	private double distanceKd = 0;

	private int wantPosition; // Measured in cm

	// PID Controller's constants
	double speedKp = 3;
	double speedKi = 0;
	double speedKd = 0;

	private double wantSpeed = 3; // Measured in cm/s

	// To control thread's life cycle
	private volatile boolean active = false;

	// XXX add pause and resume methods

	public void run() {

		if (mode == 1) {
			// Scope of variables
			double distanceError = 0;
			double integral = 0;
			double derivative = 0;

			double actualPosition;
			double oldError = 0;

			// XXX Output should be Int. Might be the cause of negative output
			int output = 0;
			int outputLimit = 100;
			int integralLimit = 100;

			byte[] motor = robot1.getMotorData();
			long startTime;

			// PID loop
			do {
				// Marking the start time of the cycle (the loop)
				startTime = System.currentTimeMillis();

				// Get distance
				actualPosition = robot1.getUltrasonicSensorValue();
				Log.i("Actual Position", String.valueOf(actualPosition));

				// //////////////// DistancePID calculation //////////////////
				distanceError = actualPosition - wantPosition;
				Log.i("Proportional", String.valueOf(distanceError));

				integral += distanceError; //XXX correct the equation
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
				output = (int) ((distanceKp * distanceError)
						+ (distanceKi * integral) + (distanceKd * derivative));
				Log.i("Output", String.valueOf(output));

				// Limiting the final output
				if (output > outputLimit) {
					output = outputLimit;
				} else if (output < -outputLimit) {
					output = -outputLimit;
				}

				// Updating the motor's data (byte array)
				motor[5] = (byte) output;
				motor[19] = (byte) output;

				// Writing data to the NXT device
				robot1.setMotorData(motor);
				robot1.write(motor);

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
			robot1.setMotorData(motor);
			robot1.write(motor);
		} else if (mode == 2) {

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

			byte[] motor = robot1.getMotorData();
			long startTime;
			do {
				// Marking the start time of the cycle (the loop)
				startTime = System.currentTimeMillis();

				// Get value form NXT
				int[] tempMotorRotationCount = robot1.getMotorPosition();

				// Setting right motor value
				motorRotationCount = tempMotorRotationCount[0];
				Log.i("Motor rotation", String.valueOf(motorRotationCount));
				Log.i("Old motor rotation",
						String.valueOf(oldMotorRotationCount));

				// To measure the speed
				time = System.currentTimeMillis();

				// Converting arbitrary rotation count to cm/s
				double motorSpeed = ((motorRotationCount - oldMotorRotationCount) / 25)
						/ ((double) (time - oldTime) / 1000);
				Log.i("Speed", String.valueOf(motorSpeed));

				// /////////////////// SpeedPID calculation
				// /////////////////////
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
				// derivative = (speedError - oldSpeedError) / (time -
				// oldTime);
				// Log.i("Derivative", String.valueOf(derivative));
				// ///////////////// SpeedPID calculation ends
				// //////////////////

				// Calculating the final output value
				output += (int) ((speedKp * speedError) + (speedKi * integral) + (speedKd * derivative));
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
				robot1.setMotorData(motor);
				robot1.write(motor);

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
			robot1.setMotorData(motor);
			robot1.write(motor);

		}

	}

	public void setWantPosition(int wantPosition) {
		// NXT's body in front of the sensor is 13 cm
		if (wantPosition <= 13) {
			this.wantPosition = 13;
		} else {
			this.wantPosition = wantPosition;
		}
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