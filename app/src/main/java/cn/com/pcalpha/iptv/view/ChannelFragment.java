package cn.com.pcalpha.iptv.view;

import android.app.Activity;
import android.app.Fragment;
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
import cn.com.pcalpha.iptv.widget.MenuListView;

/**
 * Created by caiyida on 2018/5/7.
 */

public class ChannelFragment extends Fragment {

    private Activity mainActivity;
    private MenuListView channelView ;
    private ChannelService channelService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = getActivity();
        channelService = new ChannelService(this.getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_list,container,false);

        String channelCategoryName = getArguments().getString("categoryName");
        Param4Channel param4Channel = Param4Channel.build().setCategoryName(channelCategoryName);
        List<Channel> channelList = channelService.find(param4Channel);

        ChannelAdapter channelAdapter = new ChannelAdapter(this.getActivity(),channelList);
        channelView = (MenuListView) view.findViewById(R.id.channel_list_view);
        channelView.setAdapter(channelAdapter);
        channelView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setSelection(1);
            }
        });

        channelView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChannelAdapter channelAdapter = (ChannelAdapter) parent.getAdapter();
                Channel channel = channelAdapter.getItem(position);
                channelService.setLastPlay(channel);

                getFragmentManager().popBackStackImmediate();
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        String channelCategoryName = getArguments().getString("categoryName");
        Channel channel = channelService.getLastPlay();
        if(null!=channelCategoryName&&!"".equals(channelCategoryName)&&null!=channel){
            if(channelCategoryName.equals(channel.getCategoryName())){
                ChannelAdapter adapter =(ChannelAdapter) channelView.getAdapter();
                int position = adapter.getPosition(channel);
                channelView.setSelection(position);
                channelView.requestFocus();
            }
        }
    }
}
