package activities;
import java.util.ArrayList;

import com.facebook.login.LoginManager;

import datas.Album;
import datas.Manager;
import fragments.DiscoveryFragment;
import fragments.HomeFragment;
import fragments.MatchFragment;
import fragments.ProfileFragment;
import fragments.SettingFragment;
import managers.DeviceManager;
import sdk.SDKDevice;
import slidermenu.model.NavDrawerItem;
import slidingmenu.adapter.NavDrawerListAdapter;
import sources.sitchozt.R;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
 
public class NavigationActivity extends Activity {
    private DrawerLayout mDrawerLayout;
   // private RelativeLayout mRelativeLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private Menu menu;
 
    // nav drawer title
    private CharSequence mDrawerTitle;
 
    // used to store app title
    private CharSequence mTitle;
 
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
 
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("NAVIGATION ACTIVITY");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
		Manager.setContext(this);
        initializeProfile();
        this.setTitle("Navigation");
        mTitle = mDrawerTitle = getTitle();
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        navDrawerItems = new ArrayList<NavDrawerItem>();
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, /*String.valueOf(Manager.getMatchProfiles().size())*/"1"));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
        navMenuIcons.recycle();
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);
        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }
 
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
 
        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
    }


    public void     initializeProfile() {
        System.out.println("INITIALIZE PROFILE");
        Manager.getDatabase().getAlbumsAndPictures();
        Album defined = null;
        if (Manager.getProfile().getListAlbums() != null)
            defined = Manager.getProfile().getListAlbums().getAlbumByName("Defined Pictures");
        else
        System.out.println("list album null");
        if (defined != null)
            Manager.getProfile().setImgs(defined.getList());
        else
        System.out.println("defined list = NULL" );
        if (Manager.getProfile().getImgs() != null && Manager.getProfile().getImgs().size() > 0)
        Manager.getProfile().setProfileImage(Manager.getProfile().getImgs().get(0));
        Manager.getDatabase().getMatchsAndPictures();
    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            // display view for selected nav drawer item
        	
            displayView(position);
        }
    }
 
    /**
     * Create the option menu
     * @param menu the menu to create.
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.navigation, menu);
        this.menu = menu;
      //  getMenuInflater().inflate(R.menu.albums_profile, menu); 
        return true;
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
        case R.id.action_settings:
            return true;
        case R.id.facebookButton:
        	Intent intent = new Intent(this, AlbumsActivity.class);
        	startActivityForResult(intent, 0);
        default:
            return super.onOptionsItemSelected(item);
        }
    }
 
    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
    	//boolean drawerOpen = mRelativeLayout.is
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        
        if (mDrawerList.isItemChecked(2) == true)
            menu.findItem(R.id.facebookButton).setVisible(true);
           else
            menu.findItem(R.id.facebookButton).setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }
 
    /**
     * Diplaying fragment view for selected nav drawer list item
     * @param the position of the view
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null; //= new DiscoveryFragment();
        switch (position) {
        
        case 0:
            fragment = new DiscoveryFragment();
            break;
        case 1:
            fragment = new HomeFragment();
            break;
        case 2:
            fragment = new ProfileFragment();
            break;
        case 3:
            fragment = new MatchFragment();
            break;
        case 4:
            fragment = new SettingFragment();
            break;
        case 5:
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(this, MainActivity.class);
    		startActivity(intent);
            break;
 
        default:
            break;
        }
 
        if (fragment != null) {
            Log.e("NAVIGATION", "JE PASSE");
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            //mDrawerLayout.closeDrawer(mRelativeLayout);
            mDrawerLayout.closeDrawer(mDrawerList);
            Log.e("NAVIGATION", "JE PASSE2") ;
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }
 
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
 
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
 
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
 
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) { // NOT GOOD
    	//Manager.getImageLoader().displayImage(Manager.getProfile().getProfileImage().getUrl(), ;
    	//((ProfileFragment)(navDrawerItems.get(1))).onActivityResult(requestCode, resultCode, data);
    }
 
}