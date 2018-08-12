package cn.com.pcalpha.iptv.fragment;

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
import cn.com.pcalpha.iptv.adapter.ChannelCategoryAdapter;
import cn.com.pcalpha.iptv.model.domain.ChannelCategory;
import cn.com.pcalpha.iptv.service.ChannelCategoryService;
import cn.com.pcalpha.iptv.tools.FragmentSwitcher;
import cn.com.pcalpha.iptv.view.MenuListView;

/**
 * Created by caiyida on 2018/5/7.
 */

public class ChannelCategoryFragment extends Fragment {
    private static final String TAG = "ChannelCategoryFragment";

    private Activity mainActivity;
    private MenuListView channelCategoryView;
    private ChannelCategoryService channelCategoryService;
    private FragmentSwitcher fragmentSwitcher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = getActivity();
        channelCategoryService = ChannelCategoryService.getInstance(this.getActivity());
        fragmentSwitcher = new FragmentSwitcher(R.id.frame_channel_list_container, getFragmentManager());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_category, container, false);

        List<ChannelCategory> channelCategoryList = channelCategoryService.findAll();

        for (ChannelCategory channelCategory : channelCategoryList) {
            String categoryName = channelCategory.getName();

            Bundle bundle = new Bundle();
            bundle.putString("categoryName", categoryName);
            ChannelFragment channelFragment = new ChannelFragment();
            channelFragment.setArguments(bundle);

            fragmentSwitcher.addFragment(channelFragment, categoryName,false);
        }
//        List<ChannelCategory> channelCategoryList = new ArrayList<>();
//        for(int i=0;i<20;i++){
//            ChannelCategory channelDO= new ChannelCategory();
//            channelDO.setName("频道名称");
//            channelCategoryList.add(channelDO);
//        }

        ChannelCategoryAdapter channelCategoryAdapter = new ChannelCategoryAdapter(getActivity(), channelCategoryList);
        channelCategoryView = (MenuListView) view.findViewById(R.id.channel_category_list_view);
        channelCategoryView.setAdapter(channelCategoryAdapter);
        channelCategoryView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ChannelCategoryAdapter adapter = (ChannelCategoryAdapter)parent.getAdapter();
                ChannelCategory channelCategory = (ChannelCategory)adapter.getItem(position);
                if (null != channelCategory) {
                    showChannelFragment(channelCategory.getName());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //parent.getSelectedView().setBackgroundColor(Color.argb(0, 0, 0, 0));
            }
        });

        channelCategoryView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChannelCategoryAdapter adapter = (ChannelCategoryAdapter)parent.getAdapter();
                ChannelCategory channelCategory = (ChannelCategory)adapter.getItem(position);
                if (null != channelCategory) {
                    showChannelFragment(channelCategory.getName());
                }
            }
        });

        //channelCategoryView.setSelection(1);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ChannelCategory channelCategory = channelCategoryService.getLastPlay();
        if (null != channelCategory) {
            ChannelCategoryAdapter adapter = (ChannelCategoryAdapter) channelCategoryView.getAdapter();
            int position = adapter.getPosition(channelCategory);
            channelCategoryView.setSelection(position);
            //channelCategoryView.requestFocus();
        } else {
            channelCategoryView.setSelection(0);
        }
    }


    public void showChannelFragment(String categoryName) {
        ChannelFragment channelFragment = (ChannelFragment) fragmentSwitcher.getFragment(categoryName);
        fragmentSwitcher.showFragment(channelFragment);
    }
}
