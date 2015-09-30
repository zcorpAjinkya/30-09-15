
package com.zcorp.yielloh;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zcorp.yielloh.adapter.NavDrawerListAdapter;
import com.zcorp.yielloh.model.AccountManager;
import com.zcorp.yielloh.model.AppConstants;
import com.zcorp.yielloh.model.DBHelper;
import com.zcorp.yielloh.model.ExpandableListAdapter;
import com.zcorp.yielloh.model.NavDrawerItem;
import com.zcorp.yielloh.model.Utility;

public class MainActivity extends ActionBarActivity
{
	private DrawerLayout mDrawerLayout;
	private GridView mDrawerList, mDrawerRight;
	private ActionBarDrawerToggle mDrawerToggle, mDrawerToggle2;

	private AccountManager objectAccount;
	private ProgressDialog mProgressDialog;
	private String strUserName, strEmail, strPassword, strGender, strDOB,
			Address, txtGeneralSign, access_token, refresh_token, email,
			ObjectID, txtprofile, txtGeneral, avatar_link, cover_photo_link,
			username, gender, date_of_birth, serverEmailId;

	boolean flag = false;
	boolean register = false;
	private Bitmap bitmap;
	// nav drawer title
	private CharSequence mDrawerTitle;
	LinearLayout linearLeft, linearRight;

	private ImageView drawerCoverPhoto;
	// used to store app title
	private CharSequence mTitle;

	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
	List<String> listDataChildEdit;

	final String client_Id = "f2e2ee094f51a74a7031b1e7f2558d286c647316406e251ac3cc14ad13427509";
	final String client_secrete = "7d2689ade5885f5f59caad220123097d726e9407a3b1d490381a75e5b98ccd74";
	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private TextView logout;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter, rightAdapter;
	private android.support.v7.app.ActionBar action;
	private int value;
	private Bundle bundle;

	private DBHelper database;
	private SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_layout);

		bundle = savedInstanceState;

		database = new DBHelper(this);

		db = database.getReadableDatabase();

		mTitle = mDrawerTitle = getTitle();
		objectAccount = new AccountManager(MainActivity.this);
		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		logout = (TextView) findViewById(R.id.logout);
		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);
		expListView = (ExpandableListView) findViewById(R.id.lvExp);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		Intent intent = getIntent();

		value = intent.getIntExtra("Value", 0);
		mDrawerList = (GridView) findViewById(R.id.drawer_left);
		/* mDrawerRight = (GridView) findViewById(R.id.drawer_right); */

		linearLeft = (LinearLayout) findViewById(R.id.left_drawer);
		linearRight = (LinearLayout) findViewById(R.id.right_drawer);

		drawerCoverPhoto = (ImageView) findViewById(R.id.coverPhoto);

		Cursor c = db.rawQuery(
				"SELECT * FROM " + AppConstants.TABLE_NAME_PHOTO, null);
		if (c.moveToFirst())
		{

			byte[] blob1 = c.getBlob(c.getColumnIndex("cover_photo"));

			Utility util = new Utility();
			if (blob1 != null)
			{

				drawerCoverPhoto.setImageBitmap(util.getPhoto(blob1));
			}
			else
			{

				drawerCoverPhoto.setImageDrawable(getResources().getDrawable(
						R.drawable.default_cover_photo));
			}
		}

		/* new GetUserProfile().execute("http://staging.yielloh.com/me"); */

		navDrawerItems = new ArrayList<NavDrawerItem>();

		ViewTreeObserver vto = expListView.getViewTreeObserver();

		// adding nav drawer items to array
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1), navMenuTitles[0]));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1), navMenuTitles[1]));
		// Photos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1), navMenuTitles[2]));
		// Communities, Will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
				.getResourceId(3, -1), navMenuTitles[3]));

		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
				.getResourceId(4, -1), navMenuTitles[4]));

		// add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons
				.getResourceId(5, -1), navMenuTitles[5]));

		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons
				.getResourceId(6, -1), navMenuTitles[6]));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons
				.getResourceId(7, -1), navMenuTitles[7]));
		// Home

		logout.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				objectAccount.deleteLoginInfo();

				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);
				LayoutInflater inflater = (LayoutInflater) MainActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View layout = inflater
						.inflate(R.layout.single_msg_dialog, null);
				TextView txtAMsg = (TextView) layout.findViewById(R.id.txtHead);
				Button btnOk = (Button) layout.findViewById(R.id.dialog_ok);
				txtAMsg.setText("You are Successfully Loged Out");
				builder.setView(layout);
				builder.setCancelable(false);
				final AlertDialog alertDialog = builder.create();
				if (!((Activity) MainActivity.this).isFinishing())
				{

					alertDialog.show();
				}

				btnOk.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						alertDialog.dismiss();
						finish();
						Intent intent = new Intent(MainActivity.this,
								LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TASK);
						startActivity(intent);
					}
				});

			}
		});
		action = getSupportActionBar();

		action.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		action.setCustomView(R.layout.custom_action_bar_layout);
		action.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#ff7200")));

		((TextView) action.getCustomView().findViewById(R.id.userName))
				.setText("");
		// Recycle the typed array
		navMenuIcons.recycle();

		Log.e("", "" + getIntent().getIntExtra("Value", 0));

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		/*
		 * mDrawerRight.setOnItemClickListener(new
		 * SlideMenuRightClickListener());
		 */

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);

		/*
		 * rightAdapter = new NavDrawerListAdapter(getApplicationContext(),
		 * navDrawerItems);
		 */

		// Listview Group click listener
		expListView.setOnGroupClickListener(new OnGroupClickListener()
		{

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id)
			{

				return false;
			}

		});

		// Listview Group expanded listener
		expListView.setOnGroupExpandListener(new OnGroupExpandListener()
		{

			@Override
			public void onGroupExpand(int groupPosition)
			{

			}
		});

		// Listview Group collasped listener
		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener()
		{

			@Override
			public void onGroupCollapse(int groupPosition)
			{

			}
		});

		// Listview on child click listener
		expListView.setOnChildClickListener(new OnChildClickListener()
		{

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id)
			{
				// TODO Auto-generated method stub

				return false;
			}
		});

		mDrawerList.setAdapter(adapter);

		/* mDrawerRight.setAdapter(rightAdapter); */

		// enabling action bar app icon and behaving it as toggle button
		action.setDisplayHomeAsUpEnabled(true);
		// getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.drawer_icon, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		)

		{

			public void onDrawerClosed(View view)
			{
				action.setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons

				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView)
			{
				action.setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons

				invalidateOptionsMenu();
			}
		};

		mDrawerToggle2 = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.drawer_icon, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		)

		{

			public void onDrawerClosed(View view)
			{
				action.setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons

				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView)
			{
				action.setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons

				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		mDrawerLayout.setDrawerListener(mDrawerToggle2);

		/* mDrawerLayout.openDrawer(Gravity.RIGHT); */

		if (value == 4)
		{
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container_grid, new QuotesFragment())
					.commit();

			mDrawerLayout.closeDrawer(linearLeft);
			mDrawerLayout.closeDrawer(linearRight);
		}

		if (bundle == null)
		{
			// on first time display view for first nav item
			displayView(0);
		}

		prepareListData();

		listAdapter = new ExpandableListAdapter(MainActivity.this,
				listDataHeader, listDataChild, listDataChildEdit);

		expListView.setAdapter(listAdapter);

		((TextView) action.getCustomView().findViewById(R.id.userName))
				.setText(objectAccount.getUserName());

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		expListView.setIndicatorBounds(expListView.getRight() - 100,
				expListView.getWidth());
	}

	public MainActivity()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * Slide menu item click listener
	 *
	 */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id)
		{
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	private class SlideMenuRightClickListener implements
			ListView.OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id)
		{
			// display view for selected nav drawer item
			displayViewRight(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item))
		{
			return true;
		}

		else

		if (mDrawerToggle.onOptionsItemSelected(item))
		{
			return true;
		}

		if (mDrawerToggle2.onOptionsItemSelected(item))
		{
			return true;
		}

		else

		if (mDrawerToggle2.onOptionsItemSelected(item))
		{
			return true;
		}
		Log.e("", "" + item.getItemId());
		// Handle action bar actions click
		switch (item.getItemId())
		{

		case R.id.action_settings:

		{
			if (mDrawerLayout.isDrawerOpen(Gravity.END))
			{
				mDrawerLayout.closeDrawer(Gravity.END);
				action.setDisplayHomeAsUpEnabled(true);

			}
			else
			{
				mDrawerLayout.openDrawer(Gravity.END);
				action.setDisplayHomeAsUpEnabled(false);
			}
			return true;
		}

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/*
	 * * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(linearLeft);

		boolean drawerOpen1 = mDrawerLayout.isDrawerOpen(linearRight);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);

		if (drawerOpen1)
		{

		}

		// hideMenuItems(menu, drawerOpen);
		// hideMenuItems(menu, drawerOpen1);
		return super.onPrepareOptionsMenu(menu);
	}

	/*
	 * private void hideMenuItems(Menu menu, boolean visible) {
	 * 
	 * for (int i = 0; i < menu.size(); i++) {
	 * 
	 * menu.getItem(i).setVisible(visible);
	 * 
	 * } }
	 */

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 */
	private void displayView(int position)
	{
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position)
		{
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			fragment = new MyProfileFragment();
			break;
		case 2:
			fragment = new BlogFragment();
			break;
		case 3:
			Intent intent = new Intent(MainActivity.this, InterestList.class);
			startActivity(intent);
			fragment = new HomeFragment();
			break;
		case 4:
			fragment = new QuotesFragment();
			break;
		case 5:
			fragment = new MyPhotosFragment();
			break;
		case 6:
			fragment = new MyPhotosFragment();
			break;
		case 7:
			fragment = new MyPhotosFragment();
			break;

		default:
			break;
		}

		if (fragment != null)
		{
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container_grid, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);

			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(linearLeft);
		}
		else
		{
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	private void displayViewRight(int position)
	{
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position)
		{
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			fragment = new MyProfileFragment();
			break;
		case 2:
			fragment = new BlogFragment();
			break;
		case 3:
			fragment = new MyInterestFragment();
			break;
		case 4:
			fragment = new QuotesFragment();
			break;
		case 5:
			fragment = new MyPhotosFragment();
			break;
		case 6:
			fragment = new MyPhotosFragment();
			break;
		case 7:
			fragment = new MyPhotosFragment();
			break;

		default:
			break;
		}

		if (fragment != null)
		{
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container_grid, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);

			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(linearRight);
			action.setDisplayHomeAsUpEnabled(true);
		}
		else
		{
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title)
	{
		mTitle = title;
		action.setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);

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

				Toast.makeText(MainActivity.this, "Email Id is already taken",
						Toast.LENGTH_LONG).show();
				email = "";
			}
			if ((txtprofile != null) && !txtprofile.matches(""))
			{

				Toast.makeText(MainActivity.this, "User Name is already taken",
						Toast.LENGTH_LONG).show();
				txtprofile = "";
			}
			if ((txtGeneral != null) && !txtGeneral.matches(""))
			{

				if (txtGeneral.matches("invalid_token"))
				{
					new HttpAsyncTask()
							.execute("http://staging.yielloh.com/oauth/token/");
				}
				else
				{
					Toast.makeText(MainActivity.this, txtGeneral,
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

	}

	private void prepareListData()
	{
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();
		listDataChildEdit = new ArrayList<String>();
		// Adding child data
		listDataHeader.add("General");

		// Adding child data
		List<String> General = new ArrayList<String>();
		General.add("NAME");
		General.add("EMAIL");
		General.add("PASSWORD");
		General.add("GENDER");
		General.add("BIRTHDAY");

		listDataChildEdit.add(objectAccount.getUserName());
		listDataChildEdit.add(objectAccount.getEmail());
		listDataChildEdit.add(objectAccount.getPassword());
		listDataChildEdit.add(objectAccount.getGender());
		listDataChildEdit.add(objectAccount.getDOB());

		/*
		 * List<String> nowShowing = new ArrayList<String>();
		 * nowShowing.add("The Conjuring"); nowShowing.add("Despicable Me 2");
		 * nowShowing.add("Turbo"); nowShowing.add("Grown Ups 2");
		 * nowShowing.add("Red 2"); nowShowing.add("The Wolverine");
		 * 
		 * List<String> comingSoon = new ArrayList<String>();
		 * comingSoon.add("2 Guns"); comingSoon.add("The Smurfs 2");
		 * comingSoon.add("The Spectacular Now"); comingSoon.add("The Canyons");
		 * comingSoon.add("Europa Report");
		 */

		listDataChild.put(listDataHeader.get(0), General); // Header, Child data
		/*
		 * listDataChild.put(listDataHeader.get(1), nowShowing);
		 * listDataChild.put(listDataHeader.get(2), comingSoon);
		 */
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... urls)
		{

			return Token(urls[0]);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result)
		{

			if ((txtGeneral != null) && !txtGeneral.matches(""))
			{
				Toast.makeText(getBaseContext(), txtGeneral, Toast.LENGTH_LONG)
						.show();
				txtGeneral = "";
			}
			if (flag)

			{
				objectAccount.setAccessToken(access_token);
				objectAccount.setRefreshToken(refresh_token);

				new GetUserProfile().execute("http://staging.yielloh.com/me");
				flag = false;

			}

		}
	}

	public String Token(String url)
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
			jsonObject.put("grant_type", "refresh_token");
			jsonObject.put("refresh_token", objectAccount.getRefreshToken());

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

}
