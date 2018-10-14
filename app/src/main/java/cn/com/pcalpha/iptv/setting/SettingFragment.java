package cn.com.pcalpha.iptv.setting;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import cn.com.pcalpha.iptv.R;
import cn.com.pcalpha.iptv.tools.FragmentSwitcher;
import cn.com.pcalpha.iptv.widget.MenuListView;

/**
 * Created by caiyida on 2018/7/23.
 */

public class SettingFragment extends Fragment implements SettingOptionFragment.OnOptionSelectedListener {

    private MenuListView mSettingListView;

    private FragmentSwitcher mFragmentSwitcher;
    private SettingOptionFragment mSettingOptionFragment;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettingOptionFragment = new SettingOptionFragment();
        mFragmentSwitcher = new FragmentSwitcher(R.id.frame_menu_content_container, getFragmentManager());
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        mSettingListView = (MenuListView) view.findViewById(R.id.setting_list_view);


        List<Setting> settingList = new ArrayList<>();

        //setting1
//        Setting setting1 = new Setting();
//        setting1.setLabel("网络运营商");
//        setting1.setValue("CTCC");
//
//        SettingOption option11 = new SettingOption();
//        option11.setLabel("移动");
//        option11.setValue("CMCC");
//        SettingOption option12 = new SettingOption();
//        option12.setLabel("联通");
//        option12.setValue("CUCC");
//        SettingOption option13 = new SettingOption();
//        option13.setLabel("电信");
//        option13.setValue("CTCC");
//        List<SettingOption> options = new ArrayList<>();
//        options.add(option11);
//        options.add(option12);
//        options.add(option13);
//        setting1.setOptions(options);
//
//        String settingCarrier = mSharedPreferences.getString("pref_key_carrier", "CMCC");
//        setting1.setKey("pref_key_carrier");
//        setting1.setValue(settingCarrier);

        //setting2
        Setting setting2 = new Setting();
        setting2.setLabel("解码方式");
        setting2.setValue("1");

        SettingOption option21 = new SettingOption();
        option21.setLabel("软解码");
        option21.setValue("false");
        SettingOption option22 = new SettingOption();
        option22.setLabel("硬解码");
        option22.setValue("true");
        List<SettingOption> options2 = new ArrayList<>();
        options2.add(option21);
        options2.add(option22);
        setting2.setOptions(options2);


        String usingMedisCodec = mSharedPreferences.getString("pref_key_using_media_codec", "true");
        setting2.setKey("pref_key_using_media_codec");
        setting2.setValue(usingMedisCodec);

        //settting3
        Setting setting3 = new Setting();
        setting3.setLabel("恢复默认设置");
        setting3.setValue("清空所有设置和缓存");

        //settingList.add(setting1);
        settingList.add(setting2);
        settingList.add(setting3);


        final SettingAdapter adapter = new SettingAdapter(this.getActivity(), settingList);
        mSettingListView.setAdapter(adapter);

        mSettingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SettingAdapter adapter = (SettingAdapter) parent.getAdapter();
                Setting setting = (Setting) adapter.getItem(position);
                if ("pref_key_carrier".equals(setting.getKey()) ||
                        "pref_key_using_media_codec".equals(setting.getKey())) {
                    showSettingOptionFragment(setting);
                }

            }
        });

        mSettingListView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != KeyEvent.ACTION_DOWN) {
                    return false;
                }
                if (KeyEvent.KEYCODE_DPAD_RIGHT == keyCode) {
                    if (mSettingListView.isFocused()) {
                        int position = mSettingListView.getSelectedItemPosition();
                        Setting setting = (Setting) mSettingListView.getAdapter().getItem(position);
                        showSettingOptionFragment(setting);
                        return true;
                    }

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
    }

    @Override
    public void onResume() {
        super.onResume();
    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (hidden) {   // 不在最前端显示 相当于调用了onPause();
//
//        }else{  // 在最前端显示 相当于调用了onResume();
//            mSettingListView.requestFocus();
//        }
//    }

//    private void getFocus() {
//        View v = getView();
//        getView().setFocusableInTouchMode(true);
//        getView().setFocusable(true);
//        getView().requestFocus();
//        getView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (KeyEvent.KEYCODE_DPAD_RIGHT == keyCode) {
//                    if(mSettingListView.isFocused()){
//                        int position = mSettingListView.getSelectedItemPosition();
//                        Setting setting = (Setting) mSettingListView.getAdapter().getItem(position);
//                        showSettingOptionFragment(setting);
//                        return true;
//                    }
//
//                }
//                return false;
//            }
//        });
//    }

    public void showSettingOptionFragment(Setting setting) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("setting", setting);
        SettingOptionFragment settingOptionFragment = new SettingOptionFragment();
        settingOptionFragment.setArguments(bundle);
        settingOptionFragment.setOnOptionSelectedListener(this);
        //mFragmentSwitcher.addFragment(mSettingOptionFragment, "setting", false);
        mFragmentSwitcher.showFragment(settingOptionFragment);
    }

//    public void showSettingFragment() {
//        mFragmentSwitcher.showFragment(this);
//        mSettingListView.requestFocus();
//    }

    @Override
    public void OnOptionSelected(Setting setting) {
        if (null != setting) {
            SettingAdapter adapter = (SettingAdapter) mSettingListView.getAdapter();
            int position = adapter.getPosition(setting);

            mSettingListView.setSelection(position);
            adapter.notifyDataSetChanged();

            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(setting.getKey(), setting.getValue());
            editor.commit();
        }
        mFragmentSwitcher.showFragment(this);
        mSettingListView.requestFocus();
    }
}
