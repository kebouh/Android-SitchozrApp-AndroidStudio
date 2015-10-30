package controllers;

import interfaces.OnTaskCompleteListener;

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
	
	public void getAllMessages(MatchProfile user, final ChatAdapter chatAdapter){
		chatAdapter.clear();
		listItem.clear();
		OnTaskCompleteListener onPostGetMessages = new OnTaskCompleteListener() {
			@Override
			public void onCompleteListerner(Object[] result) {
				@SuppressWarnings("unchecked")
				List<SDKMessage> messages = (List<SDKMessage>) result[1];
				for (SDKMessage message : messages){
					System.out.println(message.getUserId());
					if (message.getUserId() == Manager.getProfile().getSdkuser().getId())
						createItem(message.getMessage(), message.getDate(), true);
					else
						createItem(message.getMessage(), message.getDate(), false);
				}
				chatAdapter.addAll(getListItem());
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
		listItem.add(new ChatItem(text, date.toString(), source));
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
