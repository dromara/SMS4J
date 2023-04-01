package kim.wind.sms.comm.utils.http;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

public class HttpJsonTool {


    public static <T> T getJSONBody(Object response,Class<T>t){
        return JSONObject.parseObject(response.toString(), t);
    }

    public static <T> T getJSONBody(String response,Class<T>t){
        return JSONObject.parseObject(response, t);
    }

    /**
     * <p>说明：将返回结果序列化为一个json对象
     * <p>
     * @name: getJSONObject
     * @param response
     * @author :Wind
     */
    public static JSONObject getJSONObject(OKResponse response){
        try {
            return JSONObject.parseObject(response.getBody().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static JSONObject getJSONObject(Object obj){
        return JSONObject.parseObject(obj.toString());
    }
}
