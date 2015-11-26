package sdk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SDKDevice {
	private int		id;
	private	String	type;
	private String 	token;
	private String	language;
	private	String	applicationARN;
	private String	endPointArn;
	private Date	date;
	
	public SDKDevice(String token, String language){
		this.type = "android";
		this.token = token;
		this.language = language;
	}

	public SDKDevice(String token){
		this.type = "android";
		this.language = "en";
		this.token = token;
	}
	
	public SDKDevice	create(){
		SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
		SDKDevice device = null;
		try{
			device = service.createDevice(this);
		}catch(RuntimeException e){
			
		}
		return (device);
	}
	
	public Object			delete(){
		//SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
		//return (service.deleteDevice(this.id));
		return null;
	}
	
	public List<SDKDevice> read(){
		SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
		List<SDKDevice> device = new ArrayList<>();
		try {
			device = service.readDevice();
			System.out.println("SDK DEVICE : " + device.toString());
		} catch (Exception e){
			System.out.println("EXCEPTION : " + e.getMessage());
		}
		return (device);
	}
	
	public SDKDevice	update(){
		SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
		return (service.updateDevice(this));
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
	 * @return the endPointArn
	 */
	public String getEndPointArn() {
		return endPointArn;
	}
	/**
	 * @param endPointArn the endPointArn to set
	 */
	public void setEndPointArn(String endPointArn) {
		this.endPointArn = endPointArn;
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
