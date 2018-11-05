package cn.com.pcalpha.iptv.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import cn.com.pcalpha.iptv.R;
import cn.com.pcalpha.iptv.tools.FragmentSwitcher;
import cn.com.pcalpha.iptv.widget.MenuListView;

/**
 * Created by caiyida on 2018/7/23.
 */

public class SettingOptionFragment extends Fragment {

    private MenuListView mSettingOptionListView;
    private SettingOptionAdapter mAdapter;
    private FragmentSwitcher mFragmentSwitcher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentSwitcher = new FragmentSwitcher(R.id.frame_menu_content_container, getFragmentManager());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_option, container, false);

        mSettingOptionListView = (MenuListView) view.findViewById(R.id.settings_option_list_view);
        final Setting setting = (Setting) getArguments().getSerializable("setting");

        mAdapter = new SettingOptionAdapter(this.getActivity(), setting.getOptions());

        mSettingOptionListView.setAdapter(mAdapter);
        mSettingOptionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SettingOptionAdapter adapter = (SettingOptionAdapter) parent.getAdapter();
                adapter.setSelectedPosition(position);
                SettingOption option = (SettingOption) adapter.getItem(position);
                setting.setValue(option.getValue());

                SettingFragment settingFragment =
                        (SettingFragment)getFragmentManager().
                                findFragmentByTag("settingFragment");
                if (null != settingFragment) {
                    settingFragment.setSelectedSetting(setting);
                }
            }
        });

        mSettingOptionListView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != KeyEvent.ACTION_DOWN) {
                    return false;
                }
                if (KeyEvent.KEYCODE_BACK == keyCode || KeyEvent.KEYCODE_DPAD_LEFT == keyCode) {
                    //getFragmentManager().popBackStack();

                    SettingFragment settingFragment =
                            (SettingFragment)getFragmentManager().
                                    findFragmentByTag("settingFragment");
                    if (null != settingFragment) {
                        settingFragment.setSelectedSetting(setting);
                    }
                    //mFragmentSwitcher.showFragment(settingFragment);
                    return true;
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Setting setting = (Setting) getArguments().getSerializable("setting");
        if (null != setting) {
            if (null != setting.getOptions()) {
                for (SettingOption option : setting.getOptions()) {
                    if (setting.getValue().equals(option.getValue())) {
                        SettingOptionAdapter adapter = (SettingOptionAdapter) mSettingOptionListView.getAdapter();
                        int position = adapter.getPosition(option);
                        adapter.setSelectedPosition(position);
                        mSettingOptionListView.setSelection(position);
                        mSettingOptionListView.requestFocus();
                    }
                }
            }
        } else {
            mSettingOptionListView.setSelection(0);
            mSettingOptionListView.requestFocus();
        }


    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
