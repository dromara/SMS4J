package kim.wind.sms.comm.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;


/**
 * <p>类名: HTTPUtils
 * <p>说明：  封装okhttp3，简单化请求创建流程，并且可以当做工具类进行自动装配使用
 * <p>构建请求只需要获得OKHTTPUtils的对象，然后逐步调用即可
 * <p>如：
 * <p>http.setBaseURL("http://www.baidu.com").builder().get("/wenku").sync();
 * <p>构建一个post请求只需要：
 * <p>http.setBaseURL("http://www.baidu.com").builder().post("/wenku",object).sync();
 * <p>如果在Spring Boot中作为组件自动装配使用，{@code baseURL}将读取配置文件中的{@code okhttp.url}获取默认的请求路径，则不需要再次调用 setBaseURL()方法进行设置
 * <p>依赖于 {@code okhttp3} {@code alibaba.fastjson}  {@code lombok.slf4j}
 * <p>{@code
 * <dependency>
 *             <groupId>com.squareup.okhttp3</groupId>
 *             <artifactId>okhttp</artifactId>
 *             <version>3.14.9</version>
 * </dependency>}
 * <p>{@code
 * <dependency>
 *             <groupId>com.alibaba</groupId>
 *             <artifactId>fastjson</artifactId>
 *             <version>1.2.74</version>
 *</dependency>}
 *
 * @author :Wind
 * @date :2022/11/07  14:10
 **/
@Slf4j
public class HTTPUtils {
    /**
     * 默认路径
     */

    private String baseURL;

    /**
     * 保存的默认地址
     */

    private String defaultURL;

    /**
     * 请求对象
     */
    private volatile Request request;

    /**
     * 请求构建对象
     */
    private Request.Builder builder;

    /**
     * okhttp客户端
     */
    private volatile OkHttpClient client;

    /**
     * 最终返回的对象
     */
    private OKResponse okResponse;

    /**
     * 最终请求的url
     */
    private String url = "";

    /**
     * 请求头类型标注
     */
    public MediaType json = MediaType.parse("application/json;charset=utf-8");

    /**
     * <p>说明：请求头类型标注
     * @name: setMediaType
     * @param
     * @author :Wind
    */
    public HTTPUtils setMediaType(String json) {
        this.json = MediaType.parse(json);
        return this;
    }

    /**
     * 标记子线程处理状态
     */
    private boolean isOK = false;

    /**
     * 说明：构建一个请求对象，该方法将返回对象本身，可以连锁调用，只有调用该方法后才可以调用get  post等方法
     *
     * @name: builder
     * @author :Wind
     */
    public HTTPUtils builder() {
        this.builder = new Request.Builder();
        this.client = new OkHttpClient();
        return this;
    }

    /**
     * <p>说明：设置一个通用的请求地址
     * <p>如果不设置该地址则以输入的地址作为请求地址，如设置了改地址，会自动拼接之后builder时设置的地址
     * <p>一旦设置该地址后，在重新获取对象或者调用{@link #defaultURL()}之前，配置文件中的默认地址将会被覆盖
     *
     * @param baseURL 设置一个通用的请求地址
     * @name: setBaseURL
     * @author :Wind
     */
    public HTTPUtils setBaseURL(String baseURL) {
        this.baseURL = baseURL;
        return this;
    }

    /**
     * <p>说明：设置回默认的请求路径（配置文件中路径）
     * <p>
     *
     * @name: defaultURL
     * @author :Wind
     */
    public HTTPUtils defaultURL() {
        this.baseURL = this.defaultURL;
        return this;
    }

    /**
     * 说明：向请求中设置heard
     *
     * @param key   header的名称
     * @param value header的值
     * @name: headers
     * @author :Wind
     */
    public HTTPUtils headers(String key, String value) {
        this.builder = builder.header(key, value);
        return this;
    }

    /**
     * 说明：向请求中设置heard
     *
     * @param map Map形式的header
     * @name: headers
     * @author :Wind
     */
    public HTTPUtils headers(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            this.builder = builder.header(entry.getKey(), entry.getValue());
        }
        return this;
    }

    /**
     * 说明：发送get请求
     *
     * @param url  要发送请求的url
     * @param data 请求的参数
     * @name: get
     * @author :Wind
     */
    public HTTPUtils get(String url, Map<String, String> data) {
        Response response;
        url = extracted(url);
        url = getString(url, data);
        log.info("请求路径：" + url);
        this.request = builder.url(url).get().build();
        return this;
    }

    /**
     * 说明：发送get请求
     *
     * @param url 要发送请求的url
     * @name: get
     * @author :Wind
     */
    public HTTPUtils get(String url) {
        return get(url, null);
    }

    /**
     * 说明：以json为参数发送post请求
     * 该方法使用了JSON进行序列化，一定确保传入的data为可序列化的
     *
     * @param url  请求路径
     * @param data 请求数据尽量使用Map、Array、或实体类
     * @name: post
     * @author :Wind
     */
    public HTTPUtils post(String url, Object data) {
        url = extracted(url);
        log.info("请求路径：" + url);
        String s = JSON.toJSONString(data);
        //将数据封装到RequestBody中
        RequestBody fromBody = RequestBody.create(json, s);
        this.request = builder.post(fromBody).url(url).build();
        return this;
    }

    /**
     * <p>说明：发送格式为application/x-www-form-urlencoded的post请求
     * @name: postOrBody
     * @param
     * @author :Wind
    */
    public HTTPUtils postOrBody(String url, Map<String,String> data) {
        url = extracted(url);
        log.info("请求路径：" + url);
        //将数据封装到RequestBody中
        RequestBody fromBody = getPostRequestBody(data);
        this.request = builder.post(fromBody).url(url).build();
        return this;
    }

    private RequestBody getPostRequestBody(Map<String,String> data){
        FormBody.Builder builder1 = new FormBody.Builder();
        data.forEach(builder1::add);
        return builder1.build();
    }

    /**
     * 说明：post请求，第三个参数为true时则请求的参数在query中，此时只接受 Map<String,String> 类型的参数
     *
     * @param url     请求路径
     * @param query   在url中的参数
     * @param isQuery 参数是否在query中
     * @name: post
     * @author :Wind
     */
    public HTTPUtils post(String url, Map<String, String> query, boolean isQuery) {
        if (isQuery) {
            url = extracted(url);
            String string = getString(url, query);
            log.info("请求参数：" + string);
            RequestBody fromBody = RequestBody.create(json, "");
            this.request = builder.post(fromBody).url(url).build();
            return this;
        }
        return post(url, query);
    }

    /**
     * 说明：使用同步形式发送请求
     *
     * @return OKResponse 返回参数
     * @name: sync
     * @author :Wind
     */
    public OKResponse sync() {
        isBuild();
        Response response;
        try {
            response = client.newCall(this.request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            url = "";
            okResponse = null;
        }
        return new OKResponse().setBody(response.body()).setCode(response.code()).setHeaders(response.headers());
    }


    /**
     * <p>说明：使用异步形式发送请求
     * <p>该方法会启动一个高优先级子线程处理请求任务，但不会等待处理结果，直接返回调用对象，如需获取结果可以调用异步回调方法
     * <p>{@link #asyncCallback()}
     * <p>回调方法将会始终阻塞线程直至子线程处理完成返回结果</p>
     *
     * @return {@code OKResponse}
     * @name: async
     * @author :Wind
     */
    public HTTPUtils async() {
        HTTPUtils that = this;
        Thread t = new Thread(() -> {
            log.info("子线程开始执行");
            that.okResponse = sync();
            that.isOK = true;
            log.info("子线程请求任务结束");
        });
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
        return this;
    }

    /**
     * <p>说明：使用异步形式发送请求
     * <p>该方法会启动一个高优先级子线程处理请求任务，但不会等待处理结果，同时不会返回任何对象，处理完成的结果将会放置在调用对象的 {@link #okResponse}对象中</p>
     * <p>回调方法将会始终阻塞线程直至子线程处理完成返回结果</p>
     *
     * @param NotWait
     * @name: async
     * @author :Wind
     */
    public void async(boolean NotWait) {
        async();
    }

    /**
     * 说明：每200毫秒检测一次异步线程是否处理完成，否则将阻塞至此,直至尝试100次后抛出{@code RuntimeException("等待超时！")}
     *
     * @param
     * @name: asyncCallback
     * @author :Wind
     */
    public OKResponse asyncCallback() {
        OKResponse okResponse1 = null;
        for (int i = 0; i <= 100; i++) {
            if (isOK) break;
            if (i == 100){
                throw new RuntimeException("等待超时！");
            }
            log.info("第"+i+"次尝试获取数据");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            okResponse1 = getOkResponse();
        } finally {
            isOK = false;
            okResponse = null;
            url = "";
        }
        return okResponse1;
    }

    /**
     * 说明：将map中的数据拼接到请求地址之后
     *
     * @param add 地址
     * @param map 请求参数
     * @name: getString
     * @author :Wind
     */
    private String getString(String add, Map<String, String> map) {
        if (map != null) {
            StringBuilder addBuilder = new StringBuilder(add);
            addBuilder.append("?");
            for (Map.Entry<String, String> entry : map.entrySet()) {
                addBuilder.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
            add = addBuilder.toString();
        }
        return add;
    }

    /**
     * 说明：拼接验证url地址
     *
     * @param url URL地址
     * @name: extracted
     * @author :Wind
     */
    private String extracted(String url) {
        this.url = "";
        if (StringUtils.isEmpty(baseURL)) {
            return this.url = url;
        }
        return this.url = baseURL + url;
    }

    public OKResponse getOkResponse() {
        return this.okResponse;
    }

    private void isBuild(){
        if(this.builder == null){
            throw new RuntimeException("非法调用！未构建请求对象！");
        }
    }

    /**
     * <p>说明：将返回结果序列化到实体类中
     * <p>传入对象必须实现了getter和setter方法，否则将序列化失败
     * @name: getJSONBody
     * @param t 要序列化的对象
     * @author :Wind
     */
    public static<T> T getJSONBody(OKResponse response, Class<T> t) {
        try {
            return JSONObject.parseObject(response.getBody().string(), t);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    /**
     * <p>类名: OkHTTPUtils
     * <p>说明：  用于封装请求后返回的参数
     *
     * @author :Wind
     * @date :2022/7/11  16:12
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
        public<T> T getJSONBody(Class<T> t) {
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
}
