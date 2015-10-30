package datas;

public class FacebookResponse<T> {
	private T[]		data;
	private String	paging;
	/**
	 * @return the data
	 */
	public T[] getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(T[] data) {
		this.data = data;
	}
	/**
	 * @return the paging
	 */
	public String getPaging() {
		return paging;
	}
	/**
	 * @param paging the paging to set
	 */
	public void setPaging(String paging) {
		this.paging = paging;
	}
}
