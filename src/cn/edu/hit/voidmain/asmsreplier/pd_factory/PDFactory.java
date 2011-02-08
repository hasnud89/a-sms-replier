package cn.edu.hit.voidmain.asmsreplier.pd_factory;

import android.app.ProgressDialog;
import android.content.Context;
import cn.edu.hit.voidmain.asmsreplier.pd_factory.threads.RunningThread;

/**
 * PDFactory
 * a factory class that creates the ProgressDialog
 * @author voidmain
 *
 */
public class PDFactory {
	/**
	 * this method is highly configurable
	 * @param context
	 * @param title
	 * @param message
	 * @param icon
	 * @param isCancelable
	 * @param style
	 * @param isIndeterminate
	 * @param thread
	 * @return
	 */
	public static ProgressDialog createProgressDialog(Context context, 
			int title, 
			int message, 
			int icon, 
			boolean isCancelable, 
			int style, 
			boolean isIndeterminate, 
			RunningThread thread)
	{
		ProgressDialog progress = new ProgressDialog(context);
		progress.setProgressStyle(style); // style
		progress.setTitle(context.getString(title)); // title
		progress.setIcon(icon); // a network icon
		progress.setCancelable(isCancelable); // can be cancelled;
		progress.setMessage(context.getString(message)); // set the message
		progress.setIndeterminate(isIndeterminate); 
		
		thread.start();
		
		return progress;
	}
	
	/**
	 * this one uses some default value
	 * @param context
	 * @param title
	 * @param message
	 * @param icon
	 * @param thread
	 * @return
	 */
	public static ProgressDialog createProgressDialog(Context context, 
			int title, 
			int message, 
			int icon, 
			RunningThread thread)
	{
		return createProgressDialog(context, title, message, icon, false, ProgressDialog.STYLE_SPINNER, false, thread);
	}
}
