
package com.zcorp.yielloh.adapter;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zcorp.yielloh.R;

public class InterestListAdapter extends BaseAdapter
{

	private Context context;
	private ArrayList<String> navDrawerItems;
	private ArrayList<String> navDrawerItems1;

	public InterestListAdapter(Context context, ArrayList<String> navDrawerItems)
	{
		this.context = context;
		this.navDrawerItems = navDrawerItems;

		this.navDrawerItems1 = new ArrayList<String>();
		this.navDrawerItems1.addAll(navDrawerItems);
	}

	@Override
	public int getCount()
	{
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position)
	{
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(
					R.layout.activity_interest_list_adapter_layout, null);
		}

		TextView text = (TextView) convertView
				.findViewById(R.id.txtInterestName);

		text.setText(navDrawerItems.get(position));

		return convertView;
	}

	// Filter Class
	public void filter(String charText)
	{
		charText = charText.toLowerCase(Locale.getDefault());
		navDrawerItems.clear();
		if (charText.length() == 0)
		{
			navDrawerItems.addAll(navDrawerItems1);
		}
		else
		{
			for (String wp : navDrawerItems1)
			{
				if (wp.toLowerCase(Locale.getDefault()).contains(charText))
				{
					navDrawerItems.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}

}
