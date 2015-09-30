
package com.zcorp.yielloh;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.zcorp.yielloh.model.Utility;

public class RegisterActivity1 extends ActionBarActivity
{
	Button btnContinue;

	EditText edtName, edtEmail, edtPassword, edtConfirmPassword,
			edtDateOfBirth;

	boolean flag = false;
	ScrollView scroll;
	String gender, userName, userPassword, userConfirmPassword, userBirthDate,
			email, txtprofile, txtGeneral;
	Utility util;
	RadioButton maleRadioButton, femaleRadioButton;
	ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		// Utility class object

		util = new Utility();

		btnContinue = (Button) findViewById(R.id.sign_in);
		edtDateOfBirth = (EditText) findViewById(R.id.edtBirthDate);
		edtPassword = (EditText) findViewById(R.id.edtPassword);
		edtEmail = (EditText) findViewById(R.id.edtEmail);
		edtName = (EditText) findViewById(R.id.edtName);
		edtConfirmPassword = (EditText) findViewById(R.id.edtConfirmPassword);

		maleRadioButton = (RadioButton) findViewById(R.id.maleRadioButton);
		femaleRadioButton = (RadioButton) findViewById(R.id.femaleRadioButton);

		android.support.v7.app.ActionBar action = getSupportActionBar();
		action.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#f0960c")));
		action.setTitle("Registration");

		btnContinue.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (maleRadioButton.isChecked())
					gender = "Male";
				else
					gender = "Female";

				CheckEditTextUser();
				if (((!edtName.getText().toString().equals("")))
						&& (!edtPassword.getText().toString().equals(""))
						&& (!edtEmail.getText().toString().equals("") && (util
								.checkEmailId(edtEmail.getText().toString()
										.trim())))
						&& (!edtDateOfBirth.getText().toString().equals(""))

						&& (!gender.equals(""))
						&& (edtPassword.getText().toString().trim()
								.toLowerCase().matches(edtConfirmPassword
								.getText().toString().trim().toLowerCase())))
				{
					new HttpAsyncTask()
							.execute("http://staging.yielloh.com/users/");
				}
			}
		});

		edtDateOfBirth.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				try
				{
					util.showDatePickerDialog(RegisterActivity1.this,
							edtDateOfBirth);
				}
				catch (Exception e)
				{

					e.printStackTrace();
				}

			}
		});
		edtConfirmPassword
				.setOnEditorActionListener(new OnEditorActionListener()
				{
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event)
					{
						if ((actionId == EditorInfo.IME_ACTION_NEXT))
						{
							util.showDatePickerDialog(RegisterActivity1.this,
									edtDateOfBirth);
						}
						return false;
					}
				});
	}

	public String POST(String url)
	{
		InputStream inputStream = null;
		String result = "";
		try
		{

			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// 2. make POST request to the given URL
			HttpPost httpPost = new HttpPost(url);

			String user1 = "";

			// 3. build jsonObject
			JSONObject user = new JSONObject();

			JSONObject profile = new JSONObject();

			profile.put("gender", gender);
			profile.put("username", edtName.getText().toString().trim());
			profile.put("date_of_birth", edtDateOfBirth.getText().toString()
					.trim());

			String Valueemail = edtEmail.getText().toString().trim();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("email", Valueemail);
			jsonObject.put("password", edtPassword.getText().toString().trim());
			jsonObject.put("password_confirmation", edtConfirmPassword
					.getText().toString().trim());
			jsonObject.put("profile_attributes", profile);

			user.put("user", jsonObject);
			// 4. convert JSONObject to JSON to String
			user1 = user.toString();

			// 5. set json to StringEntity
			StringEntity se = new StringEntity(user1);

			// 6. set httpPost Entity
			httpPost.setEntity(se);

			// 7. Set some headers to inform server about the type of the
			// content
			httpPost.setHeader("Accept", "application/vnd.yielloh.v1");
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			// 8. Execute POST request to the given URL
			HttpResponse httpResponse = httpclient.execute(httpPost);

			// 9. receive response as inputStream
			String res = EntityUtils.toString(httpResponse.getEntity());

			if (res.toLowerCase().contains("errors"))
			{
				JSONObject myObject = new JSONObject(res);

				JSONObject object = myObject.getJSONObject("errors");

				flag = false;
				if (object.has("profile.username") && (object.has("email")))
				{
					txtGeneral = "Email and User Name has already taken";
				}
				else if (object.has("email"))
				{
					email = object.getString("email");
				}
				else if (object.has("profile.username"))
				{
					txtprofile = object.getString("profile.username");
				}

			}
			else
			{
				flag = true;
			}
		}
		catch (Exception e)
		{
			Log.e("InputStream", e.getLocalizedMessage());
		}

		// 11. return result
		return result;
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(RegisterActivity1.this, null,
					null);
			// Set progressBar to preogress dialog
			mProgressDialog.setContentView(R.layout.progress_bar_layout);
			mProgressDialog.setCancelable(false);
			// Show progressdialog
			mProgressDialog.show();

		}

		@Override
		protected String doInBackground(String... urls)
		{

			return POST(urls[0]);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result)
		{
			mProgressDialog.dismiss();
			if ((email != null) && !email.matches(""))
			{
				Toast.makeText(getBaseContext(), "Email Id is already taken",
						Toast.LENGTH_LONG).show();
				email = "";
			}
			if ((txtprofile != null) && !txtprofile.matches(""))
			{
				Toast.makeText(getBaseContext(), "User Name is already taken",
						Toast.LENGTH_LONG).show();
				txtprofile = "";
			}
			if ((txtGeneral != null) && !txtGeneral.matches(""))
			{
				Toast.makeText(getBaseContext(), txtGeneral, Toast.LENGTH_LONG)
						.show();
				txtGeneral = "";
			}
			if (flag)
			{

				flag = false;
				util.singleMessageAlertDialogWithFinishActivity(RegisterActivity1.this);
			}

		}
	}

	

	private void CheckEditTextUser()
	{
		String S = "";
		boolean flag = false;
		if ((edtName.getText().toString().equals("")))
		{
			S += "Please Enter Your Name";
			flag = true;

		}
		else if ((edtPassword.getText().toString().equals("")))
		{
			S += "Please Enter Password";
			flag = true;

		}
		else if (!(edtPassword.getText().toString().trim()
				.matches(edtConfirmPassword.getText().toString().trim())))
		{
			S += "Password doestn't match";
			flag = true;

		}
		else if ((edtEmail.getText().toString().equals("") || !(util
				.checkEmailId(edtEmail.getText().toString().trim()))))
		{
			S += "Please Enter a Email Id";
			flag = true;

		}
		else if ((edtDateOfBirth.getText().toString().equals("")))
		{
			S += "Please Enter Date of Birth";
			flag = true;

		}

		else if ((gender.equals("")))
		{
			S += "Please Select your Gender ";
			flag = true;

		}

		if (flag)
		{
			Toast.makeText(getApplicationContext(), S, Toast.LENGTH_SHORT)
					.show();
		}
	}

}
