package sdk;


import java.util.Date;
import java.util.List;

public class SDKLike {
	private int		id;
	private boolean	match;
	private Date	date;
	private int		userId;
	
	public SDKLike(int userId) {
		this.userId = userId;
	}
	
	public SDKLike() {
		// TODO Auto-generated constructor stub
	}

	public SDKLike	create(){
		SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
		return (service.createLike(this));
	}
	
	public static List<SDKLike>	getByToken(){
		SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
		return (service.getLikes());
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
