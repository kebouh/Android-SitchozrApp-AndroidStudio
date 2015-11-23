package database;

import java.util.ArrayList;

import database.DBHelper;
import sdk.SDKPicture;
import sdk.SDKUser;
import datas.Album;
import datas.Images;
import datas.LocationWraper;
import datas.Manager;
import datas.MatchProfile;
import datas.Profile;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBDatas {
	// Database fields
	private SQLiteDatabase database;
	private DBHelper dbHelper;

	public DBDatas(Context context) {
		dbHelper = new DBHelper(context);
		open();
	}

	public void open() {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public long createAlbum(long l, String albumName) {
		ContentValues values = new ContentValues();

		values.put(DBHelper.ALBUM_ID, l);
		values.put(DBHelper.ALBUM_NAME, albumName);

		return database.insert(DBHelper.TABLE_ALBUMS, null, values);
	}

	public long createMatch(MatchProfile match) {
		ContentValues values = new ContentValues();

		values.put(DBHelper.MATCH_ID, match.getId());
		values.put(DBHelper.MATCH_NAME, match.getFirstName());
		values.put(DBHelper.MATCH_AGE, match.getAge());
		values.put(DBHelper.MATCH_LOCATION, match.getLocation().getDistance());
		values.put(DBHelper.MATCH_GENDER, match.getGenderWanted());

		return database.insert(DBHelper.TABLE_USER_MATCH, null, values);
	}

	public long createMatch(SDKUser match) {
		ContentValues values = new ContentValues();

		values.put(DBHelper.MATCH_ID, match.getId());
		values.put(DBHelper.MATCH_NAME, match.getUsername());
		values.put(DBHelper.MATCH_AGE, match.getAge());
		LocationWraper location = new LocationWraper(Manager.getContext(), match.getLongitude(), match.getLatitude());
		System.out.println("LONGITUDE : " + match.getLongitude());
		System.out.println("LATITUDE : " + match.getLatitude());
		System.out.println(location.getDistanceTo(Manager.getProfile().getLocation().getLocation()));
		values.put(DBHelper.MATCH_LOCATION, location.getDistanceTo(Manager.getProfile().getLocation().getLocation()));
		values.put(DBHelper.MATCH_DESCRIPTION, match.getDescription());
		values.put(DBHelper.MATCH_GENDER, match.getGender().ordinal());

		return database.insert(DBHelper.TABLE_USER_MATCH, null, values);
	}

	public void changeIndexPictures(ArrayList<Images> imgs) {
		for (int i = 0; i != imgs.size(); i++) {
			ContentValues values = new ContentValues();
			values.put(DBHelper.PICTURE_INDEX, i);
			if (i == 0)
				values.put(DBHelper.PICTURE_PROFILE, true);
			else
				values.put(DBHelper.PICTURE_PROFILE, false);
			int result = database.update("PICTURES",values,DBHelper.PICTURE_ID + " = "+ imgs.get(i).getId(),null);
		}
	}

	public long createPictureProfile(Images picture, long albumId, int idUser,
			boolean profile, int index) {
		ContentValues values = new ContentValues();

		values.put(DBHelper.PICTURE_USER_ID, idUser);
		values.put(DBHelper.PICTURE_ID, picture.getId());
		values.put(DBHelper.PICTURE_PROFILE, profile);
		values.put(DBHelper.PICTURE_INDEX, index);
		values.put(DBHelper.PICTURE_URL, picture.getUrl());
		values.put(DBHelper.PICTURE_FACEBOOK_ID, picture.getFacebookId());
		values.put(DBHelper.ALBUM_ID, albumId);
		return database.insert(DBHelper.TABLE_PICTURES, null, values);
	}

	public long createPictureProfile(SDKPicture picture, long albumId, int idUser) {
		ContentValues values = new ContentValues();

		values.put(DBHelper.PICTURE_USER_ID, idUser);
		values.put(DBHelper.PICTURE_ID, picture.getId());
		values.put(DBHelper.PICTURE_PROFILE, picture.isProfilePicture());
		values.put(DBHelper.PICTURE_INDEX, picture.getIndex());
		values.put(DBHelper.PICTURE_URL, picture.getUrl());
		values.put(DBHelper.PICTURE_FACEBOOK_ID, picture.getFacebookId());
		values.put(DBHelper.ALBUM_ID, albumId);
		return database.insert(DBHelper.TABLE_PICTURES, null, values);
	}
	
	public long createPicture(SDKPicture picture, int userId) {
		ContentValues values = new ContentValues();

		values.put(DBHelper.PICTURE_USER_ID, userId);
		values.put(DBHelper.PICTURE_ID, picture.getId());
		values.put(DBHelper.PICTURE_PROFILE, picture.isProfilePicture());
		values.put(DBHelper.PICTURE_DATE, picture.getDate().toString());
		values.put(DBHelper.PICTURE_INDEX, picture.getIndex());
		values.put(DBHelper.PICTURE_URL, picture.getUrl());
		values.put(DBHelper.PICTURE_FACEBOOK_ID, picture.getFacebookId());

		return database.insert(DBHelper.TABLE_PICTURES, null, values);
	}

	public String[] splitResultConcatened(String str) {
		if (str != null)
			return (str.split(","));
		return null;
	}

	public Images getProfilePicture() {
		Cursor cursor = database.rawQuery("SELECT " + DBHelper.PICTURE_URL
				+ ", " + DBHelper.PICTURE_FACEBOOK_ID + ", "
				+ DBHelper.PICTURE_ID + " from " + DBHelper.TABLE_PICTURES
				+ " WHERE " + DBHelper.PICTURE_USER_ID + " = "
				+ Manager.getProfile().getId() + " AND "
				+ DBHelper.PICTURE_PROFILE + " AND album_id = 0", null);

		if (cursor != null) {
			while (cursor.moveToNext()) {
				Images img = new Images(cursor.getString(0), cursor.getInt(2), Long.valueOf(cursor.getString(1)));
				return img;

			}
		}
		return null;
	}

	public void getMatchsAndPictures() {
		Log.d("DBDatas", "ge tmatchs and pictures");
		Cursor cursor = database
				.rawQuery(
						"select match_id, match_name, match_age, match_location, match_description, match_gender,  group_concat(PICTURES.picture_url) as urls, group_concat(PICTURES.picture_id) as ids from USERMATCH JOIN PICTURES on USERMATCH.match_id = PICTURES.picture_user_id group by USERMATCH.match_name order by PICTURES.picture_index asc",
						null);

		if (cursor != null) {
			while (cursor.moveToNext()) {
				MatchProfile match = new MatchProfile();
				match.setFirstName(cursor.getString(cursor
						.getColumnIndex(DBHelper.MATCH_NAME)));
				match.setDescription(cursor.getString(cursor
						.getColumnIndex(DBHelper.MATCH_DESCRIPTION)));
				match.setId(cursor.getInt(cursor
						.getColumnIndex(DBHelper.MATCH_ID)));
				match.setAge(cursor.getString(cursor
						.getColumnIndex(DBHelper.MATCH_AGE)));
				
				match.getLocation().setDistance((cursor.getString(cursor
						.getColumnIndex(DBHelper.MATCH_LOCATION))));
				match.setGenderWanted(cursor.getInt(cursor
						.getColumnIndex(DBHelper.MATCH_GENDER)));

				String[] urls = splitResultConcatened(cursor.getString(cursor
						.getColumnIndex("urls")));
				String[] ids = splitResultConcatened(cursor.getString(cursor
						.getColumnIndex("ids")));
				for (int i = 0; i != urls.length; i++) {
					Log.d("urls", urls[i]);
					match.addImagesToArray(new Images(urls[i],
							Integer.valueOf(ids[i])));
				}
				Manager.addMatchProfile(match);
			}
		}
	}

	public void getPicturesById(int id) {
		Cursor cursor = database.rawQuery("select * from"
				+ DBHelper.TABLE_PICTURES + "where " + DBHelper.MATCH_ID
				+ " = " + id, null);

		Profile profile = Manager.getProfile();
		if (cursor != null) {
			while (cursor.moveToNext()) {
				profile.addImagesToArray(new Images(
						cursor.getString(cursor
								.getColumnIndex(DBHelper.PICTURE_URL)),
						cursor.getInt(cursor
								.getColumnIndex(DBHelper.PICTURE_ID))));
			}
		}
	}


	public void getAlbumsAndPictures() {
		// ArrayList<Album> list = new ArrayList<Album>();

		Cursor cursor = database
				.rawQuery(
						"SELECT "
								+ DBHelper.ALBUM_NAME
								+ ", group_concat("
								+ DBHelper.PICTURE_URL
								+ ") as urls, group_concat("
								+ DBHelper.PICTURE_ID
								+ ") as ids,  group_concat("
								+ DBHelper.PICTURE_FACEBOOK_ID
								+ ") as Fids "
								+ " FROM "
								+ " (select album_id, picture_url, picture_id, picture_facebook_id, picture_index from PICTURES order by picture_index asc) as pict"
								+

								" INNER JOIN " + DBHelper.TABLE_ALBUMS + " ON "
								+ "pict." + DBHelper.ALBUM_ID + " = "
								+ DBHelper.TABLE_ALBUMS + "."
								+ DBHelper.ALBUM_ID + " group by "
								+ DBHelper.ALBUM_NAME,
						null);

		if (cursor != null) {
			while (cursor.moveToNext()) {
				Album album = new Album(cursor.getString(0));
				String[] urls = splitResultConcatened(cursor.getString(1));
				String[] ids = splitResultConcatened(cursor.getString(2));
				String[] Fids = splitResultConcatened(cursor.getString(3));
				if (urls != null) {
					for (int i = 0; i != urls.length; i++)
						album.addImagesToList(new Images(urls[i],
								Integer.valueOf(ids[i]), Long.valueOf(Fids[i])));
					Manager.getProfile().addAlbumToList(album);
				}
				else
				Log.e("dbdata", "urls is null");
			}

			cursor.close();
		}
	}


	public void updateMatch() {

	}

	public void deletePicture(int i) {
		database.delete("PICTURES", DBHelper.PICTURE_ID
				+ " = " + i + " AND album_id = 0", null);
	}
}
