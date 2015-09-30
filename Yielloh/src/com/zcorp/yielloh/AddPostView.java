
package com.zcorp.yielloh;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.zcorp.yielloh.model.AccountManager;
import com.zcorp.yielloh.model.AppConstants;
import com.zcorp.yielloh.model.DBHelper;
import com.zcorp.yielloh.model.Utility;

public class AddPostView extends ActionBarActivity
{
	ImageView quote, text, photo;
	private android.support.v7.app.ActionBar action;
	private AccountManager objectAccount;
	private DBHelper database;
	private SQLiteDatabase db;
	private Menu menu;
	private Utility util;
	private byte[] blob;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_post);
		objectAccount = new AccountManager(AddPostView.this);
		action = getSupportActionBar();

		action.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		action.setCustomView(R.layout.custom_action_bar_layout);
		action.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#ff7200")));

		action.setHomeButtonEnabled(true);
		action.setDisplayHomeAsUpEnabled(true);

		util = new Utility();
		database = new DBHelper(AddPostView.this);

		db = database.getReadableDatabase();

		Cursor c = db.rawQuery(
				"SELECT * FROM " + AppConstants.TABLE_NAME_PHOTO, null);
		if (c.moveToFirst())
		{
			blob = c.getBlob(c.getColumnIndex("profile"));

		}

		action.setHomeAsUpIndicator(R.drawable.back_icon);
		((TextView) action.getCustomView().findViewById(R.id.userName))
				.setText(objectAccount.getUserName());
		quote = (ImageView) findViewById(R.id.quote);
		text = (ImageView) findViewById(R.id.text);
		photo = (ImageView) findViewById(R.id.photo);

		quote.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(AddPostView.this, MainActivity.class);
				intent.putExtra("Value", 4);
				startActivity(intent);

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

}
