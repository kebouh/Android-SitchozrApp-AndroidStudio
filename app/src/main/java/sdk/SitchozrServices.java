	package sdk;
import java.util.List;

import com.google.gson.JsonObject;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface SitchozrServices {
	/* --------------------------------
	 * AUTHENTICATION 
	 */
	@FormUrlEncoded
	@POST("/oauth/token/")
	SDKToken	authenticate(@Header("Authorization") String authorization, @Field("grant_type") String grant_type, @Field("username") String username, @Field("password") String password);
	
	/* --------------------------------
	 * DEVICE
	 */
	@POST("/device")
	SDKDevice		createDevice(@Body SDKDevice device);
	
	@DELETE("/match/device/{deviceId}/")
	Object			deleteDevice(@Path("deviceId") int id);
	
	@GET("/device")
	List<SDKDevice>		readDevice();
	
	@PUT("/device")
	SDKDevice		updateDevice(@Body SDKDevice device);
	
	/* --------------------------------
	 * DISLIKE
	 */
	
	@POST("/dislikes/")
	SDKDislike			createDislike(@Body SDKDislike dislike);
	
	@GET("/dislikes/")
	List<SDKDislike>	getDislikes();
	
	@GET("/dislikes/{dislikeId}")
	List<SDKDislike>	getDislikesById(@Path("dislikeId") int id);
	
	/* --------------------------------
	 * LIKE
	 */
	
	@POST("/likes/")
	SDKLike				createLike(@Body SDKLike like);
	
	@GET("/likes/")
	List<SDKLike>		getLikes();
	
	@GET("/dislikes/{likeId}")
	List<SDKDislike>	getLikesById(@Path("likeId") int id);
	
	/* --------------------------------
	 * MATCH
	 */
	
	@DELETE("/match/user/{userId}/")
	Object				deleteMatchByUserId(@Path("userId") int id);
	
	@DELETE("/match/{matchId}/")
	Object				deleteMatchById(@Path("matchId") int id);
	
	@GET("/match/{matchId}/")
	List<SDKMatch>		getMatchById(@Path("matchId") int id);	
	
	@GET("/match/user/{userId}/")
	List<SDKMatch>		getMatchesByUserId(@Path("userId") int id);
	
	@GET("/matches/")
	List<SDKMatch>		getMatches();
	
	/* --------------------------------
	 * MESSAGE
	 */
	
	@POST("/message/{userId}")
	SDKMessage			createMessage(@Path("userId") int id, @Body SDKMessage message);
	
	@GET("/messages/{userId}")
	List<SDKMessage>	readMessages(@Path("userId") int id);
	
	/* --------------------------------
	 * NOTIFICATION
	 */

	@DELETE("/notification/{notificationId}")
	Object				deleteNotification(@Path("notificationId") int id);
	
	@GET("notifications")
	SDKNotification		readNotifications();
	
	/* --------------------------------
	 * PICTURE
	 */
	
	@POST("/picture/")
	SDKPicture			createPicture(@Body SDKPicture picture);
	
	@DELETE("/picture/{pictureId}/")
	Object				deletePicture(@Path("pictureId") int pictureId);
	
	@GET("/picture/{pictureId}/")
	SDKPicture			getPictureById(@Path("pictureId") int id);
	
	@GET("/picture/user/{userId}/profilePicture/")
	SDKPicture			getProfilePicture(@Path("userId") int id);
	
	@PUT("/picture/")
	SDKPicture			updatePicture(@Body SDKPicture picture);
	
	@GET("/pictures/")
	List<SDKPicture>	getPictures();
	
	@GET("/pictures/user/{userId}/")
	List<SDKPicture>	getPicturesByUserId(@Path("userId") int userId);
	
	/* --------------------------------
	 * USER
	 */
	
	@POST("/user/")
	SDKUser 		createUser(@Body JsonObject facebookToken);
	
	@GET("/user/")
	SDKUser 		getCurrentUser();
	
	@GET("/search/withPictures")
	List<SDKUser>	getAllUsers();
	
	@GET("/user/{userId}")
	SDKUser 		getUserById(@Path("userId") int id);
	
	@DELETE("/user/{userId}")
	SDKUser 		deleteUser(@Path("userId") SDKUser user);
	
	@PUT("/user/")
	SDKUser			updateUser(@Body SDKUser user);
}
