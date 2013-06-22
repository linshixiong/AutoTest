package com.emdoor.autotest.testcase;

import com.emdoor.autotest.Configuration;
import com.emdoor.autotest.DrawView;
import com.emdoor.autotest.R;
import com.emdoor.autotest.TouchTestResultListener;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class TouchTestNew extends Activity implements TouchTestResultListener {
	private static final String TAG = TouchTestNew.class.getName();
	DrawView view;
	private TextView startLeft;
	private TextView startRight;
	private TextView endLeft;
	private TextView endRight;
	private Configuration config;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.touchtest_new);// 将view视图放到Activity中显示
		view = (DrawView) findViewById(R.id.draw_view);
		view.setTouchTestResultListener(this);
		startLeft = (TextView) findViewById(R.id.start_left);
		startRight = (TextView) findViewById(R.id.start_right);

		endLeft = (TextView) findViewById(R.id.end_left);
		endRight = (TextView) findViewById(R.id.end_right);
		config = new Configuration(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	public void onTouchTestResultOK(boolean passed, int step) {

		Log.d(TAG, "onTouchTestResultOK passed=" + passed + ",step=" + step);
		if (passed && step == 0) {
			startLeft.setVisibility(View.GONE);
			startRight.setVisibility(View.VISIBLE);
			endLeft.setVisibility(View.VISIBLE);
			endRight.setVisibility(View.GONE);

		} else {
			config.setTOUCHTestOk(passed ? 1 : 2);
			this.finish();
		}

	}

}
