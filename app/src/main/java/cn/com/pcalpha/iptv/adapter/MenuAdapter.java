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
import cn.com.pcalpha.iptv.constants.MenuCode;
import cn.com.pcalpha.iptv.model.Menu;
import cn.com.pcalpha.iptv.view.ChannelCategoryMenuFragment;
import cn.com.pcalpha.iptv.view.UploadFragment;

/**
 * Created by caiyida on 2018/4/7.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.AppRecycleHolder> {
    private Fragment fragment;
    private List<Menu> menuList;

    public MenuAdapter(List<Menu> menuList, Fragment fragment) {
        this.menuList = menuList;
        this.fragment = fragment;
    }

    @Override
    public AppRecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //找到item的布局
        View view = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.item_main_menu, parent, false);

        return new AppRecycleHolder(view);//将布局设置给holder
    }

    @Override
    public void onBindViewHolder(AppRecycleHolder holder, final int position) {
        final Menu menu = menuList.get(position);
        holder.menuName.setText(menu.getName());
        holder.itemView.setFocusable(true);
        holder.itemView.setFocusableInTouchMode(true);
        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setBackgroundColor(Color.argb(255, 255, 0, 0));
                    v.requestFocus();

                    if (MenuCode.CHANNEL == menu.getCode()) {
                        showChannelCategoryFragment();
                    } else if (MenuCode.UPLOAD == menu.getCode()) {
                        showUploadFragment();
                    } else if (MenuCode.SETTINGS == menu.getCode()) {

                    }
                } else {
                    v.setBackgroundColor(Color.argb(0, 0, 0, 0));
                    v.clearFocus();
                }


            }
        });
    }


    @Override
    public boolean onFailedToRecycleView(MenuAdapter.AppRecycleHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    private void showChannelCategoryFragment() {
        fragment.getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_menu_content_container, new ChannelCategoryMenuFragment())
                .commit();
    }

    private void showUploadFragment() {
        fragment.getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_menu_content_container, new UploadFragment())
                .commit();
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    class AppRecycleHolder extends RecyclerView.ViewHolder {
        TextView menuName;

        public AppRecycleHolder(View itemView) {
            super(itemView);
            itemView = itemView;
            menuName = (TextView) itemView.findViewById(R.id.menu_name);
        }
    }
}
