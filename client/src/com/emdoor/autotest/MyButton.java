package com.emdoor.autotest;


import com.emdoor.autotest.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyButton extends LinearLayout {

	TextView textView;
	CheckedTextView checkBox;
	Boolean isCheck;
	

	public MyButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyButton(Context context, AttributeSet attrs) {

		super(context, attrs);

		// TODO Auto-generated constructor stub

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		inflater.inflate(R.layout.imgbtn, this);

		textView = (TextView) findViewById(R.id.myTV);
		checkBox = (CheckedTextView) findViewById(R.id.myCB);
		checkBox.setCheckMarkDrawable(android.R.drawable.ic_input_add);
		
//		checkBox.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				CheckedTextView flag=((CheckedTextView)v);
//				flag.toggle();
//
//				if (flag.isChecked()) {
//					isCheck = true;
//					checkBox.setCheckMarkDrawable(android.R.drawable.ic_input_delete);
//				}else{
//					isCheck = false;
//					checkBox.setCheckMarkDrawable(android.R.drawable.ic_input_add);					
//				}
//			}
//
//			
//		});

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Mybtn);

		String text = a.getString(R.styleable.Mybtn_selftext);
		if (text != null) {
			textView.setText(text);
		}

		a.recycle();

	}
	

	public void setCheckBoxState(Boolean ischecked){
		
		checkBox.setChecked(ischecked);
		
		if(ischecked){
			isCheck = true;
			checkBox.setCheckMarkDrawable(android.R.drawable.ic_input_delete);
		}else{
			isCheck = false;
			checkBox.setCheckMarkDrawable(android.R.drawable.ic_input_add);			
		}
		
	}
	
	public void setOnClickCheckBoxState(View v){
		CheckedTextView flag=((CheckedTextView)v);
		flag.toggle();

		if (flag.isChecked()) {
			isCheck = true;
			checkBox.setCheckMarkDrawable(android.R.drawable.ic_input_delete);
		}else{
			isCheck = false;
			checkBox.setCheckMarkDrawable(android.R.drawable.ic_input_add);					
		}
	}

}
