package cn.com.pcalpha.iptv.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.com.pcalpha.iptv.R;

/**
 * Created by caiyida on 2018/6/28.
 */

public class MenuAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private Context mContext;

    private List<Menu> mMenuList;

    public MenuAdapter(Context mContext, List<Menu> mMenuList) {
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mMenuList = mMenuList;
    }

    @Override
    public int getCount() {
        return mMenuList == null ? 0 : mMenuList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMenuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Menu menu = mMenuList.get(position);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_menu, parent, false);
            holder = new ViewHolder();
            holder.menuName = (TextView) convertView.findViewById(R.id.menu_name);
            holder.menu = menu;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.menuName.setText(menu.getName());
        holder.menu = menu;

        return convertView;
    }

    public static class ViewHolder {
        private TextView menuName;
        private Menu menu;

        public TextView getMenuName() {
            return menuName;
        }

        public void setMenuName(TextView menuName) {
            this.menuName = menuName;
        }

        public Menu getMenu() {
            return menu;
        }

        public void setMenu(Menu menu) {
            this.menu = menu;
        }
    }
}
