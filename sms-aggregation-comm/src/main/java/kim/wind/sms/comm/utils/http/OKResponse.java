package kim.wind.sms.comm.utils.http;

import com.alibaba.fastjson.JSONObject;
import okhttp3.Headers;
import okhttp3.ResponseBody;

import java.io.IOException;

/**
 * OKResponse
 * <p> 用于封装请求后返回的参数
 *
 * @author :Wind
 * 2023/3/31  23:52
 **/
public class OKResponse {
    private ResponseBody body;
    private Headers headers;
    private Integer code;

    public ResponseBody getBody() {
        return body;
    }

    public OKResponse setBody(ResponseBody body) {
        this.body = body;
        return this;
    }

    public Headers getHeaders() {
        return headers;
    }

    public OKResponse setHeaders(Headers headers) {
        this.headers = headers;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public OKResponse setCode(Integer code) {
        this.code = code;
        return this;
    }

    /**
     * <p>说明：将返回结果序列化到实体类中
     * <p>传入对象必须实现了getter和setter方法，否则将序列化失败
     * @name: getJSONBody
     * @param t 要序列化的对象
     * @author :Wind
     */
    public <T> T getJSONBody(Class<T> t) {
        try {
            return JSONObject.parseObject(this.getBody().string(), t);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * <p>说明：将返回结果序列化为一个json对象
     * <p>
     * @name: getJSONObject
     * @author :Wind
     */
    public  JSONObject getJSONObject(){
        try {
            return JSONObject.parseObject(this.getBody().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "OKResponse{" +
                "body=" + body +
                ", headers=" + headers +
                ", code=" + code +
                '}';
    }
}