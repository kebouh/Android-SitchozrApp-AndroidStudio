package sdk;
import java.io.UnsupportedEncodingException;

import android.util.Base64;

public class SDKToken {
	private String	grant_type = "password";
	private String	username;
	private String	password;
	private String	token_type;
	private String 	access_token;
	private	int	expires_in;
	
	private static String encodeBasicAuth(String appKey, String appSecret) {
		final String credentials = appKey + ":" + appSecret;
		String string= null ;
		try {
			string = "Basic " + Base64.encodeToString(credentials.getBytes("UTF-8"), Base64.NO_WRAP);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return (string);
	}
	
	public SDKToken(String token){
		this.access_token = token;
	}
	
	public SDKToken	authenticate() {
		SitchozrSDK sdk = SitchozrSDK.getInstance();
		sdk.initSitchozrAdapter();
		SitchozrServices service = sdk.getSitchozrServices();
		String auth = encodeBasicAuth(sdk._app_key, sdk._app_secret);
		SDKToken token = service.authenticate(auth, this.grant_type, this.username, this.password);
		sdk.initWithHeader(token);
		return (token);
		//return (service.authenticate(auth, this.grant_type, this.username, this.password));
	}
	
	public SDKToken(SDKToken token) {
		this.grant_type = token.grant_type;
		this.username = token.username;
		this.password = token.password;
		this.token_type = token.token_type;
		this.access_token = token.access_token;
		this.expires_in = token.expires_in;
	}
	
	public SDKToken(SDKUser user){
		this.username = user.getKey();
		this.password = user.getSecret();
	}
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the token_type
	 */
	public String getToken_type() {
		return token_type;
	}
	/**
	 * @param token_type the token_type to set
	 */
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}
	/**
	 * @return the access_token
	 */
	public String getAccess_token() {
		return access_token;
	}
	/**
	 * @param access_token the access_token to set
	 */
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	/**
	 * @return the expires_in
	 */
	public int getExpires_in() {
		return expires_in;
	}
	/**
	 * @param expires_in the expires_in to set
	 */
	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}
	
}
