package home.yaron.weather;

import home.yaron.tests.JsonHelper;
import home.yaron.weather.WeatherForcast.WList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

public class GsonHelper
{
	public final static String ASSET_WHEATHER_JERUSALEM_JSON = "WheatherJerusalemJson.txt";
	public static final String JSON_FILE_NAME = "JsonWeather.json";	

	public String readWeatherJsonFromAsset(Context context)
	{		
		String text = null; 
		InputStream input = null;

		try
		{			
			input = context.getAssets().open(ASSET_WHEATHER_JERUSALEM_JSON);			
			int size = input.available();
			byte[] buffer = new byte[size];
			input.read(buffer);
			input.close();			
			text = new String(buffer);		
		}
		catch(Exception ex)
		{	
			if( input != null )
			{
				try
				{
					input.close();
				} 
				catch (IOException e)
				{				
					e.printStackTrace();
				}
			}

			ex.printStackTrace();
		}

		return text;
	}

	public WeatherForcast parseJsonToWeatherForcast(String jsonString)
	{		
		Gson gson = new Gson();
		return gson.fromJson(jsonString, WeatherForcast.class);		
	}

	public ArrayList<HashMap<String, Object>> weatherForcastToMap(WeatherForcast weatherForcast)
	{
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>(weatherForcast.list.size());
	
		for( WList listItem : weatherForcast.list )
		{
			HashMap<String,Object> map = new HashMap<String,Object>(5);

			map.put(WeatherForcast.MAX,listItem.temp.day); 			
			map.put(WeatherForcast.HUMIDITY, listItem.humidity);
			map.put(WeatherForcast.DATE, new Date(listItem.dt * 1000)); // Convert to Java date.
			map.put(WeatherForcast.DESCRIPTION, listItem.weather.get(0).description);
			list.add(map);
		}	

		return list;
	}	

	public WeatherForcastData loadWeatherData(Context context,URL Url)
	{
		WeatherForcastData weatherForcastData = null;

		try
		{			
			JsonHelper jsonHelper = new JsonHelper();
			String jsonString = jsonHelper.loadingJsonFromUrl(Url.toString());
			Log.v("Yaron","url:"+jsonString);

			if( jsonString != null && jsonString.contains("cod") && jsonString.contains("200") )
			{
				weatherForcastData = processWeatherJson(jsonString);
				writeJsonStringToFile(context, jsonString, GsonHelper.JSON_FILE_NAME);
			}			
		} 
		catch(Exception e)
		{
			e.printStackTrace();
			weatherForcastData = null;		
		}		

		return weatherForcastData;
	}

	public WeatherForcastData loadWeatherData(Context context,String fileName)
	{		
		WeatherForcastData weatherForcastData = null;

		try
		{
			String jsonString = readJsonStringFromFile(context,fileName);			
			if( jsonString != null )
			{
				Log.v("Yaron","from file:"+jsonString);
				weatherForcastData = processWeatherJson(jsonString);
			}
		} 
		catch(Exception e)
		{
			e.printStackTrace();
			weatherForcastData = null;
		}		

		return weatherForcastData;		
	}

	private WeatherForcastData processWeatherJson(String jsonString)
	{
		ArrayList<HashMap<String, Object>> list = null;
		WeatherForcastData weatherForcastData = new WeatherForcastData();		

		try
		{			
			WeatherForcast weatherForcast = parseJsonToWeatherForcast(jsonString);
			list = weatherForcastToMap(weatherForcast);
			weatherForcastData.computeAverageTemperature(list);	
			weatherForcastData.orderList(list);
			weatherForcastData.addHeatIndexToList(list);
			weatherForcastData.setHeaderDates(list); // Set start date and end date formated string.
			weatherForcastData.setWeatherList(list);			
			weatherForcastData.setCityName(weatherForcast.city.name);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			weatherForcastData = null;
		}		

		return weatherForcastData;		
	}	

	public void writeJsonStringToFile(Context context,String jsonString,String fileName) throws IOException
	{
		FileWriter out = null;

		try
		{
			final File file = new File(context.getFilesDir(),fileName);
			out = new FileWriter(file);
			out.write(jsonString);
		}
		finally
		{				
			if( out != null ) 
				out.close();
		}
	}

	public String readJsonStringFromFile(Context context,String fileName) throws IOException
	{		
		final StringBuilder stringBuilder = new StringBuilder();		
		BufferedReader in = null;

		final File file = new File(context.getFilesDir(),fileName);
		final FileReader fileReader = new FileReader(file);

		try
		{
			String line;
			in = new BufferedReader(fileReader);
			while ((line = in.readLine()) != null) 
				stringBuilder.append(line);
		}
		finally
		{
			if( in != null )
				in.close();

			if( fileReader != null )
				fileReader.close();
		}

		return stringBuilder.toString();
	}	
}
