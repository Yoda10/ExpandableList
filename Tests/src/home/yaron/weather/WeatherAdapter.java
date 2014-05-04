package home.yaron.weather;

import home.yaron.testsApp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class WeatherAdapter extends SimpleAdapter 
{
	private Context context;
	private List<? extends Map<String, ?>> weatherList;
	private int average;
	//private String cityName;

	public WeatherAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to,int average)
	{
		super(context, data, resource, from, to);
		this.context = context;
		this.weatherList = data;
		this.average = average;

		//		// Calculate average.
		//		float total = 0;		
		//		float tmp;
		//		for( Map<String, ?> listItem : data)
		//		{
		//			tmp = (Float)listItem.get("max");
		//			total += tmp;
		//		}
		//
		//		average = Math.round(total / data.size());
	}

	//	public List<? extends Map<String, ?>> getWeatherList() 
	//	{
	//		return this.weatherList;
	//	}	
	//	
	//	public void setWeatherList(List<? extends Map<String, ?>> data)
	//	{
	//		this.weatherList = data;
	//	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{		
		if( convertView == null )
		{
			final LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.weather_item, null);
		}

		//		// Tempreture on item.
		final float maxTemp = (Float)weatherList.get(position).get(WeatherForcast.MAX); 
		//		TextView name = (TextView)convertView.findViewById(R.id.item_weather_name);
		//		name.setText(String.valueOf(maxTemp));

		// weather view - position
		final WeatherView weatherView = (WeatherView)convertView.findViewById(R.id.item_weather_sun);		 
		//weatherView.setPosition(Math.round(maxTemp)-1); //org
		final float scalePosition = (weatherView.getSteps() / 2F) + maxTemp - average; // new
		weatherView.setPosition(Math.round(scalePosition)-1); // new
		weatherView.setTextDrawable(String.valueOf(Math.round(maxTemp)));

		// Day of the week
		final Date date = (Date)weatherList.get(position).get("date");		
		final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE",Locale.US);		
		weatherView.setLeftText(dateFormat.format(date));			

		// weather view - drawable Id
		//int heatIndex = (Integer)weatherList.get(position).get(WeatherForcastData.HEAT_INDEX);
		setDrawableId(weatherView, maxTemp,position);

		return convertView;
	}	

	private void setDrawableId(WeatherView weatherView, float maxTemp, int position)
	{
		float heatIndex = -1; // Heat index was not computed under entry temperature.
		if( maxTemp >= WeatherForcastData.HEAT_INDEX_ENTRY_TEMPERATURE )
			heatIndex = (Float)weatherList.get(position).get(WeatherForcastData.HEAT_INDEX);	

		if( (maxTemp > 0F && maxTemp < WeatherForcastData.HEAT_INDEX_ENTRY_TEMPERATURE) || (heatIndex >= 20F && heatIndex < 27F) )
		{
			weatherView.setDrawableId(R.drawable.sun);
		}
		else if( maxTemp >= WeatherForcastData.HEAT_INDEX_ENTRY_TEMPERATURE && heatIndex >= 27F && heatIndex < 32F ) // Low
		{
			weatherView.setDrawableId(R.drawable.sun_heat_l1);
		}
		else if( maxTemp >= WeatherForcastData.HEAT_INDEX_ENTRY_TEMPERATURE && heatIndex >= 32F && heatIndex < 41F ) // Medium
		{
			weatherView.setDrawableId(R.drawable.sun_heat_l2);
		}
		else if( maxTemp >= WeatherForcastData.HEAT_INDEX_ENTRY_TEMPERATURE && heatIndex >= 41F && heatIndex < 54F ) // Danger
		{
			weatherView.setDrawableId(R.drawable.sun_heat_l3);
		}
		else if( maxTemp >= WeatherForcastData.HEAT_INDEX_ENTRY_TEMPERATURE && heatIndex >= 54F ) // Extreme danger
		{
			weatherView.setDrawableId(R.drawable.sun_heat_l4);
		}
		else if( maxTemp <= 0F ) // Snow
		{
			weatherView.setDrawableId(R.drawable.snowman);
		}		
		else
		{
			Log.v("Yaron", "Image not found on range.");
		}
	}

	//	private void setDrawableId(WeatherView weatherView, float maxTemp)
	//	{
	//		if( maxTemp >= 1F && maxTemp < 4.2F )
	//		{
	//			weatherView.setDrawableId(R.drawable.winter_1_1);
	//		}
	//		else if( maxTemp >= 4.2F && maxTemp < 8.4F )
	//		{
	//			weatherView.setDrawableId(R.drawable.winter_2);
	//		}
	//		else if( maxTemp >= 8.4F && maxTemp < 12.6F )
	//		{
	//			weatherView.setDrawableId(R.drawable.winter_3);
	//		}
	//		else if( maxTemp >= 12.6F && maxTemp < 16.8F )
	//		{
	//			weatherView.setDrawableId(R.drawable.winter_4);
	//		}
	//		else if( maxTemp >= 16.8F && maxTemp < 21F )
	//		{
	//			weatherView.setDrawableId(R.drawable.spring_5_19); // 17 tmp
	//		}
	//		else if( maxTemp >= 21F && maxTemp < 25.2F )
	//		{
	//			weatherView.setDrawableId(R.drawable.spring_6);
	//		}
	//		else if( maxTemp >= 25.2F && maxTemp < 29.4F )
	//		{
	//			weatherView.setDrawableId(R.drawable.summer_7_25);
	//		}
	//		else if( maxTemp >= 29.4F && maxTemp < 33.6F )
	//		{
	//			weatherView.setDrawableId(R.drawable.summer_8);
	//		}
	//		else if( maxTemp >= 33.6F && maxTemp < 37.8F )
	//		{
	//			weatherView.setDrawableId(R.drawable.summer_9);
	//		}
	//		else if( maxTemp >= 37.8F ) // till 42 tmp
	//		{
	//			weatherView.setDrawableId(R.drawable.summer_10);
	//		}
	//		else
	//		{
	//			Log.v("Yaron", "Image not found on range.");
	//		}
	//	}
}