package cn.com.pcalpha.iptv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.com.pcalpha.iptv.R;
import cn.com.pcalpha.iptv.model.domain.Channel;
import cn.com.pcalpha.iptv.model.domain.Setting;
import cn.com.pcalpha.iptv.model.domain.SettingOption;

/**
 * Created by caiyida on 2018/6/28.
 */

public class SettingAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Setting> settingsList;


    public SettingAdapter(Context mContext, List<Setting> settingsList) {
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.settingsList = settingsList;
    }

    @Override
    public int getCount() {
        return settingsList == null ? 0 : settingsList.size();
    }

    @Override
    public Object getItem(int position) {
        return settingsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Setting settings = settingsList.get(position);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_setting, parent, false);
            holder = new ViewHolder();
            holder.label = (TextView) convertView.findViewById(R.id.setting_label);
            holder.option = (TextView)convertView.findViewById(R.id.setting_option);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.label.setText(settings.getLabel());
        if(null!=settings.getOptions()){
            for(SettingOption option:settings.getOptions()){
                if(settings.getValue().equals(option.getValue())){
                    holder.option.setText(option.getLabel());
                }
            }
        }

        return convertView;
    }

    public int getPosition(Setting setting){
        int position = settingsList.indexOf(setting);
        return position;
    }

    public static class ViewHolder {
        private TextView label;
        private TextView option;
        private TextView value;
    }
}
