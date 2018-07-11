package cn.com.pcalpha.iptv.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.pcalpha.iptv.R;
import cn.com.pcalpha.iptv.adapter.MenuAdapter;
import cn.com.pcalpha.iptv.constants.MenuCode;
import cn.com.pcalpha.iptv.widget.FocusFixedLinearLayoutManager;
import cn.com.pcalpha.iptv.model.Menu;

/**
 * Created by caiyida on 2018/5/6.
 */

public class MainMenuFragment extends Fragment {

    private FrameLayout fragementMainMenuContainer;
    private TextView menuItemChannel;
    private TextView menuItemSettings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_menu,container,false);

        final RecyclerView menuListView = (RecyclerView) view.findViewById(R.id.menu_list_view);

        List<Menu> menuList = new ArrayList<>();

        menuList.add(new Menu(MenuCode.CHANNEL,"频道"));
        menuList.add(new Menu(MenuCode.UPLOAD,"上传"));
        //menuList.add(new Menu(MenuCode.SETTINGS,"设置"));

        menuListView.setLayoutManager(new FocusFixedLinearLayoutManager(this.getActivity(),LinearLayoutManager.VERTICAL,false));
        menuListView.setAdapter(new MenuAdapter(menuList,this));

        menuListView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){

                }else{

                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }




}
