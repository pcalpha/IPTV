package cn.com.pcalpha.iptv.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.com.pcalpha.iptv.R;

/**
 * Created by caiyida on 2018/6/28.
 */

public class ChannelCategoryAdapter extends BaseAdapter {
    private Context mContext;
    private ChannelCategoryDao mChannelCategoryDao;
    private LayoutInflater mLayoutInflater;

    private List<ChannelCategory> mChannelCategoryList;
    private ChannelCategory mLastPlayChannelCategory;

    public ChannelCategoryAdapter(Context mContext, List<ChannelCategory> mChannelCategoryList) {
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mChannelCategoryDao = ChannelCategoryDao.getInstance(mContext);
        this.mChannelCategoryList = mChannelCategoryList;
        this.mLastPlayChannelCategory = mChannelCategoryDao.getLastPlay();
    }

    @Override
    public int getCount() {
        return mChannelCategoryList == null ? 0 : mChannelCategoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return mChannelCategoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChannelCategoryAdapter.ViewHolder holder;
        ChannelCategory channelCategory = mChannelCategoryList.get(position);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_channel_category, parent, false);
            holder = new ChannelCategoryAdapter.ViewHolder();
            holder.channelCategoryName = (TextView) convertView.findViewById(R.id.channel_category_name);
            holder.playing = (ViewStub) convertView.findViewById(R.id.category_playing);
            holder.channelCategory = channelCategory;
            convertView.setTag(holder);
        } else {
            holder = (ChannelCategoryAdapter.ViewHolder) convertView.getTag();
        }

        //ChannelCategory mLastPlayChannelCategory = mChannelCategoryDao.getLastPlay();
        if (channelCategory.equals(mLastPlayChannelCategory)) {
            convertView.setSelected(true);
            convertView.requestFocus();
            holder.playing.setVisibility(View.VISIBLE);
        } else {
            holder.playing.setVisibility(View.GONE);
        }

        holder.channelCategoryName.setText(channelCategory.getName());
        return convertView;
    }

    public int getPosition(ChannelCategory channel){
        int position = mChannelCategoryList.indexOf(channel);
        return position;
    }

    public static class ViewHolder {
        private TextView channelCategoryName;
        private ViewStub playing;

        private ChannelCategory channelCategory;

        public TextView getChannelCategoryName() {
            return channelCategoryName;
        }

        public void setChannelCategoryName(TextView channelCategoryName) {
            this.channelCategoryName = channelCategoryName;
        }

        public ViewStub getPlaying() {
            return playing;
        }

        public void setPlaying(ViewStub playing) {
            this.playing = playing;
        }

        public ChannelCategory getChannelCategory() {
            return channelCategory;
        }

        public void setChannelCategory(ChannelCategory channelCategory) {
            this.channelCategory = channelCategory;
        }
    }
}
