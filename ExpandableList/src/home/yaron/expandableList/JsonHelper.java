package home.yaron.expandableList;

import home.yaron.XExpandableList.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

/**
 * Can check json validation on http://jsonlint.com
 * Examples of json on res/values/strings
 */
public class JsonHelper
{
	// Urls
	public static final String  OPEN_WEATHER_MAP_URL = "http://api.openweathermap.org/data/2.5/weather?q=London,uk";
	public static final String  ANDROID_HIVE_URL = "http://api.androidhive.info/contacts/";

	/**
	 * For learing to work with json.
	 */
	private final String myJson1;
	private final String myJson2;
	private final String myJson3;
	private final String myJson4;

	private HttpPost httpPost;	

	public JsonHelper(Context context)
	{
		myJson1 = context.getString(R.string.json1);
		myJson2 = context.getString(R.string.json2);
		myJson3 = context.getString(R.string.json3);
		myJson4 = context.getString(R.string.weather);
	}

	/**
	 * For learing to work with json.
	 */
	public JsonHelper()
	{	
		myJson1 = null;
		myJson2 = null;
		myJson3 = null;
		myJson4 = null;
	}	

	/**
	 * For learing to work with json.
	 */	
	public String jsonExamples()
	{
		String ret = null;

		try
		{
			// JSON 1
			JSONObject json1 = new JSONObject(myJson1);
			JSONArray empy = json1.getJSONArray("employees");
			String lastName = empy.getJSONObject(0).getString("lastName");

			// JSON 2
			JSONObject json2 = new JSONObject(myJson2);
			JSONArray xxx = json2.getJSONArray("xxx");
			JSONObject obj1 = xxx.getJSONObject(1);
			JSONArray geo = obj1.getJSONArray("geo");
			double sec = geo.getDouble(1);		

			// JSON 3
			JSONObject json3 = new JSONObject(myJson3);
			JSONObject sys = json3.getJSONObject("sys");
			String sunset = sys.getString("sunset");

			// JSON 4
			JSONObject json4 = new JSONObject(myJson4);
			JSONObject clouds = json4.getJSONObject("clouds");
			int all = clouds.getInt("all");

			ret = lastName+" "+Double.toString(sec)+" "+sunset+" "+all;
		} 
		catch (JSONException e)
		{
			e.printStackTrace();
		}

		return ret;
	}

	public String loadingJsonFromUrl(String url) throws ClientProtocolException, IOException
	{
		String result = null;

		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used. 
		int timeoutConnection = 3000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT) 
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 5000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);		

		//  Making HTTP request - defaultHttpClient
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.setParams(httpParameters);
		httpPost = new HttpPost(url);
		HttpResponse httpResponse = httpClient.execute(httpPost);		
		HttpEntity httpEntity = httpResponse.getEntity();
		InputStream is = httpEntity.getContent();

		BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null)
		{
			sb.append(line + "\n");
		}
		is.close();		
		result = sb.toString();

		Log.v("Yaron","Loading Json from web.");

		return result;		
	}

	public JSONObject createJsonObject(String jsonString) throws JSONException
	{		
		JSONObject jsonObj = new JSONObject(jsonString);
		return jsonObj;		
	}

	public JsonContactResult JsonContactsToLists(JSONObject jsonObject) throws JSONException
	{
		JsonContactResult jsonContactResult = new JsonContactResult();

		JSONArray contacts = jsonObject.getJSONArray("contacts");

		for(int i = 0; i < contacts.length(); i++)
		{
			// Parse Json object.
			JSONObject jsonContact = contacts.getJSONObject(i);
			String id = jsonContact.getString("id");
			String name = jsonContact.getString("name");
			String email = jsonContact.getString("email");
			String address = jsonContact.getString("address");
			String gender = jsonContact.getString("gender");
			JSONObject jsonPhone = jsonContact.getJSONObject("phone");
			String mobile = jsonPhone.getString("mobile");
			String home = jsonPhone.getString("home");
			String office = jsonPhone.getString("office");

			// Create contact group item.
			HashMap<String, String> contactGroupItem = new HashMap<String, String>();
			contactGroupItem.put("id", id);
			contactGroupItem.put("name", name);
			contactGroupItem.put("email", email);
			contactGroupItem.put("address", address);
			contactGroupItem.put("gender", gender);
			//contactGroupList.add(contactGroupItem);
			jsonContactResult.getContactGroupList().add(contactGroupItem);

			// Create contact child item. (One child only to a group in this Json)
			HashMap<String, String> contactChildItem1 = new HashMap<String, String>();
			contactChildItem1.put("mobile", mobile);
			contactChildItem1.put("home", home);
			contactChildItem1.put("office", office+" :"+String.valueOf(i));

			// Create contact children entry
			ArrayList<Map<String, String>> contactChildrenEntry = new ArrayList<Map<String,String>>();
			contactChildrenEntry.add(contactChildItem1);

			// Add second child for the even contacts.
			if( i % 2 == 0 )
			{
				HashMap<String, String> contactChildItem2 = new HashMap<String, String>();
				contactChildItem2.put("mobile", mobile);
				contactChildItem2.put("home", home);
				contactChildItem2.put("office", office+" :"+String.valueOf(i));
				contactChildrenEntry.add(contactChildItem2);
			}

			//contactChildList.add(contactChildrenEntry);
			jsonContactResult.getContactChildList().add(contactChildrenEntry);
		}

		return jsonContactResult;
	}

	public void abortHttpRequest()
	{	
		Log.v("Yaron","Http abort");
		if( httpPost != null )
		{
			Log.v("Yaron","Http abort call !");
			httpPost.abort();
		}
	}

	public class JsonContactResult
	{
		private List<Map<String, String>> contactGroupList = null;
		private List<List<Map<String, String>>> contactChildList = null;		

		JsonContactResult()
		{
			contactGroupList = new ArrayList<Map<String,String>>();
			contactChildList = new ArrayList<List<Map<String,String>>>();			
		}

		public List<Map<String, String>> getContactGroupList()
		{
			return contactGroupList;
		}		

		public List<List<Map<String, String>>> getContactChildList() 
		{
			return contactChildList;
		}
	}
}
