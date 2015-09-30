
package com.zcorp.yielloh.model;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

public class DatePickerExample extends DialogFragment
{
	private OnDateSetListener onDateSetListener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);

		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog dialog = new DatePickerDialog(getActivity(),
				onDateSetListener, year, month, day);

		dialog.getDatePicker().setSpinnersShown(true);

		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#f0960c")));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		return dialog;
	}

	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		if (activity instanceof OnDateSetListener)
		{
			onDateSetListener = (OnDateSetListener) activity;
		}
	}

}
