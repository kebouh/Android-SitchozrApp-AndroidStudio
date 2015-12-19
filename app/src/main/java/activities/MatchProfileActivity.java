package activities;

import android.content.Intent;
import android.view.View;

import animation.DropDownAnimation;
import datas.Manager;
import Abstract.AbstractUsersData;
import service.WebSocketIntentService;
import sources.sitchozt.R;
import sources.sitchozt.VideoActivity;


public class MatchProfileActivity extends ProfileActivity {


	@Override
	public void onResume() {
		super.onResume();
		Manager.context = this;
	}

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
			Manager.voipManager.getTcpManager().getTcpCommand().getCodeAndPerformAction("call " + /*String.valueOf(Manager.getProfile().getId())*/ String.valueOf(id));
			Intent video = new Intent(this, VideoActivity.class);
			//this.startActivity(video);
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