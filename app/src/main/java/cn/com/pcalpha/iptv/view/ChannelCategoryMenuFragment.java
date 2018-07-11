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
import cn.com.pcalpha.iptv.adapter.ChannelCategoryAdapter;
import cn.com.pcalpha.iptv.widget.FocusFixedLinearLayoutManager;
import cn.com.pcalpha.iptv.model.ChannelCategory;

/**
 * Created by caiyida on 2018/5/7.
 */

public class ChannelCategoryMenuFragment extends Fragment {

    private RecyclerView channelCategoryView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_category,container,false);

        final RecyclerView channelCategoryView = (RecyclerView) view.findViewById(R.id.channel_category_list_view);
        final Activity mainActivity = getActivity();

        List<ChannelCategory> channelCategoryList = new ArrayList<>();
        for(int i=0;i<20;i++){
            ChannelCategory cc = new ChannelCategory();
            cc.setName("分类"+i);
            channelCategoryList.add(cc);
        }
        channelCategoryView.setLayoutManager(new FocusFixedLinearLayoutManager(this.getActivity(),LinearLayoutManager.VERTICAL,false));
        channelCategoryView.setAdapter(new ChannelCategoryAdapter(channelCategoryList,this));

        channelCategoryView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){

                }else{

                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //addChannelFragment();
    }


}
