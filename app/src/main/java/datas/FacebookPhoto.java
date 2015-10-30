package datas;

import java.util.List;

public class FacebookPhoto {
	private	long			id;
	//private	Date		created_time;
	private int				height;
	private	String			icon;
	private	List<PlatformImageSource>	images;
	private	String			picture;
	
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
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}
	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	/**
	 * @return the picture
	 */
	public String getPicture() {
		return picture;
	}
	/**
	 * @param picture the picture to set
	 */
	public void setPicture(String picture) {
		this.picture = picture;
	}
	/**
	 * @return the images
	 */
	public List<PlatformImageSource> getImages() {
		return images;
	}
	/**
	 * @param images the images to set
	 */
	public void setImages(List<PlatformImageSource> images) {
		this.images = images;
	}
}
