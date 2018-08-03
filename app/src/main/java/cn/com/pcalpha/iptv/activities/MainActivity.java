///*
// * Copyright (C) 2014 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
// * in compliance with the License. You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software distributed under the License
// * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
// * or implied. See the License for the specific language governing permissions and limitations under
// * the License.
// */
//
//package cn.com.pcalpha.iptv.activities;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import cn.com.pcalpha.iptv.R;
//import cn.com.pcalpha.iptv.model.domain.Channel;
//import cn.com.pcalpha.iptv.service.ChannelService;
//
///*
// * MainActivity class that loads {@link MainFragment}.
// */
//public class MainActivity extends Activity {
//
//    private TextView channelNoTextView;
//
//    private ChannelService channelService;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        channelService = new ChannelService(this);
//
//        channelNoTextView = findViewById(R.id.channelNo);
//        channelNoTextView.setVisibility(View.INVISIBLE);
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_0 || keyCode == KeyEvent.KEYCODE_NUMPAD_0) {
//            inputChannelNoView(0);
//            return false;
//        } else if (keyCode == KeyEvent.KEYCODE_1 || keyCode == KeyEvent.KEYCODE_NUMPAD_1) {
//            inputChannelNoView(1);
//            return false;
//        } else if (keyCode == KeyEvent.KEYCODE_2 || keyCode == KeyEvent.KEYCODE_NUMPAD_2) {
//            inputChannelNoView(2);
//            return false;
//        } else if (keyCode == KeyEvent.KEYCODE_3 || keyCode == KeyEvent.KEYCODE_NUMPAD_3) {
//            inputChannelNoView(3);
//            return false;
//        } else if (keyCode == KeyEvent.KEYCODE_4 || keyCode == KeyEvent.KEYCODE_NUMPAD_4) {
//            inputChannelNoView(4);
//            return false;
//        } else if (keyCode == KeyEvent.KEYCODE_5 || keyCode == KeyEvent.KEYCODE_NUMPAD_5) {
//            inputChannelNoView(5);
//            return false;
//        } else if (keyCode == KeyEvent.KEYCODE_6 || keyCode == KeyEvent.KEYCODE_NUMPAD_6) {
//            inputChannelNoView(6);
//            return false;
//        } else if (keyCode == KeyEvent.KEYCODE_7 || keyCode == KeyEvent.KEYCODE_NUMPAD_7) {
//            inputChannelNoView(7);
//            return false;
//        } else if (keyCode == KeyEvent.KEYCODE_8 || keyCode == KeyEvent.KEYCODE_NUMPAD_8) {
//            inputChannelNoView(8);
//            return false;
//        } else if (keyCode == KeyEvent.KEYCODE_9 || keyCode == KeyEvent.KEYCODE_NUMPAD_9) {
//            inputChannelNoView(9);
//            return false;
//        } else {
//            return super.onKeyDown(keyCode, event);
//        }
//
//    }
//
//    private void inputChannelNoView(int channelNo) {
//        Integer lastChannelNo = channelNoTextView.getText().toString().isEmpty() ? 0 : Integer.parseInt(channelNoTextView.getText().toString());
//        final Integer newChannelNo = lastChannelNo * 10 + channelNo;
//        channelNoTextView.setText(String.valueOf(newChannelNo));
//        channelNoTextView.setVisibility(View.VISIBLE);
//
//        channelNoTextView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                final Integer currentChannelNo = channelNoTextView.getText().toString().isEmpty() ? 0 : Integer.parseInt(channelNoTextView.getText().toString());
//
//                if (newChannelNo == currentChannelNo) {
//                    channelNoTextView.setVisibility(View.INVISIBLE);
//                    channelNoTextView.setText(null);
//
//
//                    Channel channel = channelService.get(newChannelNo);
//                    if (null == channel) {
//                        Toast.makeText(getApplicationContext(), "无此频道", Toast.LENGTH_LONG).show();
//                        return;
//                    }
//                    Intent intent = new Intent(MainActivity.this, VideoActivity.class);
//                    intent.putExtra("CHANNEL", channel);
//
//                    getApplicationContext().startActivity(intent);
//                }
//            }
//        }, 3000);
//
//    }
//
//
//}
