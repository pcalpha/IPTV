package cn.com.pcalpha.iptv.update;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.com.pcalpha.iptv.channel.Channel;
import cn.com.pcalpha.iptv.channel.ChannelDao;
import cn.com.pcalpha.iptv.channel.stream.ChannelStream;
import cn.com.pcalpha.iptv.channel.stream.ChannelStreamDao;
import cn.com.pcalpha.iptv.channel.category.ChannelCategory;
import cn.com.pcalpha.iptv.channel.category.ChannelCategoryDao;
import cn.com.pcalpha.iptv.tools.Sha1Utils;

public class AutoUpdateService {
    public static final String TAG = "AutoUpdateService";
    private static final String FILE_NAME = "channel.json";
    private static final String URL = "https://raw.githubusercontent.com/pcalpha/IPTV/master/share/channels.json";

    private Context mContext;
    private ChannelDao mChannelDao;
    private ChannelCategoryDao mChannelCategoryDao;
    private ChannelStreamDao mChannelStreamDao;


    private static AutoUpdateService singleton;
    public static AutoUpdateService getInstance(Context context) {
        if (null == singleton) {
            synchronized (AutoUpdateService.class) {
                if (null == singleton) {
                    singleton = new AutoUpdateService(context);
                }
            }
        }
        return singleton;
    }

    private AutoUpdateService(Context context) {
        this.mContext = context;
        mChannelDao = ChannelDao.getInstance(context);
        mChannelCategoryDao = ChannelCategoryDao.getInstance(context);
        mChannelStreamDao = ChannelStreamDao.getInstance(context);
    }

    public void execute(){
        new AutoUpdateTask().execute();
    }

    class AutoUpdateTask extends AsyncTask<Void, Void, Void> {

        public AutoUpdateTask() {
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String localJson = readFromLocalFile();
            String localSha1 = Sha1Utils.getSha1(localJson);

            String remoteJson = getRemote(URL);
            String remoteSha1 = Sha1Utils.getSha1(remoteJson);
            if(localSha1!=null&&localSha1.equals(remoteSha1)){
                return null;
            }
            if(null==remoteJson){
                return null;
            }
            writeToLocalFile(remoteJson);

            List<Channel> channelList = new ArrayList<>();
            try{
                channelList = JSON.parseArray(remoteJson, Channel.class);
            }catch (Exception e){
                Log.e(TAG,e.getMessage());
            }
            if(null==channelList||channelList.size()==0){
                return null;
            }

            List<ChannelStream> channelStreamList = new ArrayList<>();
            List<ChannelCategory> channelCategoryList = new ArrayList<>();
            List<String> categoryNameList = new ArrayList<>();

            for (Channel channel : channelList) {
                String categoryName = channel.getCategoryName();
                if (!categoryNameList.contains(categoryName)) {
                    ChannelCategory category = new ChannelCategory();
                    category.setName(categoryName);
                    categoryNameList.add(categoryName);

                    channelCategoryList.add(category);
                }


                int x = 0;
                for (ChannelStream stream : channel.getChannelStreams()) {
                    stream.setChannelName(channel.getName());
                    if(null==stream.getName()||"".equals(stream.getName())){
                        stream.setName("源"+x);
                        x++;
                    }
                    channelStreamList.add(stream);
                }
            }

            mChannelDao.clear();
            mChannelStreamDao.clear();
            mChannelCategoryDao.clear();

            mChannelDao.insert(channelList);
            mChannelStreamDao.insert(channelStreamList);
            mChannelCategoryDao.insert(channelCategoryList);

            return null;
        }
    }

    private String getRemote(String path) {
        try {
            URL url = new URL(path.trim());
            //打开连接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            if (200 == urlConnection.getResponseCode()) {
                //得到输入流
                InputStream is = urlConnection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while (-1 != (len = is.read(buffer))) {
                    baos.write(buffer, 0, len);
                }
                baos.flush();
                return baos.toString("utf-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String readFromLocalFile(){
        try {
            FileInputStream fis = mContext.openFileInput(FILE_NAME);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while (-1 != (len = fis.read(buffer))) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            return baos.toString("UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void writeToLocalFile(String json){
        try {
            FileOutputStream fos = mContext.openFileOutput(FILE_NAME,Context.MODE_PRIVATE);
            ByteArrayInputStream bais = new ByteArrayInputStream(json.getBytes());

            byte[] buffer = new byte[1024];
            int len = 0;
            while (-1 != (len = bais.read(buffer))) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}