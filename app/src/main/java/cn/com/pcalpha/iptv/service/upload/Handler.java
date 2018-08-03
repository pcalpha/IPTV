package cn.com.pcalpha.iptv.service.upload;

import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;

/**
 * Created by caiyida on 2018/7/30.
 */

public interface Handler {
    public Response serve(IHTTPSession session);
}
