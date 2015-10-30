package sdk;

import java.io.IOException;
import java.net.HttpURLConnection;
import retrofit.client.Request;
import retrofit.client.UrlConnectionClient;

public final class MyUrlConnectionClient extends UrlConnectionClient {
	  @Override protected HttpURLConnection openConnection(Request request) {
	    HttpURLConnection connection = null;
		try {
			connection = super.openConnection(request);
			connection.setConnectTimeout(10 * 1000);
			connection.setReadTimeout(10 * 1000);
	    
		} catch (IOException e) {
			e.printStackTrace();
		}
		return connection;
	  }
	}