package cn.com.pcalpha.iptv.setting;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    private MenuListView settingOptionListView;
    private SettingOptionAdapter adapter;
    private FragmentSwitcher fragmentSwitcher;
    private OnOptionSelectedListener onOptionSelectedListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentSwitcher = new FragmentSwitcher(R.id.frame_menu_content_container, getFragmentManager());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_option, container, false);

        settingOptionListView = (MenuListView) view.findViewById(R.id.settings_option_list_view);
        final Setting setting = (Setting) getArguments().getSerializable("setting");

        adapter = new SettingOptionAdapter(this.getActivity(), setting.getOptions());

        settingOptionListView.setAdapter(adapter);
        settingOptionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SettingOptionAdapter adapter = (SettingOptionAdapter) parent.getAdapter();
                adapter.setSelectedPosition(position);
                SettingOption option = (SettingOption) adapter.getItem(position);
                setting.setValue(option.getValue());
                if (null != onOptionSelectedListener) {
                    onOptionSelectedListener.OnOptionSelected(setting);
                }
            }
        });

        settingOptionListView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != KeyEvent.ACTION_DOWN) {
                    return false;
                }
                if (KeyEvent.KEYCODE_BACK == keyCode || KeyEvent.KEYCODE_DPAD_LEFT == keyCode) {
                    //getFragmentManager().popBackStack();
                    if (null != onOptionSelectedListener) {
                        onOptionSelectedListener.OnOptionSelected(setting);
                    }
                    //fragmentSwitcher.showFragment(settingFragment);
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
                        SettingOptionAdapter adapter = (SettingOptionAdapter) settingOptionListView.getAdapter();
                        int position = adapter.getPosition(option);
                        adapter.setSelectedPosition(position);
                        settingOptionListView.setSelection(position);
                        settingOptionListView.requestFocus();
                    }
                }
            }
        } else {
            settingOptionListView.setSelection(0);
            settingOptionListView.requestFocus();
        }


    }

    @Override
    public void onResume() {
        super.onResume();

    }


    public OnOptionSelectedListener getOnOptionSelectedListener() {
        return onOptionSelectedListener;
    }

    public void setOnOptionSelectedListener(OnOptionSelectedListener onOptionSelectedListener) {
        this.onOptionSelectedListener = onOptionSelectedListener;
    }

    public interface OnOptionSelectedListener {
        public void OnOptionSelected(Setting setting);
    }

}
