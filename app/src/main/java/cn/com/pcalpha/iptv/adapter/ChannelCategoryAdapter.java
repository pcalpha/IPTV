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
import cn.com.pcalpha.iptv.model.ChannelCategory;
import cn.com.pcalpha.iptv.view.ChannelMenuFragment;

/**
 * Created by caiyida on 2018/4/7.
 */

public class ChannelCategoryAdapter extends RecyclerView.Adapter<ChannelCategoryAdapter.AppRecycleHolder> {
    private Fragment fragment;
    private List<ChannelCategory> channelCategoryList;

    public ChannelCategoryAdapter(List<ChannelCategory> channelCategoryList, Fragment fragment) {
        this.channelCategoryList = channelCategoryList;
        this.fragment = fragment;
    }

    @Override
    public AppRecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //找到item的布局
        View view = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.item_channel_category, parent, false);

        return new AppRecycleHolder(view);//将布局设置给holder
    }

    @Override
    public void onBindViewHolder(AppRecycleHolder holder, final int position) {
        final ChannelCategory channelCategory = channelCategoryList.get(position);
        holder.channelCategoryName.setText(channelCategory.getName());
        holder.itemView.setFocusable(true);
        holder.itemView.setFocusableInTouchMode(true);
        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                RecyclerView parent = (RecyclerView) v.getParent();
                if (hasFocus) {
                    v.setBackgroundColor(Color.argb(255, 255, 0, 0));
                    v.requestFocus();

                    showChannelFragment();
                } else {
                    v.setBackgroundColor(Color.argb(0, 0, 0, 0));
                    v.clearFocus();
                }

            }
        });


    }

    private void showChannelFragment() {
        fragment.getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_channel_list_container, new ChannelMenuFragment())
                //.addToBackStack(FragmentTag.CHANNEL_FRAGMENT)
                .commit();

    }


    @Override
    public boolean onFailedToRecycleView(ChannelCategoryAdapter.AppRecycleHolder holder) {
        return super.onFailedToRecycleView(holder);
    }


    @Override
    public int getItemCount() {
        return channelCategoryList.size();
    }

    class AppRecycleHolder extends RecyclerView.ViewHolder {
        TextView channelCategoryName;

        public AppRecycleHolder(View itemView) {
            super(itemView);
            channelCategoryName = (TextView) itemView.findViewById(R.id.channel_category_name);
        }
    }
}
