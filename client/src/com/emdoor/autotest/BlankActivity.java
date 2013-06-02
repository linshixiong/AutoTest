package com.emdoor.autotest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

public class BlankActivity extends Activity {
	private LinearLayout layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blank);
		layout=(LinearLayout)findViewById(R.id.full_screen_frame);
			
	}
	@Override
	protected void onResume() {
		Intent intent=getIntent();
		int color=intent.getIntExtra("background_color", Color.WHITE);
		layout.setBackgroundColor(color);
		super.onResume();
	}
	
	
	
}
