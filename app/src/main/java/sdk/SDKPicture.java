package sdk;

import android.util.Log;

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
		this(facebookId, url, index, isProfile);
		this.id = id;
	}
	
	public SDKPicture(long facebookId, String url, int index, boolean isProfile){
		this.facebookId = Long.toString(facebookId);
		this.url = url;
		this.index = index;
		this.profilePicture = isProfile;
	}
	
	public SDKPicture(FacebookPhoto photo, int index, boolean isProfile) {
		this(photo, index);
		this.profilePicture = isProfile;
	}
	
	public SDKPicture(FacebookPhoto photo, int index) {
		// TODO VOIR COMMENT RECUPERER LA BONNE PHOTO
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
		SDKPicture picture = null;
		try {
			SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
			picture = service.createPicture(this);
		}
		catch (Exception e){
			Log.w(SitchozrSDK.WARNING_TAG , "An error occured while creating user picture");
		}
		return (picture);
	}
	
	public Object delete(){
		Object object = null;
		try {
			SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
			object = service.deletePicture(this.id);
		}
		catch (Exception e) {
			Log.w(SitchozrSDK.WARNING_TAG, "An error occured while deleting user picture");
		}
		return (object);
	}
	
	public List<SDKPicture>	getAll(){
		List<SDKPicture> result = null;
		try {
			SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
			result = service.getPictures();
		}
		catch (Exception e) {
			Log.w(SitchozrSDK.WARNING_TAG, "An error occured while getting all user pictures");
		}
		return (result);
	}
	
	public List<SDKPicture>	getByUserId(int userId){
		List<SDKPicture>	result = null;
		try {
			SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
			result = service.getPicturesByUserId(userId);
		}
		catch (Exception e){
			Log.w(SitchozrSDK.WARNING_TAG, "An error occured while getting pictures by user id");
		}
		return (result);
	}
	
	public SDKPicture	getById(){
		SDKPicture result = null;
		try {
			SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
			result = service.getPictureById(this.id);
		}
		catch (Exception e){
			Log.w(SitchozrSDK.WARNING_TAG, "An error occured while getting picture by id");
		}
		return (result);
	}
	
	public SDKPicture	getProfilePicture(int userId){
		SDKPicture result = null;
		try {
			SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
			result = service.getProfilePicture(userId);
		}
		catch (Exception e){
			Log.w(SitchozrSDK.WARNING_TAG, "An error occured while getting profile picture by user id");
		}
		return (result);
	}

	public SDKPicture	update(){
		SDKPicture result = null;
		try {
			SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
			result = service.updatePicture(this);
		}
		catch (Exception e){
			Log.w(SitchozrSDK.WARNING_TAG, "An error occured while updating picture");
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
