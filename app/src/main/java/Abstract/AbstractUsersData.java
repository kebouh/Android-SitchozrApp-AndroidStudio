package Abstract;

import java.util.ArrayList;

import com.facebook.AccessToken;

import datas.MatchProfile;
import sdk.SDKUser;
import datas.Images;
import datas.LocationWraper;
import datas.Manager;

public abstract class AbstractUsersData {

	private int id;
	private ArrayList<Images> imgs;
	private Images profileImage;
	private String firstName;
	private String lastName;
	private String age;
	private LocationWraper location;
	private Integer[] ageRange = new Integer[2];
	private Integer[] distanceRange = new Integer[2];
	private String description;
	private int genderWanted = 0;
	private SDKUser	sdkuser;
	private AccessToken accessToken;

	public AbstractUsersData()
	{		
		imgs = new ArrayList<Images>();
		this.location = new LocationWraper(Manager.getContext());
	}
	
	public AbstractUsersData(SDKUser sdkuser, AccessToken accessToken) {
		this.sdkuser = sdkuser;
		this.description = sdkuser.getDescription();
		this.setAccessToken(accessToken);
		this.id = sdkuser.getId();
		this.firstName = sdkuser.getUsername();
		this.lastName = null;				
		this.age = sdkuser.getAge();
		this.ageRange[0] = sdkuser.getDiscoveryMinAge();
		this.ageRange[1] = sdkuser.getDiscoveryMaxAge();
		this.distanceRange[0] = 0;
		this.distanceRange[1] = sdkuser.getDiscoveryDistance();
		imgs = new ArrayList<Images>();
		if (this instanceof MatchProfile)
		System.out.println("User latitude: " + sdkuser.getLatitude() + " : " + sdkuser.getLongitude());
		this.location = new LocationWraper(Manager.getContext(), sdkuser.getLatitude(), sdkuser.getLongitude());
		if(sdkuser.isDiscoveryMen() && sdkuser.isDiscoveryWomen())
			this.genderWanted = 3;
		else if(sdkuser.isDiscoveryWomen())
			this.genderWanted = 2;
		else if(sdkuser.isDiscoveryMen())
			this.genderWanted = 1;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the imgs
	 */
	public ArrayList<Images> getImgs() {
		return imgs;
	}

	public void addImagesToArray(Images img)
	{
		imgs.add(img);
	}
	
	/**
	 * @param imgs
	 *            the imgs to set
	 */
	public void setImgs(ArrayList<Images> imgs) {
		this.imgs = imgs;
	}

	/**
	 * @return the profilePicture
	 */
	public Images getProfileImage() {
		return profileImage;
	}

	/**
	 * @param profilePicture
	 *            the profilePicture to set
	 */
	public void setProfileImage(Images img) {
		this.profileImage = img;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the age
	 */
	public String getAge() {
		return age;
	}

	/**
	 * @param age
	 *            the age to set
	 */
	public void setAge(String age) {
		this.age = age;
	}

	/**
	 * @return the location
	 */
	public LocationWraper getLocation() {
		return location;
	}

	/**
	 * @param location2
	 *            the location to set
	 */
	public void setLocation(LocationWraper location2) {
		this.location = location2;
	}

	/**
	 * @return the ageRange
	 */
	public Integer[] getAgeRange() {
		return ageRange;
	}

	/**
	 * @param ageRange
	 *            the ageRange to set
	 */
	public void setAgeRange(Integer[] ageRange) {
		this.ageRange = ageRange;
	}

	/**
	 * @return the distanceRange
	 */
	public Integer[] getDistanceRange() {
		return distanceRange;
	}

	/**
	 * @param distanceRange
	 *            the distanceRange to set
	 */
	public void setDistanceRange(Integer[] distanceRange) {
		this.distanceRange = distanceRange;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getGenderWanted() {
		// TODO Auto-generated method stub
		return genderWanted;
	}
	
	public void setGenderWanted(int gender)
	{
		this.genderWanted = gender;
	}


	/**
	 * @return the sdkuser
	 */
	public SDKUser getSdkuser() {
		return sdkuser;
	}


	/**
	 * @param sdkuser the sdkuser to set
	 */
	public void setSdkuser(SDKUser sdkuser) {
		this.sdkuser = sdkuser;
	}

	/**
	 * @return the accessToken
	 */
	public AccessToken getAccessToken() {
		return accessToken;
	}

	/**
	 * @param accessToken the accessToken to set
	 */
	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
	}
}
