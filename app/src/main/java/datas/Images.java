package datas;

/**
 * Picture encapsulation
 *
 */
public class Images {

	private String url = null;
	private Long	facebookId;
	private int id;


	public Images(String url, int id) {
		this.url = url;
		this.id = id;
	}

	public Images(String url, int id, Long facebookId) {
		this.url = url;
		this.id = id;
		this.facebookId = facebookId;
	}
	

	public Images(FacebookPhoto photo) {
		this.url = photo.getPicture();
		this.facebookId = photo.getId();
		this.id = 0;
	}

	public int getId()
	{
		return id;
	}
	
	public String getUrl()
	{
		return url;
	}
	



	public void setId(int id) {
		this.id = id;
	}

	public long getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(long facebookId) {
		this.facebookId = facebookId;
	}
}
