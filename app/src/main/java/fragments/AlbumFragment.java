package fragments;

import datas.Manager;
import sources.sitchozt.R;
import adapters.GridViewAdapter;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class AlbumFragment extends Fragment {
    public final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
	private View rootView = null;
	private Context context;
	public GridView gridView;
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
    	context = this.getActivity();
    	Manager.setContext(context);
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
        }
        rootView = inflater.inflate(R.layout.album_fragment, container, false);
        updateArticleView(0);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            updateArticleView(args.getInt(ARG_POSITION));
        } else if (mCurrentPosition != -1) {
            updateArticleView(mCurrentPosition);
        }
    }

    public void updateArticleView(int position) {  	
    	System.out.println("UPDATE ARTICLE VIEW");
    	gridView = (GridView) rootView.findViewById(R.id.gridview);
		gridView.setColumnWidth(1);
		GridViewAdapter pa = null;
		if (mCurrentPosition != -1){
			pa = new GridViewAdapter(context, Manager.getProfile().getListAlbums().getAlbumAtPosition(position).getList(), gridView, 1, R.layout.gridview_item_album);
		}
		gridView.setAdapter(pa);
        mCurrentPosition = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_POSITION, mCurrentPosition);
    }
}