package cn.com.pcalpha.iptv.activities;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import cn.com.pcalpha.iptv.R;
import cn.com.pcalpha.iptv.service.UploadService;

/**
 * Created by caiyida on 2018/2/8.
 */

public class UploadActivity extends Activity {
    private static final String TAG = "UploadActivity";

    private TextView textView;
    private UploadService uploadService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_upload);
        textView = findViewById(R.id.showServerInfo);
        List<String> ipList = getLocalIp();

        StringBuilder sb = new StringBuilder();
        sb.append("用浏览器打开\n");
        if(null!=ipList){
            for(String ip:ipList){
                sb.append("http://"+ip+":"+UploadService.DEFAULT_SERVER_PORT+"/upload.html\n");
            }
        }
        sb.append("上传源文件");

        //String url="http://"+ip+":"+UploadService.DEFAULT_SERVER_PORT+"/upload.html";
        //String text = "用浏览器打开\n"+url+"上传源文件";
        textView.setText(sb.toString());

        uploadService = new UploadService(this.getApplicationContext());
        try {
            uploadService.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uploadService.stop();
    }

    /**
     * 得到有限网关的IP地址
     *
     * @return
     */
    private List<String> getLocalIp() {
        List<String> ipList = new ArrayList<>();
        try {
            // 获取本地设备的所有网络接口
            Enumeration<NetworkInterface> enumerationNi = NetworkInterface
                    .getNetworkInterfaces();
            while (enumerationNi.hasMoreElements()) {
                NetworkInterface networkInterface = enumerationNi.nextElement();
                String interfaceName = networkInterface.getDisplayName();
                Log.i("tag", "网络名字" + interfaceName);

                // 如果是有限网卡
                //if (interfaceName.equals("eth0")) {
                    Enumeration<InetAddress> enumIpAddr = networkInterface
                            .getInetAddresses();

                    while (enumIpAddr.hasMoreElements()) {
                        // 返回枚举集合中的下一个IP地址信息
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        // 不是回环地址，并且是ipv4的地址
                        if (!inetAddress.isLoopbackAddress()
                                && inetAddress instanceof Inet4Address) {
                            Log.i("tag", inetAddress.getHostAddress() + "   ");

                            ipList.add(inetAddress.getHostAddress());
                        }
                    }
                //}
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipList;

    }

    public static String getWlanIp(Context context){
        WifiManager wifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo=wifiManager.getConnectionInfo();
        return  intToIpAddr(wifiInfo.getIpAddress());
    }

    private static String intToIpAddr(int ip){
        return (ip & 0xFF)+"."
                + ((ip>>8)&0xFF) + "."
                + ((ip>>16)&0xFF) + "."
                + ((ip>>24)&0xFF);
    }
}
