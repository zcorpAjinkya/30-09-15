
package com.zcorp.yielloh;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zcorp.yielloh.adapter.InterestListAdapter;
import com.zcorp.yielloh.model.AccountManager;
import com.zcorp.yielloh.model.AppConstants;
import com.zcorp.yielloh.model.DBHelper;
import com.zcorp.yielloh.model.Utility;

public class AddPeopleFollowers extends ActionBarActivity
{
	private ImageView search, add_interest;
	private EditText edtSearchList;
	private android.support.v7.app.ActionBar action;
	private Menu menu;
	private DBHelper database;
	private SQLiteDatabase db;
	private ListView list;
	private ArrayList<String> navDrawerItems = new ArrayList<String>();
	private String strUserName, strEmail, strPassword, filename, image,
			txtProfile, strGender, strDOB, Address, txtGeneralSign,
			access_token, refresh_token, email, ObjectID, txtprofile,
			txtGeneral, avatar_link, cover_photo_link, username, gender,
			date_of_birth, serverEmailId, id, interest;
	private InterestListAdapter adapter;

	private AccountManager objectAccount;
	boolean register = false;
	boolean flag = false;
	private Uri fileUri;
	private ProgressDialog mProgressDialog;

	private byte[] blob;
	private Utility util;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_follower_user_layout);

		// list = (ListView) findViewById(R.id.listInterest);
		objectAccount = new AccountManager(this);
		database = new DBHelper(AddPeopleFollowers.this);

		db = database.getReadableDatabase();
		action = getSupportActionBar();

		action.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		action.setCustomView(R.layout.custom_action_bar_layout);
		action.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#ff7200")));

		action.setHomeButtonEnabled(true);
		action.setDisplayHomeAsUpEnabled(true);

		Cursor cur = db.rawQuery("SELECT * FROM "
				+ AppConstants.TABLE_NAME_INTEREST, null);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		if (cur != null)
		{
			if (cur.moveToFirst())
			{
				do
				{
					String interestName = cur.getString(cur
							.getColumnIndex("interest"));

					navDrawerItems.add(interestName);
				} while (cur.moveToNext());
			}
		}

		new AddInterest().execute("http://staging.yielloh.com/profiles");
		/*
		 * adapter = new InterestListAdapter(AddPeopleFollowers.this,
		 * navDrawerItems);
		 * 
		 * list.setAdapter(adapter);
		 */

		util = new Utility();

		hideSoftKeyboard();

		Cursor c = db.rawQuery(
				"SELECT * FROM " + AppConstants.TABLE_NAME_PHOTO, null);
		if (c.moveToFirst())
		{
			blob = c.getBlob(c.getColumnIndex("profile"));

		}

		action.setHomeAsUpIndicator(R.drawable.back_icon);
		((TextView) action.getCustomView().findViewById(R.id.userName))
				.setText("Add User");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		this.menu = menu;

		getMenuInflater().inflate(R.menu.blank_menu, menu);

		MenuItem rec = this.menu.findItem(R.id.blank_menu);

		Bitmap src = util.getPhoto(blob);
		Bitmap resizedBitmap = Bitmap.createScaledBitmap(src, 200, 200, false);
		Bitmap output = Bitmap.createBitmap(resizedBitmap.getWidth(),
				resizedBitmap.getHeight(), Config.ARGB_8888);

		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, resizedBitmap.getWidth(),
				resizedBitmap.getHeight());
		final RectF rectF = new RectF(rect);
		// final float roundPx = 200;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawCircle(100, 100, 90, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(util.getPhoto(blob), rect, rect, paint);
		util.getPhoto(blob).recycle();
		rec.setIcon(resizeImage(output, 300, 300));

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			FragmentManager fm = this.getSupportFragmentManager();
			if (fm.getBackStackEntryCount() > 0)
			{
				Log.i("MainActivity", "popping backstack");
				fm.popBackStack();
				return true;
			}
			else
			{
				Log.i("MainActivity", "nothing on backstack, calling super");
				super.onBackPressed();
				return true;
			}

		case R.id.blank_menu:

		}
		return super.onOptionsItemSelected(item);
	}

	private Drawable resizeImage(Bitmap resId, int w, int h)
	{
		// load the origial Bitmap

		int width = resId.getWidth();
		int height = resId.getHeight();
		int newWidth = w;
		int newHeight = h;
		// calculate the scale
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// create a matrix for the manipulation
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(resId, 0, 0, width, height,
				matrix, true);
		return new BitmapDrawable(resizedBitmap);
	}

	public void hideSoftKeyboard()
	{
		if (getCurrentFocus() != null)
		{
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
					.getWindowToken(), 0);
		}
	}

	private class AddInterest extends AsyncTask<String, String, String>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			mProgressDialog = ProgressDialog.show(AddPeopleFollowers.this,
					null, null);
			if (!mProgressDialog.isShowing())
			{

				// Set progressBar to preogress dialog
				mProgressDialog.setContentView(R.layout.progress_bar_layout);
				mProgressDialog.setCancelable(false);
				// Show progressdialog
				mProgressDialog.show();
			}

		}

		@Override
		protected String doInBackground(String... urls)
		{

			return INTEREST(urls[0]);
		}

		@Override
		protected void onPostExecute(String result)
		{

			if ((email != null) && !email.matches(""))
			{
				mProgressDialog.dismiss();
				Toast.makeText(AddPeopleFollowers.this,
						"Email Id is already taken", Toast.LENGTH_LONG).show();
				email = "";
			}
			if ((txtprofile != null) && !txtprofile.matches(""))
			{
				mProgressDialog.dismiss();
				Toast.makeText(AddPeopleFollowers.this,
						"User Name is already taken", Toast.LENGTH_LONG).show();
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
					Toast.makeText(AddPeopleFollowers.this, txtGeneral,
							Toast.LENGTH_LONG).show();
				}
				txtGeneral = "";
			}
			if (register)
			{
				mProgressDialog.dismiss();
				register = false;
				Log.e("Result", result);
				// finish();

			}

		}
	}

	public String INTEREST(String url)
	{

		String result = "";
		try
		{

			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// 2. make POST request to the given URL
			HttpGet httpPost = new HttpGet(url);

			String user1 = "";

			// 3. build jsonObject

			// 4. convert JSONObject to JSON to String

			// 5. set json to StringEntity

			// 6. set httpPost Entity

			// 7. Set some headers to inform server about the type of the
			// content
			httpPost.setHeader("Accept", "application/vnd.yielloh.v1");
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Authorization",
					"Bearer " + objectAccount.getAccessToken());
			httpPost.setHeader("Content-type", "application/json");

			Log.e("", objectAccount.getAccessToken());
			// 8. Execute POST request to the given URL
			HttpResponse httpResponse = httpclient.execute(httpPost);

			// 9. receive response as inputStream
			String res = EntityUtils.toString(httpResponse.getEntity());

			Log.e("Responce", res);
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

				/*
				 * JSONObject myObject = new JSONObject(res);
				 * 
				 * JSONArray array = myObject.getJSONArray("interest_list"); if
				 * (array != null) { db.delete(AppConstants.TABLE_NAME_INTEREST,
				 * null, null); for (int i = 0; i < array.length(); i++) {
				 * 
				 * interest = array.getString(i);
				 * 
				 * ContentValues values = new ContentValues();
				 * 
				 * values.put("interest", interest);
				 * 
				 * db.insert(AppConstants.TABLE_NAME_INTEREST, null, values);
				 * 
				 * } }
				 */
			}
		}
		catch (Exception e)
		{

			Log.e("InputStream", "" + e);
		}

		return result;
	}

}
