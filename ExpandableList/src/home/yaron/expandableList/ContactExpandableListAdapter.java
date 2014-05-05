package home.yaron.expandableList;

import home.yaron.XExpandableList.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;


public class ContactExpandableListAdapter extends BaseExpandableListAdapter {

	private Activity contextActivity;
	private List<List<Map<String, String>>> contactChildList;
	private List<Map<String,String>> contactGroupList;

	public ContactExpandableListAdapter(Activity context, List<Map<String,String>> groupList,
			List<List<Map<String, String>>> childList)
	{
		this.contextActivity = context;
		this.contactGroupList = groupList;
		this.contactChildList = childList;	
	}

	public Map<String, String> getChild(int groupPosition, int childPosition)
	{
		return contactChildList.get(groupPosition).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition)
	{
		return childPosition;
	}

	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) 
	{
		HashMap<String, String> childEntry = (HashMap<String,String>)getChild(groupPosition, childPosition);		

		if (convertView == null) 
		{
			LayoutInflater inflater = contextActivity.getLayoutInflater();
			convertView = inflater.inflate(R.layout.child_item, null);
		}

		TextView home = (TextView)convertView.findViewById(R.id.child_home);
		home.setText(childEntry.get("home"));
		TextView mobile = (TextView)convertView.findViewById(R.id.child_mobile);
		mobile.setText(childEntry.get("mobile"));
		TextView office = (TextView)convertView.findViewById(R.id.child_office);
		office.setText(childEntry.get("office"));	

		return convertView;
	}

	public int getChildrenCount(int groupPosition)
	{
		return contactChildList.get(groupPosition).size();		
	}

	public Map<String, String> getGroup(int groupPosition)
	{
		return contactGroupList.get(groupPosition);
	}

	public int getGroupCount() {
		return contactGroupList.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		HashMap<String, String> groupEntry = (HashMap<String,String>)getGroup(groupPosition);
		if (convertView == null)
		{
			LayoutInflater infalInflater = (LayoutInflater)contextActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.group_item,null);
		}

		TextView id = (TextView)convertView.findViewById(R.id.group_id);		
		id.setText(groupEntry.get("id"));
		TextView name = (TextView)convertView.findViewById(R.id.group_name);		
		name.setText(groupEntry.get("name"));
		TextView email = (TextView)convertView.findViewById(R.id.group_email);		
		email.setText(groupEntry.get("email"));
		TextView address = (TextView)convertView.findViewById(R.id.group_address);		
		address.setText(groupEntry.get("address"));
		TextView gender = (TextView)convertView.findViewById(R.id.group_gender);		
		gender.setText(groupEntry.get("gender"));

		return convertView;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}