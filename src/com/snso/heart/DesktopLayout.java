package com.snso.heart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class DesktopLayout extends LinearLayout {

	public DesktopLayout(Context context) {
		super(context);
		setOrientation(LinearLayout.VERTICAL);// ˮƽ����
		

		//���ÿ��
		LayoutParams layout = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//		DisplayMetrics dm = new DisplayMetrics(); 
//		dm = context.getApplicationContext().getResources().getDisplayMetrics(); 
//		LayoutParams layout = new LayoutParams(dm.widthPixels,dm.heightPixels);
		this.setLayoutParams(layout);
		
		View view = LayoutInflater.from(context).inflate(  
                R.layout.desklayout, null); 
		this.addView(view);
	}

}
