package org.dromara.sms4j.unisms.core;

import cn.hutool.json.JSONObject;
import org.dromara.sms4j.comm.exception.SmsBlendException;

import java.util.Objects;

public class UniResponse {
    public static final String REQUEST_ID_HEADER_KEY = "x-uni-request-id";
    public String requestId;
    public String code;
    public String message;
    public String status;
    public JSONObject data = null;
    public Object raw;

    public UniResponse(){}

    /**
     * Create a new Uni Response.
     *
     * @param response raw HTTP response
     */
    public UniResponse(final JSONObject response) throws SmsBlendException {
        JSONObject body = response.getJSONObject("data");
        if (!Objects.isNull(body)) {
            this.status = body.getJSONArray("messages").getJSONObject(0).getStr("status");
            this.requestId = body.getJSONArray("messages").getJSONObject(0).getStr("id");
            this.raw = body;
            this.data = body;
        }
        if (!"400".equals(this.status)) {
            String code = response.getStr("code");
            if (!"0".equals(code)) {
                this.message = response.getStr("message");
            } else {
                this.message = body.getJSONArray("messages").getStr(0);
            }
            this.code = code;
        } else {
            throw new SmsBlendException(response.getStr("message"), "-1");
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
