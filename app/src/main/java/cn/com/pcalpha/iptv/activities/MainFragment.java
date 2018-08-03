///*
// * Copyright (C) 2017 The Android Open Source Project
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
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v17.leanback.app.BackgroundManager;
//import android.support.v17.leanback.app.BrowseFragment;
//import android.support.v17.leanback.app.RowsFragment;
//import android.support.v17.leanback.widget.ArrayObjectAdapter;
//import android.support.v17.leanback.widget.HeaderItem;
//import android.support.v17.leanback.widget.HorizontalGridView;
//import android.support.v17.leanback.widget.ListRow;
//import android.support.v17.leanback.widget.ListRowPresenter;
//import android.support.v17.leanback.widget.ListRowView;
//import android.support.v17.leanback.widget.OnItemViewClickedListener;
//import android.support.v17.leanback.widget.OnItemViewSelectedListener;
//import android.support.v17.leanback.widget.Presenter;
//import android.support.v17.leanback.widget.Row;
//import android.support.v17.leanback.widget.RowPresenter;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.resource.drawable.GlideDrawable;
//import com.bumptech.glide.request.animation.GlideAnimation;
//import com.bumptech.glide.request.target.SimpleTarget;
//
//import cn.com.pcalpha.iptv.R;
//import cn.com.pcalpha.iptv.model.domain.ChannelCategory;
//import cn.com.pcalpha.iptv.model.domain.Channel;
//import cn.com.pcalpha.iptv.service.ChannelService;
//
//public class MainFragment extends BrowseFragment {
//    private static final String TAG = "MainFragment";
//
//    private static final int BACKGROUND_UPDATE_DELAY = 300;
//    private static final int GRID_ITEM_WIDTH = 320;
//    private static final int GRID_ITEM_HEIGHT = 320;
//
//    private final Handler mHandler = new Handler();
//    private ArrayObjectAdapter mRowsAdapter;
//    private Drawable mDefaultBackground;
//    private DisplayMetrics mMetrics;
//    private Timer mBackgroundTimer;
//    private String mBackgroundUri;
//    private BackgroundManager mBackgroundManager;
//
//    private ChannelService channelService;
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//
//        channelService = new ChannelService(getActivity());
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        Log.i(TAG, "onCreate");
//
//        initedSelectedChannel=false;
//
//        //prepareBackgroundManager();
//
//        setupUIElements();
//
//        setupEventListeners();
//
//        startChannel();
//
//        loadRows();
//    }
//
//
//    @Override
//    public void onStart(){
//        super.onStart();
//
//        initSelectedChannel();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (null != mBackgroundTimer) {
//            Log.d(TAG, "onDestroy: " + mBackgroundTimer.toString());
//            mBackgroundTimer.cancel();
//        }
//    }
//
//    private void setSelectedPosition(final int position, final int subPosition){
//        final RowsFragment views = getRowsFragment();
//        views.setSelectedPosition(position,true, new Presenter.ViewHolderTask(){
//            @Override
//            public void run(Presenter.ViewHolder holder) {
//                ListRowView listRowView = (ListRowView)holder.view;
//                HorizontalGridView horizontalGridView = listRowView.getGridView();
//                if(subPosition+1>horizontalGridView.getChildCount()){
//                    return ;
//                }
//                horizontalGridView.setSelectedPosition(subPosition);
//            }
//        });
//    }
//
//    private void startChannel(){
//        try {
//            Channel channel = channelService.getLastAccess();
//            if (null == channel) {
//                Toast.makeText(this.getActivity(), "请先导入直播源", Toast.LENGTH_LONG).show();
//                return;
//            }
//            Intent intent = new Intent(this.getActivity(), VideoActivity.class);
//            intent.putExtra("CHANNEL", channel);
//
//            this.startActivity(intent);
//        } catch (Exception e){
//            channelService.dropTable();
//            channelService.createTable();
//
//        }
//    }
//
//
//
//    private static class Position{
//        private int row;
//        private int col;
//
//        public Position(int row, int col) {
//            this.row = row;
//            this.col = col;
//        }
//
//        public int getRow() {
//            return row;
//        }
//
//        public void setRow(int row) {
//            this.row = row;
//        }
//
//        public int getCol() {
//            return col;
//        }
//
//        public void setCol(int col) {
//            this.col = col;
//        }
//    }
//    /**
//     * key channel no
//     * value position[][]
//     */
//    private Map<Integer,Position> channelPosition = new HashMap<>();
//    private void loadRows() {
//        //channelService.initChannel();
//        List<Channel> channels = channelService.query();
//
//
//
//        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
//        CardPresenter cardPresenter = new CardPresenter();
//
//        HeaderItem CCTVHeader = new HeaderItem(ChannelCategory.CCTV, "央视");
//        HeaderItem STVHeader = new HeaderItem(ChannelCategory.STV, "卫视");
//        HeaderItem OtherHeader = new HeaderItem(ChannelCategory.OTHER, "其他");
//
//        ArrayObjectAdapter CCTVListRowAdapter = new ArrayObjectAdapter(cardPresenter);
//        ArrayObjectAdapter STVListRowAdapter = new ArrayObjectAdapter(cardPresenter);
//        ArrayObjectAdapter OtherListRowAdapter = new ArrayObjectAdapter(cardPresenter);
//
//        if(null!=channels){
//            for(Channel channel:channels){
//                if(ChannelCategory.CCTV==channel.getType()){
//                    CCTVListRowAdapter.add(channel);
//                    channelPosition.put(channel.getNo(),new Position(0,CCTVListRowAdapter.size()-1));
//                }else if(ChannelCategory.STV==channel.getType()){
//                    STVListRowAdapter.add(channel);
//                    channelPosition.put(channel.getNo(),new Position(1,STVListRowAdapter.size()-1));
//                }else{
//                    OtherListRowAdapter.add(channel);
//                    channelPosition.put(channel.getNo(),new Position(2,OtherListRowAdapter.size()-1));
//                }
//            }
//        }
//
//
//        mRowsAdapter.add(new ListRow(CCTVHeader, CCTVListRowAdapter));
//        mRowsAdapter.add(new ListRow(STVHeader, STVListRowAdapter));
//        mRowsAdapter.add(new ListRow(OtherHeader, OtherListRowAdapter));
//
//
//
//        HeaderItem gridHeader = new HeaderItem(4, "设置");
//
//        GridItemPresenter mGridPresenter = new GridItemPresenter();
//        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGridPresenter);
//        gridRowAdapter.add("上传源");
//        //gridRowAdapter.add(getString(R.string.error_fragment));
//        //gridRowAdapter.add(getResources().getString(R.string.personal_settings));
//        mRowsAdapter.add(new ListRow(gridHeader, gridRowAdapter));
//
//        setAdapter(mRowsAdapter);
//    }
//
//    private boolean initedSelectedChannel = false;
//    private void initSelectedChannel(){
//        if(!initedSelectedChannel){
//            Channel channel = channelService.getLastAccess();
//            if(null!=channel){
//                Position p = channelPosition.get(channel.getNo());
//
//                setSelectedPosition(p.getRow(),p.getCol());
//                initedSelectedChannel = true;
//            }
//        }
//    }
//
//    private void prepareBackgroundManager() {
//
//        mBackgroundManager = BackgroundManager.getInstance(getActivity());
//        mBackgroundManager.attach(getActivity().getWindow());
//        mDefaultBackground = getResources().getDrawable(R.drawable.default_background);
//        mMetrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
//    }
//
//    private void setupUIElements() {
//        // setBadgeDrawable(getActivity().getResources().getDrawable(
//        // R.drawable.videos_by_google_banner));
//        setTitle(getString(R.string.browse_title)); // Badge, when set, takes precedent
//        // over title
//        setHeadersState(HEADERS_HIDDEN);
//        setHeadersTransitionOnBackEnabled(true);
//
//        setSearchOrbViewOnScreen(false);
//
//        // set fastLane (or headers) background color
//        setBrandColor(getResources().getColor(R.color.fastlane_background));
//        // set search ic_launcher color
//        setSearchAffordanceColor(getResources().getColor(R.color.search_opaque));
//    }
//
//    //是否显示搜索按钮
//    private void setSearchOrbViewOnScreen(boolean onScreen) {
//        View searchOrbView = getTitleViewAdapter().getSearchAffordanceView();
//        if (searchOrbView != null) {
//            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) searchOrbView.getLayoutParams();
//            if(false==onScreen){
//                lp.setMargins(0,-200,0,0);
//            }
//            //lp.setMarginStart(onScreen ? 0 : -200);
//            searchOrbView.setLayoutParams(lp);
//        }
//    }
//
//    private void setupEventListeners() {
//        setOnSearchClickedListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        setOnItemViewClickedListener(new ItemViewClickedListener());
//        setOnItemViewSelectedListener(new ItemViewSelectedListener());
//    }
//
//    protected void updateBackground(String uri) {
//        int width = mMetrics.widthPixels;
//        int height = mMetrics.heightPixels;
//        Glide.with(getActivity())
//                .load(uri)
//                .centerCrop()
//                .error(mDefaultBackground)
//                .into(new SimpleTarget<GlideDrawable>(width, height) {
//                    @Override
//                    public void onResourceReady(GlideDrawable resource,
//                                                GlideAnimation<? super GlideDrawable>
//                                                        glideAnimation) {
//                        mBackgroundManager.setDrawable(resource);
//                    }
//                });
//        mBackgroundTimer.cancel();
//    }
//
//    private void startBackgroundTimer() {
//        if (null != mBackgroundTimer) {
//            mBackgroundTimer.cancel();
//        }
//        mBackgroundTimer = new Timer();
//        mBackgroundTimer.schedule(new UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY);
//    }
//
//    private final class ItemViewClickedListener implements OnItemViewClickedListener {
//        @Override
//        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
//                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
//            if (item instanceof Channel) {
//                Channel channel = (Channel) item;
//
//                Log.d(TAG, "Item: " + item.toString());
//                Intent intent = new Intent(getActivity(), VideoActivity.class);
//                intent.putExtra("CHANNEL", channel);
//
//                getActivity().startActivity(intent);
//            } else if (item instanceof String) {
//                if (((String) item).contains("上传源")) {
//                    Intent intent = new Intent(getActivity(), UploadFragment.class);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(getActivity(), ((String) item), Toast.LENGTH_SHORT)
//                            .show();
//                }
//            }
//        }
//    }
//
//    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
//        @Override
//        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
//                                   RowPresenter.ViewHolder rowViewHolder, Row row) {
////            CustomImageCardView cardView = (CustomImageCardView) itemViewHolder.view;
////
////            if (item instanceof Channel) {
////                mBackgroundUri = ((Channel) item).getIconUrl();
////                startBackgroundTimer();
////            }
//        }
//    }
//
//    private class UpdateBackgroundTask extends TimerTask {
//
//        @Override
//        public void run() {
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    updateBackground(mBackgroundUri);
//                }
//            });
//        }
//    }
//
//    private class GridItemPresenter extends Presenter {
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent) {
//            TextView view = new TextView(parent.getContext());
//            view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
//            view.setFocusable(true);
//            view.setFocusableInTouchMode(true);
//            view.setBackgroundColor(getResources().getColor(R.color.default_background));
//            view.setTextColor(Color.WHITE);
//            view.setGravity(Gravity.CENTER);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
//            ((TextView) viewHolder.view).setText((String) item);
//        }
//
//        @Override
//        public void onUnbindViewHolder(ViewHolder viewHolder) {
//        }
//    }
//
//}
