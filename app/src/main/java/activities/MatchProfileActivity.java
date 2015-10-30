package activities;

import android.content.Intent;
import android.view.View;
import datas.Manager;
import Abstract.AbstractUsersData;
import sources.sitchozt.R;


public class MatchProfileActivity extends ProfileActivity {	
	    @Override
	    protected int getLayoutResourceId() {
	        return R.layout.activity_match_profile;
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
			Intent chat = new Intent(this, ChatActivity.class);
			chat.putExtra("ID", id);
			startActivity(chat);
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