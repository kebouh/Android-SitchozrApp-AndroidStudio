package activities;

import android.content.Intent;
import android.view.View;

import animation.DropDownAnimation;
import datas.Manager;
import Abstract.AbstractUsersData;
import sources.sitchozt.R;
import sources.sitchozt.VideoActivity;


public class MatchProfileActivity extends ProfileActivity {


	    @Override
	    protected int getLayoutResourceId() {
	        return R.layout.activity_match_profile;
	    }

	@Override
	protected void initializeDropdown() {
		DropDownAnimation dda = new DropDownAnimation(this, R.id.matchRoot, R.layout.match_dropdown_content, 500);
		dda.initializeDropDown(null);
	}

	@Override
		protected AbstractUsersData getUser(int id) {
			return Manager.getMatchProfileById(id);
		}
		
		
		public void launchChat(View v)
		{
			Intent chat = new Intent(this, ChatActivity.class);
			chat.putExtra("ID", id);
			startActivity(chat);
		}
		
		public void launchVideo(View v)
		{
			Intent video = new Intent(this, VideoActivity.class);
			video.putExtra("ID_CONTACT", id);
			video.putExtra("ID_USER", Manager.getProfile().getId());
			startActivity(video);
		}
		
		public void launchAudio(View v)
		{
			Intent chat = new Intent(this, ChatActivity.class);
			chat.putExtra("ID", id);
			startActivity(chat);
		}
		
		public void launchDelete(View v)
		{
			Intent chat = new Intent(this, ChatActivity.class);
			chat.putExtra("ID", id);
			startActivity(chat);
		}
}