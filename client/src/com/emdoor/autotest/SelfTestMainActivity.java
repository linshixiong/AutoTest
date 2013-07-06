package com.emdoor.autotest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SelfTestMainActivity extends Activity implements OnClickListener {
	private Button semisTestButton;
	private Button endTestButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.selftest_main);
		Settings.init(this);
		semisTestButton = (Button) findViewById(R.id.button_semis);
		semisTestButton.setOnClickListener(this);
		endTestButton = (Button) findViewById(R.id.button_end);
		endTestButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		boolean semis=false;
		if(v.getId()==R.id.button_semis){
			semis=true;
		}else if(v.getId()==R.id.button_end){
			semis=false;
		}
		Intent intent=new Intent();
		intent.putExtra("semifinished", semis);
		intent.setClass(this, SelfTestNewActivity.class);
		startActivity(intent);
	}

}
