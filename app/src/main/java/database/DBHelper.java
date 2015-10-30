package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {


	public final static String DATABASE_NAME = "DBSitchozr.db";
	public final static int DATABASE_VERSION = 1;

	
	//TABLES
	public final static String TABLE_PICTURES = "Pictures";
	public final static String TABLE_ALBUMS = "Albums";
	public final static String TABLE_USER_MATCH = "UserMatch";

	//PICTURES
	public final static String PICTURE_AUTO_ID = "picture_auto_id";

	public final static String PICTURE_USER_ID = "picture_user_id";
	public final static String PICTURE_ID = "picture_id";
	public final static String PICTURE_PROFILE = "picture_profile";
	public final static String PICTURE_DATE = "picture_date";
	public final static String PICTURE_INDEX = "picture_index";
	public final static String PICTURE_URL = "picture_url";
	public final static String PICTURE_ORDER = "picture_order";
	public final static String PICTURE_FACEBOOK_ID = "picture_facebook_id";
	
	//ALBUMS
	public final static String ALBUM_AUTO_ID = "album_auto_id";

	public final static String ALBUM_ID = "album_id";
	public final static String ALBUM_NAME = "album_name";
	
	//MATCHS
	public final static String MATCH_ID = "match_id";
	public final static String MATCH_NAME = "match_name";
	public final static String ACCESS_TOKEN = "access_token";
	public final static String MATCH_AGE = "match_age";
	public final static String MATCH_LOCATION = "match_location";
	public final static String MATCH_GENDER = "match_gender";
	public final static String MATCH_AUTO_ID = "match_auto_id";
	public final static String MATCH_DESCRIPTION = "match_description";

	
	public final static String CREATE_TABLE_MATCH = String.format(
			 "CREATE TABLE IF NOT EXISTS %S (" +
	                    " %s integer primary key autoincrement," +
	                    " %s text," +
	                    " %s text," +
	                    " %s text," +
	                    " %s text," +
	                    " %s integer," +
	                    " %s integer," +
	                    " %s integer)",
	                    TABLE_USER_MATCH, MATCH_AUTO_ID, MATCH_NAME, MATCH_ID, ACCESS_TOKEN, MATCH_AGE, MATCH_LOCATION, MATCH_DESCRIPTION, MATCH_GENDER);

	

	public final static String CREATE_TABLE_ALBUM = String.format(
			 "CREATE TABLE IF NOT EXISTS %S (" +
	                    " %s integer primary key autoincrement," +
	                    " %s text," +
	                    " %s text)",
	                    TABLE_ALBUMS, ALBUM_AUTO_ID, ALBUM_ID, ALBUM_NAME);
	

	public final static String CREATE_TABLE_PICTURES = String.format(
			 "CREATE TABLE IF NOT EXISTS %S (" +
	                    " %s integer primary key autoincrement," +
	                    " %s text," +
	                    " %s text," +
	                    " %s boolean," +
	                    " %s text," +
	                    " %s integer," +
	                    " %s text," +
	                    " %s text," +
	                    " %s text)",
	                    TABLE_PICTURES, PICTURE_AUTO_ID, PICTURE_USER_ID, PICTURE_ID, PICTURE_PROFILE, PICTURE_DATE, PICTURE_INDEX, PICTURE_URL,PICTURE_FACEBOOK_ID, ALBUM_ID, PICTURE_ORDER);

	
	public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("dbjhelper", "On create");
		db.execSQL(CREATE_TABLE_MATCH);
		db.execSQL(CREATE_TABLE_ALBUM);
		db.execSQL(CREATE_TABLE_PICTURES);
		Log.d("PATH DB",  db.getPath());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_MATCH);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ALBUM);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_PICTURES);

		// create new tables
		onCreate(db);
	}
}
