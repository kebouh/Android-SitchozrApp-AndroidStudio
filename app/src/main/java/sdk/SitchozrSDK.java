package sdk;

import Tools.Tools;
import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class SitchozrSDK {
	private final	String		API_URL 			= "https://api.sitchozr.com/";
	public final 	String		_app_key			= "YyuxK7QbTDstmTaX45A2rJjlABtqPT";
	public final 	String		_app_secret			= "QEfoyllNekxFNDvnEh5OeINyOSWehB";

	public static final	String		ERROR_TAG			= "SDK_ERROR";
	public static final	String		WARNING_TAG			= "SDK_WARNING";

	private	SitchozrServices 	_sitchozr_services 	= null;
	
	public void initSitchozrAdapter() {
		if (!Tools.isNetworkAvailable())
			return;
		Gson gson = new GsonBuilder()
		.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
		.create();
		RestAdapter.Builder	builder = new RestAdapter.Builder()
		.setClient(new MyUrlConnectionClient())
		.setEndpoint(API_URL)
		.setLogLevel(RestAdapter.LogLevel.FULL)
		.setConverter(new GsonConverter(gson));
		RestAdapter adapter = builder.build();
        _sitchozr_services = adapter.create(SitchozrServices.class);
	}
	
	public void reInitAdapter() {
		initSitchozrAdapter();
	}
	
	public void initWithHeader(final SDKToken token) {
		if (!Tools.isNetworkAvailable())
			return;
		Gson gson = new GsonBuilder()
		.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
		.create();
		RequestInterceptor requestInterceptor = new RequestInterceptor() {
			@Override
			public void intercept(RequestFacade request) {
				request.addHeader("Authorization", "Bearer " + token.getAccess_token());
			}
		};
		RestAdapter restAdapter = new RestAdapter.Builder()
		.setClient(new MyUrlConnectionClient())
		.setErrorHandler(new ErrorHandler() {
                    @Override
                    public Throwable handleError(RetrofitError cause) {
						return new Exception();
                    }
                })
		.setEndpoint(API_URL)
		.setLogLevel(RestAdapter.LogLevel.FULL)
		.setConverter(new GsonConverter(gson))
		.setRequestInterceptor(requestInterceptor)
		.build();
		_sitchozr_services = restAdapter.create(SitchozrServices.class);
	}
	
	private SitchozrSDK(){
		initSitchozrAdapter();	
	}
	
	/*
	 * Returns services associated to SitchozrAPI
	 */
	public SitchozrServices getSitchozrServices(){
		return (_sitchozr_services);
	}
	
	private static class SingletonHolder {
		// INSTANCE DU SINGLETON (+ INSTANCIATION)
		private final static SitchozrSDK _instance = new SitchozrSDK();
	}
 
	/*
	 * Returns an instance of SitchozrSDK (singleton)
	 */ 
	public static SitchozrSDK getInstance() {
		return SingletonHolder._instance;
	}
	
	/*
	 * Returns the _app_key
	 */
	public String	getAppKey(){
		return (_app_key);
	}
	
	/*
	 * Returns the _app_secret
	 */
	public String	getAppSecret(){
		return (_app_secret);
	}
}
