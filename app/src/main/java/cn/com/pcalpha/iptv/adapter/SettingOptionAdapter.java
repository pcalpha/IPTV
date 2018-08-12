package cn.com.pcalpha.iptv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import cn.com.pcalpha.iptv.R;
import cn.com.pcalpha.iptv.model.domain.Channel;
import cn.com.pcalpha.iptv.model.domain.Setting;
import cn.com.pcalpha.iptv.model.domain.SettingOption;

/**
 * Created by caiyida on 2018/6/28.
 */

public class SettingOptionAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<SettingOption> optionList;
    private int selectedPosition=0;

    public SettingOptionAdapter(Context mContext, List<SettingOption> settingsList) {
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.optionList = settingsList;
    }

    @Override
    public int getCount() {
        return optionList == null ? 0 : optionList.size();
    }

    @Override
    public Object getItem(int position) {
        return optionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        SettingOption settingOption = optionList.get(position);
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
        int position = optionList.indexOf(option);
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
