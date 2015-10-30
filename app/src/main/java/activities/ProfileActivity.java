package activities;

import memory.ImageLoader;
import datas.Manager;
import sources.sitchozt.R;
import Abstract.AbstractUsersData;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

public abstract class ProfileActivity extends Activity {

	private ImageSwitcher imageSwitcher = null;
	private AbstractUsersData match = null;
	private int position;
	CountDownTimer countDown = null;
	protected int id;
	private TextView descriptionText = null;
	private TextView localization = null;
	private TextView name = null;
	private TextView age = null;
	int height;
	int width;
	ViewGroup container;
	private ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Manager.setContext(this);

		imageLoader = Manager.getImageLoader();
		setContentView(getLayoutResourceId());
		position = 1;
		Manager.getImageLoader().clearCache();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			id = extras.getInt("ID");
			match = getUser(id);
			if (match != null) {		
				for (int n = 0; n != match.getImgs().size(); n++)
					imageLoader.prefloadImage(match.getImgs().get(n).getUrl(),
							1, height, width);
				initSwitcher();
				imageLoader.displayImage(match
						.getImgs().get(0).getUrl(),
						imageSwitcher, 1, height, width);			
				getViewById();
				setValuesToViews();
			}
		}
	}

	protected abstract AbstractUsersData getUser(int id);

	protected abstract int getLayoutResourceId();

	private void setValuesToViews() {
		descriptionText.setText(match.getDescription());
		name.setText(match.getFirstName());
		localization.setText(match.getLocation().getDistance() + " km");
		age.setText(match.getAge());
		if (match.getGenderWanted() == 0)
			((ImageView)findViewById(R.id.manwoman)).setImageResource(R.drawable.manwomanselected);
		else if (match.getGenderWanted() == 1)
			((ImageView)findViewById(R.id.man)).setImageResource(R.drawable.manselected);
		else if (match.getGenderWanted() == 2)
			((ImageView)findViewById(R.id.woman)).setImageResource(R.drawable.womanselected);
	}

	private void getViewById() {
		localization = (TextView) findViewById(R.id.localizationOnPicture);
		name = (TextView) findViewById(R.id.nameOnPicture);
		age = (TextView) findViewById(R.id.ageValueOnPicture);
		descriptionText = (TextView) findViewById(R.id.descriptionText);
		
	}

	public void initSwitcher() {
		imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher1);
		
		imageSwitcher.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), PictureGallery.class);
				intent.putExtra("ID", id);
				Log.d("ProfileActivity", "Call PictureGallery");
				startActivity(intent);
			}
		});

		imageSwitcher.setFactory(new ViewFactory() {
			@Override
			public View makeView() {
				ImageView myView = new ImageView(getApplicationContext());
				myView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				myView.setLayoutParams(new ImageSwitcher.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				return myView;
			}
		});
		if (match.getImgs() != null && match.getImgs().size() > 0)
			countDown();
	}

	@Override
	public void onStop() {
		super.onStop();
		if (countDown != null)
			countDown.cancel();
		imageLoader.clearCache();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (countDown != null)
			countDown.cancel();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (countDown != null)
			countDown.start();
	}

	public void countDown() {
		countDown = new CountDownTimer(3000, 500) {
			public void onTick(long millisUntilFinished) {

			}

			public void onFinish() {
				next(null);
			}
		}.start();
	}

	public void next(View view) {
		Animation in = AnimationUtils.loadAnimation(this,
				android.R.anim.slide_out_right);
		Animation out = AnimationUtils.loadAnimation(this,
				android.R.anim.slide_in_left);
		imageSwitcher.setInAnimation(out);
		imageSwitcher.setOutAnimation(in);
		if (position == match.getImgs().size())
			position = 0;
		Manager.getImageLoader().displayImage(
				match.getImgs().get(position).getUrl(), imageSwitcher, 1,
				height, width);
		position++;
		countDown.start();
	}
}
