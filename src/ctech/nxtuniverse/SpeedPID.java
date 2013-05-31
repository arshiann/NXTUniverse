package ctech.nxtuniverse;

public class SpeedPID extends Thread {

	double kp = 100;
	double ki = 0;
	double kd = 0;

	double speedError;
	double integral = 0;
	double derivative;

	double oldSpeedError = 0;

	int motorRotationCount;
	int oldMotorRotationCount = 0;

	double outputSubtotal;
	int output;

	double wantSpeed = 0.5; // Measured in m/s

	boolean active = true;

	long time = 0; // XXX Pseudo time
	long oldTime = 0; // XXX Pseudo old time

	public void run() {
		byte[] motor = Control.motorDataStructure;
		
		byte[] confirmationTone = { 0x06, 0x00, (byte) 0x80, 0x03,
				0x0B, 0x02, (byte) 0xFA, 0x00 };
//		Control.write(confirmationTone);
		
		while (active) {
			
			


			// get value form NXT
			int[] tempMotorRotationCount = Control.getMotorRotationCount();

			
			
			// setting right motor value
			motorRotationCount = tempMotorRotationCount[0];

			oldTime = time; // XXX Pseudo time
			time = System.currentTimeMillis();

			// Converting rotation to m/s

			double motorSpeed = ((double) (motorRotationCount - oldMotorRotationCount) / (double) 2500)
					/ (time - oldTime); // XXX Pseudo time
			// motor speed = { (rotation)*25*100 } / dt
			// 25 rotation = 1 cm
			// 100 cm = 1 m
			// delta t = old time - current time
			
			// PID Calculation
			speedError = wantSpeed - motorSpeed;

			integral += speedError * (time - oldTime) ; // XXX Pseudo time

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
			output = (int) ((kp * speedError) + (ki * integral) + (kd * derivative));

			// Limiting final output power
			if (output > 100) {
				output = 100;
			} else if (output < -100) {
				output = -100;
			}

			motor[5] = (byte) output;
			motor[19] = (byte) output;
			Control.write(motor);

			// XXX get output power from (m/s) or (rotations)
			// output = (int) outputInRotation;

			oldSpeedError = speedError;
			oldTime = time;
			oldMotorRotationCount = motorRotationCount;
			
			try {
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// long timeElapsed = System.currentTimeMillis() - startTime;
			//
			// if (timeElapsed < 250) {
			// try {
			// Thread.sleep(250 - timeElapsed);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// }

			// Control.display5.setText(time + " s");

		}

		motor[5] = (byte) 0;
		motor[19] = (byte) 0;
		Control.write(motor);

	}

	public void kill() {
		active = false;
	}

}