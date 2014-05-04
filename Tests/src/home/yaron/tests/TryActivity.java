package home.yaron.tests;

import java.util.concurrent.Executors;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

/**  
 * Just for development tests.
 */
public class TryActivity extends Activity
{	
	//public static final int LIST_SELECTION_INIT = -99;

	// Members variables
	//private String jsonString = null;
	//	private int listSelection = TryActivity.LIST_SELECTION_INIT;
	//	private View fragmentView;
	//	private JsonAsyncLoad jsonAsyncLoad;


	@Override
	public void onCreate(Bundle savedInstanceState)
	{		
		super.onCreate(savedInstanceState);		

		for( int i=0; i< 3 ; i++)
		{
			// Load json from url parse it and set adapter to the list view.
			JsonAsyncLoad jsonAsyncLoad = new JsonAsyncLoad(this);
			jsonAsyncLoad.executeOnExecutor(Executors.newSingleThreadExecutor(),JsonHelper.ANDROID_HIVE_URL);
		}
	}		

	//private JsonProgressDialog jsonProgressDialog = null;

	private class JsonAsyncLoad extends AsyncTask<String, Void, Integer>
	{
		//private Activity contextActivity;
		//		private JsonHandler jsonHandler = null;

		JsonAsyncLoad(Activity activity)
		{
			//contextActivity = activity;
		}

		//		@Override
		//		protected void onPreExecute()
		//		{		
		//			super.onPreExecute();			
		//			//jsonProgressDialog = new JsonProgressDialog(getActivity());
		//			//jsonProgressDialog.show();
		//		}	

		protected Integer doInBackground(String... urls)
		{		
			//JsonContactResult jsonContactResult = null;
			//String jsonString = ((MainActivity)contextActivity).getJsonString();

			return null;
		}

		//	@Override
		//	protected void onProgressUpdate(Void... values)
		//	{		
		//		super.onProgressUpdate(values);
		//	}

		@Override
		protected void onPostExecute(Integer result)
		{	
			//if( jsonProgressDialog != null ) jsonProgressDialog.cancel();	


			Log.v("Yaron","onPostExecute");
			//this.cancel(false);
			//Toast toast = Toast.makeText(getApplicationContext(), "Problem on loading JSON", Toast.LENGTH_LONG);
			//toast.show();


			//			//		
			//			List<Map> list = new ArrayList<Map>();
			//
			//			Map map = new HashMap();
			//			map.put("userIcon", R.drawable.scott);
			//			map.put("username", "Shen");
			//			map.put("usertext", "This is a simple sample for SimpleAdapter");
			//			list.add(map);
			//			map = new HashMap();
			//			map.put("userIcon", R.drawable.ricardo);
			//			map.put("username", "Ricardo");
			//			map.put("usertext", "This is a simple sample for SimpleAdapter");
			//			list.add(map);			
		}	
		
	}
}