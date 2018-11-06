package cn.com.pcalpha.iptv.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import cn.com.pcalpha.iptv.R;
import cn.com.pcalpha.iptv.setting.SettingOption;

/**
 * Created by caiyida on 2018/6/28.
 */

public class SettingOptionAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private List<SettingOption> mOptionList;
    private int selectedPosition=0;

    public SettingOptionAdapter(Context mContext, List<SettingOption> settingsList) {
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mOptionList = settingsList;
    }

    @Override
    public int getCount() {
        return mOptionList == null ? 0 : mOptionList.size();
    }

    @Override
    public Object getItem(int position) {
        return mOptionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        SettingOption settingOption = mOptionList.get(position);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_setting_option, parent, false);
            holder = new ViewHolder();
            holder.label = (TextView) convertView.findViewById(R.id.setting_option_label);
            holder.radio = (RadioButton) convertView.findViewById(R.id.setting_option_radio);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.label.setText(settingOption.getLabel());

        if(selectedPosition==position){
            convertView.setSelected(true);
            convertView.requestFocus();
            holder.radio.setChecked(true);
        }else{
            holder.radio.setChecked(false);
        }

        return convertView;
    }

    public int getPosition(SettingOption option){
        int position = mOptionList.indexOf(option);
        return position;
    }

    public void setSelectedPosition(int position){
        selectedPosition = position;
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        private TextView label;
        private RadioButton radio;

    }
}
