package model;

public class ChatItem {

	private String message;
	private String time;
	private boolean source;
	
	public ChatItem(String message, String time, boolean source) {
		super();
		this.message = message;
		this.time = time;
		this.source = source;
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
	 * @return the time
	 */
	public String getTime() {
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}
	/**
	 * @return the source
	 */
	public boolean isSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(boolean source) {
		this.source = source;
	}
	
}
