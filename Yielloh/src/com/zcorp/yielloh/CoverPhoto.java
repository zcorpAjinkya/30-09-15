
package com.zcorp.yielloh;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.zcorp.yielloh.model.AccountManager;
import com.zcorp.yielloh.model.AppConstants;
import com.zcorp.yielloh.model.DBHelper;

public class CoverPhoto extends Fragment
{
	private SQLiteDatabase db;
	private DBHelper database;

	private byte[] coverPhoto, profilePhoto1, data2;
	WebView mWebView;
	private ImageView addImage, profilePhoto;
	private File file;
	private byte[] imageBytes, data1;
	private Button btnNext, btnSkip;
	private Bitmap bitmap;
	View rootView;
	private int serverResponseCode = 0;
	private ProgressDialog mProgressDialog;

	private AccountManager objectAccount;
	boolean register = false;
	boolean flag = false;
	private Uri fileUri;
	private String strUserName, strEmail, strPassword, filename, image,
			txtProfile, strGender, strDOB, Address, txtGeneralSign,
			access_token, refresh_token, email, ObjectID, txtprofile,
			txtGeneral, avatar_link, cover_photo_link, username, gender,
			date_of_birth, serverEmailId;

	public CoverPhoto()
	{
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{

		rootView = inflater.inflate(R.layout.fragment_cover_photo, container,
				false);

		database = new DBHelper(getActivity());

		db = database.getWritableDatabase();

		addImage = (ImageView) rootView.findViewById(R.id.imagePlus);

		profilePhoto = (ImageView) rootView.findViewById(R.id.profilePhoto);

		objectAccount = new AccountManager(getActivity());

		btnSkip = (Button) rootView.findViewById(R.id.skip);
		btnNext = (Button) rootView.findViewById(R.id.next);

		btnNext.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				flag = true;

				new GetUserProfile().execute("http://staging.yielloh.com/me");

			}
		});

		addImage.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				intent.setType("image/*");
				intent.putExtra("crop", "true");
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("outputX", 96);
				intent.putExtra("outputY", 96);

				intent.putExtra("return-data", true);
				intent.putExtra("scale", true);
				intent.putExtra("outputFormat",
						Bitmap.CompressFormat.JPEG.toString());
				startActivityForResult(
						Intent.createChooser(intent, "Select File"), 1);

			}
		});

		return rootView;
	}

	@Override
	public void setMenuVisibility(boolean menuVisible)
	{
		// TODO Auto-generated method stub
		super.setMenuVisibility(menuVisible);

		if (menuVisible)
		{

			new GetUserProfile().execute("http://staging.yielloh.com/me");

		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == getActivity().RESULT_OK
				&& data != null && data.getData() != null)
		{

			Uri uri = data.getData();

			try
			{
				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
						Locale.getDefault()).format(new Date());

				filename = "coverPhoto_" + timeStamp + ".jpg";

				if (data != null)
				{
					Bundle extras = data.getExtras();
					bitmap = extras.getParcelable("data");

				}
				// Log.d(TAG, String.valueOf(bitmap));

				File mediaStorageDir = new File(
						Environment.getExternalStorageDirectory(),
						"Yielloh/Cover Photo");

				if (!mediaStorageDir.exists())
				{
					if (!mediaStorageDir.mkdirs())
					{
						Log.e("Error", "File Directory");
					}

				}

				fileUri = Uri.fromFile(new File(mediaStorageDir.getPath()
						+ File.separator + "IMG_" + timeStamp + ".jpg"));

				OutputStream stream = new FileOutputStream(fileUri.getPath());
				bitmap.compress(CompressFormat.JPEG, 100, stream);

				flag = true;

				new uploadCoverPhoto()
						.execute("http://staging.yielloh.com/update_profile");

			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private class uploadCoverPhoto extends AsyncTask<String, Void, String>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(getActivity(), null, null);
			// Set progressBar to preogress dialog
			mProgressDialog.setContentView(R.layout.progress_bar_layout);
			mProgressDialog.setCancelable(false);
			// Show progressdialog
			mProgressDialog.show();

		}

		@Override
		protected String doInBackground(String... urls)
		{
			Log.e("File Path", fileUri.getPath());

			return uploadFile(urls[0], fileUri.getPath());
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result)
		{

			if (register)
			{
				register = false;

				new GetUserProfile().execute("http://staging.yielloh.com/me");

			}
		}
	}

	public String uploadFile(String url, String filepath)
	{

		String result = "";
		try
		{
			image = convertFileToString(filepath);
			Log.e("file", image);

			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// 2. make POST request to the given URL
			HttpPut httpPut = new HttpPut(url);

			String user1 = "";

			// 3. build jsonObject

			JSONObject profile = new JSONObject();

			JSONObject cover_photo = new JSONObject();
			JSONObject objectJSON = new JSONObject();

			objectJSON.put("data", image);
			objectJSON.put("filename", filename);
			objectJSON.put("content_type", "image/jpg");

			cover_photo.put("cover_photo", objectJSON);
			profile.put("profile", cover_photo);

			// 4. convert JSONObject to JSON to String
			user1 = profile.toString();

			// 5. set json to StringEntity
			StringEntity se = new StringEntity(user1);

			// 6. set httpPost Entity
			httpPut.setEntity(se);

			Log.e("Token", objectAccount.getAccessToken());
			// 7. Set some headers to inform server about the type of the
			// content
			httpPut.setHeader("Accept", "application/vnd.yielloh.v1");
			httpPut.setHeader("Accept", "application/json");
			httpPut.setHeader("Authorization",
					"Bearer " + objectAccount.getAccessToken());
			httpPut.setHeader("Content-type", "application/json");

			// 8. Execute POST request to the given URL
			HttpResponse httpResponse = httpclient.execute(httpPut);

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
					txtProfile = object.getString("profile.username");
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

				objectAccount.setCoverPhotoLink(myObject
						.getString("cover_photo"));
			}
		}
		catch (Exception e)
		{
			Log.e("InputStream", e.getLocalizedMessage());
		}

		return result;
	}

	public String convertFileToString(String pathOnSdCard)
	{
		String strFile = null;
		try
		{

			File file = new File(pathOnSdCard);

			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(file), null, o);

			// The new size we want to scale to
			final int REQUIRED_SIZE = 256;

			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE
					&& o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			Bitmap bit = BitmapFactory.decodeStream(new FileInputStream(file),
					null, o2);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			bit.compress(Bitmap.CompressFormat.JPEG, 90, baos);
			data1 = baos.toByteArray();

			strFile = Base64.encodeToString(data1, Base64.NO_WRAP);

			Log.e("File 64", strFile);

		}
		catch (IOException e)
		{
			Log.e("File 64", "" + e);
			e.printStackTrace();
		}
		return strFile;
	}

	private class GetUserProfile extends AsyncTask<String, String, String>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			if (flag)
			{

				mProgressDialog = ProgressDialog
						.show(getActivity(), null, null);
				if (!mProgressDialog.isShowing())
				{

					// Set progressBar to preogress dialog
					mProgressDialog
							.setContentView(R.layout.progress_bar_layout);
					mProgressDialog.setCancelable(false);
					// Show progressdialog
					mProgressDialog.show();
				}
			}

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
				Toast.makeText(getActivity(), "Email Id is already taken",
						Toast.LENGTH_LONG).show();
				email = "";
			}
			if ((txtprofile != null) && !txtprofile.matches(""))
			{
				mProgressDialog.dismiss();
				Toast.makeText(getActivity(), "User Name is already taken",
						Toast.LENGTH_LONG).show();
				txtprofile = "";
			}
			if ((txtGeneral != null) && !txtGeneral.matches(""))
			{
				mProgressDialog.dismiss();

				if (txtGeneral.matches("invalid_token"))
				{

				}
				else
				{
					Toast.makeText(getActivity(), txtGeneral, Toast.LENGTH_LONG)
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

				new LoadImage().execute(objectAccount.getAvatarLink());

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
				profilePhoto1 = baos.toByteArray();

			}
			catch (Exception e)
			{
				Log.e("Image", "" + e);
			}
			return bitmap;
		}

		protected void onPostExecute(Bitmap image)
		{

			if (flag)
			{
				new LoadCoverImage().execute(objectAccount.getCoverLinkLogin());
			}
			else
			{
				if (image == null)
				{
					profilePhoto.setImageDrawable(getResources().getDrawable(
							R.drawable.app_icon));
					Toast.makeText(getActivity(),
							"You Haven't set profile Picture",
							Toast.LENGTH_LONG).show();
				}
				else
				{

					profilePhoto.setImageBitmap(image);
				}
			}
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

			profilePhoto.setImageBitmap(image);

			db.delete(AppConstants.TABLE_NAME_PHOTO, null, null);

			ContentValues values = new ContentValues();
			values.put("profile", profilePhoto1);

			values.put("cover_photo", coverPhoto);

			db.insert(AppConstants.TABLE_NAME_PHOTO, null, values);

			if (flag)
			{

				mProgressDialog.dismiss();
				startMainActivty();
			}
		}
	}

	public void startMainActivty()
	{
		Intent intent = new Intent(getActivity(), MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);

		startActivity(intent);
		getActivity().finish();
	}
}
