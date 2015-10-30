package sdk;

import java.util.Date;

public class SDKNotification {
	private int		id;
	private String	type;
	private String	token;
	private String	language;
	private	String	applicationARN;
	private String	endPointARN;
	private	Date	date;
	
	public Object			delete(){
		SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
		return (service.deleteNotification(this.id));
	}
	
	public SDKNotification	read(){
		SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
		return (service.readNotifications());
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}
	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}
	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}
	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	/**
	 * @return the applicationARN
	 */
	public String getApplicationARN() {
		return applicationARN;
	}
	/**
	 * @param applicationARN the applicationARN to set
	 */
	public void setApplicationARN(String applicationARN) {
		this.applicationARN = applicationARN;
	}
	/**
	 * @return the endPointARN
	 */
	public String getEndPointARN() {
		return endPointARN;
	}
	/**
	 * @param endPointARN the endPointARN to set
	 */
	public void setEndPointARN(String endPointARN) {
		this.endPointARN = endPointARN;
	}
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
