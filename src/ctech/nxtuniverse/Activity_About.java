package ctech.nxtuniverse;

import android.app.Activity;
import android.os.Bundle;

public class Activity_About extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
}
