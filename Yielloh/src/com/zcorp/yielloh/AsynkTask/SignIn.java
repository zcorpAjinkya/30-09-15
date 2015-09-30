
package com.zcorp.yielloh.AsynkTask;

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
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zcorp.yielloh.LoginActivity;
import com.zcorp.yielloh.MainActivity;
import com.zcorp.yielloh.R;
import com.zcorp.yielloh.model.AccountManager;
import com.zcorp.yielloh.model.AppConstants;
import com.zcorp.yielloh.model.DBHelper;
import com.zcorp.yielloh.model.Utility;

public class SignIn extends AsyncTask<String, Void, String>
{
	private Context mContext;
	private ListView listServices;
	private String[] servicesArray;
	private ScrollView scrollParent;
	private boolean flag = false;
	private TextView lblSignUp, lblSocialLogin;
	private Button btnSignIn, btnCreateUser;

	boolean register = false;
	private CheckBox chkRememberMe;
	private EditText edtUsername, edtPassword;
	private String strUserName, strEmail, strPassword, strGender, strDOB,
			Address, txtGeneralSign, access_token, refresh_token, email,
			ObjectID, txtprofile, txtGeneral, avatar_link, cover_photo_link,
			username, gender, date_of_birth, serverEmailId;
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

	public SignIn(Context paramContext)
	{
		this.mContext = paramContext;

	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		mProgressDialog = ProgressDialog.show(mContext, null, null);
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
			Toast.makeText(mContext, txtGeneral, Toast.LENGTH_LONG).show();
			txtGeneral = "";
		}
		if (flag)

		{

			objectAccount.loginUser(edtUsername.getText().toString().trim(),
					edtPassword.getText().toString().trim(), access_token,
					refresh_token);

			new GetUserProfile().execute("http://staging.yielloh.com/me");

			flag = false;

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

			return Profile(urls[0]);
		}

		@Override
		protected void onPostExecute(String result)
		{

			if ((email != null) && !email.matches(""))
			{
				mProgressDialog.dismiss();
				Toast.makeText(mContext, "Email Id is already taken",
						Toast.LENGTH_LONG).show();
				email = "";
			}
			if ((txtprofile != null) && !txtprofile.matches(""))
			{
				mProgressDialog.dismiss();
				Toast.makeText(mContext, "User Name is already taken",
						Toast.LENGTH_LONG).show();
				txtprofile = "";
			}
			if ((txtGeneral != null) && !txtGeneral.matches(""))
			{
				mProgressDialog.dismiss();

				if (txtGeneral.matches("invalid_token"))
				{
					new SignIn(mContext)
							.execute("http://staging.yielloh.com/oauth/token/");
				}
				else
				{
					Toast.makeText(mContext, txtGeneral, Toast.LENGTH_LONG)
							.show();
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

		public String Profile(String url)
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

			Intent intent = new Intent(mContext, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TASK);

			Log.e("ds", objectAccount.getRefreshToken());
			mContext.startActivity(intent);
			((Activity) mContext).finish();

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
