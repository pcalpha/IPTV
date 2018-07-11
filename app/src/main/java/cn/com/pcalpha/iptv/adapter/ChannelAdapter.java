package cn.com.pcalpha.iptv.adapter;

import android.app.Fragment;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.com.pcalpha.iptv.R;
import cn.com.pcalpha.iptv.model.Channel;

/**
 * Created by caiyida on 2018/4/7.
 */

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.AppRecycleHolder> {
    private Fragment fragment;
    private List<Channel> channelList;

    public ChannelAdapter(List<Channel> channelList, Fragment fragment) {
        this.channelList = channelList;
        this.fragment = fragment;
    }

    @Override
    public AppRecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //找到item的布局
        View view = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.item_channel, parent, false);

        return new AppRecycleHolder(view);//将布局设置给holder
    }

    @Override
    public void onBindViewHolder(AppRecycleHolder holder, final int position) {
        final Channel channel = channelList.get(position);
        holder.channelName.setText(channel.getName());
        holder.itemView.setFocusable(true);
        holder.itemView.setFocusableInTouchMode(true);
        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setBackgroundColor(Color.argb(255, 255, 0, 0));
                    v.requestFocus();
                } else {
                    v.setBackgroundColor(Color.argb(0, 0, 0, 0));
                    v.clearFocus();
                }
            }
        });
    }

    @Override
    public boolean onFailedToRecycleView(AppRecycleHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public int getItemCount() {
        return channelList.size();
    }

    class AppRecycleHolder extends RecyclerView.ViewHolder {
        TextView channelName;

        public AppRecycleHolder(View itemView) {
            super(itemView);
            channelName = (TextView) itemView.findViewById(R.id.channel_name);
        }
    }
}
