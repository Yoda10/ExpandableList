package home.yaron.customViews;

import java.net.URL;

import home.yaron.testsApp.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

public class TryActivity2 extends Activity implements OnClickListener
{
	public static String URL_WEATHER_FORCAST = "http://api.openweathermap.org/data/2.5/forecast/daily?q=MyCity&mode=json&units=metric&cnt=10";	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);
		
		 //Remove title bar
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);

	    //Remove notification bar
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    
//	    int width = getResources().getDisplayMetrics().widthPixels; // Yaron remove me !!!
//	    int height = getResources().getDisplayMetrics().heightPixels; // Yaron remove me !!!
//	    float density = getResources().getDisplayMetrics().density; // Yaron remove me !!!
//	    float widthDpi = getResources().getDisplayMetrics().widthPixels / density; // Yaron remove me !!!	    
	    
		setContentView(R.layout.activity_try2);

//		GsonHelper gsonHelper = new GsonHelper(this);		
//		String jsonString = gsonHelper.readWeatherJsonFromAsset();
//		WeatherForcast weatherForcast = gsonHelper.parseJsonToWeatherForcast(jsonString);
//		ArrayList<HashMap<String, Object>> list = gsonHelper.weatherForcastToMap(weatherForcast);
	}

	@Override
	public void onClick(View v)
	{
//		EditText editText = (EditText)findViewById(R.id.try2_editText1);
//		String value = editText.getText().toString();
//
//		WeatherView weather = (WeatherView)findViewById(R.id.try2_weather);
//		weather.setPosition(Integer.parseInt(value));	
	}		
}
