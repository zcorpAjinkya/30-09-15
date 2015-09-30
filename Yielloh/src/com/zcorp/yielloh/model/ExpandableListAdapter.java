
package com.zcorp.yielloh.model;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.zcorp.yielloh.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter
{

	private Context _context;
	private List<String> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<String>> _listDataChild;
	private List<String> _listDataChildEdit;

	public ExpandableListAdapter(Context context, List<String> listDataHeader,
			HashMap<String, List<String>> listChildData,
			List<String> _listDataChildEdit)
	{
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
		this._listDataChildEdit = _listDataChildEdit;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon)
	{
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent)
	{

		final String childText = (String) getChild(groupPosition, childPosition);
		final String edtChildText = _listDataChildEdit.get(childPosition);

		if (convertView == null)
		{
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item, null);
		}

		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.lblListItem);

		txtListChild.setText(childText);

		EditText edtListChild = (EditText) convertView
				.findViewById(R.id.edit_list_child);

		edtListChild.setText(edtChildText);

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.size();
	}

	@Override
	public Object getGroup(int groupPosition)
	{
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount()
	{
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition)
	{
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent)
	{

		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null)
		{
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group, null);
		}

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.lblListHeader);

		lblListHeader.setText(headerTitle);

		EditText edtListHeader = (EditText) convertView
				.findViewById(R.id.edit_list_child);

		return convertView;
	}

	@Override
	public boolean hasStableIds()
	{
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return true;
	}

}
