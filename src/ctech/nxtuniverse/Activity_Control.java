package ctech.nxtuniverse;

import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class Activity_Control extends Activity {

	// Buttons
	private Button test, test2, test3, test4;
	private Button forward, backward, left, right;

	// Bluetooth Communication - In and Out Streams
	private static Robot robot1 = Activity_Main.robot1;

	OutputStream outStream;
	InputStream inStream;

	// PID Controllers
	static PID pid;
	static boolean pidRunning = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);

		try {
			outStream = robot1.getOutStream();
			inStream = robot1.getInStream();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Primary navigation buttons
		forward = (Button) findViewById(R.id.forward);
		backward = (Button) findViewById(R.id.backward);
		left = (Button) findViewById(R.id.left);
		right = (Button) findViewById(R.id.right);

		// Test buttons
		test = (Button) findViewById(R.id.test);
		test2 = (Button) findViewById(R.id.test2);
		test3 = (Button) findViewById(R.id.button_test3);
		test4 = (Button) findViewById(R.id.button_test4);

		forward.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) { // button is touched
					robot1.move(NXTValue.GO_FORWARD, 100);
				} else if (action == MotionEvent.ACTION_UP) { // is not touched
					robot1.move(NXTValue.STOP, 0);
				}
				return true;
			}
		});

		backward.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) { // button is touched
					robot1.move(NXTValue.GO_BACKWARD, 100);
				} else if (action == MotionEvent.ACTION_UP) { // is not touched
					robot1.move(NXTValue.STOP, 0);
				}
				return true;
			}
		});

		right.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) { // button is touched
					robot1.move(NXTValue.TURN_RIGHT, 100);
				} else if (action == MotionEvent.ACTION_UP) { // is not touched
					robot1.move(NXTValue.STOP, 0);
				}
				return true;
			}
		});

		left.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) { // button is touched
					robot1.move(NXTValue.TURN_LEFT, 100);
				} else if (action == MotionEvent.ACTION_UP) { // is not touched
					robot1.move(NXTValue.STOP, 0);
				}
				return true;
			}
		});

		test.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (!pidRunning) {
					pid = new PID(robot1, NXTValue.PID_MODE_DISTANCE, 50);
					// distancePid.setWantPosition(50);
					pid.start();
					test.setText("Running");
				} else {
					pid.kill();
					test.setText("Stopped");
				}
				pidRunning = !pidRunning;

			}
		});

		test2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (!pidRunning) {
					pid = new PID(robot1, NXTValue.PID_MODE_SPEED, 15);
					// distancePid.setWantPosition(50);
					pid.start();
					test2.setText("Running");
				} else {
					pid.kill();
					test2.setText("Stopped");
				}
				pidRunning = !pidRunning;

				// if (speedPid == null) {
				// speedPid = new SpeedPID(3);
				// speedPid.start();
				// test2.setText("Running");
				// sleep(5000);
				// speedPid.setWantSpeed(15);
				// } else {
				// speedPid.kill();
				// test2.setText("Stopped");
				// }

			}
		});

		test3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		test4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
	}

	// Methods

	/**
	 * Simple navigating method. Describe the direction in parameter using
	 * constants from NXTValue class.
	 * 
	 * @param Direction
	 *            - Constants from NXTValue class
	 */

	/**
	 * Converts int array to a string.
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
	protected void onDestroy() {
		super.onDestroy();
		finish();
	}
}