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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import cn.com.pcalpha.iptv.R;
import cn.com.pcalpha.iptv.constants.FragmentTag;
import cn.com.pcalpha.iptv.model.domain.Channel;
import tv.danmaku.ijk.media.example.widget.media.AndroidMediaController;
import tv.danmaku.ijk.media.example.widget.media.IjkVideoView;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/*
 * MainActivity class that loads {@link MainFragment}.
 */
public class MainActivity extends AppCompatActivity {

    private IjkVideoView videoView;
    private ChannelReceiver channelReceiver;
    private Channel currentChannel;

    private static final String receiverAction = "cn.com.pcalpha.iptv.broadcasereceiver.action.channelbroadcase";

    static {
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
    }

    private void initReceiver(){
        channelReceiver = new ChannelReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(receiverAction);
        registerReceiver(channelReceiver, intentFilter);
    }

    private void initViews(Bundle savedInstanceState) {
        videoView = findViewById(R.id.video_view);
        //AndroidMediaController controller = new AndroidMediaController(this, false);
        videoView.setMediaController(null);
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showMainMenuFragment();
                return false;
            }
        });
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMainMenuFragment();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews(savedInstanceState);

        initReceiver();

    }

    @Override
    protected void onStop() {
        super.onStop();
        videoView.release(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        play(currentChannel);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(channelReceiver);
    }

    //
//    @Override
//    protected void onNewIntent(Intent intent){
//        super.onNewIntent(intent);
//        currentChannel = (Channel) intent.getSerializableExtra("CHANNEL");
//    }

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

    private void play(Channel channel) {
        if (null != channel) {
            currentChannel = channel;

            videoView.release(true);
            videoView.setVideoURI(Uri.parse(channel.getSrc()));
            videoView.start();

            Toast.makeText(this, channel.getName(), Toast.LENGTH_LONG).show();
        }
    }



    class ChannelReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            Channel channel = (Channel) bundle.get("channel");
            play(channel);
        }
    }
}
