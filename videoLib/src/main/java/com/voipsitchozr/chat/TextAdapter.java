package com.voipsitchozr.chat;

import java.util.ArrayList;

import com.example.voipsitchozr.R;

import android.app.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TextAdapter extends ArrayAdapter<TextItem> {

	Context context;
	 public TextAdapter(Context context) {
	        super(context, 0);
	        this.context = context;
	 }

	 public TextAdapter(Context context, ArrayList<TextItem> list) {
	        super(context, 0, list);
	        this.context = context;
	 }
	 
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        TextItem item = getItem(position);
	        ViewHolder holder;
	        System.out.println("getview");
	        //if (convertView == null) {
	            LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();

	            convertView = inflater.inflate(R.layout.chat_text, parent, false);
	            holder = new ViewHolder(convertView);
	            RelativeLayout.LayoutParams layoutParamsM = (RelativeLayout.LayoutParams)holder.message.getLayoutParams();
	            if (item.isSource() == false)
	            {
	            	layoutParamsM.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
	            	holder.message.setBackgroundColor(0x953498db);
	            }
	            else
	            {
	            	layoutParamsM.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
	            	holder.message.setBackgroundColor(0x952ecc71);
	            }
	            convertView.setTag(holder);
/*	        } else {
	            holder = (ViewHolder) convertView.getTag();
	        }*/

	        holder.message.setText(item.getMessage());
	       // holder.time.setText(item.getTime());
	        return convertView;
	    }

	    private static class ViewHolder {
	        TextView message;
	       // TextView time;

	        ViewHolder(View layout) {
	            message = (TextView) layout.findViewById(R.id.textChatLive);
	         //   time = (TextView) layout.findViewById(R.id.textTime);
	        }
	    }
	
}
