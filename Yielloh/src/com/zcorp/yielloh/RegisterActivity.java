
package com.zcorp.yielloh;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

import com.zcorp.yielloh.model.AccountManager;
import com.zcorp.yielloh.model.AppConstants;
import com.zcorp.yielloh.model.ConnectionDetector;
import com.zcorp.yielloh.model.DBHelper;
import com.zcorp.yielloh.model.Utility;

public class RegisterActivity extends ActionBarActivity
{
	Button btnContinue;

	EditText edtName, edtEmail, edtPassword, edtConfirmPassword,
			edtDateOfBirth;

	boolean flag = false;
	boolean register = false;
	ScrollView scroll;

	Utility util;
	RadioButton maleRadioButton, femaleRadioButton;
	ProgressDialog mProgressDialog;
	private Boolean isInternetPresent;
	private AccountManager objectAccount;
	private String strUserName, strEmail, strPassword, strGender, strDOB,
			Address, txtGeneralSign, access_token, refresh_token, email,
			ObjectID, txtprofile, txtGeneral, id, interest;

	final String client_Id = "f2e2ee094f51a74a7031b1e7f2558d286c647316406e251ac3cc14ad13427509";
	final String client_secrete = "7d2689ade5885f5f59caad220123097d726e9407a3b1d490381a75e5b98ccd74";
	private SQLiteDatabase db;
	private DBHelper database;

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

		// internet Availability checking
		database = new DBHelper(RegisterActivity.this);

		db = database.getWritableDatabase();

		objectAccount = new AccountManager(RegisterActivity.this);

		edtConfirmPassword
				.setOnEditorActionListener(new OnEditorActionListener()
				{
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event)
					{
						if ((actionId == EditorInfo.IME_ACTION_NEXT))
						{
							util.showDatePickerDialog(RegisterActivity.this,
									edtDateOfBirth);
						}
						return false;
					}
				});

		edtDateOfBirth.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				try
				{
					util.showDatePickerDialog(RegisterActivity.this,
							edtDateOfBirth);
				}
				catch (Exception e)
				{

					e.printStackTrace();
				}

			}
		});

		btnContinue.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				ConnectionDetector cd = new ConnectionDetector(
						RegisterActivity.this);

				isInternetPresent = cd.isConnectingToInternet();

				strEmail = edtEmail.getText().toString().trim();
				strPassword = edtPassword.getText().toString().trim();
				strDOB = edtDateOfBirth.getText().toString().trim();
				strUserName = edtName.getText().toString().trim();

				if (maleRadioButton.isChecked())
					strGender = "Male";
				else
					strGender = "Female";

				if (isInternetPresent)
				{
					CheckEditTextUser();
					if (((!strUserName.equals("")))
							&& (!strPassword.equals(""))
							&& (!strEmail.equals("") && (util
									.checkEmailId(strEmail)))
							&& (!strDOB.equals(""))

							&& (!strGender.equals(""))
							&& (strPassword.toLowerCase()
									.matches(edtConfirmPassword.getText()
											.toString().trim().toLowerCase())))
					{
						new HttpAsyncTask()
								.execute("http://staging.yielloh.com/users/");
					}

				}
				else
				{

					util.setSingleMsgAlert(RegisterActivity.this,
							"Please make sure that you are connected to Internet");
				}
			}
		});

	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(RegisterActivity.this, null,
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

		@Override
		protected void onPostExecute(String result)
		{

			if ((email != null) && !email.matches(""))
			{
				mProgressDialog.dismiss();
				Toast.makeText(getBaseContext(), "Email Id is already taken",
						Toast.LENGTH_LONG).show();
				email = "";
			}
			if ((txtprofile != null) && !txtprofile.matches(""))
			{
				mProgressDialog.dismiss();
				Toast.makeText(getBaseContext(), "User Name is already taken",
						Toast.LENGTH_LONG).show();
				txtprofile = "";
			}
			if ((txtGeneral != null) && !txtGeneral.matches(""))
			{
				mProgressDialog.dismiss();
				Toast.makeText(getBaseContext(), txtGeneral, Toast.LENGTH_LONG)
						.show();
				txtGeneral = "";
			}
			if (register)
			{

				register = false;
				Log.e("Result", result);

				new SignIn().execute("http://staging.yielloh.com/oauth/token/");
			}

		}
	}

	public String POST(String url)
	{

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

			profile.put("gender", strGender);
			profile.put("username", strUserName);
			profile.put("date_of_birth", strDOB);

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("email", strEmail);
			jsonObject.put("password", strPassword);
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

			Log.e("Register", res);

			if (res.toLowerCase().contains("errors"))
			{
				JSONObject myObject = new JSONObject(res);

				JSONObject object = myObject.getJSONObject("errors");

				register = false;
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
			else if (res.toLowerCase().contains("errors"))
			{
				JSONObject myObject = new JSONObject(res);

				txtGeneral = myObject.getString("error");
			}
			else
			{
				register = true;
				JSONObject myObject = new JSONObject(res);
				JSONArray array = myObject.getJSONArray("interest_list");
				if (array != null)
				{
					db.delete(AppConstants.TABLE_NAME_INTEREST, null, null);
					for (int i = 0; i < array.length(); i++)
					{
						JSONObject object = (JSONObject) array.get(i);

						id = object.getString("id");
						interest = object.getString("name");

						ContentValues values = new ContentValues();
						values.put("interest_id", id);

						values.put("interest", interest);

						db.insert(AppConstants.TABLE_NAME_INTEREST, null,
								values);

					}
				}
			}
		}
		catch (Exception e)
		{
			mProgressDialog.dismiss();
			Log.e("InputStream", e.getLocalizedMessage());
		}

		return result;
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

		else if ((strGender.equals("")))
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

	private class SignIn extends AsyncTask<String, Void, String>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... urls)
		{

			return Sign(urls[0]);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result)
		{
			mProgressDialog.dismiss();

			if ((txtGeneralSign != null) && !txtGeneralSign.matches(""))
			{
				Toast.makeText(getBaseContext(), txtGeneralSign,
						Toast.LENGTH_LONG).show();
				txtGeneralSign = "";
			}
			if (flag)

			{

				objectAccount.loginUser(edtEmail.getText().toString().trim(),
						edtPassword.getText().toString().trim(), access_token,
						refresh_token);

				flag = false;

				Intent intent = new Intent(RegisterActivity.this,
						UpdateProfile.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
				finish();

			}

		}
	}

	public String Sign(String url)
	{

		String result = "";
		try
		{

			// 1. create HttpClient
			HttpClient httpclientsignIn = new DefaultHttpClient();

			// 2. make POST request to the given URL
			HttpPost httpPostSignin = new HttpPost(url);

			String user1 = "";

			// 3. build jsonObject

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("grant_type", "password");
			jsonObject.put("password", strPassword);
			jsonObject.put("email", strEmail);
			jsonObject.put("client_id", client_Id);
			jsonObject.put("client_secret", client_secrete);

			// 4. convert JSONObject to JSON to String
			user1 = jsonObject.toString();

			// 5. set json to StringEntity
			StringEntity se = new StringEntity(user1);

			// 6. set httpPost Entity
			httpPostSignin.setEntity(se);

			// 7. Set some headers to inform server about the type of the
			// content
			httpPostSignin.setHeader("Accept", "application/vnd.yielloh.v1");
			httpPostSignin.setHeader("Accept", "application/json");
			httpPostSignin.setHeader("Content-type", "application/json");

			// 8. Execute POST request to the given URL
			HttpResponse httpResponse = httpclientsignIn
					.execute(httpPostSignin);

			// 9. receive response as inputStream
			String res = EntityUtils.toString(httpResponse.getEntity());

			Log.e("Response", res);

			if (res.toLowerCase().contains("error"))
			{
				JSONObject myObject = new JSONObject(res);

				flag = false;
				if (myObject.has("error"))
				{
					txtGeneralSign = "Invalid email or Password";
				}

			}
			else
			{
				flag = true;
				JSONObject myObject = new JSONObject(res);
				if (myObject.has("access_token"))
				{
					access_token = myObject.getString("access_token");
					refresh_token = myObject.getString("refresh_token");

					Log.e("Access Token", access_token);
					Log.e("Refresh Token", refresh_token);

				}
			}

		}
		catch (Exception e)
		{
			Log.e("InputStream", "" + e);
		}

		// 11. return result
		return result;
	}

}