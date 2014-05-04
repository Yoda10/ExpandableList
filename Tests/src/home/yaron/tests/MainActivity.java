package home.yaron.tests;

import home.yaron.testsApp.R;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity
{
	// Members variables
	private String jsonString = null;
	private int listSelection = ListFragment.LIST_SELECTION_INIT;

	public int getListSelection()
	{
		return listSelection;
	}

	public void setListSelection(int value)
	{
		this.listSelection = value;
	}

	public String getJsonString()
	{
		return jsonString;		
	}

	public void setJsonString(String value)
	{
		jsonString = value;		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// Do not create fragments state in the super of on create.
		if( savedInstanceState != null && savedInstanceState.getParcelable("android:fragments") != null )
		{			
			savedInstanceState.remove("android:fragments");
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if( savedInstanceState != null )
		{
			jsonString = savedInstanceState.getString("JsonString");
			listSelection = savedInstanceState.getInt("ListSelection");		
		}

		createActionTabs();		
	}

	private void createActionTabs()
	{
		// Create action tabs.
		ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		

		// Create fragments
		ListFragment listFragment = new ListFragment();
		AboutFragment aboutFragment = new AboutFragment();

		// Tab list.
		Tab tabList = bar.newTab();
		tabList.setText("List");				
		tabList.setTabListener(new MyTabsListener<ListFragment>(listFragment,getApplicationContext())); // bind the fragments to the tabs - set up tabListeners for each tab			
		bar.addTab(tabList);

		// Tab about.
		Tab tabAbout = bar.newTab();
		tabAbout.setText("About");			
		tabAbout.setTabListener(new MyTabsListener<AboutFragment>(aboutFragment,getApplicationContext())); // bind the fragments to the tabs - set up tabListeners for each tab			
		bar.addTab(tabAbout);

		bar.setSelectedNavigationItem(0);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{			
		super.onSaveInstanceState(outState);		
		outState.putString("JsonString",jsonString);		
		outState.putInt("ListSelection",listSelection);	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{		
		// Inflate the menu items for use in the action bar	    
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}	

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.main_menu_action_1:				
			return true;
		case R.id.main_menu_action_2:
			return true;			
		default:
			return super.onOptionsItemSelected(item);
		}
	}	

	// TabListenr class for managing user interaction with the ActionBar tabs. The
	// application context is passed in pass it in constructor, needed for the
	// toast.
	public class MyTabsListener<T extends Fragment> implements ActionBar.TabListener
	{		
		public T fragment;
		public Context context;

		public MyTabsListener(T fragment, Context context)
		{
			this.fragment = fragment;
			this.context = context;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) 
		{
			Toast.makeText(context, "Reselected!", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft)
		{
			Toast.makeText(context, "Selected!", Toast.LENGTH_SHORT).show();				

			//ft.attach(fragment);
			ft.replace(R.id.fragment_container, fragment);					
		}


		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft)
		{
			Toast.makeText(context, "Unselected!", Toast.LENGTH_SHORT).show();			

			//ft.detach(fragment);
			ft.remove(fragment);
		}		
	}
}

