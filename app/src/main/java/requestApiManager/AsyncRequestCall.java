package requestApiManager;

import interfaces.IRequestProcess;
import interfaces.OnTaskCompleteListener;
import enumerator.REQUESTENUM;
import android.os.AsyncTask;

/**
 * Class that execute task in background
 *
 */
public class AsyncRequestCall extends AsyncTask<Object[], Integer, Object[]>	/* Perform asynchronous request call  */ {
	private OnTaskCompleteListener _listener = null;
	
	/**
	 * Constructor
	 * @param the interface to call at the end of the task
	 *
	 */
	public AsyncRequestCall(OnTaskCompleteListener listener) {
		_listener = listener;
	}
	
	/**
	 * Execute task in background
	 * @params the parameters for the request
	 * @return The request answer
	 *
	 */
	
	@Override
	protected Object[] doInBackground(Object[]... params) {
		Object[] result = null;
		IRequestProcess requestFunction = RequestManager.getFunctionFromEnum((REQUESTENUM)params[0][0]);	
		if (requestFunction != null){
			result = requestFunction.performRequest(params[0]);
		}
		return result;
		
	}
	
	/**
	 * Execute once the task is over
	 * 
	 * @param result
	 * the task result
	 *
	 */
	@Override
	protected void onPostExecute(Object[] result) {
		if (_listener != null)
			_listener.onCompleteListerner(result);
		
     }
}