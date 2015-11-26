package controllers;

import android.app.Activity;

import interfaces.OnTaskCompleteListener;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import adapters.ChatAdapter;
import sdk.SDKMessage;
import datas.Manager;
import datas.MatchProfile;
import managers.MessageManager;
import model.ChatItem;

public class ChatController {
	private ArrayList<ChatItem>		listItem;
	
	public ChatController()
	{
		listItem = new ArrayList<ChatItem>();
	}
	
	public void getAllMessages(Activity activity, MatchProfile user, final ChatAdapter chatAdapter){

		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				chatAdapter.clear();
				listItem.clear();
			}
		});

		OnTaskCompleteListener onPostGetMessages = new OnTaskCompleteListener() {
			@Override
			public void onCompleteListerner(Object[] result) {
				@SuppressWarnings("unchecked")
				List<SDKMessage> messages = (List<SDKMessage>) result[1];
				for (SDKMessage message : messages){
					if (message.getUserId() == Manager.getProfile().getSdkuser().getId())
						createItem(message.getMessage(), message.getDate(), true);
					else
						createItem(message.getMessage(), message.getDate(), false);
				}
				//sortArrayList();
				//sortArrayList();
				chatAdapter.addAll(getListItem());
				chatAdapter.notifyDataSetChanged();

					}
		};
		MessageManager.ApiReadByUser(onPostGetMessages, user.getId());
	}
	
	public void addItem(ChatItem item)
	{
		listItem.add(item);
	}
	
	public void	createItem(String text, Date date, boolean source)
	{
		listItem.add(new ChatItem(text, date, source));
	}

	public void sortArrayList() {
		Collections.sort(listItem, new Comparator<ChatItem>() {
			public int compare(ChatItem o1, ChatItem o2) {
				if (o1.getTime() == null || o2.getTime() == null)
					return 0;
				return o1.getTime().compareTo(o2.getTime());
			}
		});
	}
	
	/**
	 * @return the listItem
	 */
	public ArrayList<ChatItem> getListItem() {
		return listItem;
	}

	/**
	 * @param listItem the listItem to set
	 */
	public void setListItem(ArrayList<ChatItem> listItem) {
		this.listItem = listItem;
	}	
}
