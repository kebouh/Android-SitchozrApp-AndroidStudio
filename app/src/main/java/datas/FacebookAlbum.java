package datas;

public class FacebookAlbum {
	private long id;
	private boolean can_upload;
	private int	count;
	private String	cover_photo;
	//private Date	created_time;
	private String	description;
	private String	link;
	private String	name;
	private String	privacy;
	//private Date	updated_time;
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the can_upload
	 */
	public boolean isCan_upload() {
		return can_upload;
	}
	/**
	 * @param can_upload the can_upload to set
	 */
	public void setCan_upload(boolean can_upload) {
		this.can_upload = can_upload;
	}
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}
	/**
	 * @return the cover_photo
	 */
	public String getCover_photo() {
		return cover_photo;
	}
	/**
	 * @param cover_photo the cover_photo to set
	 */
	public void setCover_photo(String cover_photo) {
		this.cover_photo = cover_photo;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}
	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the privacy
	 */
	public String getPrivacy() {
		return privacy;
	}
	/**
	 * @param privacy the privacy to set
	 */
	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}
	
}
