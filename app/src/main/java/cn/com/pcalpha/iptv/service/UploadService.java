package cn.com.pcalpha.iptv.service;

import android.content.Context;
import android.content.res.AssetManager;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.pcalpha.iptv.model.Channel;
import fi.iki.elonen.NanoHTTPD;

public class UploadService extends NanoHTTPD {
    private Context context;

    private ChannelService channelService;

    public static final int DEFAULT_SERVER_PORT = 10080;
    public static final String TAG = UploadService.class.getSimpleName();

    private static final String PATH_ROOT = "/";
    private static final String PATH_UPLOAD = "/upload";
    private static String[] headers = new String[]{"频道号", "频道名称", "频道类别", "图标地址", "源地址"};


    //private List<SharedFile> fileList;//用于分享的文件列表


    public UploadService(Context context) {
        super(DEFAULT_SERVER_PORT);
        this.context = context;
        channelService = new ChannelService(context);
    }

    //当接受到连接时会调用此方法
    public Response serve(IHTTPSession session) {
        if (PATH_ROOT.equals(session.getUri())) {
            return responseHtml(session, "web/404.html");
        } else if (PATH_UPLOAD.equals(session.getUri())) {
            if(Method.GET==session.getMethod()){
                return responseHtml(session, "web/upload.html");
            }else if(Method.POST==session.getMethod()){
                return responseUpload(session);
            }
        } else if("/channel.csv".equals(session.getUri())){
            return responseFile(session, "web/channel.csv");
        }
        return responseHtml(session, "web/404.html");
    }

    public Response responseFile(IHTTPSession session, String fileName) {
        return response(session,fileName,"application/octet-stream");
    }

    public Response responseHtml(IHTTPSession session, String fileName) {
        return response(session,fileName,"text/html");
    }

    public Response response(IHTTPSession session, String fileName, String mimeType) {
        try {
            AssetManager assetMgr = context.getAssets();
            InputStream is = assetMgr.open(fileName, AssetManager.ACCESS_BUFFER);

            // 返回OK，同时传送文件，为了安全这里应该再加一个处理，即判断这个文件是否是我们所分享的文件，避免客户端访问了其他个人文件
            return newFixedLengthResponse(Response.Status.OK, mimeType, is, is.available());
        } catch (Exception e) {
            e.printStackTrace();
            return response404(session, e.getMessage());
        }
    }

    public Response responseUpload(IHTTPSession session) {
        try {
            Map<String, String> files = new HashMap<>();
            session.parseBody(files);
            if (null != files) {
                File file = new File(files.get("file"));
                List<CSVRecord> lines = readCsvFile(file, headers, 1);

                channelService.clear();
                try {
                    if (null != lines) {
                        for (CSVRecord line : lines) {
                            Channel channel = new Channel(
                                    Integer.parseInt(line.get("频道号")),
                                    line.get("频道名称"),
                                    Integer.parseInt(line.get("频道类别")),
                                    line.get("图标地址"),
                                    line.get("源地址"));
                            channelService.insert(channel);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("请检查格式");
                    return responseFile(session, "web/error.html");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return responseFile(session, "web/error.html");
        }
        return responseFile(session, "web/success.html");
    }

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