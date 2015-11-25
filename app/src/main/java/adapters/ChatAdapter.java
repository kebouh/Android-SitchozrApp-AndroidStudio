package adapters;

import sources.sitchozt.R;
import model.ChatItem;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChatAdapter extends ArrayAdapter<ChatItem> {

    public ChatAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatItem item = getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();

            convertView = inflater.inflate(R.layout.chat_items, parent, false);
            holder = new ViewHolder(convertView);
            RelativeLayout.LayoutParams layoutParamsM = (RelativeLayout.LayoutParams)holder.message.getLayoutParams();
            RelativeLayout.LayoutParams layoutParamsD = (RelativeLayout.LayoutParams)holder.time.getLayoutParams();

            if (item.isSource() == false)
            {
            	layoutParamsM.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
            	layoutParamsD.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
                holder.message.setBackgroundColor(0x953498db);
            }
            else
            {
            	layoutParamsD.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
            	layoutParamsM.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
                holder.message.setBackgroundColor(0x952ecc71);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.message.setText(item.getMessage());
        holder.time.setText(item.getTime());
        return convertView;
    }

    private static class ViewHolder {
        TextView message;
        TextView time;

        ViewHolder(View layout) {
            message = (TextView) layout.findViewById(R.id.textChat);
            time = (TextView) layout.findViewById(R.id.textTime);
        }
    }
}