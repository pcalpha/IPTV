package cn.com.pcalpha.iptv.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.com.pcalpha.iptv.R;
import cn.com.pcalpha.iptv.setting.Setting;
import cn.com.pcalpha.iptv.setting.SettingOption;

/**
 * Created by caiyida on 2018/6/28.
 */

public class SettingAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private List<Setting> mSettingsList;


    public SettingAdapter(Context mContext, List<Setting> mSettingsList) {
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mSettingsList = mSettingsList;
    }

    @Override
    public int getCount() {
        return mSettingsList == null ? 0 : mSettingsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSettingsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Setting settings = mSettingsList.get(position);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_setting, parent, false);
            holder = new ViewHolder();
            holder.label = (TextView) convertView.findViewById(R.id.setting_label);
            holder.option = (TextView) convertView.findViewById(R.id.setting_option);
            holder.arrow = (TextView) convertView.findViewById(R.id.setting_arrow);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.label.setText(settings.getLabel());
        if (null != settings.getOptions()) {
            for (SettingOption option : settings.getOptions()) {
                if (settings.getValue().equals(option.getValue())) {
                    holder.option.setText(option.getLabel());
                }
            }
            holder.arrow.setVisibility(View.VISIBLE);
        } else {
            holder.option.setText(settings.getValue());
            holder.arrow.setVisibility(View.GONE);
        }
        return convertView;
    }

    public int getPosition(Setting setting) {
        int position = mSettingsList.indexOf(setting);
        return position;
    }

    public static class ViewHolder {
        private TextView label;
        private TextView option;
        private TextView arrow;
    }
}
