package ctech.nxtuniverse;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;

public class Activity_Navigate extends Activity {

	private EditText pidValueInput;
	private Button forward, backward, left, right, speedPid, distancePid,
			killPid;
	private SeekBar power;
	private ProgressBar pidRunning;
	private Robot robot1 = Activity_Main.robot1;

	private PID pid;

	// private byte[] robot1MotorData = robot1.getMotorData();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigate);

		pidValueInput = (EditText) findViewById(R.id.navigate_editText_pidValueInput);

		pidRunning = (ProgressBar) findViewById(R.id.navigate_progressBar_pidRunning);
		pidRunning.setVisibility(View.GONE);

		forward = (Button) findViewById(R.id.navigate_button_forward);
		backward = (Button) findViewById(R.id.navigate_button_backward);
		left = (Button) findViewById(R.id.navigate_button_left);
		right = (Button) findViewById(R.id.navigate_button_right);
		speedPid = (Button) findViewById(R.id.navigate_button_speedPid);
		distancePid = (Button) findViewById(R.id.navigate_button_distancePid);
		killPid = (Button) findViewById(R.id.navigate_button_kill);

		power = (SeekBar) findViewById(R.id.navigate_seekBar_power);
		power.setProgress(50);

		forward.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();

				if (action == MotionEvent.ACTION_DOWN) { // button is touched
					robot1.move(NXTValue.GO_FORWARD, power.getProgress());
					Log.i("power", power.getProgress() + "");
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
					robot1.move(NXTValue.GO_BACKWARD, power.getProgress());
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
					robot1.move(NXTValue.TURN_RIGHT, power.getProgress());
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
					robot1.move(NXTValue.TURN_LEFT, power.getProgress());
				} else if (action == MotionEvent.ACTION_UP) { // is not touched
					robot1.move(NXTValue.STOP, 0);
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
						pid = new PID(robot1, NXTValue.PID_MODE_DISTANCE, pidValue);
						pid.start();
					} else {
						pid.kill();
						pid = new PID(robot1, NXTValue.PID_MODE_DISTANCE, pidValue);
						pid.start();
					}
					pidRunning.setVisibility(View.VISIBLE);
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
					pidRunning.setVisibility(View.VISIBLE);
				} else {
					Log.i("pid value input", "nothing was entered");// XXX
				}
			}
		});

		killPid.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pid != null) {
					pid.kill();
				}
				pidRunning.setVisibility(View.GONE);
			}
		});

	}

}
