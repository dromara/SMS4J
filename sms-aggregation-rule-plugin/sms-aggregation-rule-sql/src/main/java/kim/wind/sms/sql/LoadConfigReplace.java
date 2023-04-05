package kim.wind.sms.sql;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.PostConstruct;
import kim.wind.sms.aliyun.config.AlibabaSmsConfig;
import kim.wind.sms.autoimmit.config.SmsConfig;
import kim.wind.sms.autoimmit.enumerate.ConfigType;
import kim.wind.sms.comm.utils.SpringUtil;
import kim.wind.sms.sql.utils.JDBCTool;

import java.util.Map;

public class LoadConfigReplace {
    private final SpringUtil springUtil;
    private final SmsConfig smsConfig;
    private final JDBCTool jdbcTool;

    public LoadConfigReplace(SpringUtil springUtil, SmsConfig smsConfig, JDBCTool jdbcTool) {
        this.springUtil = springUtil;
        this.smsConfig = smsConfig;
        this.jdbcTool = jdbcTool;
    }

    @PostConstruct
    public void init() {
        if (smsConfig.getConfigType().equals(ConfigType.SQL_CONFIG)) {
            Map<String, String> select = jdbcTool.select();
            String key = "";
            String value = "";
            for (Map.Entry<String, String> set : select.entrySet()) {
                key = set.getKey();
                value = set.getValue();
            }
            JSONObject jsonObject = JSONObject.parseObject(value);
            switch (key){
                case "alibaba":
                    AlibabaSmsConfig sqlAlibaba = JSONObject.toJavaObject(jsonObject, AlibabaSmsConfig.class);
                    AlibabaSmsConfig alibabaSmsConfig = new AlibabaSmsConfig();
                    BeanUtil.copyProperties(sqlAlibaba,alibabaSmsConfig);
                    springUtil.deleteBean("alibabaSmsConfig");
                    springUtil.createBean(alibabaSmsConfig.getClass().getName(),alibabaSmsConfig);
                    break;
            }

            springUtil.replaceBean("smsConfig", SmsConfig.class);
        }
    }
}
