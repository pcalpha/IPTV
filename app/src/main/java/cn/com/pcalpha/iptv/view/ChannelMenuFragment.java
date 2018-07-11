package cn.com.pcalpha.iptv.view;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.com.pcalpha.iptv.R;
import cn.com.pcalpha.iptv.adapter.ChannelAdapter;
import cn.com.pcalpha.iptv.widget.FocusFixedLinearLayoutManager;
import cn.com.pcalpha.iptv.model.Channel;

/**
 * Created by caiyida on 2018/5/7.
 */

public class ChannelMenuFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_list,container,false);


        final RecyclerView channelListView = (RecyclerView) view.findViewById(R.id.channel_list_view);
        final Activity mainActivity = getActivity();
        List<Channel> channelList = new ArrayList<>();
        for(int i=0;i<10;i++){
            Channel cc = new Channel();
            cc.setName("频道"+i);
            channelList.add(cc);
        }
        channelListView.setLayoutManager(new FocusFixedLinearLayoutManager(this.getActivity(),LinearLayoutManager.VERTICAL,false));
        channelListView.setAdapter(new ChannelAdapter(channelList,this));

        return view;
    }
}
