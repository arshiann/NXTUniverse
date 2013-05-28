package ctech.nxtuniverse;

public class SpeedPID extends Thread {

	double kp = 1;
	double ki = 0;
	double kd = 0;

	double speedError;
	double integral = 0;
	double derivative;

	double oldSpeedError = 0;

	int motorRotationCount;

	double outputSubtotal;
	int output;

	int wantSpeed = 2; // Measured in m/s

	boolean active = true;

	double time = 0; // XXX Pseudo time
	double oldTime = 0; // XXX Pseudo old time

	public void run() {
		while (active) {

			// get value form NXT
			int[] tempMotorRotationCount = Control.getMotorRotationCount();

			// setting right motor value
			motorRotationCount = tempMotorRotationCount[0];

			oldTime = time; // XXX Pseudo time
			time++; // XXX Pseudo time

			// Converting rotation to m/s

			double motorSpeed = ((double) motorRotationCount / (double) 2500)
					/ (time - oldTime); // XXX Pseudo time
			// motor speed = { (rotation)*25*100 } / dt
			// 25 rotation = 1 cm
			// 100 cm = 1 m
			// delta t = old time - current time

			// PID Calculation
			speedError = wantSpeed - motorSpeed;

			integral += speedError * time; // XXX Pseudo time

			if (integral > 100) {
				integral = 100;
			} else if (integral < -100) {
				integral = -100;
			}

			derivative = (speedError - oldSpeedError) / (time - oldTime); // XXX
																			// Pseudo
																			// time
			// End of PID Calculation

			// Value im m/s
			outputSubtotal = ((kp * speedError) + (ki * integral) + (kd * derivative));

			// converting back to rotation
			double outputInRotation = outputSubtotal * (time - oldTime)
					* ((double) 2500);
			// XXX Pseudo time

			// Limiting final output power
			// if (Output > 100) {
			// Output = 100;
			// } else if (Output < -100) {
			// Output = -100;
			// }

			// XXX get output power from (m/s) or (rotations)
			output = (int) outputInRotation;

			oldSpeedError = speedError;

			// long timeElapsed = System.currentTimeMillis() - startTime;
			//
			// if (timeElapsed < 250) {
			// try {
			// Thread.sleep(250 - timeElapsed);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// }

		}
	}

	public void kill() {
		active = false;
	}

}