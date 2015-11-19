package sdk;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import datas.AgeCalculator;

public class SDKUser {
	public enum Gender { 
		male,
		female
	};
	
	private int					id;
	private	String				key;
	private String				secret;
	private String				username;
	private Gender				gender;
	private	Date				birthday;
	private String				description;
	private Date				lastActivity;
	private boolean				discoveryEnable;
	private	int					discoveryDistance;
	private int					discoveryMinAge;
	private int					discoveryMaxAge;
	private boolean				discoveryMen;
	private boolean				discoveryWomen;
	private	Double				latitude;
	private	Double				longitude;
	private String				age;
	private int					accuracy;
	private String				facebookId;
	private String				accessTokenFacebook;
	private	List<SDKPicture>	pictures;
	
	public SDKUser(){}
	
	public SDKUser(String accessTokenFacebook){
		this.accessTokenFacebook = accessTokenFacebook;
	}
	
	public SDKUser(int id){
		this.id = id;
	}
	
	public SDKUser(SDKUser sdkUser) {
		this.id = sdkUser.id;
		this.key = sdkUser.key;
		this.secret = sdkUser.secret;
		this.username = sdkUser.username;
		this.gender = sdkUser.gender;
		this.birthday = sdkUser.birthday;
		this.description = sdkUser.description;
		this.lastActivity = sdkUser.lastActivity;
		this.discoveryEnable = sdkUser.discoveryEnable;
		this.discoveryDistance = sdkUser.discoveryDistance;
		this.discoveryMinAge = sdkUser.discoveryMinAge;
		this.discoveryMaxAge = sdkUser.discoveryMaxAge;
		this.discoveryMen = sdkUser.discoveryMen;
		this.discoveryWomen = sdkUser.discoveryWomen;
		this.latitude = sdkUser.latitude;
		this.longitude = sdkUser.longitude;
		this.age = sdkUser.age;
		this.accuracy = sdkUser.accuracy;
		this.facebookId = sdkUser.facebookId;
		this.accessTokenFacebook = sdkUser.accessTokenFacebook;
		this.pictures = sdkUser.pictures;
		System.out.println("USER_BIRTHDAY : " +  Integer.toString(AgeCalculator.calculate(this.birthday)));
		this.age = Integer.toString(AgeCalculator.calculate(this.birthday));
	}

	public SDKToken	authenticate(){
		SDKToken token = new SDKToken(this);
		return (token.authenticate());
	}
	
	public void 	delete() {
		SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
		service.deleteUser(this);
	}
	
	public SDKUser		create() {
		SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		Map<String, String>	map = new HashMap<String, String>();
		map.put("accessTokenFacebook", this.getAccessTokenFacebook());
		return (service.createUser((JsonObject)parser.parse(gson.toJson(map))));
	}
	
	public SDKUser		update() {
		SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
		return (service.updateUser(this));
	}
	
	public SDKUser 		getById(){
		SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
		return (service.getUserById(this.getId()));
	}
	
	public SDKUser getCurrentUser(String token) {
		SitchozrSDK sdk = SitchozrSDK.getInstance();
		SDKToken sdktoken = new SDKToken(token);
		sdk.initWithHeader(sdktoken);
		SitchozrServices service = sdk.getSitchozrServices();
		return (service.getCurrentUser());
	}

	public List<SDKUser>	getAll(){
		SitchozrSDK sdk = SitchozrSDK.getInstance();
		SitchozrServices service = sdk.getSitchozrServices();
		return (service.getAllUsers());
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
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the secret
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * @param secret the secret to set
	 */
	public void setSecret(String secret) {
		this.secret = secret;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the gender
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	/**
	 * @return the birthday
	 */
	public Date getBirthday() {
		return birthday;
	}

	/**
	 * @param birthdate the birthday to set
	 */
	public void setBirthday(Date birthdate) {
		this.birthday = birthdate;
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
	 * @return the lastActivity
	 */
	public Date getLastActivity() {
		return lastActivity;
	}

	/**
	 * @param lastActivity the lastActivity to set
	 */
	public void setLastActivity(Date lastActivity) {
		this.lastActivity = lastActivity;
	}

	/**
	 * @return the discoveryEnable
	 */
	public boolean isDiscoveryEnable() {
		return discoveryEnable;
	}

	/**
	 * @param discoveryEnable the discoveryEnable to set
	 */
	public void setDiscoveryEnable(boolean discoveryEnable) {
		this.discoveryEnable = discoveryEnable;
	}

	/**
	 * @return the discoveryDistance
	 */
	public int getDiscoveryDistance() {
		return discoveryDistance;
	}

	/**
	 * @param discoveryDistance the discoveryDistance to set
	 */
	public void setDiscoveryDistance(int discoveryDistance) {
		this.discoveryDistance = discoveryDistance;
	}

	/**
	 * @return the discoveryMinAge
	 */
	public int getDiscoveryMinAge() {
		return discoveryMinAge;
	}

	/**
	 * @param discoveryMinAge the discoveryMinAge to set
	 */
	public void setDiscoveryMinAge(int discoveryMinAge) {
		this.discoveryMinAge = discoveryMinAge;
	}

	/**
	 * @return the discoveryMaxAge
	 */
	public int getDiscoveryMaxAge() {
		return discoveryMaxAge;
	}

	/**
	 * @param discoveryMaxAge the discoveryMaxAge to set
	 */
	public void setDiscoveryMaxAge(int discoveryMaxAge) {
		this.discoveryMaxAge = discoveryMaxAge;
	}

	/**
	 * @return the discoveryMen
	 */
	public boolean isDiscoveryMen() {
		return discoveryMen;
	}

	/**
	 * @param discoveryMen the discoveryMen to set
	 */
	public void setDiscoveryMen(boolean discoveryMen) {
		this.discoveryMen = discoveryMen;
	}

	/**
	 * @return the discoveryWomen
	 */
	public boolean isDiscoveryWomen() {
		return discoveryWomen;
	}

	/**
	 * @param discoveryWomen the discoveryWomen to set
	 */
	public void setDiscoveryWomen(boolean discoveryWomen) {
		this.discoveryWomen = discoveryWomen;
	}

	/**
	 * @return the accuracy
	 */
	public int getAccuracy() {
		return accuracy;
	}

	/**
	 * @param accuracy the accuracy to set
	 */
	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
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
	 * @return the accessTokenFacebook
	 */
	public String getAccessTokenFacebook() {
		return accessTokenFacebook;
	}

	/**
	 * @param accessTokenFacebook the accessTokenFacebook to set
	 */
	public void setAccessTokenFacebook(String accessTokenFacebook) {
		this.accessTokenFacebook = accessTokenFacebook;
	}

	public String getAge() {
		return Integer.toString(AgeCalculator.calculate(this.birthday));
	}

	public void setAge(String age) {
		this.age = age;
	}

	/**
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the pictures
	 */
	public List<SDKPicture> getPictures() {
		return pictures;
	}

	/**
	 * @param pictures the pictures to set
	 */
	public void setPictures(List<SDKPicture> pictures) {
		this.pictures = pictures;
	}	
}
