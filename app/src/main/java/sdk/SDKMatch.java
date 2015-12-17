package sdk;


import android.util.Log;

import java.util.Date;
import java.util.List;

public class SDKMatch {
	private int		id;
	private Date	date;
	private int		userId;
	
	public void	deleteById(){
		try {
			SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
			service.deleteMatchById(this.id);
		}
		catch (Exception e){
			Log.w(SitchozrSDK.WARNING_TAG, "An error occured while deleting match by id");
		}
	}
	
	public List<SDKMatch>	getByToken(){
		List<SDKMatch>	result = null;
		try {
			SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
			result = service.getMatches();
		}
		catch (Exception e){
			Log.w(SitchozrSDK.WARNING_TAG, "An error occured while getting match (using token)");
		}
		return (result);
	}
	
	public List<SDKMatch>	get(){
		List<SDKMatch>	result = null;
		try {
			SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
			result = service.getMatchesByUserId(this.id);
		}
		catch (Exception e){
			Log.w(SitchozrSDK.WARNING_TAG, "An error occured while getting match (using id)");
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
