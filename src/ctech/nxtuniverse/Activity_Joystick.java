package ctech.nxtuniverse;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Activity_Joystick extends Activity {

	// loading joystick image
	ImageView joystick;

	// loading three textviews
	TextView tvRightPower;
	TextView tvLeftPower;
	TextView tvWheelPower;
	
	//loading seekbar
	SeekBar sbPower;
	// declaring radius
	double radius;
	
	//declaring and setting power
	int wheelPower = 100;
	
	// declaring coordinates of the joystick
	int joystick_x;
	int joystick_y;

	// getting robot data
	private Robot robot1 = Activity_Main.robot[0];
	private byte[] motorData = robot1.getMotorData();

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joystick);

		// linking to xml
		joystick = (ImageView) findViewById(R.id.ivJoystick);
		// linking to xml
		tvRightPower = (TextView) findViewById(R.id.tvRightPower);
		tvLeftPower = (TextView) findViewById(R.id.tvLeftPower);
		tvWheelPower = (TextView) findViewById(R.id.tvRadiusError);
		
		//linking to xml
		sbPower = (SeekBar) findViewById(R.id.sbPower);
		sbPower.setMax(50);
		sbPower.setProgress(50);
		
		//updating the power textview
		updateNetPowerTv(wheelPower);
		
		//force joystick to measure itself
		joystick.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

		// calculating radius
		radius = joystick.getMeasuredWidth() / 2;

		// getting joystick position
		joystick_x = (int) (joystick.getRight() - joystick.getMeasuredWidth() / 2);
		joystick_y = (int) (joystick.getBottom() - joystick.getMeasuredHeight() / 2);
	
		// on touch
		joystick.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				// calculating position of touch
				double xTouched = event.getX();
				double yTouched = event.getY();

				// if the joystick is touched
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					
					// checking if touch is outside of joystick
					if (getDistanceFromCenter((int) xTouched, (int) yTouched) < radius) {
						// moverobot
						moveRobot(xTouched, yTouched);
					}

					// logging the touch point
					// Log.i("NXT",
					// String.valueOf(xTouched) + " x "
					// + String.valueOf(yTouched + " y "));
					//
					// // logging the position of the knob
					// Log.i("NXT", "Knob at" + knob.getX() + " x " +
					// knob.getY()
					// + " y");

					// if the joystick is held
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					xTouched =  event.getX();
					yTouched =  event.getY();
					
					// checking if touch is outside of joystick
					if (getDistanceFromCenter( xTouched, yTouched) <= radius) {
						updateNetPowerTv(wheelPower);
						// moving robot
						moveRobot(xTouched, yTouched);
						
					} else {

						tvWheelPower.setTextColor(Color.RED);
						tvWheelPower.setText("Please touch inside the joystick area");
						stopRobot();
						xTouched = joystick_x;
						yTouched = joystick_y;
						return false;
					}

					// logging the touch point
					// Log.i("NXT",
					// String.valueOf(xTouched) + " x "
					// + String.valueOf(yTouched + " y"));
					//
					// // logging the position of the knob
					// Log.i("NXT", "Knob at" + knob.getX() + " x " +
					// knob.getY()
					// + " y");

					// if the touch is completed
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					
					updateNetPowerTv(wheelPower);
					
					
					//stopping the robot
					stopRobot();
					xTouched = joystick_x;
					yTouched = joystick_y;
					// logging the position of the knob
					// Log.i("NXT", "Knob at" + knob.getX() + " x " +
					// knob.getY()
					// + " y");

				}

				return true;
			}

		});
		
		sbPower.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar arg0, int progress, boolean fromUser) {
				wheelPower = progress + 50;
				tvWheelPower.setText("Power: " + wheelPower);
			}
		});
	}

	@SuppressLint("NewApi")
	private void moveRobot(double xTouched, double yTouched) {

		// Log.i("NXT", "xTouched: " + xTouched + " yTouched: " + yTouched +
		// " radius " + radius);

		// calculating x and y difference of touch and center of joystick
		double xDifference = getXDifference(xTouched);
		double yDifference = getYDifference(yTouched);
		
		// Log.i("NXT", "xDifference: " + xDifference + " yDifference: " +
		// yDifference);

		// getting to to a ratio of 1
		xDifference /= radius;
		yDifference /= radius;
		
		if (yDifference>-0.5 && yDifference<0.5){
			yDifference = 0;
		}
		
		xDifference *= wheelPower;
		yDifference *= wheelPower;

		// Log.i("NXT", "xDifference: " + xDifference + " yDifference: " +
		// yDifference);

		// declaring power variables
		double rightPower = 0;
		double leftPower = 0;

		// caculating power
		rightPower = Math.max(-wheelPower,
				Math.min(wheelPower, -(yDifference + xDifference )));
		leftPower = Math.max(-100,
				Math.min(wheelPower, -(yDifference  - xDifference )));

		// setting power calculated to listview
		updateWheelPowerTv(rightPower, leftPower);

		// writing to robot
		motorData[5] = (byte) rightPower;
		motorData[19] = (byte) leftPower;
		robot1.setMotorData(motorData);
		robot1.write(motorData);

		// Log.i("NXT", "Right Power: " + rightPower + " Left Power: " +
		// leftPower);
	}

	private void updateWheelPowerTv(double rightPower, double leftPower) {
		if (rightPower < 0) {
			tvRightPower.setTextColor(Color.RED);
		} else {
			tvRightPower.setTextColor(Color.GREEN);
		}
		tvRightPower.setText("RMP: "
				+ String.valueOf(rightPower));

		if (leftPower < 0) {
			tvLeftPower.setTextColor(Color.RED);
		} else {
			tvLeftPower.setTextColor(Color.GREEN);
		}
		tvLeftPower.setText("LMP: "
				+ String.valueOf(leftPower));
		
	}
	
	private void updateNetPowerTv(int power){
		tvWheelPower.setTextColor(Color.WHITE);
		tvWheelPower.setText("Power: " + wheelPower);
	}

	private void stopRobot() {
		// writing zero to both motors
		tvRightPower.setTextColor(Color.WHITE);
		tvRightPower.setText("RMP: 0");
		tvLeftPower.setTextColor(Color.WHITE);
		tvLeftPower.setText("LMP: 0");
		motorData[5] = 0;
		motorData[19] = 0;
		robot1.setMotorData(motorData);
		robot1.write(motorData);
	}

	/**
	 * Calculates the distance from the centre of the joystick to the center of
	 * the knob
	 * 
	 * @param knob_x
	 * @param knob_y
	 * @return the distance from the centre of the joystick to the center of the
	 *         knob
	 */
	protected double getDistanceFromCenter(double xTouched, double yTouched) {
		double xDifference = getXDifference(xTouched);
		double yDifference = getYDifference(yTouched);

		double distance = Math.sqrt(Math.pow(xDifference, 2)
				+ Math.pow(yDifference, 2));

		return distance;
	}

	/**
	 * Calculates the difference of the x coordinates of the joystick and the
	 * knob in pixels
	 * 
	 * @return difference of the x coordinates of the joystick and the knob in
	 *         pixels
	 */
	protected double getXDifference(double xPoint) {
		joystick.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		return (xPoint - joystick.getMeasuredWidth()/2);
	}

	/**
	 * Calculates the difference of the y coordinates of the joystick and the
	 * knob in pixels
	 * 
	 * @return difference of the y coordinates of the joystick and the knob in
	 *         pixels
	 */
	protected  double getYDifference(double yPoint) {
		return (yPoint - radius);
	}

}
