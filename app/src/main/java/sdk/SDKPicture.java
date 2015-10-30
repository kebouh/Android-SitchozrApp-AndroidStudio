package sdk;


import java.util.Date;
import java.util.List;

import datas.FacebookPhoto;
import datas.PlatformImageSource;

public class SDKPicture {
	private int		id;
	private boolean	profilePicture = false;
	private String 	name;
	private Date	date;
	private String 	url;
	private int		index;
	private String	facebookId;
	
	public SDKPicture(){}
	
	public SDKPicture(int id){
		this.id = id;
	}
	
	
	public SDKPicture(int id, long facebookId, String url, int index, boolean isProfile){
		this.id = id;
		this.facebookId = Long.toString(facebookId);
		this.url = url;
		this.index = index;
		this.profilePicture = isProfile;
	}
	
	public SDKPicture(long facebookId, String url, int index, boolean isProfile){
		this.facebookId = Long.toString(facebookId);
		this.url = url;
		this.index = index;
		this.profilePicture = isProfile;
	}
	
	public SDKPicture(FacebookPhoto photo, int index, boolean isProfile) {
		this.facebookId = Long.toString(photo.getId());
		this.profilePicture = isProfile;
		System.out.println("PHOTOS SIZE : " + photo);
		if (photo.getImages() != null){
			for (PlatformImageSource source : photo.getImages()){
				if (source.getWidth() < 650)
					this.url = photo.getImages().get(0).getSource();
			}
			this.index = index;
		}
	}
	
	public SDKPicture(FacebookPhoto photo, int index) {
		this.facebookId = Long.toString(photo.getId());
		if (photo.getImages() != null){
			for (PlatformImageSource source : photo.getImages()){
				if (source.getWidth() < 650)
					this.url = photo.getImages().get(0).getSource();
			}
			this.index = index;
		}
	}

	public SDKPicture create(){
		SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
		return (service.createPicture(this));
	}
	
	public Object delete(){
		SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
		return (service.deletePicture(this.id));
	}
	
	public List<SDKPicture>	getAll(){
		SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
		List<SDKPicture> result = service.getPictures();
		return (result);
	}
	
	public List<SDKPicture>	getByUserId(int userId){
		SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
		return (service.getPicturesByUserId(userId));
	}
	
	public SDKPicture	getById(){
		SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
		return (service.getPictureById(this.id));
	}
	
	public SDKPicture	getProfilePicture(int userId){
		SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
		return (service.getProfilePicture(userId));
	}

	public SDKPicture	update(){
		SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
		return (service.updatePicture(this));
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
	 * @return the profilePicture
	 */
	public boolean isProfilePicture() {
		return profilePicture;
	}
	/**
	 * @param profilePicture the profilePicture to set
	 */
	public void setProfilePicture(boolean profilePicture) {
		this.profilePicture = profilePicture;
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
	 * @return the facebookId
	 */
	public String getFacebookId() {
		return facebookId;
	}
	/**
	 * @param facebookId the facebookId to set
	 */
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
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
}
