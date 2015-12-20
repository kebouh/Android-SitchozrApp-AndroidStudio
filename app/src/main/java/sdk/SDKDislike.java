package sdk;


import android.util.Log;

import java.util.Date;
import java.util.List;

import Tools.Tools;

public class SDKDislike {
	private int		id;
	private Date	date;
	private int		userId;

	public SDKDislike() {}

	public SDKDislike	create(){
		if (!Tools.isNetworkAvailable())
			return null;
			SDKDislike result = null;
		try {
			SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
			result = service.createDislike(this);
		}
		catch (Exception e){
			Log.w(SitchozrSDK.WARNING_TAG, "An error occured while creating dislike");
		}
		return (result);
	}

	public static List<SDKDislike>	getByToken(){
		List<SDKDislike>	result = null;
		try {
			SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
			result = service.getDislikes();
		}
		catch (Exception e){
			Log.w(SitchozrSDK.WARNING_TAG, "An error occured while getting dislikes (using token)");
		}
		return (result);
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
