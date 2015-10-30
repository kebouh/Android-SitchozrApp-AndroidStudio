package datas;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import sdk.SDKUser;
import managers.UserManager;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class LocationWraper {

	LocationManager locationManager;
	Location location = null;
	double latitude;
	double longitude;	
	String city;
	String distance;
	Context context = null;

	public LocationWraper(Context context) {
		locationManager = (LocationManager) Manager.getAppContext().getSystemService(Context.LOCATION_SERVICE);
		this.context = context;
	}

	private void updateLocationInAPI(){
		if (Manager.getProfile() != null) {
			SDKUser user = Manager.getProfile().getSdkuser();
			// Only update in API if location is different
			if (user.getLatitude() != latitude || user.getLongitude() != longitude) {
				user.setLatitude(latitude);
				user.setLongitude(longitude);
				// UPDATE PROFILE LOCALLY (OR WRONG DISTANCES)
				UserManager.ApiUpdate(null, user);
				Toast.makeText(Manager.getAppContext(), "User location updated (latitude : " + latitude + ", longitude : " + longitude, Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public void initConnection() {
		LocationManager locationManager = (LocationManager) Manager.getAppContext().getSystemService(Context.LOCATION_SERVICE);
		// update the location if user move more than 1000m and every 10min
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10*60*1000, 1000, new LocationListener() {
					@Override
					public void onStatusChanged(String s, int i, Bundle bundle) {
						updateLocationInAPI();
					}

					@Override
					public void onProviderEnabled(String s) {
					}

					@Override
					public void onProviderDisabled(String s) {
					}

					@Override
					public void onLocationChanged(final Location location) {
						updateLocationInAPI();
					}
				});
		int i = 0;
		while (location == null && i != 10)
		location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		//if (location == null) {
		//	System.out.println("Location == NULL");
		//	return;
		//}
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		/*
		Geocoder gcd = new Geocoder(Manager.getAppContext(), Locale.getDefault());
		List<Address> addresses;
		try {
			addresses = gcd.getFromLocation(latitude, longitude, 1);
			if (addresses.size() > 0) {
				System.out.println(addresses.get(0).getLocality());
				city = addresses.get(0).getLocality();
				
			} else
				city = "Adresses undefined";

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	public LocationWraper(Context context, double longitude, double latitude) {
		locationManager = (LocationManager) Manager.getAppContext().getSystemService(Context.LOCATION_SERVICE);
		location = new Location(Context.LOCATION_SERVICE);
		this.latitude = latitude;
		this.longitude = longitude;
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		/*
		Geocoder gcd = new Geocoder(Manager.getAppContext(), Locale.FRANCE);
		List<Address> addresses;
		try {
			int i = 0;
			while (i != 10)
			{
			addresses = gcd.getFromLocation(latitude, longitude, 1);
			if (addresses == null)
				System.out.println("adresses == null");
			System.out.println("before get adress");
			if (addresses.size() > 0) {
				System.out.println("Localoty: " + addresses.get(0).getLocality());
				System.out.println("Country: " + addresses.get(0).getCountryName());

				city = addresses.get(0).getLocality();
			} else
				city = "Adresses undefined";
			i++;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

	public LocationManager getLocationManager() {
		return locationManager;
	}

	public void setLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public void distanceTo(Location loc) {
		if (loc != null) {
			Float val = this.location.distanceTo(loc);
			if (val != null)
				distance = String.valueOf(Math.round(val / 1000));
		}
	}
	
	public String getDistanceTo(Location loc) {
		if (loc != null) {
			Float val = this.location.distanceTo(loc);
			if (val != null){
				distance = String.valueOf(Math.round(val / 1000));
				return (distance);
			}
		}
		return (null);
	}

	public boolean isGpsActivated() {
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			return false;
		return true;

	}
}
