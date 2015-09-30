
package com.zcorp.yielloh.model;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.zcorp.yielloh.R;

public class Utility extends Activity
{
	public void setSingleMsgAlert(Context mCtx, String msg)
	{

		AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
		LayoutInflater inflater = (LayoutInflater) mCtx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.single_msg_dialog, null);
		TextView txtAMsg = (TextView) layout.findViewById(R.id.txtHead);
		Button btnOk = (Button) layout.findViewById(R.id.dialog_ok);
		txtAMsg.setText(msg);
		builder.setView(layout);
		final AlertDialog alertDialog = builder.create();

		if (!((Activity) mCtx).isFinishing())
		{

			alertDialog.show();
		}
		alertDialog.show();
		btnOk.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				alertDialog.dismiss();

			}
		});
	}

	public void showSettingsAlert(Context ctx)
	{
		final Context ctx2 = ctx;
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
		alertDialog.setTitle("SETTINGS");
		alertDialog
				.setMessage("Enable Location Provider! Go to settings menu?");
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						ctx2.startActivity(intent);
					}
				});
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.cancel();
					}
				});
		alertDialog.show();
	}

	public boolean checkEmailId(String emailId)
	{
		boolean flag = false;
		if (emailId != null
				&& emailId
						.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
			flag = true;
		else
			flag = false;
		return flag;
	}

	public static byte[] getBytes(Bitmap bitmap)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 0, stream);
		return stream.toByteArray();
	}

	// convert from byte array to bitmap
	public Bitmap getPhoto(byte[] image)
	{
		/*
		 * ByteArrayInputStream imageStream = new ByteArrayInputStream(image);
		 * Bitmap theImage = BitmapFactory.decodeStream(imageStream);
		 */
		return BitmapFactory.decodeByteArray(image, 0, image.length);
	}

	public void showDatePickerDialog(Context context, EditText edit)
	{
		final EditText edit1 = edit;
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.set_time);
		dialog.show();

		final DatePicker datePicker = (DatePicker) dialog
				.findViewById(R.id.date_picker);
		datePicker.setMaxDate(System.currentTimeMillis());
		Button ok = (Button) dialog.findViewById(R.id.ok);
		Button cancel = (Button) dialog.findViewById(R.id.cancle);

		ok.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				int month = datePicker.getMonth() + 1;
				int day = datePicker.getDayOfMonth();
				int year = datePicker.getYear();

				String s = day + "/" + month + "/" + year;
				edit1.setText(s);
				dialog.dismiss();

			}

		});
		cancel.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				dialog.dismiss();

			}
		});
	}

	public void singleMessageAlertDialogWithFinishActivity(Context context)
	{
		final Context context1 = context;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.single_msg_dialog, null);
		TextView txtAMsg = (TextView) layout.findViewById(R.id.txtHead);
		Button btnOk = (Button) layout.findViewById(R.id.dialog_ok);
		txtAMsg.setText("User Successfully Registered !");
		builder.setView(layout);
		final AlertDialog alertDialog = builder.create();

		if (!((Activity) context).isFinishing())
		{

			alertDialog.show();
		}
		alertDialog.show();
		btnOk.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				alertDialog.dismiss();
				((Activity) context1).finish();
			}
		});

	}

	
}
