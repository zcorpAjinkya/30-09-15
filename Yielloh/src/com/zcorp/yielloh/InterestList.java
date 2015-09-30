
package com.zcorp.yielloh;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Intent;
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
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zcorp.yielloh.adapter.InterestListAdapter;
import com.zcorp.yielloh.model.AppConstants;
import com.zcorp.yielloh.model.DBHelper;
import com.zcorp.yielloh.model.Utility;

public class InterestList extends ActionBarActivity
{
	private ImageView search, add_interest;
	private EditText edtSearchList;
	private android.support.v7.app.ActionBar action;
	private Menu menu;
	private DBHelper database;
	private SQLiteDatabase db;
	private ListView list;
	private ArrayList<String> navDrawerItems = new ArrayList<String>();

	private InterestListAdapter adapter;

	private byte[] blob;
	private Utility util;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interest_list_layout);

		search = (ImageView) findViewById(R.id.search);
		list = (ListView) findViewById(R.id.listInterest);
		add_interest = (ImageView) findViewById(R.id.add_interest);
		edtSearchList = (EditText) findViewById(R.id.edt_search_interest);

		database = new DBHelper(InterestList.this);

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

		adapter = new InterestListAdapter(InterestList.this, navDrawerItems);

		list.setAdapter(adapter);

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
				.setText("My Interests");

		search.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (edtSearchList.isShown())
				{

				}
				else
				{
					edtSearchList.setVisibility(View.VISIBLE);
				}
			}
		});

		add_interest.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(InterestList.this,
						AddInterests.class);

				startActivity(intent);

			}
		});

		edtSearchList.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3)
			{
				String text = edtSearchList.getText().toString()
						.toLowerCase(Locale.getDefault());
				adapter.filter(text);

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0)
			{
				String text = edtSearchList.getText().toString()
						.toLowerCase(Locale.getDefault());
				adapter.filter(text);
			}
		});

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
}
