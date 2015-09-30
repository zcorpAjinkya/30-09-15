
package com.zcorp.yielloh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.FileChannel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zcorp.yielloh.model.AccountManager;
import com.zcorp.yielloh.model.AppConstants;
import com.zcorp.yielloh.model.DBHelper;
import com.zcorp.yielloh.model.Utility;

public class HomeFragment extends Fragment
{
	private ImageView profilePhoto, coverPhoto, addPost, imgFollowInterest,
			imgAddPeopleFollowers;
	private TextView userTagLine, interestNumber, followerNumber;
	private View rootView;
	private Bitmap bitmap;
	private AccountManager objectAccount;
	private ProgressDialog mProgressDialog;
	private String strUserName, strEmail, strPassword, strGender, strDOB,
			Address, txtGeneralSign, access_token, refresh_token, email,
			ObjectID, txtprofile, txtGeneral, avatar_link, cover_photo_link,
			username, gender, date_of_birth, serverEmailId, DB_PATH;

	boolean flag = false;
	boolean register = false;

	private DBHelper database;
	private SQLiteDatabase db;
	private int interest;

	public HomeFragment()
	{
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		objectAccount = new AccountManager(getActivity());

		rootView = inflater.inflate(R.layout.fragment_home, container, false);

		database = new DBHelper(getActivity());

		db = database.getReadableDatabase();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
		{
			DB_PATH = getActivity().getFilesDir().getAbsolutePath()
					.replace("files", "databases")
					+ File.separator;
		}
		else
		{
			DB_PATH = getActivity().getFilesDir().getPath()
					+ getActivity().getPackageName() + "/databases/";
		}

		interestNumber = (TextView) rootView.findViewById(R.id.interestNumber);
		profilePhoto = (ImageView) rootView.findViewById(R.id.profilePhoto);
		coverPhoto = (ImageView) rootView.findViewById(R.id.coverPhoto);
		addPost = (ImageView) rootView.findViewById(R.id.addPost);
		imgAddPeopleFollowers = (ImageView) rootView
				.findViewById(R.id.addPeopleFollowers);
		imgFollowInterest = (ImageView) rootView
				.findViewById(R.id.followInterest);

		Cursor cursor = db.query(AppConstants.TABLE_NAME_INTEREST, null, null,
				null, null, null, null);
		interest = cursor.getCount();

		try
		{
			writeToSD();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		interestNumber.setText(String.valueOf(interest));

		Cursor c = db.rawQuery(
				"SELECT * FROM " + AppConstants.TABLE_NAME_PHOTO, null);
		if (c.moveToFirst())
		{
			byte[] blob = c.getBlob(c.getColumnIndex("profile"));
			byte[] blob1 = c.getBlob(c.getColumnIndex("cover_photo"));

			Utility util = new Utility();
			if (blob != null)
			{
				profilePhoto.setImageBitmap(util.getPhoto(blob));

			}
			else
			{
				profilePhoto.setImageDrawable(getResources().getDrawable(
						R.drawable.app_icon));

			}

			if (blob1 != null)
			{
				coverPhoto.setImageBitmap(util.getPhoto(blob1));
			}
			else
			{

				coverPhoto.setImageDrawable(getResources().getDrawable(
						R.drawable.default_cover_photo));
			}
		}

		addPost.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getActivity(), AddPostView.class);

				startActivity(intent);

			}
		});

		imgFollowInterest.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				if (interest == 0)
				{
					Intent intent = new Intent(getActivity(),
							AddInterests.class);

					startActivity(intent);
				}
				else
				{
					Intent intent = new Intent(getActivity(),
							InterestList.class);

					startActivity(intent);

				}
			}
		});

		imgAddPeopleFollowers.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				if (interest == 0)
				{
					Intent intent = new Intent(getActivity(),
							AddPeopleFollowers.class);

					startActivity(intent);
				}
				else
				{
					Intent intent = new Intent(getActivity(),
							InterestList.class);

					startActivity(intent);

				}
			}
		});
		Log.e("", objectAccount.getAccessToken());
		return rootView;
	}

	@Override
	public void setMenuVisibility(boolean menuVisible)
	{
		// TODO Auto-generated method stub
		super.setMenuVisibility(menuVisible);

		if (menuVisible)
		{

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

			}
			catch (Exception e)
			{
				Log.e("Image", "" + e);
			}
			return bitmap;
		}

		protected void onPostExecute(Bitmap image)
		{

			if (image != null)
			{
				profilePhoto.setImageBitmap(image);

			}
			else
			{

				Toast.makeText(getActivity(),
						"Image Does Not exist or Network Error",
						Toast.LENGTH_SHORT).show();

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

			}
			catch (Exception e)
			{
				Log.e("Image", "" + e);
			}
			return bitmap;
		}

		protected void onPostExecute(Bitmap image)
		{

			if (image != null)
			{
				coverPhoto.setImageBitmap(image);

			}
			else
			{

				Toast.makeText(getActivity(),
						"Image Does Not exist or Network Error",
						Toast.LENGTH_SHORT).show();

			}
		}
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
				Toast.makeText(getActivity(), txtGeneral, Toast.LENGTH_LONG)
						.show();
				txtGeneral = "";
			}
			if (register)
			{
				mProgressDialog.dismiss();
				register = false;
				Log.e("Result", result);

				objectAccount.userProfile(avatar_link, cover_photo_link,
						username, gender, date_of_birth, serverEmailId);

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
				else if (res.toLowerCase().contains("errors"))
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
				mProgressDialog.dismiss();
				Log.e("InputStream", e.getLocalizedMessage());
			}

			return result;
		}

	}

	public void writeToSD() throws IOException
	{
		File sd = Environment.getExternalStorageDirectory();

		if (sd.canWrite())
		{
			String currentDBPath = AppConstants.DATABASE_NAME;
			String backupDBPath = "backupname.db";
			File currentDB = new File(DB_PATH, currentDBPath);
			File backupDB = new File(sd, backupDBPath);

			Log.e("DB", "" + currentDB.exists());
			if (currentDB.exists())
			{
				FileChannel src = new FileInputStream(currentDB).getChannel();
				FileChannel dst = new FileOutputStream(backupDB).getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();
			}
		}
	}

	private void onrestart()
	{
		Cursor cursor = db.query(AppConstants.TABLE_NAME_INTEREST, null, null,
				null, null, null, null);
		interest = cursor.getCount();

	}

}
