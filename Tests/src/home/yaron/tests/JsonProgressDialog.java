package home.yaron.tests;

import android.app.ProgressDialog;
import android.content.Context;

public class JsonProgressDialog extends ProgressDialog
{
	public JsonProgressDialog(Context context)
	{
		super(context);

		// prepare for a progress bar dialog.		
		this.setCancelable(true);
		this.setMessage("Json downloading ...");
		this.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		this.setProgress(100);
		this.setMax(100);	
	}	
}
