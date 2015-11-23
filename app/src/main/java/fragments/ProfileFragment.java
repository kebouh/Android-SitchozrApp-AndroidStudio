package fragments;

import sdk.SDKUser;
import sources.sitchozt.R;
import memory.ImageLoader;
import datas.Album;
import datas.Manager;
import datas.Profile;
import Graphism.RangeSeekBar;
import Graphism.RangeSeekBar.OnRangeSeekBarChangeListener;
import activities.GalleryDragAndDrop;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * The profile fragment where profile is edited
 */
public class ProfileFragment extends Fragment {

	private View 		rootView = null;
	private Profile 	profile = Manager.getProfile();
	private TextView 	minAge = null;
	private TextView 	maxAge = null;
	private TextView	minDistance = null;
	private TextView 	maxDistance = null;
	private EditText 	descriptionEdit = null;
	private TextView 	descriptionText = null;
	private ImageView 	profilePicture = null;
	private boolean 	editMode = false;
	private TextView 	localization = null;
	private TextView 	name = null;
	private ImageView 	male = null;
	private ImageView 	female = null;
	private ImageView 	bothGender = null;
	private Intent 		startGallery;
	ImageLoader 		imageLoader = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		startGallery = new Intent(this.getActivity(), GalleryDragAndDrop.class);
		rootView = inflater.inflate(R.layout.activity_profile_fragment, container, false);
		profile = Manager.getProfile();
		if (profile.getListAlbums().getAlbumlist().size() <= 1)
			Manager.getDatabase().getAlbumsAndPictures();
		System.out.println("IMAGES : " + profile);

		Manager.setContext(this.getActivity());
		getViewById();
    	setValuesToViews();
    	addListeners();
		return rootView;
	}

	private void setValuesToViews() {
		Profile profile = Manager.getProfile();
		
		minAge.setText(Integer.toString(profile.getSdkuser().getDiscoveryMinAge()));
		maxAge.setText(Integer.toString(profile.getSdkuser().getDiscoveryMaxAge()));

		minDistance.setText(String.valueOf(0));
		maxDistance.setText(Integer.toString(profile.getSdkuser().getDiscoveryDistance()));
		
		
		imageLoader = Manager.getImageLoader();
		
		Display display = this.getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		profilePicture.setLayoutParams(new FrameLayout.LayoutParams(
				size.x, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		if (profile.getProfileImage() != null)
        imageLoader.displayImage(profile.getProfileImage().getUrl(), profilePicture, 0, 0, 0);

		descriptionText.setText(profile.getSdkuser().getDescription());

		name.setText(profile.getFirstName());

		localization.setText(profile.getLocation().getCity());
		
		if (profile.getSdkuser().isDiscoveryMen() == true && profile.getSdkuser().isDiscoveryWomen() == true)
			bothGender.setImageResource(R.drawable.manwomanselected);
		else if (profile.getSdkuser().isDiscoveryMen() == true)
			male.setImageResource(R.drawable.manselected);
		else if (profile.getSdkuser().isDiscoveryWomen() == true)
			female.setImageResource(R.drawable.womanselected);
	}

	private void addListeners() {
		addPictureListener();
		addDescriptionListener();
		addRangeSeekBarAge();
		addRangeSeekBarDist();
	}

	private void getViewById() {
		profilePicture = (ImageView) rootView.findViewById(R.id.profilePicture);
		localization = (TextView) rootView
				.findViewById(R.id.localizationOnPicture);
		minAge = (TextView) rootView.findViewById(R.id.minAge);
		maxAge = (TextView) rootView.findViewById(R.id.maxAge);
		minDistance = (TextView) rootView.findViewById(R.id.minDist);
		maxDistance = (TextView) rootView.findViewById(R.id.maxDist);
		name = (TextView) rootView.findViewById(R.id.nameOnPicture);

		descriptionEdit = (EditText) rootView
				.findViewById(R.id.descriptionEdit);
		descriptionText = (TextView) rootView
				.findViewById(R.id.descriptionText);
		
		male = (ImageView)rootView.findViewById(R.id.man);
		female = (ImageView)rootView.findViewById(R.id.woman);
		bothGender = (ImageView)rootView.findViewById(R.id.manwoman);
		
	}

	private void addDescriptionListener() {
		male.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if (profile.getGenderWanted() != 0)
				{
					profile.setGenderWanted(0);
					male.setImageResource(R.drawable.manselected);
					female.setImageResource(R.drawable.womannotselected);
					bothGender.setImageResource(R.drawable.manwomannotselected);
					SDKUser user = profile.getSdkuser();
					user.setDiscoveryMen(true);
					user.setDiscoveryWomen(false);
				}
				Manager.saveProfile();
			}
		});
		
		female.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if (profile.getGenderWanted() != 1)
				{
					profile.setGenderWanted(1);
					female.setImageResource(R.drawable.womanselected);
					male.setImageResource(R.drawable.mannotselected);
					bothGender.setImageResource(R.drawable.manwomannotselected);
					SDKUser user = profile.getSdkuser();
					user.setDiscoveryMen(false);
					user.setDiscoveryWomen(true);
				}
				Manager.saveProfile();
			}
		});
		
		bothGender.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if (profile.getGenderWanted() != 2)
				{
					profile.setGenderWanted(2);
					male.setImageResource(R.drawable.mannotselected);
					female.setImageResource(R.drawable.womannotselected);
					bothGender.setImageResource(R.drawable.manwomanselected);
					SDKUser user = profile.getSdkuser();
					user.setDiscoveryMen(true);
					user.setDiscoveryWomen(true);
				}					
				Manager.saveProfile();
			}
		});
		descriptionText.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				editMode = true;
				descriptionEdit.setText(descriptionText.getText().toString());
				descriptionText.setVisibility(View.GONE);
				descriptionEdit.setVisibility(View.VISIBLE);
				return false;

			}
		});

		LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.profileFrag);
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (editMode == true) {
					editMode = false;
					descriptionText.setText(descriptionEdit.getText()
							.toString());
					profile.setDescription(descriptionText.getText().toString());
					descriptionText.setVisibility(View.VISIBLE);
					descriptionEdit.setVisibility(View.GONE);
					SDKUser user = profile.getSdkuser();
					user.setDescription(descriptionEdit.getText().toString());
					Manager.saveProfile();
				}
			}
		});
	}

	/**
	 * Add a listener on the profile picture
	 */
	private void addPictureListener() {
		profilePicture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startGallery.putExtra("ID", profile.getId());
				startActivityForResult(startGallery, 1);
			}

		});
	}

	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	if (profile.getProfileImage() != null)
    	imageLoader.displayImage(profile.getProfileImage().getUrl(), profilePicture, 0, 0, 0);
	else
		profilePicture.setImageBitmap(null);
	}
	
	/**
	 * Add a listener on the range seekBar for the age range
	 */
	void addRangeSeekBarAge() {
		RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(18, 90, this.getActivity());
		seekBar.setSelectedMinValue(profile.getSdkuser().getDiscoveryMinAge());
		seekBar.setSelectedMaxValue(profile.getSdkuser().getDiscoveryMaxAge());
		seekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
				profile.getAgeRange()[0] = minValue;
				profile.getAgeRange()[1] = maxValue;
				minAge.setText(String.valueOf(minValue));
				maxAge.setText(String.valueOf(maxValue));
				SDKUser user = profile.getSdkuser();
				user.setDiscoveryMinAge(minValue);
				user.setDiscoveryMaxAge(maxValue);
				Manager.saveProfile();
			}
		});
		// add RangeSeekBar to pre-defined layout
		LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.age);
		layout.addView(seekBar);
	}

	/**
	 * Add a listener distance range seekBar
	 */
	void addRangeSeekBarDist() {
		RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(0, 160, this.getActivity());
		seekBar.setSelectedMinValue(0);
		seekBar.setSelectedMaxValue(profile.getSdkuser().getDiscoveryDistance());
		seekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
				profile.getDistanceRange()[0] = minValue;
				profile.getDistanceRange()[1] = maxValue;
				minDistance.setText("0");
				maxDistance.setText(String.valueOf(maxValue));
				SDKUser user = profile.getSdkuser();
				user.setDiscoveryDistance(maxValue);
				Manager.saveProfile();
			}
		});
		// add RangeSeekBar to pre-defined layout
		LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.dist);
		// SeekBar xseek = (SeekBar) rootView.findViewById(R.id.dist);
		layout.addView(seekBar);
	}
	
}
