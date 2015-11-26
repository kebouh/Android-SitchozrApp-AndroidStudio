package com.voipsitchozr.chat;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.voipsitchozr.R;
import com.voipsitchozr.utils.ConcurrentQueue;
import com.voipsitchozr.views.ChatLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class ChatView {

	Context context;
	FrameLayout frameLayout;
	ChatLayout	chatLayout;
	EditText	edit;
	ConcurrentQueue<String> queueMsg;
	public ChatView(Context context, FrameLayout frameLayout)
	{
		this.context = context;
		this.frameLayout = frameLayout;
		edit = new EditText(context);
		edit.setId(5364);
		chatLayout = new ChatLayout(context, this);
		chatLayout.setClickable(true);
		queueMsg = new ConcurrentQueue<String>();

	}
	
	public void initChatManager()
	{
		RelativeLayout	textLayout = new RelativeLayout(context);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.addRule(RelativeLayout.ALIGN_BOTTOM);
		//params.addRule(RelativeLayout.BELOW, chatLayout.getId());
		textLayout.setLayoutParams(params);
	
		LayoutParams chatParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		chatParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		chatLayout.setLayoutParams(chatParams);
		chatLayout.setId(98765);
		edit.setHint("write message...");

		Resources res = context.getResources();

		Drawable drawable = res.getDrawable(R.drawable.edit_text_style);
		edit.setBackground(drawable);
		edit.setImeOptions(EditorInfo.IME_ACTION_SEND);
		edit.setSingleLine();
		edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				sendText();
				return true;
			}
		});
				//edit.setImeActionLabel("Send", KeyEvent.KEYCODE_ENTER);
		edit.setId(45678);
		LayoutParams editParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		//editParams.addRule(RelativeLayout.END_OF, send.getId());
		edit.setLayoutParams(editParams);
		
		LayoutParams sendParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		sendParams.addRule(RelativeLayout.RIGHT_OF, edit.getId());

		textLayout.addView(edit);
		chatLayout.addView(textLayout);
		frameLayout.addView(chatLayout);		
	//	frameLayout.addView(textLayout);
		

	}
	
	void	sendText()
	{
				String message = edit.getText().toString();
				edit.setText("");
				queueMsg.add(message);
				chatLayout.addItem(new TextItem(message, true));
	}

	public 	String	getPendingMessage()
	{
		return queueMsg.poll();
	}
	
	public 	boolean	isPendingQueueEmpty()
	{
		return queueMsg.isEmpty();
	}
	
	public void		addItem(String mess)
	{
		chatLayout.addItem(new TextItem(mess, false));
	}
	
	public	void	addItemOnUiThread(final String message)
	{/*
		VoipManager.activity.runOnUiThread(new Runnable() {
            public void run()
            {
            	chatLayout.addItem(new TextItem(message, false));
            }
        });*/
	}

	public ChatLayout	getChatLayout() {
		return this.chatLayout;
	}

	public EditText getEditText() {
		return edit;
	}
}
