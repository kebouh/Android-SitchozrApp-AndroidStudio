package com.voipsitchozr.views;

import java.util.ArrayList;

import com.voipsitchozr.chat.ChatView;
import com.voipsitchozr.chat.TextAdapter;
import com.voipsitchozr.chat.TextItem;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class ChatLayout extends RelativeLayout {

	Context 				context;
	TextAdapter				adapter;
	ChatView				chatView;
	ListView				list;
	ArrayList<TextItem>		listItem;	
	
	public ChatLayout(Context context, ChatView chatView) {
		super(context);
		this.context = context;
		this.chatView = chatView;
		this.list = new ListView(context);
		this.listItem = new ArrayList<TextItem>();
		this.adapter = new TextAdapter(context, listItem);
		
		list.setAdapter(adapter);

	//	list.getDivider().seœ.setDividerColor(Color.TRANSPARENT);
		list.setDivider(null);
		list.setDividerHeight(7);
		list.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
		list.setStackFromBottom(true);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		//params.addRule(RelativeLayout.ALIGN_TOP, chatView.getEditText().getId());
		list.setLayoutParams(params);

		this.addView(list);
		TextItem fake = new TextItem("hoiiiii", true);
		TextItem fake1 = new TextItem("hoiiiii ttt", false);

		listItem.add(fake);
		adapter.notifyDataSetChanged();
		/*
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT); 
		//context.getResources().getDrawable(android.R.drawable.vide)
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		this.setLayoutParams(params);
		*/
		// TODO Auto-generated constructor stub
	}

	public	void	updateList()
	{
		//adapter.clear();
		System.out.println("sizeList: " + listItem.size());
		//adapter.addAll(listItem);
		adapter.notifyDataSetChanged();
		Log.d("chat", "UpdateList");
		System.out.println("size: " + adapter.getCount());
		//list.setSelection(adapter.getCount());
	}
	
	public void	addItem(TextItem item)
	{
		//listItem.add(item);
		//adapter.clear();
		//adapter.add(item);
		//listItem.add(item);
		listItem.add(listItem.size() -1, item);

		//adapter.addAll(listItem);
		adapter.notifyDataSetChanged();
		//updateList();
			//listItem.add(item);
		//updateList();
		//adapter.add(item);
		//adapter.notifyDataSetChanged();
		//list.setSelection(adapter.getCount());
	}
}
