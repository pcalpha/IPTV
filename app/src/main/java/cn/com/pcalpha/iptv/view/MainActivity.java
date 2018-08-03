/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package cn.com.pcalpha.iptv.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import cn.com.pcalpha.iptv.R;
import cn.com.pcalpha.iptv.constants.FragmentTag;

/*
 * MainActivity class that loads {@link MainFragment}.
 */
public class MainActivity extends AppCompatActivity {

    private RelativeLayout videoPlayerContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews(savedInstanceState);
    }

    private void findViews(Bundle savedInstanceState) {
        videoPlayerContainer = findViewById(R.id.video_player_container);
    }

    private MenuFragment showMainMenuFragment() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.fragement_main_menu_container);
        MenuFragment mainFragment = new MenuFragment();
        if (null == fragment) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragement_main_menu_container, new MenuFragment(), FragmentTag.MAIN_MENU_FRAGMENT)
                    .addToBackStack(FragmentTag.MAIN_MENU_FRAGMENT)
                    .commit();
        }
        return mainFragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (KeyEvent.KEYCODE_DPAD_UP == keyCode) {
//            Channel channel = channelService.getLastAccess();
//            play(channel.getNext());
//        } else if (KeyEvent.KEYCODE_DPAD_DOWN == keyCode) {
//            Channel channel = channelService.getLastAccess();
//            play(channel.getPre());
//        } else if (KeyEvent.KEYCODE_DPAD_RIGHT == keyCode) {
//            Channel channel = channelService.getLastAccess();
//            channel.preSrcIndex();
//            play(channel);
//        } else if (KeyEvent.KEYCODE_DPAD_LEFT == keyCode) {
//            Channel channel = channelService.getLastAccess();
//            channel.nextSrcIndex();
//            play(channel);
        if (KeyEvent.KEYCODE_MENU == keyCode||KeyEvent.KEYCODE_ENTER==keyCode) {
            showMainMenuFragment();
        } else if (KeyEvent.KEYCODE_BACK == keyCode) {
            getFragmentManager().popBackStackImmediate();
        }
        return false;
    }


}
