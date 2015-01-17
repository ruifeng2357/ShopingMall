package com.damytech.yilebang;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		NewsService.m_ctxMain = context;

		Intent service = new Intent(context, NewsService.class);
		context.startService(service);
	}
}
