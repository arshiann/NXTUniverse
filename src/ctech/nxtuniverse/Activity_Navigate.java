package ctech.nxtuniverse;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class Activity_Navigate extends Activity {

	private EditText pidValueInput;
	private Button forward, backward, left, right;
	private Button speedPid, distancePid, killPid;
	private SeekBar powerBar;
	private Robot robot1;

	private int power;

	private PID pid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigate);

		// Setting the robot
		robot1 = Activity_Main.robot[0];

		pidValueInput = (EditText) findViewById(R.id.navigate_editText_pidValueInput);

		// Buttons
		forward = (Button) findViewById(R.id.navigate_button_forward);
		backward = (Button) findViewById(R.id.navigate_button_backward);
		left = (Button) findViewById(R.id.navigate_button_left);
		right = (Button) findViewById(R.id.navigate_button_right);
		speedPid = (Button) findViewById(R.id.navigate_button_speedPid);
		distancePid = (Button) findViewById(R.id.navigate_button_distancePid);
		killPid = (Button) findViewById(R.id.navigate_button_kill);

		// Seek bar
		powerBar = (SeekBar) findViewById(R.id.navigate_seekBar_power);
		powerBar.setProgress(75);
		power = powerBar.getProgress();

		powerBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				power = powerBar.getProgress();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});

		forward.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();

				if (action == MotionEvent.ACTION_DOWN) {
					robot1.move(NXTValue.DIRECTION_FORWARD, power);
				} else if (action == MotionEvent.ACTION_UP) {
					robot1.move(NXTValue.DIRECTION_STOP, 0);
				}
				return true;
			}
		});

		backward.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					robot1.move(NXTValue.DIRECTION_BACKWARD, power);
				} else if (action == MotionEvent.ACTION_UP) {
					robot1.move(NXTValue.DIRECTION_STOP, 0);
				}
				return true;
			}
		});

		right.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					robot1.move(NXTValue.DIRECTION_RIGHT, power);
				} else if (action == MotionEvent.ACTION_UP) {
					robot1.move(NXTValue.DIRECTION_STOP, 0);
				}
				return true;
			}
		});

		left.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					robot1.move(NXTValue.DIRECTION_LEFT, power);
				} else if (action == MotionEvent.ACTION_UP) {
					robot1.move(NXTValue.DIRECTION_STOP, 0);
				}
				return true;
			}
		});

		distancePid.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int pidValue;
				if (pidValueInput.length() != 0) {
					pidValue = Integer.parseInt(pidValueInput.getText()
							.toString());

					if (pid == null) {
						pid = new PID(robot1, NXTValue.PID_MODE_DISTANCE,
								pidValue);
						pid.start();
					} else {
						pid.kill();
						pid = new PID(robot1, NXTValue.PID_MODE_DISTANCE,
								pidValue);
						pid.start();
					}
				} else {
					Log.i("pid value input", "nothing was entered");// XXX
				}
			}
		});

		speedPid.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				int pidValue;
				if (pidValueInput.length() != 0) {
					pidValue = Integer.parseInt(pidValueInput.getText()
							.toString());
					if (pid == null) {
						pid = new PID(robot1, NXTValue.PID_MODE_SPEED, pidValue);
						pid.start();
					} else {
						pid.kill();
						pid = new PID(robot1, NXTValue.PID_MODE_SPEED, pidValue);
						pid.start();
					}
				} else {
					Log.i("pid value input", "nothing was entered");// XXX
				}
			}
		});

		killPid.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				killPidThread();
			}
		});

	}

	private void killPidThread() {
		if (pid != null) {
			pid.kill();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Stop the thread
		killPidThread();
		
		// Stop the robot in case if the robot is still moving
		robot1.move(NXTValue.DIRECTION_STOP, 0);
		
		finish();
	}

}
