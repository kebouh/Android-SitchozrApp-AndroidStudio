package notifications;

import com.google.gson.annotations.SerializedName;

public class SitchozrNotification {
	public enum	NotificationType{
		@SerializedName("message")
		MESSAGE,
		@SerializedName("match")
		MATCH
	}
	
	private NotificationType	type;
	private	int					notificationId;
	private SitchozrMessageNotification	message;
	private	int					userId;
	
	/**
	 * @return the type
	 */
	public NotificationType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(NotificationType type) {
		this.type = type;
	}
	/**
	 * @return the notificationId
	 */
	public int getNotificationId() {
		return notificationId;
	}
	/**
	 * @param notificationId the notificationId to set
	 */
	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}
	/**
	 * @return the message
	 */
	public SitchozrMessageNotification getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(SitchozrMessageNotification message) {
		this.message = message;
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
