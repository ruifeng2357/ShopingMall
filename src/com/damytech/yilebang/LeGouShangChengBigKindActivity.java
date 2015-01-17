package com.damytech.yilebang;

import com.damytech.Utils.ResolutionSet;

import android.os.Bundle;
import android.app.Activity;

public class LeGouShangChengBigKindActivity extends Activity {	

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.legoushangchengbigkind);
		
		ResolutionSet._instance.iterateChild(findViewById(R.id.rlLeGouShangChengBigKindLayout));
	}
}
