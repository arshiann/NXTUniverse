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
	 * 
	 * @param robot
	 *            - Robot
	 * @param mode
	 *            - Refer to NXTValue class
	 * @param target
	 *            - Distance(cm) or Speed(cm/s)
	 */
	PID(Robot robot, int mode, int target) {
		if (mode == NXTValue.PID_MODE_DISTANCE) {
			setWantDistance(target);
			active = true;
		} else if (mode == NXTValue.PID_MODE_SPEED) {
			setWantSpeed(target);
			active = true;
		}
		this.mode = mode;
		this.robot1 = robot;
	}

	private Robot robot1;

	private int mode;

	// Distance PID Controller's constants
	private double distanceKp = 5;
	private double distanceKi = 0;
	private double distanceKd = 0;
	private int wantDistance; // Measured in cm

	// Speed PID Controller's constants
	private double speedKp = 3;
	private double speedKi = 0;
	private double speedKd = 0;
	private double wantSpeed; // Measured in cm/s

	// 
	
	// To control thread's life cycle
	private volatile boolean active = false;

	// XXX add pause and resume methods

	public void run() {

		if (mode == NXTValue.PID_MODE_DISTANCE) {
			// Scope of variables
			double distanceError = 0;
			double distanceIntegral = 0;
			double distanceDerivative = 0;

			double actualDistance;
			double oldDistanceError = 0;

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
				actualDistance = robot1.getDistanceFromUltrasonicSensor();
				Log.i("Actual Distance", String.valueOf(actualDistance));

				// //////////////// DistancePID calculation //////////////////
				distanceError = actualDistance - wantDistance;
				Log.i("Proportional", String.valueOf(distanceError));

				distanceIntegral += distanceError; // XXX correct the equation
				Log.i("Integral", String.valueOf(distanceIntegral));

				// Limiting the integral
				if (distanceIntegral > integralLimit) {
					distanceIntegral = integralLimit;
				} else if (distanceIntegral < -integralLimit) {
					distanceIntegral = -integralLimit;
				}

				distanceDerivative = (distanceError - oldDistanceError);
				Log.i("Derivative", String.valueOf(distanceDerivative));
				// ////////////// DistancePID calculation ends ///////////////

				// Calculating the final output
				output = (int) ((distanceKp * distanceError)
						+ (distanceKi * distanceIntegral) + (distanceKd * distanceDerivative));
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
				oldDistanceError = distanceError;

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
		} else if (mode == NXTValue.PID_MODE_SPEED) {

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
				int[] tempMotorRotationCount = robot1.getMotorRotation();

				// Setting right motor value
				motorRotationCount = tempMotorRotationCount[0];
				Log.i("Motor rotation", String.valueOf(motorRotationCount));
				Log.i("Old motor rotation",
						String.valueOf(oldMotorRotationCount));

				// To measure the speed
				time = System.currentTimeMillis();

				// Converting arbitrary rotation count to cm/s
				double motorSpeed = ((motorRotationCount - oldMotorRotationCount) / 20.46)
						/ ((double) (time - oldTime) / 1000);
				Log.i("Speed", String.valueOf(motorSpeed));

				

				// ////////////// SpeedPID calculation /////////////////
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
				// //////////// SpeedPID calculation ends //////////////

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

	public void setWantDistance(int wantDistance) {
		// NXT's body in front of the sensor is 13 cm
		if (wantDistance <= 13) {
			this.wantDistance = 13;
		} else {
			this.wantDistance = wantDistance;
		}
	}

	public void setWantSpeed(double wantSpeed) {
		// max speed test
		// 07-02 23:42:48.897: I/maxSpeed(4460): 51.28677195797114
		if (wantSpeed < 60) {
			this.wantSpeed = wantSpeed;
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