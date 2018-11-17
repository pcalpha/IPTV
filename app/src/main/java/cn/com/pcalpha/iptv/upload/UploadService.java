package cn.com.pcalpha.iptv.upload;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.pcalpha.iptv.channel.Channel;
import cn.com.pcalpha.iptv.channel.ChannelDao;
import cn.com.pcalpha.iptv.channel.stream.ChannelStream;
import cn.com.pcalpha.iptv.channel.stream.ChannelStreamDao;
import cn.com.pcalpha.iptv.channel.category.ChannelCategory;
import cn.com.pcalpha.iptv.channel.category.ChannelCategoryDao;
import fi.iki.elonen.NanoHTTPD;

public class UploadService extends NanoHTTPD {
    private static final String PATH_ROOT = "/";
    private static final String PATH_WEB = "/web";
    private static final String PATH_UPLOAD = "/upload";
    private static final String PATH_CSS = "/css";
    private static final String PATH_JS = "/js";
    private static String[] headers = new String[]{"频道类别", "频道号", "频道名称", "源地址"};

    private Context mContext;

    private ChannelDao mChannelDao;
    private ChannelCategoryDao mChannelCategoryDao;
    private ChannelStreamDao mChannelStreamDao;

    public static final int DEFAULT_SERVER_PORT = 10080;
    public static final String TAG = "UploadService";

    //private List<SharedFile> fileList;//用于分享的文件列表

    public UploadService(Context context) {
        super(DEFAULT_SERVER_PORT);
        this.mContext = context;
        mChannelDao = ChannelDao.getInstance(context);
        mChannelCategoryDao = ChannelCategoryDao.getInstance(context);
        mChannelStreamDao = ChannelStreamDao.getInstance(context);
    }

    //当接受到连接时会调用此方法
    @Override
    public Response serve(IHTTPSession session) {
        if (PATH_ROOT.equals(session.getUri())) {
            return responseHtml(session, "web/upload.html");
        } else if (PATH_UPLOAD.equals(session.getUri())) {
            if (Method.GET == session.getMethod()) {
                return responseHtml(session, "web/upload.html");
            } else if (Method.POST == session.getMethod()) {
                return responseJson(session);
            }
        } else if (session.getUri().contains(PATH_CSS)) {
            return responseCSS(session, "web" + session.getUri());
        } else if (session.getUri().contains(PATH_JS)) {
            return responseJS(session, "web" + session.getUri());
        } else if (session.getUri().equals("/channel.json")) {
            return responseFile(session, "web/channel.json");
        }
        return responseHtml(session, "web/404.html");
    }

    public Response responseCSS(IHTTPSession session, String fileName) {
        return response(session, fileName, "text/css");
    }

    public Response responseJS(IHTTPSession session, String fileName) {
        return response(session, fileName, "text/javascript");
    }

    public Response responseFile(IHTTPSession session, String fileName) {
        return response(session, fileName, "application/octet-stream");
    }

    public Response responseHtml(IHTTPSession session, String fileName) {
        return response(session, fileName, "text/html");
    }

    public Response response(IHTTPSession session, String fileName, String mimeType) {
        AssetManager assetMgr = mContext.getAssets();
        InputStream is = null;
        try {
            is = assetMgr.open(fileName, AssetManager.ACCESS_STREAMING);

            return newFixedLengthResponse(Response.Status.OK, mimeType, is, is.available());
        } catch (Exception e) {
            e.printStackTrace();
            return response404(session, e.getMessage());
        }
    }

    public Response responseJson(IHTTPSession session) {
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        try {
            Map<String, String> files = new HashMap<>();
            session.parseBody(files);
            if (null != files) {
                File file = new File(files.get("file"));
                FileInputStream fis = new FileInputStream(file);
//                br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
//                String line = "";
//                while ((line = br.readLine()) != null) {
//                    sb.append(line);
//                }
//                String json = sb.toString();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while (-1 != (len = fis.read(buffer))) {
                    baos.write(buffer, 0, len);
                }
                baos.flush();
                String json = baos.toString("UTF-8");

                List<Channel> channelList = new ArrayList<>();
                try{
                    channelList = JSON.parseArray(json, Channel.class);
                }catch (Exception e){
                    Log.e(TAG,e.getMessage());
                }
                if(null==channelList||channelList.size()==0){
                    throw new RuntimeException("empty channelList");
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
                        if(null==stream.getCarrier()||"".equals(stream.getCarrier())){
                            stream.setCarrier("CMCC");
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
            }
        } catch (Exception e) {
            e.printStackTrace();
            return responseFile(session, "web/error.html");
        }
        return responseFile(session, "web/success.html");
    }

//    public Response responseUpload(IHTTPSession session) {
//        try {
//            Map<String, String> files = new HashMap<>();
//            session.parseBody(files);
//            if (null != files) {
//                File file = new File(files.get("file"));
//                List<CSVRecord> lines = readCsvFile(file, headers, 1);
//
//                //清空表
//                channelService.clear();
//                channelCategoryService.clear();
//                try {
//                    if (null != lines) {
//                        List<String> channelCategoryList = new ArrayList<>();
//                        List<Channel> channelList = new ArrayList<>();
//                        for (CSVRecord line : lines) {
//                            String channelCategoryName = line.get("频道类别");
//                            String channelNo = line.get("频道号");
//                            String channelName = line.get("频道名称");
//                            String channelSrc = line.get("源地址");
//
//
//                            if (!channelCategoryList.contains(channelCategoryName)) {
//                                channelCategoryList.add(channelCategoryName);
//                            }
//
//                            Channel channel = new Channel(
//                                    channelNo,
//                                    channelName,
//                                    channelSrc,
//                                    channelCategoryName);
//                            channelList.add(channel);
//                        }
//
//                        for (int x = 0; x < channelCategoryList.size(); x++) {
//                            String categoryName = channelCategoryList.get(x);
//                            ChannelCategory channelCategory = new ChannelCategory(x, categoryName);
//                            channelCategoryService.save(channelCategory);
//                        }
//
//                        for (int y = 0; y < channelList.size(); y++) {
//                            Channel channel = channelList.get(y);
//                            channel.setId(y);
//                            channelService.save(channel);
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    System.out.println("请检查格式");
//                    return responseFile(session, "web/error.html");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return responseFile(session, "web/error.html");
//        }
//        return responseFile(session, "web/success.html");
//    }

    //页面不存在，或者文件不存在时
    public Response response404(IHTTPSession session, String url) {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html><html>body>");
        builder.append("Sorry,Can't Found" + url + " !");
        builder.append("</body></html>\n");
        return newFixedLengthResponse(builder.toString());
    }

    // 读取csv文件 传参数 文件 表头 从第几行开始
    private static List<CSVRecord> readCsvFile(File file, String[] fileHeaders, Integer lineStart) {
        BufferedReader br = null;
        CSVParser csvFileParser = null;
        List<CSVRecord> list = null;
        // 创建CSVFormat（header mapping）
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(fileHeaders);
        try {
            // 初始化FileReader object
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));//解决乱码问题
            // 初始化 CSVParser object
            csvFileParser = new CSVParser(br, csvFileFormat);
            // CSV文件records
            List<CSVRecord> csvRecords = csvFileParser.getRecords();

            list = new ArrayList();
            for (int i = lineStart; i < csvRecords.size(); i++) {
                CSVRecord record = csvRecords.get(i);
                list.add(record);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                csvFileParser.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}