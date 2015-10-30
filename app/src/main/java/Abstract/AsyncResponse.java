package Abstract;



public interface AsyncResponse {
	/**
	 *  
	 * Interface to implement in activity to update UI after backgroudn process 
	 *
	 * @param  Specify output
	 */
	public void processFinish(Boolean output);
}