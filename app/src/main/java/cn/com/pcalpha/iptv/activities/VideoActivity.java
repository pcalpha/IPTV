//package cn.com.pcalpha.iptv.activities;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.content.pm.ActivityInfo;
//import android.content.res.Configuration;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.os.PowerManager;
//import android.support.annotation.RequiresApi;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.Menu;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.TableLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import cn.com.pcalpha.iptv.R;
//import cn.com.pcalpha.iptv.model.Channel;
//import cn.com.pcalpha.iptv.service.ChannelService;
//import tv.danmaku.ijk.media.player.IjkMediaPlayer;
//import tv.danmaku.ijk.example.widget.media.AndroidMediaController;
//import tv.danmaku.ijk.example.widget.media.IjkVideoView;
//
//public class VideoActivity extends AppCompatActivity {
//    private String TAG = "VideoActivity";
//
//    private IjkVideoView videoView;
//    private TextView showChannelNoTextView;
//    private TextView inputChannelNoTextView;
//    private ActionBar mActionBar;
//    private Handler showChannelNoHandler = new Handler();
//    private Handler inputChannelNoHandler = new Handler();
//    //private TableLayout mHudView;
//
//    private ChannelService channelService;
//    private Channel currentChannel;
//
//    static {
//        IjkMediaPlayer.loadLibrariesOnce(null);
//        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_video);
//
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
//        //mHudView = (TableLayout) findViewById(R.id.hud_view);
//        videoView = (IjkVideoView) findViewById(R.id.ijkPlayer);
//        showChannelNoTextView = (TextView) findViewById(R.id.showChannelNo);
//        inputChannelNoTextView = (TextView) findViewById(R.id.inputChannelNo);
//
//
//        //videoView.setHudView(mHudView);
//        channelService = new ChannelService(this);
//        currentChannel = (Channel) getIntent().getSerializableExtra("CHANNEL");
//
//        AndroidMediaController controller = new AndroidMediaController(this, false);
//        videoView.setMediaController(controller);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//    }
//
//    private void play(Channel channel) {
//        if (null != channel) {
//            currentChannel = channel;
//
//            videoView.release(true);
//            videoView.setVideoURI(Uri.parse(channel.getSrc()));
//            videoView.start();
//
//            showChannelNoView(channel.getNo());
//            Toast.makeText(this, channel.getName(), Toast.LENGTH_LONG).show();
//
//            channelService.setLastAccess(channel.getNo());
//        }
//    }
//
//    private void showChannelNoView(int channelNo) {
//        inputChannelNoTextView.setVisibility(View.INVISIBLE);
//
//        showChannelNoTextView.setText(String.valueOf(channelNo));
//        showChannelNoTextView.setVisibility(View.VISIBLE);
//
//        showChannelNoHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                showChannelNoTextView.setVisibility(View.INVISIBLE);
//            }
//        }, 3000);
//    }
//
//    private void inputChannelNoView(int channelNo) {
//        showChannelNoTextView.setVisibility(View.INVISIBLE);
//
//        Integer lastChannelNo = inputChannelNoTextView.getText().toString().isEmpty() ? 0 : Integer.parseInt(inputChannelNoTextView.getText().toString());
//        final Integer newChannelNo = lastChannelNo * 10 + channelNo;
//        inputChannelNoTextView.setText(String.valueOf(newChannelNo));
//        inputChannelNoTextView.setVisibility(View.VISIBLE);
//
//        inputChannelNoHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                final Integer currentChannelNo = inputChannelNoTextView.getText().toString().isEmpty() ? 0 : Integer.parseInt(inputChannelNoTextView.getText().toString());
//
//                if (newChannelNo == currentChannelNo) {
//                    inputChannelNoTextView.setVisibility(View.INVISIBLE);
//                    inputChannelNoTextView.setText(null);
//
//                    Channel channel = channelService.get(newChannelNo);
//                    if (null == channel) {
//                        Toast.makeText(getApplicationContext(), "无此频道", Toast.LENGTH_LONG).show();
//                        return;
//                    }
//                    play(channel);
//                }
//            }
//        }, 3000);
//
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
//            play(channelService.next());
//
//            return false;
//        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
//            play(channelService.pre());
//
//            return false;
//        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
//            channelService.nextSrc(currentChannel);
//            play(currentChannel);
//
//            Toast.makeText(this, "切换到源" + (currentChannel.getSrcIndex()), Toast.LENGTH_LONG).show();
//            return false;
//        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
//            channelService.preSrc(currentChannel);
//            play(currentChannel);
//
//            Toast.makeText(this, "切换到源" + (currentChannel.getSrcIndex()), Toast.LENGTH_LONG).show();
//            return false;
//        } else if (keyCode == KeyEvent.KEYCODE_0 || keyCode == KeyEvent.KEYCODE_NUMPAD_0) {
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
//        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                //this.enterPictureInPictureMode();
//                //this.onW
//            }
//            return false;
//        } else {
//            return super.onKeyDown(keyCode, event);
//        }
//
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//    }
//
//    @Override
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    protected void onPause() {
//        super.onPause();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        videoView.release(true);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        play(currentChannel);
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent){
//        super.onNewIntent(intent);
//        currentChannel = (Channel) intent.getSerializableExtra("CHANNEL");
//    }
//}
