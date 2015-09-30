
package com.zcorp.yielloh;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class LoginPreviewScreen extends Activity
{

	TextView lblMobileNo, lblForgotPassword, lblSkip;
	Button btnSignIn, btnNewAccount;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_preview_login);

		btnSignIn = (Button) findViewById(R.id.sign_in);
		btnNewAccount = (Button) findViewById(R.id.new_account);

		btnSignIn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(LoginPreviewScreen.this,
						LoginActivity.class);
				startActivity(intent);

			}
		});
		btnNewAccount.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(LoginPreviewScreen.this,
						RegisterActivity.class);
				startActivity(intent);

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		return super.onOptionsItemSelected(item);
	}

	private class Login extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params)
		{

			try
			{

			}
			catch (Exception e)
			{
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{

		}
	}

}
