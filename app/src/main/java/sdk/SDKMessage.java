package sdk;

import android.util.Log;

import java.util.Date;
import java.util.List;

public class SDKMessage {
	private int 	id;
	private String	message = null;
	private Date	date;
	private int		userId;
	
	public SDKMessage(String message, Date date, int userId){
		this.message = message;
		this.date = date;
		this.userId = userId;
	}
	
	public SDKMessage() {}

	public List<SDKMessage>	get(int userId){
		List<SDKMessage>	result = null;
		try {
			SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
			result = service.readMessages(this.userId);
		}
		catch (Exception e){
			Log.w(SitchozrSDK.WARNING_TAG, "An error occured while getting message");
		}
		return (result);
	}
	
	public SDKMessage		create(){
		SDKMessage message = null;
		SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
		try {
			if (this.message.length() > 0)
				message = service.createMessage(this.userId, this);
		} catch (Exception e) {
			Log.w(SitchozrSDK.WARNING_TAG, "An error occured while creating message");
		}
		return (message);
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
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
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

	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
