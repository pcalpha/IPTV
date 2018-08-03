package cn.com.pcalpha.iptv.view;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import cn.com.pcalpha.iptv.widget.FragmentSwitcher;
import cn.com.pcalpha.iptv.widget.MenuListView;

/**
 * Created by caiyida on 2018/5/6.
 */

public class MenuFragment extends Fragment {
    private Activity mainActivity;
    private FrameLayout fragementMenuContainer;
    private ChannelCategoryFragment channelCategoryFragment;
    private SettingsFragment settingsFragment;
    private UploadFragment uploadFragment;
    private FragmentSwitcher fragmentSwitcher;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        channelCategoryFragment = new ChannelCategoryFragment();
        settingsFragment = new SettingsFragment();
        uploadFragment = new UploadFragment();
        fragmentSwitcher = new FragmentSwitcher(R.id.frame_menu_content_container, getFragmentManager());
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
                    showSettingsFragment();
                } else if (Menu.MenuCode.UPLOAD == holder.getMenu().getCode()) {
                    showUploadFragment();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.getChildCount();
            }
        });

        return view;
    }

    public void showChannelCategoryFragment() {
        fragmentSwitcher.showFragment(channelCategoryFragment);
    }

    public void showSettingsFragment() {
        fragmentSwitcher.showFragment(settingsFragment);
    }

    public void showUploadFragment() {
        fragmentSwitcher.showFragment(uploadFragment);
    }


}
