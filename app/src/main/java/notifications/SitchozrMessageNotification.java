package notifications;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class SitchozrMessageNotification {
	
	
	private int					id;
	private String				title;
	private String				message;
	//private Date				date;
	
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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
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
	/*public Date getDate() {
		return date;
	}*/
	/**
	 * @param date the date to set
	 */
	/*public void setDate(Date date) {
		this.date = date;
	}*/
}
