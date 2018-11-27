package cn.com.pcalpha.iptv.channel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;

import cn.com.pcalpha.iptv.R;
import cn.com.pcalpha.iptv.common.Action;
import cn.com.pcalpha.iptv.widget.MenuListView;

/**
 * Created by caiyida on 2018/5/7.
 */

public class ChannelFragment extends Fragment {

    private Context mContext;
    private MenuListView mChannelView;
    private ChannelDao mChannelDao;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mChannelDao = ChannelDao.getInstance(this.getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_list, container, false);

        String channelCategoryName = getArguments().getString("categoryName");
        Param4Channel param4Channel = Param4Channel.build().setCategoryName(channelCategoryName);
        List<Channel> channelList = mChannelDao.find(param4Channel);

        ChannelAdapter channelAdapter = new ChannelAdapter(this.getActivity(), channelList);
        mChannelView = (MenuListView) view.findViewById(R.id.channel_list_view);
        mChannelView.setAdapter(channelAdapter);

        mChannelView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Channel lastPlay= mChannelDao.getLastPlay();
//
                ChannelAdapter channelAdapter = (ChannelAdapter) parent.getAdapter();
                Channel channel = channelAdapter.getItem(position);
//                if (channel.equals(lastPlay)) {
//                    getFragmentManager().popBackStackImmediate();
//                    return;
//                }
                playChannel(channel);
                getFragmentManager().popBackStackImmediate();
            }
        });


        return view;
    }

    private void playChannel(Channel channel) {
        Intent intent = new Intent(Action.PLAY_CHANNEL_ACTION);
        intent.putExtra("channel", channel);//向广播接收器传递数据
        getActivity().sendBroadcast(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        String categoryName = getArguments().getString("categoryName");
        Channel channel = mChannelDao.getLastPlay();
        if (null != channel) {
            if (null != categoryName && !"".equals(categoryName)) {
                if (categoryName.equals(channel.getCategoryName())) {
                    ChannelAdapter adapter = (ChannelAdapter) mChannelView.getAdapter();
                    int position = adapter.getPosition(channel);
                    mChannelView.setSelection(position);
                    mChannelView.requestFocus();
                }
            }
        } else {
            mChannelView.setSelection(0);
            mChannelView.requestFocus();
        }

    }
}
