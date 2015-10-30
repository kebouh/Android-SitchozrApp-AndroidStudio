package sdk;


import java.util.Date;
import java.util.List;

public class SDKMatch {
	private int		id;
	private Date	date;
	private int		userId;
	
	public void	deleteById(){
		SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
		service.deleteMatchById(this.id);
	}
	
	public List<SDKMatch>	getByToken(){
		SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
		return (service.getMatches());
	}
	
	public List<SDKMatch>	get(){
		SitchozrServices service = SitchozrSDK.getInstance().getSitchozrServices();
		return (service.getMatchesByUserId(this.id));
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
