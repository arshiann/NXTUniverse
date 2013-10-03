package ctech.nxtuniverse;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class Activity_Control extends Activity {

	// Buttons
	private Button forward, backward, left, right;
	private Button test1, test2;

	private Robot robot;

	// PID Controllers
	static PID pid;
	static boolean pidRunning = false;

	int test2Counter = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);

		// Setting the robot
		robot = Activity_Main.robot[0];

		// Primary navigation buttons
		forward = (Button) findViewById(R.id.button_control_forward);
		backward = (Button) findViewById(R.id.button_control_backward);
		left = (Button) findViewById(R.id.button_control_left);
		right = (Button) findViewById(R.id.button_control_right);

		// Other buttons
		test1 = (Button) findViewById(R.id.button_control_test1);
		test1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				robot.addToPositionPID(Robot.SIDE_BOTH, 20);
			}
		});

		test2 = (Button) findViewById(R.id.button_control_test2);
		test2.setText("test2");
		test2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (test2Counter % 2 == 0) {
					robot.startPositionPID();
				} else {
					robot.stopPositionPID();
				}
				test2Counter++;
			}
		});

		forward.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});

		backward.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});

		left.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});

		right.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
	}

	/**
	 * Converts an int array to a string.
	 * 
	 * @param Int
	 *            array - The int array that will be converted into string.
	 * @return The string representation of the array.
	 */
	public static String intArrayToString(int[] array) {
		String stringRepresentationOfTheArray = "";

		// Concatenating
		for (int i = 0; i < array.length; i++) {
			stringRepresentationOfTheArray += array[i] + ", ";
		}
		return stringRepresentationOfTheArray;
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
}