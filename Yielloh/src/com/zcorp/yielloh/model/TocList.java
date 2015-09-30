
package com.zcorp.yielloh.model;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zcorp.yielloh.R;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TocList extends ArrayAdapter
{
	private Context ctx;
	private ArrayList<String> homeList;

	public ArrayList<String> chapter = new ArrayList<String>();
	private ArrayList<Integer> myIntegerArrayList = new ArrayList<Integer>();

	private ArrayList<Integer> levels, co_status, part_indicator;
	private ArrayList<Boolean> final_clicked;
	@SuppressWarnings("unused")
	private int which_app, length;
	String chaptername;
	boolean flag = false;
	int pos;

	private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	static class ListRowHolder
	{
		TextView txtChaptername;
		TextView txtChapno;
		ImageView imgNavigator;
		CheckBox imgNavigator1;
		RelativeLayout rltoclist;

	}

	public TocList(Context context, int item_layout_id,
			ArrayList<String> objects, ArrayList<Integer> levels,
			ArrayList<Integer> co_status, ArrayList<Boolean> final_clicked,
			ArrayList<Integer> part_indicator, int which_app)
	{
		super(context, item_layout_id, objects);
		this.ctx = context;
		this.homeList = objects;
		this.length = homeList.size();
		this.levels = levels;
		this.co_status = co_status;
		this.final_clicked = final_clicked;
		this.part_indicator = part_indicator;
		this.which_app = which_app;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{

		pos = position;
		LayoutInflater layoutInflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final ListRowHolder listRowHolder;

		if (convertView == null)
		{
			switch (levels.get(position))
			{
			case 1:
				convertView = layoutInflater.inflate(R.layout.first_list_item,
						parent, false);
				break;
			case 2:
				convertView = layoutInflater.inflate(R.layout.first_list_item,
						parent, false);
				break;
			case 3:
				convertView = layoutInflater.inflate(R.layout.first_list_item,
						parent, false);
				break;
			case 4:
				convertView = layoutInflater.inflate(R.layout.first_list_item,
						parent, false);
				break;

			}

			listRowHolder = new ListRowHolder();
			listRowHolder.txtChapno = (TextView) convertView
					.findViewById(R.id.txtChapno);
			listRowHolder.txtChaptername = (TextView) convertView
					.findViewById(R.id.txtChapter);
			listRowHolder.imgNavigator = (ImageView) convertView
					.findViewById(R.id.imgNavigator);
			listRowHolder.imgNavigator1 = (CheckBox) convertView
					.findViewById(R.id.imgNavigator1);
			listRowHolder.rltoclist = (RelativeLayout) convertView
					.findViewById(R.id.rltoclist);

			/*
			 * listRowHolder.imgNavigator1.setTag(pos);
			 * listRowHolder.imgNavigator1.setOnCheckedChangeListener(this);
			 */

			convertView.setTag(listRowHolder);

		}
		else
		{

			listRowHolder = (ListRowHolder) convertView.getTag();
			listRowHolder.imgNavigator1.getTag(position);

			/*
			 * if (levels.get(position) == 3) {
			 * 
			 * Log.e("Postion", pos + ""); Log.e("Cha", homeList.get(pos));
			 * Log.e("Chapter", listRowHolder.txtChaptername.getText()
			 * .toString()); Log.e("Status",
			 * listRowHolder.imgNavigator1.isShown() + ""); for (int i = 0; i <
			 * modelList.size(); i++) { if
			 * (homeList.get(pos).matches(modelList.get(i).getName()) &&
			 * modelList.get(i).getPosition() == pos) {
			 * 
			 * listRowHolder.imgNavigator1.setChecked(true); } else {
			 * listRowHolder.imgNavigator1.setChecked(false); } } }
			 */
		}
		listRowHolder.imgNavigator1.setTag(pos);

		listRowHolder.imgNavigator1.setChecked(myIntegerArrayList
				.contains(position));

		listRowHolder.imgNavigator1
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
				{
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked)
					{
						if (isChecked)
						{
							myIntegerArrayList
									.add((Integer) listRowHolder.imgNavigator1
											.getTag());

						}
						else
						{
							myIntegerArrayList
									.remove((Object) listRowHolder.imgNavigator1
											.getTag());
						}
					}
				});

		listRowHolder.imgNavigator1.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Log.e("Check", "" + listRowHolder.imgNavigator1.isChecked());

				if (listRowHolder.imgNavigator1.isChecked())
				{
					if (!chapter.contains(listRowHolder.txtChaptername
							.getText().toString()))
					{
						chapter.add(listRowHolder.txtChaptername.getText()
								.toString());
						Log.e("List", "" + chapter);
					}
				}
				else
				{
					chapter.remove(listRowHolder.txtChaptername.getText()
							.toString());
					Log.e("List", "" + chapter);
				}

			}
		});
		/*
		 * OnCheckedChangeListener checkListener = new OnCheckedChangeListener()
		 * {
		 * 
		 * @Override public void onCheckedChanged(CompoundButton buttonView,
		 * boolean isChecked) {
		 * 
		 * if (isChecked) {
		 * 
		 * flag = true; obj = new Model();
		 * obj.setName(listRowHolder.txtChaptername.getText() .toString());
		 * obj.setSelected(true); obj.setPosition(pos); modelList.add(obj);
		 * 
		 * Toast.makeText(ctx,
		 * listRowHolder.txtChaptername.getText().toString(),
		 * Toast.LENGTH_LONG).show(); } } };
		 */

		if (levels.get(position) == 3)
		{
			setExpandCollapse1(levels.get(position), co_status.get(position),
					listRowHolder.imgNavigator, listRowHolder.imgNavigator1);
		}
		else
		{
			setExpandCollapse(levels.get(position), co_status.get(position),
					listRowHolder.imgNavigator, listRowHolder.imgNavigator1);
		}

		switch (part_indicator.get(position))
		{
		case 1:

			switch (levels.get(position))
			{
			case 1:
				convertView
						.setBackgroundResource(R.drawable.first_list_item_selector);
				listRowHolder.txtChaptername.setTextColor(Color.WHITE);
				listRowHolder.txtChapno.setText("");
				break;
			/*
			 * case 111:
			 * convertView.setBackgroundResource(R.drawable.first_list_item_selector
			 * ); listRowHolder.txtChaptername.setTextColor(Color.WHITE);
			 * listRowHolder.txtChapno.setText(""); break;
			 */
			case 2:
				listRowHolder.txtChaptername.setTextColor(Color.BLACK);
				convertView
						.setBackgroundResource(R.drawable.second_list_item_selector);
				listRowHolder.txtChapno.setText("");
				break;
			case 3:
				listRowHolder.txtChaptername.setTextColor(Color.BLACK);
				convertView
						.setBackgroundResource(R.drawable.third_list_item_selector);
				listRowHolder.txtChapno.setText("");
				break;
			case 4:
				listRowHolder.txtChaptername.setTextColor(Color.BLACK);
				convertView
						.setBackgroundResource(R.drawable.forth_list_item_selector);
				listRowHolder.txtChapno.setText("");
				break;
			}
			break;
		}

		chaptername = homeList.get(position);

		listRowHolder.txtChaptername.setText(Html.fromHtml(chaptername));

		if (final_clicked.get(position))
		{

			if (levels.get(position) == 111)
				listRowHolder.imgNavigator
						.setImageResource(R.drawable.right_white);
			else
				listRowHolder.imgNavigator
						.setImageResource(R.drawable.right_white);
		}

		convertView.setTag(listRowHolder);

		return convertView;

	}

	private String opchapter;
	private String[] chap_parts = new String[2];

	public String[] giveChapterParts(String chapter, int which_app)
	{
		opchapter = chapter;
		chap_parts[0] = opchapter.substring(0, opchapter.indexOf(" ") + 1);
		chap_parts[1] = opchapter.substring(opchapter.indexOf(" ") + 1,
				opchapter.length());

		return chap_parts;

	}

	public void setExpandCollapse(int level, int co_state,
			ImageView imgNavigator, CheckBox img)
	{

		switch (co_state)
		{
		case 0:
			if (level == 1 || level == 11)
			{
				img.setVisibility(View.GONE);
				imgNavigator.setVisibility(View.VISIBLE);
				imgNavigator.setImageResource(R.drawable.plus_white);
			}
			else if (level == 4)
				imgNavigator.setImageResource(R.drawable.right_white);
			else
			{
				img.setVisibility(View.GONE);
				imgNavigator.setVisibility(View.VISIBLE);
				imgNavigator.setImageResource(R.drawable.plus_white);
			}
			break;
		case 1:
			if (level == 1 || level == 11)

			{
				img.setVisibility(View.GONE);
				imgNavigator.setVisibility(View.VISIBLE);
				imgNavigator.setImageResource(R.drawable.minus_white);
			}
			else
			{
				img.setVisibility(View.GONE);
				imgNavigator.setVisibility(View.VISIBLE);
				imgNavigator.setImageResource(R.drawable.minus_white);
			}
			break;
		}
	}

	public void setExpandCollapse1(int level, int co_state,
			ImageView imgNavigator, CheckBox img)
	{

		switch (co_state)
		{
		case 0:
			if (level == 1 || level == 11)
			{
				imgNavigator.setVisibility(View.VISIBLE);
				img.setVisibility(View.GONE);
				imgNavigator.setImageResource(R.drawable.plus_white);
			}
			else if (level == 3)
			{
				imgNavigator.setVisibility(View.GONE);
				img.setVisibility(View.VISIBLE);

			}
			else
			{
				img.setVisibility(View.GONE);
				imgNavigator.setImageResource(R.drawable.plus_white);
				imgNavigator.setVisibility(View.VISIBLE);
			}
			break;
		case 1:
			if (level == 1 || level == 11)
			{
				imgNavigator.setVisibility(View.GONE);
				img.setVisibility(View.VISIBLE);
			}
			else if (level == 3)
			{
				imgNavigator.setVisibility(View.GONE);
				img.setVisibility(View.VISIBLE);

			}
			else
			{
				img.setVisibility(View.GONE);
				imgNavigator.setImageResource(R.drawable.plus_white);
				imgNavigator.setVisibility(View.VISIBLE);
			}
			break;
		}
	}

	/*
	 * @Override public Object[] getSections() { // TODO Auto-generated method
	 * stub return null; }
	 * 
	 * @Override public int getPositionForSection(int section) { for (int i =
	 * section; i >= 0; i--) { for (int j = 0; j < getCount(); j++) { if (i ==
	 * 0) { // For numeric section for (int k = 0; k <= 9; k++) { if
	 * (StringMatcher.match( String.valueOf(homeList.get(pos).charAt(0)),
	 * String.valueOf(k))) return j;
	 * 
	 * } } else {
	 * 
	 * if (StringMatcher.match( String.valueOf(homeList.get(pos).charAt(0)),
	 * String.valueOf(mSections.charAt(i)))) return j;
	 * 
	 * } } } return 0; }
	 * 
	 * @Override public int getSectionForPosition(int position) { // TODO
	 * Auto-generated method stub return 0; }
	 */
}
