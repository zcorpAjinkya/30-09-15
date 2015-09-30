
package com.zcorp.yielloh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.zcorp.yielloh.model.AccountManager;

public class SplashScreenActivity extends Activity
{

	private AccountManager objectAccountManagerSplash;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LOW_PROFILE);

		objectAccountManagerSplash = new AccountManager(
				SplashScreenActivity.this);

		setContentView(R.layout.activity_splash_screen);
		MyCounter mc = new MyCounter(3000, 1000); // set timer for splash screen
		mc.start();

	}

	private class MyCounter extends CountDownTimer
	{

		public MyCounter(long millisInFuture, long countDownInterval)
		{
			super(millisInFuture, countDownInterval);

		}

		@Override
		public void onFinish()
		{

			if (objectAccountManagerSplash.getLoginStatus())
			{
				Intent intent = new Intent(SplashScreenActivity.this,
						MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
				
			}
			else
			{
				Intent intent = new Intent(SplashScreenActivity.this,
						LoginPreviewScreen.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}

		}

		@Override
		public void onTick(long millisUntilFinished)
		{

		}
	}

}
