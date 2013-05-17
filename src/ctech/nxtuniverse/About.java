package ctech.nxtuniverse;

import android.app.Activity;
import android.os.Bundle;
//import android.widget.TextView;

public class About extends Activity {

	//TextView title, timePeriod, school, teamText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
}
