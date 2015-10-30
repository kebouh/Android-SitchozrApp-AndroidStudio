package activities;

import datas.Manager;
import sources.sitchozt.R;
import fragments.AlbumFragment;
import fragments.HeadlinesFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class AlbumsActivity extends FragmentActivity implements HeadlinesFragment.OnHeadlineSelectedListener {
	
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_albums);
			Manager.setContext(this);
	        if (findViewById(R.id.fragment_container) != null) {
	            if (savedInstanceState != null) {
	                return;
	            }
	            HeadlinesFragment firstFragment = new HeadlinesFragment();
	            firstFragment.setArguments(getIntent().getExtras());

	            getSupportFragmentManager().beginTransaction()
	                    .add(R.id.fragment_container, firstFragment).commit();
	        }
	    }

	    public void onArticleSelected(int position) {
	        AlbumFragment articleFrag = (AlbumFragment) getSupportFragmentManager().findFragmentById(R.id.article_fragment);
	        if (articleFrag != null) {
	            articleFrag.updateArticleView(position);
	        } else {
	        	changeFragment(position);
	        }
	    }
	    
	    public void changeFragment(int position) {
	    	AlbumFragment newFragment = new AlbumFragment();
            Bundle args = new Bundle();
            args.putInt(AlbumFragment.ARG_POSITION, position);
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
	    }

}
