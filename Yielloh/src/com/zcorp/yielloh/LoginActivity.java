
package com.zcorp.yielloh;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zcorp.yielloh.model.AccountManager;
import com.zcorp.yielloh.model.AppConstants;
import com.zcorp.yielloh.model.ConnectionDetector;
import com.zcorp.yielloh.model.DBHelper;
import com.zcorp.yielloh.model.Utility;

public class LoginActivity extends Activity
{
	private ListView listServices;
	private String[] servicesArray;
	private ScrollView scrollParent;
	private boolean flag = false;
	private TextView lblSignUp, lblSocialLogin, lblForgotPassword;
	private Button btnSignIn, btnCreateUser;

	boolean register = false;
	private CheckBox chkRememberMe;
	private EditText edtUsername, edtPassword;
	private String strUserName, strEmail, strPassword, strGender, strDOB,
			Address, txtGeneralSign, access_token, refresh_token, email,
			ObjectID, txtprofile, txtGeneral, avatar_link, cover_photo_link,
			username, gender, date_of_birth, serverEmailId, id, interest;
	private ProgressDialog mProgressDialog;

	final String client_Id = "f2e2ee094f51a74a7031b1e7f2558d286c647316406e251ac3cc14ad13427509";
	final String client_secrete = "7d2689ade5885f5f59caad220123097d726e9407a3b1d490381a75e5b98ccd74";

	private Boolean isInternetPresent;
	private Utility util;
	private AccountManager objectAccount;
	private Bitmap bitmap;

	private SQLiteDatabase db;
	private DBHelper database;

	private byte[] coverPhoto, profilePhoto, data2;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		// Utility class object

		util = new Utility();

		// Initialise Database
		database = new DBHelper(LoginActivity.this);

		db = database.getWritableDatabase();

		// internet Availability checking
		objectAccount = new AccountManager(LoginActivity.this);

		ConnectionDetector cd = new ConnectionDetector(LoginActivity.this);

		isInternetPresent = cd.isConnectingToInternet();

		edtUsername = (EditText) findViewById(R.id.edtName);
		edtPassword = (EditText) findViewById(R.id.edtPassword);

		lblSignUp = (TextView) findViewById(R.id.txtSignUp);
		lblForgotPassword = (TextView) findViewById(R.id.forgot_password);
		lblSocialLogin = (TextView) findViewById(R.id.txtSocialLogin);
		edtPassword.setTypeface(edtUsername.getTypeface());

		btnSignIn = (Button) findViewById(R.id.btnSignIn);

		lblSocialLogin.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Toast.makeText(LoginActivity.this, "This function lunch soon",
						Toast.LENGTH_LONG).show();

			}
		});

		lblForgotPassword.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Toast.makeText(LoginActivity.this, "This function lunch soon",
						Toast.LENGTH_LONG).show();

			}
		});

		lblSignUp.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(intent);

			}
		});

		btnSignIn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				ConnectionDetector cd = new ConnectionDetector(
						LoginActivity.this);

				isInternetPresent = cd.isConnectingToInternet();
				if (isInternetPresent)
				{
					CheckEditTextUser();
					if (((!edtUsername.getText().toString().equals(""))
							&& (util.checkEmailId(edtUsername.getText()
									.toString().trim())) && (!edtPassword
							.getText().toString().equals(""))))
					{
						new HttpAsyncTask()
								.execute("http://staging.yielloh.com/oauth/token/");

					}

				}
				else
				{

					util.setSingleMsgAlert(LoginActivity.this,
							"Please make sure that you are connected to Internet");
				}
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

	private void CheckEditTextUser()
	{
		String S = "";
		boolean flag = false;
		if ((edtUsername.getText().toString().equals("")))
		{
			S += "Please Enter Your Email ID";
			flag = true;

		}
		else if ((edtUsername.getText().toString().equals("") || !(util
				.checkEmailId(edtUsername.getText().toString().trim()))))
		{
			S += "Please Enter a Email Id";
			flag = true;

		}
		else if ((edtPassword.getText().toString().equals("")))
		{
			S += "Please Enter Password";
			flag = true;

		}

		if (flag)
		{
			Toast.makeText(getApplicationContext(), S, Toast.LENGTH_SHORT)
					.show();
		}
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(LoginActivity.this, null,
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

			if ((txtGeneral != null) && !txtGeneral.matches(""))
			{
				mProgressDialog.dismiss();
				Toast.makeText(getBaseContext(), txtGeneral, Toast.LENGTH_LONG)
						.show();
				txtGeneral = "";
			}
			if (flag)

			{

				objectAccount.loginUser(
						edtUsername.getText().toString().trim(), edtPassword
								.getText().toString().trim(), access_token,
						refresh_token);

				new GetUserProfile().execute("http://staging.yielloh.com/me");

				flag = false;

			}

		}
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

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("grant_type", "password");
			jsonObject.put("password", edtPassword.getText().toString().trim());
			jsonObject.put("email", edtUsername.getText().toString().trim());
			jsonObject.put("client_id", client_Id);
			jsonObject.put("client_secret", client_secrete);

			// 4. convert JSONObject to JSON to String
			user1 = jsonObject.toString();

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

			Log.e("Response", res);

			if (res.toLowerCase().contains("error"))
			{
				JSONObject myObject = new JSONObject(res);

				flag = false;
				if (myObject.has("error"))
				{
					txtGeneral = "Invalid email or Password";
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

	private class GetUserProfile extends AsyncTask<String, String, String>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... urls)
		{

			return PROFILE(urls[0]);
		}

		@Override
		protected void onPostExecute(String result)
		{

			if ((email != null) && !email.matches(""))
			{
				mProgressDialog.dismiss();
				Toast.makeText(LoginActivity.this, "Email Id is already taken",
						Toast.LENGTH_LONG).show();
				email = "";
			}
			if ((txtprofile != null) && !txtprofile.matches(""))
			{
				mProgressDialog.dismiss();
				Toast.makeText(LoginActivity.this,
						"User Name is already taken", Toast.LENGTH_LONG).show();
				txtprofile = "";
			}
			if ((txtGeneral != null) && !txtGeneral.matches(""))
			{
				mProgressDialog.dismiss();

				if (txtGeneral.matches("invalid_token"))
				{
					new HttpAsyncTask()
							.execute("http://staging.yielloh.com/oauth/token/");
				}
				else
				{
					Toast.makeText(LoginActivity.this, txtGeneral,
							Toast.LENGTH_LONG).show();
				}
				txtGeneral = "";
			}
			if (register)
			{

				register = false;
				Log.e("Result", result);

				objectAccount.userProfile(avatar_link, cover_photo_link,
						username, gender, date_of_birth, serverEmailId);

				new LoadCoverImage().execute(objectAccount.getCoverLinkLogin());

			}

		}

		public String PROFILE(String url)
		{

			String result = "";
			try
			{

				// 1. create HttpClient
				HttpClient httpclient = new DefaultHttpClient();

				// 2. make POST request to the given URL
				HttpGet httpGet = new HttpGet(url);

				String user1 = "";

				// 4. convert JSONObject to JSON to String

				// 5. set json to StringEntity

				// 7. Set some headers to inform server about the type of the
				// content
				httpGet.setHeader("Accept", "application/vnd.yielloh.v1");
				httpGet.setHeader("Accept", "application/json");
				httpGet.setHeader("Content-type", "application/json");
				httpGet.setHeader("Authorization",
						"Bearer " + objectAccount.getAccessToken());

				// 8. Execute POST request to the given URL
				HttpResponse httpResponse = httpclient.execute(httpGet);

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
				else if (res.toLowerCase().contains("error"))
				{
					JSONObject myObject = new JSONObject(res);

					txtGeneral = myObject.getString("error");
				}
				else
				{
					register = true;

					JSONObject myObject = new JSONObject(res);

					serverEmailId = myObject.getString("email");

					if (myObject.has("profile"))
					{
						JSONObject object = myObject.getJSONObject("profile");

						avatar_link = object.getString("avatar");
						cover_photo_link = object.getString("cover_photo");
						username = object.getString("username");
						gender = object.getString("gender");
						date_of_birth = object.getString("date_of_birth");
					}

					JSONArray array = myObject.getJSONArray("interest_list");
					if (array != null)
					{
						db.delete(AppConstants.TABLE_NAME_INTEREST, null, null);
						for (int i = 0; i < array.length(); i++)
						{

							interest = array.getString(i);

							ContentValues values = new ContentValues();

							values.put("interest", interest);

							db.insert(AppConstants.TABLE_NAME_INTEREST, null,
									values);

						}
					}

				}
			}
			catch (Exception e)
			{

				Log.e("InputStream", e.getLocalizedMessage());
			}

			return result;
		}

	}

	private class LoadImage extends AsyncTask<String, String, Bitmap>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

		}

		protected Bitmap doInBackground(String... args)
		{
			try
			{
				/*
				 * bitmap = BitmapFactory.decodeStream((InputStream) new URL(
				 * args[0]).getContent());
				 */

				BitmapFactory.Options o = new BitmapFactory.Options();
				o.inJustDecodeBounds = true;
				BitmapFactory.decodeStream(
						(InputStream) new URL(args[0]).getContent(), null, o);

				// Find the correct scale value. It should be the power of 2.
				final int REQUIRED_SIZE = 1024;
				int width_tmp = o.outWidth, height_tmp = o.outHeight;
				int scale = 1;
				while (true)
				{
					if (width_tmp / 2 < REQUIRED_SIZE
							|| height_tmp / 2 < REQUIRED_SIZE)
						break;
					width_tmp /= 2;
					height_tmp /= 2;
					scale *= 2;
				}

				// decode with inSampleSize
				BitmapFactory.Options o2 = new BitmapFactory.Options();
				o2.inSampleSize = scale;
				bitmap = BitmapFactory.decodeStream((InputStream) new URL(
						args[0]).getContent(), null, o2);

				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				profilePhoto = baos.toByteArray();

			}
			catch (Exception e)
			{
				Log.e("Image", "" + e);
			}
			return bitmap;
		}

		protected void onPostExecute(Bitmap image)
		{
			mProgressDialog.dismiss();

			db.delete(AppConstants.TABLE_NAME_PHOTO, null, null);
			ContentValues values = new ContentValues();
			values.put("profile", profilePhoto);

			values.put("cover_photo", coverPhoto);

			db.insert(AppConstants.TABLE_NAME_PHOTO, null, values);

			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TASK);

			Log.e("ds", objectAccount.getRefreshToken());
			startActivity(intent);
			finish();

		}
	}

	private class LoadCoverImage extends AsyncTask<String, String, Bitmap>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

		}

		protected Bitmap doInBackground(String... args)
		{
			try
			{
				BitmapFactory.Options o = new BitmapFactory.Options();
				o.inJustDecodeBounds = true;
				BitmapFactory.decodeStream(
						(InputStream) new URL(args[0]).getContent(), null, o);

				// Find the correct scale value. It should be the power of 2.
				final int REQUIRED_SIZE = 2048;
				int width_tmp = o.outWidth, height_tmp = o.outHeight;
				int scale = 1;
				while (true)
				{
					if (width_tmp / 2 < REQUIRED_SIZE
							|| height_tmp / 2 < REQUIRED_SIZE)
						break;
					width_tmp /= 2;
					height_tmp /= 2;
					scale *= 2;
				}

				// decode with inSampleSize
				BitmapFactory.Options o2 = new BitmapFactory.Options();
				o2.inSampleSize = scale;
				bitmap = BitmapFactory.decodeStream((InputStream) new URL(
						args[0]).getContent(), null, o2);

				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				coverPhoto = baos.toByteArray();

			}
			catch (Exception e)
			{
				Log.e("Image", "" + e);
			}
			return bitmap;
		}

		protected void onPostExecute(Bitmap image)
		{

			new LoadImage().execute(objectAccount.getAvatarLinkLogin());
		}
	}

}
