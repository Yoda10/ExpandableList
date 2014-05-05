package home.yaron.expandableList;

import home.yaron.XExpandableList.R;
import home.yaron.expandableList.JsonHelper.JsonContactResult;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

public class ListFragment extends Fragment
{	
	public static final int LIST_SELECTION_INIT = -99;

	// Members variables	
	private View fragmentView;
	private JsonAsyncLoad jsonAsyncLoad;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{		
		super.onCreate(savedInstanceState);		

		// Yaron Json learnings tries.
		//JsonHandler jsonHandler = new JsonHandler(getActivity());
		//jsonHandler.tryJson();

		// Load json from url parse it and set adapter to the list view.
		jsonAsyncLoad = new JsonAsyncLoad(getActivity());
		jsonAsyncLoad.execute(JsonHelper.ANDROID_HIVE_URL);
	}	

	@Override
	public void onPause()
	{
		super.onPause();
		if( jsonProgressDialog != null && jsonProgressDialog.isShowing() ) 
			jsonProgressDialog.cancel();		
	}

	@Override
	public void onDestroy()
	{		
		super.onDestroy();
		if( jsonAsyncLoad != null && jsonAsyncLoad.getStatus() != AsyncTask.Status.FINISHED )
			jsonAsyncLoad.cancel(true);

		jsonAsyncLoad = null;
		fragmentView = null;
		jsonProgressDialog = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		// Inflate the layout for this fragment.
		fragmentView = inflater.inflate(R.layout.fragment_list, container, false);

		ExpandableListView contactListView = (ExpandableListView)fragmentView.findViewById(R.id.main_contacts_list);
		contactListView.setOnGroupClickListener(new OnGroupClickListener()
		{
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) 
			{			
				rememberListSelection(groupPosition);
				return false;
			}			
		});		

		return fragmentView;	
	}	

	private void rememberListSelection(int position)
	{
		((MainActivity)getActivity()).setListSelection(position);
	}

	private JsonProgressDialog jsonProgressDialog = null;

	private class JsonAsyncLoad extends AsyncTask<String, Void, JsonContactResult>
	{
		private Activity contextActivity;
		private JsonHelper jsonHandler = null;

		JsonAsyncLoad(Activity activity)
		{
			contextActivity = activity;
		}

		@Override
		protected void onPreExecute()
		{		
			super.onPreExecute();			
			jsonProgressDialog = new JsonProgressDialog(getActivity());
			jsonProgressDialog.show();
		}	

		protected JsonContactResult doInBackground(String... urls)
		{		
			JsonContactResult jsonContactResult = null;
			String jsonString = ((MainActivity)contextActivity).getJsonString();

			try
			{
				jsonHandler = new JsonHelper();			
				if( jsonString == null && !isCancelled() )
				{
					jsonString = jsonHandler.loadingJsonFromUrl(urls[0]);
					((MainActivity)contextActivity).setJsonString(jsonString);
				}

				if( jsonString != null && !isCancelled() )
				{
					JSONObject jsonObject = jsonHandler.createJsonObject(jsonString);
					jsonContactResult = jsonHandler.JsonContactsToLists(jsonObject);					
				}	
			}
			catch(Exception e)
			{			
				e.printStackTrace();				
				return null;
			}			

			return jsonContactResult;
		}		

		@Override
		protected void onPostExecute(JsonContactResult result)
		{	
			if( jsonProgressDialog != null && jsonProgressDialog.isShowing() )
				jsonProgressDialog.cancel();

			if( result != null )
			{
				ExpandableListView contactListView = (ExpandableListView)fragmentView.findViewById(R.id.main_contacts_list);				

				// Set SimpleExpandableListAdapter. - fast way
				SimpleExpandableListAdapter expListAdapter = new SimpleExpandableListAdapter(
						contextActivity,
						result.getContactGroupList(),
						android.R.layout.simple_expandable_list_item_1,
						new String[] { "name" },
						new int[] { android.R.id.text1 },
						result.getContactChildList(),
						android.R.layout.simple_expandable_list_item_2,
						new String[] { "mobile", "home", "office" },
						new int[] { android.R.id.text1, android.R.id.text2, android.R.id.text2 }
						);

				contactListView.setAdapter(expListAdapter);

				// Set user selection on the list.
				int listSelection = ((MainActivity)getActivity()).getListSelection();				
				if( listSelection != ListFragment.LIST_SELECTION_INIT ) 
					contactListView.setSelection(listSelection);				
			}
			else
			{
				Log.v("Yaron","Problem on loading json and convert it to lists.");
				Toast toast = Toast.makeText(getActivity(), "Problem on loading JSON", Toast.LENGTH_LONG);
				toast.show();
			}

			contextActivity = null; // to the GC.						
		}

		@Override
		protected void onCancelled()
		{	
			Log.v("Yaron","onCancelled");

			super.onCancelled();
			if( jsonProgressDialog != null && jsonProgressDialog.isShowing() )
				jsonProgressDialog.cancel();

			if( jsonHandler != null )
				jsonHandler.abortHttpRequest();

			contextActivity = null;
		}
	}	
}