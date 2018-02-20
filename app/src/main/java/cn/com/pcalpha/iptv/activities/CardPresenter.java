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

package cn.com.pcalpha.iptv.activities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import cn.com.pcalpha.iptv.R;
import cn.com.pcalpha.iptv.model.Channel;

/*
 * A CardPresenter is used to generate Views and bind Objects to them on demand.
 * It contains an Image CardView
 */
public class CardPresenter extends Presenter {
    private static final String TAG = "CardPresenter";

    private static final int CARD_WIDTH = 320;
    private static final int CARD_HEIGHT = 180;
    private static int sSelectedBackgroundColor;
    private static int sDefaultBackgroundColor;
    private static int sPlayingBackgroundColor;
    private Drawable mDefaultCardImage;
    private Channel selectedChannel;

    private static void updateCardBackgroundColor(ImageCardView view, boolean selected) {
        int color = selected ? sSelectedBackgroundColor : sDefaultBackgroundColor;
        // Both background colors should be set because the view's background is temporarily visible
        // during animations.
        view.setBackgroundColor(color);
        view.findViewById(R.id.info_field).setBackgroundColor(color);
    }

    private static void updatePlayingCardBackgroundColor(ImageCardView view, boolean selected) {
        int color = selected ? sPlayingBackgroundColor : sDefaultBackgroundColor;
        // Both background colors should be set because the view's background is temporarily visible
        // during animations.
        view.setBackgroundColor(color);
        view.findViewById(R.id.info_field).setBackgroundColor(color);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        Log.d(TAG, "onCreateViewHolder");

        sDefaultBackgroundColor = parent.getResources().getColor(R.color.default_background);
        sSelectedBackgroundColor = parent.getResources().getColor(R.color.selected_background);
        sPlayingBackgroundColor =  parent.getResources().getColor(R.color.playing_background);
        /*
         * This template uses a default image in res/drawable, but the general case for Android TV
         * will require your resources in xhdpi. For more information, see
         * https://developer.android.com/training/tv/start/layouts.html#density-resources
         */
        mDefaultCardImage = parent.getResources().getDrawable(R.drawable.movie);

        final ImageCardView cardView = new ImageCardView(parent.getContext()) {
            @Override
            public void setSelected(boolean selected) {
                updateCardBackgroundColor(this, selected);
//                if(isFocus()){
//                    updatePlayingCardBackgroundColor(this, true);
//                }else{
//                    updatePlayingCardBackgroundColor(this, false);
//                }
                super.setSelected(selected);
            }
        };
        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        updateCardBackgroundColor(cardView, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        Channel channel = (Channel) item;
        ImageCardView cardView = (ImageCardView) viewHolder.view;

        Log.d(TAG, "onBindViewHolder");

        cardView.setTitleText(channel.getNo()+"  "+channel.getName());
        //cardView.setContentText(movie.getStudio());
        cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);

        //ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap image = convertToImage(channel.getName());

        ImageView iv = cardView.getMainImageView();
        iv.setImageBitmap(image);

//        if(1==channel.getLastAccess()){
//            //cardView.setFocus(true);
//            cardView.setSelected(true);
//        }

        if (channel.getIconUrl() != null) {
//            Glide.with(viewHolder.view.getContext())
//                    .load(baos.toByteArray())
//                    .centerCrop()
//                    .error(mDefaultCardImage)
//                    .into(cardView.getMainImageView());
        }
    }

    public Bitmap convertToImage(String text){
        Bitmap bmp = Bitmap.createBitmap(320,180, Bitmap.Config.ARGB_8888); //图象大小要根据文字大小算下,以和文本长度对应
        Canvas canvasTemp = new Canvas(bmp);
        canvasTemp.drawColor(Color.BLACK);
        Paint p = new Paint();
        String familyName ="宋体";
        Typeface font = Typeface.create(familyName,Typeface.BOLD);
        p.setColor(Color.GRAY);
        p.setTypeface(font);
        p.setTextSize(22);
        p.setTextAlign(Paint.Align.CENTER);
        p.setStyle(Paint.Style.FILL);


        canvasTemp.drawText(text,160,100,p);

        return bmp;
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        Log.d(TAG, "onUnbindViewHolder");
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        // Remove references to images so that the garbage collector can free up memory
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }
}
