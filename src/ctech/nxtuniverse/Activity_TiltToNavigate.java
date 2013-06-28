package ctech.nxtuniverse;

import java.text.DecimalFormat;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Activity_TiltToNavigate extends Activity implements
		SensorEventListener {

	private ImageView activeImg;
	private TextView rightMotorPowerDisplay, leftMotorPowerDisplay;
	private TextView startNote;
	private Button setMax, setZero; // XXX Min-Max

	private double xAcc, yAcc, zAcc;
	private double xAngle, yAngle, zAngle;
	private double xAngleOffsetValue, yAngleOffsetValue, zAngleOffsetValue;
	private double xAngleCalibrated, yAngleCalibrated, zAngleCalibrated;
	private double powerPerDeg;
	private int leftPower;
	private int rightPower;
	private double userPrefXAngleForMax = 90;
	private double userPrefXAngleForZero = 45;// XXX Min-Max

	private boolean active = false;
	private boolean activityStarted = false;
	private boolean robotStopped = false;

	private Robot robot1 = Activity_Main.robot1;
	private byte[] motorData = robot1.getMotorData();

	private SensorManager sensorManager;
	private Sensor accelerometerSensor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tilt_navigate);

		activeImg = (ImageView) findViewById(R.id.tilt2navigate_imageView_xImage);

		startNote = (TextView) findViewById(R.id.tilt2navigate_textView_note);
		rightMotorPowerDisplay = (TextView) findViewById(R.id.tilt2navigate_textView_rightMotorPower);
		leftMotorPowerDisplay = (TextView) findViewById(R.id.tilt2navigate_textView_leftMotorPower);

		setMax = (Button) findViewById(R.id.tilt2navigate_button_setMax);
		setZero = (Button) findViewById(R.id.tilt2navigate_button_setZero);// XXX
																			// Min-Max

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		if (sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null) {
			accelerometerSensor = sensorManager
					.getDefaultSensor(Sensor.TYPE_GRAVITY);
			sensorManager.registerListener(this, accelerometerSensor,
					SensorManager.SENSOR_DELAY_NORMAL);
		} else {
			Log.i("Log", "No accelerometer sensor found"); // XXX handle this
															// error
			finish();
		}

		activeImg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!active) {

					active = true;
					activeImg.setImageResource(R.drawable.connection_connected);

					if (!activityStarted) {
						startNote.setVisibility(View.GONE);
						setMax.setVisibility(View.VISIBLE);
						setZero.setVisibility(View.VISIBLE); // XXX Min-Max
						activityStarted = true;
					}

					rightMotorPowerDisplay.setTextColor(Color.GREEN);
					leftMotorPowerDisplay.setTextColor(Color.GREEN);

				} else {
					active = false;

					rightMotorPowerDisplay.setTextColor(Color.RED);
					leftMotorPowerDisplay.setTextColor(Color.RED);

					activeImg
							.setImageResource(R.drawable.connection_disconnected);
				}
			}
		});

		setMax.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				userPrefXAngleForMax = xAngleCalibrated;
			}
		});

		setZero.setOnClickListener(new View.OnClickListener() {// XXX Min-Max

			@Override
			public void onClick(View v) {
				userPrefXAngleForZero = xAngleCalibrated;
			}
		});

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	// android:theme="@android:style/Theme.NoTitleBar"

	@Override
	public void onSensorChanged(SensorEvent event) {

		xAcc = event.values[0];
		yAcc = event.values[1];
		zAcc = event.values[2];

		xAngle = (Math.toDegrees(Math.acos(xAcc / 9.81)));
		yAngle = (Math.toDegrees(Math.acos(yAcc / 9.81)));
		zAngle = (Math.toDegrees(Math.acos(zAcc / 9.81)));

		xAngleCalibrated = xAngle - xAngleOffsetValue;
		yAngleCalibrated = yAngle - yAngleOffsetValue;
		zAngleCalibrated = zAngle - zAngleOffsetValue;

		if (zAngleCalibrated > 90) {
			xAngleCalibrated = -xAngleCalibrated;
		}

		powerPerDeg = (100 / (userPrefXAngleForMax - userPrefXAngleForZero));
		// XXX if using max-min
		// powerPerDeg = (200 / (userPrefXAngleForMax - userPrefXAngleForMin));
		//
		// else if using max-zero
		// powerPerDeg = (100 / (userPrefXAngleForMax - userPrefXAngleForZero));

		// Straight
		rightPower = leftPower = (int) ((xAngleCalibrated - 45) * powerPerDeg);
		Log.i("Power per deg", "" + powerPerDeg);

		// +/- the tilt on y axis
		if (yAngleCalibrated > 90) {
			leftPower += (int) ((90 - yAngleCalibrated) / 20 * 100);
		} else {
			rightPower += (int) -((90 - yAngleCalibrated) / 20 * 100);
		}
		// 20 deg to reach power of 0 therefore 40 deg to reach power of
		// -100

		// Limiting final power
		if (leftPower > 100) {
			leftPower = 100;
		} else if (leftPower < -100) {
			leftPower = -100;
		}
		if (rightPower > 100) {
			rightPower = 100;
		} else if (rightPower < -100) {
			rightPower = -100;
		}

		if (active) {
			motorData[5] = (byte) rightPower;
			motorData[19] = (byte) leftPower;
			robot1.setMotorData(motorData);
			robot1.write(motorData);
			robotStopped = false;

			// Log.i("Log", "" + rightPower);
			// Log.i("Log", "" + leftPower);

			// y 90
			// right 60
			// left 120
		} else {

			if (!robotStopped) {
				motorData[5] = 0;
				motorData[19] = 0;
				robot1.setMotorData(motorData);
				robot1.write(motorData);
				robotStopped = true;
			}
		}

		DecimalFormat df = new DecimalFormat("#.##");
		Log.i("X Angle", df.format(xAngleCalibrated) + "");
		Log.i("Y Angle", df.format(yAngleCalibrated) + "");
		Log.i("Z Angle", df.format(zAngleCalibrated) + "");

		if (!activityStarted) {
			if (rightPower > 50 || rightPower < 0) {
				rightMotorPowerDisplay.setTextColor(Color.RED);
			} else {
				rightMotorPowerDisplay.setTextColor(Color.GREEN);
			}

			if (leftPower > 50 || leftPower < 0) {
				leftMotorPowerDisplay.setTextColor(Color.RED);
			} else {
				leftMotorPowerDisplay.setTextColor(Color.GREEN);
			}
		}

		rightMotorPowerDisplay.setText(df.format(rightPower) + "");
		leftMotorPowerDisplay.setText(df.format(leftPower) + "");
	}

	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
		finish();
	}

}
