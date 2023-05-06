package org.dromara.sms4j.unisms.core;

import com.alibaba.fastjson.JSONObject;
import org.dromara.sms4j.comm.exception.SmsBlendException;


public class UniResponse {
    public static final String REQUEST_ID_HEADER_KEY = "x-uni-request-id";
    public String requestId;
    public String code;
    public String message;
    public String status;
    public JSONObject data = null;
    public Object raw;

    /**
     * Create a new Uni Response.
     *
     * @param response raw HTTP response
     */
    public UniResponse(final JSONObject response) throws SmsBlendException {
        JSONObject body = response.getJSONObject("data");
        this.status = body.getJSONArray("messages").getJSONObject(0).getString("status");
        this.requestId = body.getJSONArray("messages").getJSONObject(0).getString("id");
        this.raw = body;

        if (this.status != "400") {
            String code = response.getString("code");
            String message = body.getJSONArray("messages").getString(0);
            if (!"0".equals(code)) {
                throw new SmsBlendException(message, code, this.requestId);
            }
            this.code = code;
            this.message = message;
            this.data = body;
        }
        else {
            throw new SmsBlendException(response.getString("message"), "-1");
        }
    }

    @Override
    public String toString() {
        return "UniResponse{" +
                "requestId='" + requestId + '\'' +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", status='" + status + '\'' +
                ", data=" + data +
                ", raw=" + raw +
                '}';
    }
}
