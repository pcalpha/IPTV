package cn.com.pcalpha.iptv.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;

import cn.com.pcalpha.iptv.R;
import cn.com.pcalpha.iptv.adapter.ChannelAdapter;
import cn.com.pcalpha.iptv.model.bo.Param4Channel;
import cn.com.pcalpha.iptv.model.domain.Channel;
import cn.com.pcalpha.iptv.service.ChannelService;
import cn.com.pcalpha.iptv.view.MenuListView;

/**
 * Created by caiyida on 2018/5/7.
 */

public class ChannelFragment extends Fragment {

    private Activity mainActivity;
    private MenuListView channelView;
    private ChannelService channelService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = getActivity();
        channelService = ChannelService.getInstance(this.getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_list, container, false);

        String channelCategoryName = getArguments().getString("categoryName");
        Param4Channel param4Channel = Param4Channel.build().setCategoryName(channelCategoryName);
        List<Channel> channelList = channelService.find(param4Channel);

        ChannelAdapter channelAdapter = new ChannelAdapter(this.getActivity(), channelList);
        channelView = (MenuListView) view.findViewById(R.id.channel_list_view);
        channelView.setAdapter(channelAdapter);

        channelView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Channel lastPlay= channelService.getLastPlay();

                ChannelAdapter channelAdapter = (ChannelAdapter) parent.getAdapter();
                Channel channel = channelAdapter.getItem(position);
                if (channel.equals(lastPlay)) {
                    return;
                }
                changeChannel(channel);
                getFragmentManager().popBackStackImmediate();
            }
        });


        return view;
    }

    private void changeChannel(Channel channel) {
        Intent intent = new Intent("cn.com.pcalpha.iptv.broadcasereceiver.action.channelbroadcase");
        intent.putExtra("channel", channel);//向广播接收器传递数据
        getActivity().sendBroadcast(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        String categoryName = getArguments().getString("categoryName");
        Channel channel = channelService.getLastPlay();
        if (null != channel) {
            if (null != categoryName && !"".equals(categoryName)) {
                if (categoryName.equals(channel.getCategoryName())) {
                    ChannelAdapter adapter = (ChannelAdapter) channelView.getAdapter();
                    int position = adapter.getPosition(channel);
                    channelView.setSelection(position);
                    channelView.requestFocus();
                }
            }
        } else {
            channelView.setSelection(0);
            channelView.requestFocus();
        }

    }
}
