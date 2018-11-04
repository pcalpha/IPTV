package cn.com.pcalpha.iptv.channel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.com.pcalpha.iptv.R;

/**
 * Created by caiyida on 2018/6/28.
 */

public class ChannelAdapter extends BaseAdapter {
    private Context mContext;
    private ChannelDao mChannelDao;
    private LayoutInflater mLayoutInflater;

    private View.OnFocusChangeListener onItemFocusChangeListener;

    private List<Channel> mChannelList;

    public ChannelAdapter(Context mContext, List<Channel> mChannelList) {
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mChannelDao = ChannelDao.getInstance(mContext);
        this.mChannelList = mChannelList;
    }

    @Override
    public int getCount() {
        return mChannelList==null? 0 : mChannelList.size();
    }

    @Override
    public Channel getItem(int position) {
        return mChannelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChannelAdapter.ViewHolder holder;
        ListView listView = (ListView) parent;
        Channel channel = mChannelList.get(position);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_channel, parent, false);
            holder = new ChannelAdapter.ViewHolder();
            holder.channelNo = (TextView) convertView.findViewById(R.id.channel_no);
            holder.channelName = (TextView) convertView.findViewById(R.id.channel_name);
            holder.playing = (ViewStub) convertView.findViewById(R.id.channel_playing);
            holder.channel = channel;
            convertView.setTag(holder);
        } else {
            holder = (ChannelAdapter.ViewHolder) convertView.getTag();
        }


        Channel lastPlay = mChannelDao.getLastPlay();
        if (channel.equals(lastPlay)) {
            convertView.setSelected(true);
            convertView.requestFocus();
            holder.playing.setVisibility(View.VISIBLE);
        } else {
            holder.playing.setVisibility(View.GONE);
        }
        holder.channelNo.setText(channel.getNo().toString());
        holder.channelName.setText(channel.getName());
        return convertView;
    }

    public int getPosition(Channel channel){
        int position = mChannelList.indexOf(channel);
        return position;
    }

    public static class ViewHolder {
        private TextView channelNo;
        private TextView channelName;
        private ViewStub playing;

        private Channel channel;

        public Channel getChannel() {
            return channel;
        }

        public void setChannel(Channel channel) {
            this.channel = channel;
        }
    }

    public void addAll(List<Channel> mChannelList){
        this.mChannelList.addAll(mChannelList);
        notifyDataSetChanged();
    }

    public View.OnFocusChangeListener getOnItemFocusChangeListener() {
        return onItemFocusChangeListener;
    }

    public void setOnItemFocusChangeListener(View.OnFocusChangeListener onItemFocusChangeListener) {
        this.onItemFocusChangeListener = onItemFocusChangeListener;
    }
}
