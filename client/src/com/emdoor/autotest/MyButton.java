package com.emdoor.autotest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyButton extends LinearLayout{
	private View view;
	public MyButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		LayoutInflater.from(context).inflate(R.layout.my_button, this, true);
		view= findViewById(R.id.my_button); 
	}


	
	public void setTitle(String title){
		((TextView)findViewById(R.id.text_title)).setText(title);
	}
	
	public void setSummary(String summary){
		((TextView)findViewById(R.id.text_summary)).setText(summary);
	}
}
