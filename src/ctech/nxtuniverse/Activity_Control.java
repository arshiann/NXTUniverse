package ctech.nxtuniverse;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class Activity_Control extends Activity {

	// Buttons
	private Button forward, backward, left, right;

	private Robot robot1;

	// PID Controllers
	static PID pid;
	static boolean pidRunning = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);

		// Setting the robot
		robot1 = Activity_Main.robot[0];

		// Primary navigation buttons
		forward = (Button) findViewById(R.id.forward);
		backward = (Button) findViewById(R.id.backward);
		left = (Button) findViewById(R.id.left);
		right = (Button) findViewById(R.id.right);

		forward.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) { // button is touched
					robot1.move(NXTValue.DIRECTION_FORWARD, 100);
				} else if (action == MotionEvent.ACTION_UP) { // is not touched
					robot1.move(NXTValue.DIRECTION_STOP, 0);
				}
				return true;
			}
		});

		backward.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) { // button is touched
					robot1.move(NXTValue.DIRECTION_BACKWARD, 100);
				} else if (action == MotionEvent.ACTION_UP) { // is not touched
					robot1.move(NXTValue.DIRECTION_STOP, 0);
				}
				return true;
			}
		});

		right.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) { // button is touched
					robot1.move(NXTValue.DIRECTION_RIGHT, 100);
				} else if (action == MotionEvent.ACTION_UP) { // is not touched
					robot1.move(NXTValue.DIRECTION_STOP, 0);
				}
				return true;
			}
		});

		left.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) { // button is touched
					robot1.move(NXTValue.DIRECTION_LEFT, 100);
				} else if (action == MotionEvent.ACTION_UP) { // is not touched
					robot1.move(NXTValue.DIRECTION_STOP, 0);
				}
				return true;
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