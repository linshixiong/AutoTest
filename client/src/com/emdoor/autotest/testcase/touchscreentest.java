package com.emdoor.autotest.testcase;

import java.io.FileOutputStream;
import java.io.PrintStream;

import com.emdoor.autotest.Configuration;
import com.emdoor.autotest.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.res.Resources;
import android.provider.Settings;

public class touchscreentest extends Activity {

	private LinearLayout fl;
	private ExplosionView exv1;
	private AnimationDrawable exa1;
	Button btn01, btn02, btn03;
	TextView tv01;
	private Resources res;
	int points = 0;
	int temp = 0;
	Configuration config;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.touchscreen);

		fl = (LinearLayout)findViewById(R.id.LinearLayout01);
		btn01 = (Button)findViewById(R.id.btntouchscreenok);
		btn02 = (Button)findViewById(R.id.btntouchfail);
		btn03 = (Button)findViewById(R.id.btntouchback);
		tv01 = (TextView)findViewById(R.id.pointcount);
		
		config = new Configuration(this);
		
		tv01.setText(String.format(getString(R.string.points), points));
		btn01.setVisibility(btn01.INVISIBLE);

		fl.setBackgroundResource(R.drawable.bg);

		exv1 = new ExplosionView(this);
		exv1.setVisibility(View.INVISIBLE);

		exv1.setBackgroundResource(R.anim.explosion);
		exa1 = (AnimationDrawable) exv1.getBackground();
		
		fl.addView(exv1);
		

		//fl.setOnTouchListener(new LayoutListener());
		setContentView(fl);	
		
		btn02.setOnClickListener(listener);
		btn03.setOnClickListener(listener);		
	}

	class ExplosionView extends ImageView {
		public ExplosionView(Context context) {
			super(context);
		}

		// handle the location of the explosion
		public void setLocation(int top, int left) {
			this.setFrame(left, top, left + 40, top + 40);
		}
	}

	private final Button.OnClickListener listener = new Button.OnClickListener() {

		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch(arg0.getId()){
			case R.id.btntouchscreenok:
				config.setTOUCHTestOk(1);
				break;
			case R.id.btntouchfail:
				config.setTOUCHTestOk(2);
				break;
			default :
				config.setTOUCHTestOk(0);
				break;
				
			}
			touchscreentest.this.finish();
		}
	};		
	
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		if(event.getPointerCount() > points){				
			points = event.getPointerCount();
			tv01.setText(String.format(getString(R.string.points), points));
			if(points == 5){
				btn01.setVisibility(btn01.VISIBLE);
				config.setTOUCHTestOk(1);
				touchscreentest.this.finish();	
			}
		}
		return super.onTouchEvent(event);
	}

	class LayoutListener implements OnTouchListener {

		public boolean onTouch(View v, MotionEvent event) {				
			

			exv1.setVisibility(View.INVISIBLE);
			exa1.stop();
			float x = event.getX();
			float y = event.getY();
			exv1.setLocation((int) y - 20, (int) x - 20);
			exv1.setVisibility(View.VISIBLE);
			exa1.start();
			
			return false;

		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Settings.System.putInt(getContentResolver(),"pointer_location", 0);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		super.onPause();
		Settings.System.putInt(getContentResolver(),"pointer_location", 1);
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		Settings.System.putInt(getContentResolver(),"pointer_location", 0);
		
	}	
	
}
