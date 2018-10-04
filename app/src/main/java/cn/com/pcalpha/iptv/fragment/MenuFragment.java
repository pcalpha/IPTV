package cn.com.pcalpha.iptv.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import cn.com.pcalpha.iptv.R;
import cn.com.pcalpha.iptv.adapter.MenuAdapter;
import cn.com.pcalpha.iptv.model.domain.Menu;
import cn.com.pcalpha.iptv.model.domain.Setting;
import cn.com.pcalpha.iptv.tools.FragmentSwitcher;
import cn.com.pcalpha.iptv.view.MenuListView;

/**
 * Created by caiyida on 2018/5/6.
 */

public class MenuFragment extends Fragment {
    private Activity mainActivity;
    private FrameLayout fragementMenuContainer;
    private ChannelCategoryFragment channelCategoryFragment;
    private SettingFragment settingFragment;
    private UploadFragment uploadFragment;
    private FragmentSwitcher fragmentSwitcher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentSwitcher = new FragmentSwitcher(R.id.frame_menu_content_container, getFragmentManager());

        channelCategoryFragment = new ChannelCategoryFragment();
        settingFragment = new SettingFragment();
        uploadFragment = new UploadFragment();

        fragmentSwitcher.addFragment(channelCategoryFragment,"channelCategoryFragment",false);
        fragmentSwitcher.addFragment(settingFragment,"settingFragment",false);
        fragmentSwitcher.addFragment(uploadFragment,"uploadFragment",false);
        mainActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        final MenuListView menuListView = (MenuListView) view.findViewById(R.id.menu_list_view);

        List<Menu> menuList = new ArrayList<>();

        menuList.add(new Menu(Menu.MenuCode.CHANNEL, "频道"));
        menuList.add(new Menu(Menu.MenuCode.SETTINGS, "设置"));
        menuList.add(new Menu(Menu.MenuCode.UPLOAD, "上传"));
        //menuList.add(new Menu(MenuCode.SETTINGS,"设置"));

        menuListView.setSelected(true);
        menuListView.setVerticalScrollBarEnabled(false);
        menuListView.setAdapter(new MenuAdapter(this.getActivity(), menuList));
        menuListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //view.requestFocus();

                MenuAdapter.ViewHolder holder = (MenuAdapter.ViewHolder) view.getTag();
                if (Menu.MenuCode.CHANNEL == holder.getMenu().getCode()) {
                    showChannelCategoryFragment();
                } else if (Menu.MenuCode.SETTINGS == holder.getMenu().getCode()) {
                    showSettingFragment();
                } else if (Menu.MenuCode.UPLOAD == holder.getMenu().getCode()) {
                    showUploadFragment();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.getChildCount();
            }
        });

        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuAdapter.ViewHolder holder = (MenuAdapter.ViewHolder) view.getTag();
                if (Menu.MenuCode.CHANNEL == holder.getMenu().getCode()) {
                    showChannelCategoryFragment();
                } else if (Menu.MenuCode.SETTINGS == holder.getMenu().getCode()) {
                    showSettingFragment();
                } else if (Menu.MenuCode.UPLOAD == holder.getMenu().getCode()) {
                    showUploadFragment();
                }
            }
        });

//        menuListView.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (KeyEvent.KEYCODE_DPAD_RIGHT == keyCode) {
//                    return true;
//                }
//                return false;
//            }
//        });
        menuListView.requestFocus();
        return view;
    }

    public void showChannelCategoryFragment() {
        fragmentSwitcher.showFragment(channelCategoryFragment);
    }

    public void showSettingFragment() {
        fragmentSwitcher.showFragment(settingFragment);
    }

    public void showUploadFragment() {
        fragmentSwitcher.showFragment(uploadFragment);
    }



}
