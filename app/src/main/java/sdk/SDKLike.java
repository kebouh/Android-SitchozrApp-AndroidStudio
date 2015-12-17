package sdk;


import android.util.Log;

import java.util.Date;
import java.util.List;

public class SDKLike {
	private int		id;
	private boolean	match;
	private Date	date;
	private int		userId;
	
	public SDKLike() {}

	public SDKLike	create(){
		SDKLike result = null;
		try {
			SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
			result = service.createLike(this);
		}
		catch (Exception e){
			Log.w(SitchozrSDK.WARNING_TAG, "An error occured while creating like");
		}
		return (result);
	}
	
	public static List<SDKLike>	getByToken(){
		List<SDKLike>	result = null;
		try {
			SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
			result = service.getLikes();
		}
		catch (Exception e){
			Log.w(SitchozrSDK.WARNING_TAG, "An error occured while getting likes (using token)");
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
	 * @return the match
	 */
	public boolean isMatch() {
		return match;
	}
	/**
	 * @param match the match to set
	 */
	public void setMatch(boolean match) {
		this.match = match;
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
