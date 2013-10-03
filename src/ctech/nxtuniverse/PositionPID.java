package ctech.nxtuniverse;

import android.util.Log;

public class PositionPID extends Thread {
	
	public PositionPID(Robot robot){
		this.robot = robot;
		active = true;
	}
	
	private Robot robot;
	private int leftWantPosition;
	private int rightWantPosition;
	private byte[] motorData;

	private boolean active;
	
	public void run() {
		
		robot.resetMotorRotation();
		
		motorData = robot.getMotorData();
		
		int[] motorPosition;
		int rightPosition;
		int leftPosition;
		int rightError;
		int leftError;
		int mulConst = 1;
		int rightPower;
		int leftPower;

		int limit = 100;
		int minLimit = 5;

//		motorData[7] = 0x00;
//		motorData[21] = 0x00;

		do {

			motorPosition = robot.getMotorRotation();
			
			rightPosition = motorPosition[0];
			Log.i("rightPos", rightPosition + "");
			leftPosition = motorPosition[1];
			Log.i("leftPos", leftPosition + "");

			rightError = rightWantPosition - rightPosition;
			Log.i("rightErr", rightError + "");
			leftError = leftWantPosition - leftPosition;
			Log.i("leftErr", leftError + "");
			
			rightPower = mulConst * rightError;
			Log.i("rightPower", rightPower + "");

			leftPower = mulConst * leftError;
			Log.i("leftPower", leftPower + "");

			if (rightPower > limit) {
				rightPower = limit;
			} else if (rightPower < -limit) {
				rightPower = -limit;
			}

			if (leftPower > limit) {
				leftPower = limit;
			} else if (leftPower < -limit) {
				leftPower = -limit;
			}

			motorData[5] = (byte) rightPower;
			motorData[19] = (byte) leftPower;
			robot.write(motorData);

		} while (active);

		robot.move(NXTValue.DIRECTION_STOP, 0);
	}

	public void moveToNextBlock(){
		rightWantPosition += robot.cmToRotation(25);
		leftWantPosition += robot.cmToRotation(25);
	}
	
	public void moveToPreviousBlock(){
		rightWantPosition += robot.cmToRotation(-25);
		leftWantPosition += robot.cmToRotation(-25);
	}
	
	public void turnRight(){
		rightWantPosition += robot.cmToRotation(-10);
		leftWantPosition += robot.cmToRotation(10);
	}
	
	public void turnLeft(){
		rightWantPosition += robot.cmToRotation(10);
		leftWantPosition += robot.cmToRotation(-10);
	}
	
	
	
	public void kill(){
		active = false;
	}

	
}
