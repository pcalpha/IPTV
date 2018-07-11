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
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import cn.com.pcalpha.iptv.R;
import cn.com.pcalpha.iptv.constants.FragmentTag;
import cn.com.pcalpha.iptv.model.Channel;
import cn.com.pcalpha.iptv.service.ChannelService;
import tv.danmaku.ijk.example.widget.media.IjkVideoView;

/*
 * MainActivity class that loads {@link MainFragment}.
 */
public class MainActivity extends AppCompatActivity {

    private RelativeLayout relativeVideoPlayerContainer;
    private FrameLayout fragementMainMenuContainer;
    private IjkVideoView videoView;

    private ChannelService channelService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        channelService = new ChannelService(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        findViews(savedInstanceState);
    }

    private void findViews(Bundle savedInstanceState) {
        //relativeVideoPlayerContainer = findViewById(R.id.relative_video_player_container);
        videoView = findViewById(R.id.video_view);
        videoView.setFocusable(false);
    }

    private void showMainMenuFragment() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.fragement_main_menu_container);
        if (null == fragment) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragement_main_menu_container, new MainMenuFragment(), FragmentTag.MAIN_MENU_FRAGMENT)
                    .addToBackStack(FragmentTag.MAIN_MENU_FRAGMENT)
                    .commit();
        }
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

    private void play(Channel channel) {
        if (null != channel) {
            videoView.release(true);
            videoView.setVideoURI(Uri.parse(channel.getSrc()));
            videoView.start();

            //showChannelNoView(channel.getNo());
            Toast.makeText(this, channel.getName(), Toast.LENGTH_LONG).show();

            channelService.setLastAccess(channel.getNo());
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //videoView.stopPlayback();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //videoView.release(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //videoView.resume();
        //play(currentChannel);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //currentChannel = (Channel) intent.getSerializableExtra("CHANNEL");
    }


}
