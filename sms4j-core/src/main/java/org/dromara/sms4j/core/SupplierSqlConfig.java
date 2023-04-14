package org.dromara.sms4j.core;

import org.dromara.sms4j.aliyun.config.AlibabaConfig;
import org.dromara.sms4j.cloopen.config.CloopenConfig;
import org.dromara.sms4j.comm.enumerate.SupplierType;
import org.dromara.sms4j.comm.utils.JDBCTool;
import org.dromara.sms4j.comm.utils.SmsUtil;
import org.dromara.sms4j.core.config.SupplierFactory;
import org.dromara.sms4j.huawei.config.HuaweiConfig;
import org.dromara.sms4j.jdcloud.config.JdCloudConfig;
import org.dromara.sms4j.tencent.config.TencentConfig;
import org.dromara.sms4j.unisms.config.UniConfig;
import org.dromara.sms4j.yunpian.config.YunpianConfig;

import java.util.Map;

/**
 * SupplierSqlConfig
 * <p> 处理sql配置源
 * @author :Wind
 * 2023/4/11  19:42
 **/
public class SupplierSqlConfig {
    private static Map<String, String> select;

    static {
        select = JDBCTool.selectConfig();
    }

    /**
     *  SupplierSqlConfig
     * <p>在类初始化是完成方法调用
     * @author :Wind
    */
    public SupplierSqlConfig() {
        alibaba();
        huawei();
        jingdong();
        tencent();
        uniSms();
        yunPian();
        cloopen();
    }

    /**
     *  alibaba
     * <p>数据库读取并设置阿里云短信
     * @author :Wind
    */
    public static void alibaba(){
        AlibabaConfig alibabaConfig = SmsUtil.jsonForObject(select.get(SupplierType.ALIBABA.getName()), AlibabaConfig.class);
        SmsUtil.copyBean(alibabaConfig, SupplierFactory.getAlibabaConfig());
    }

    /**
     *  huawei
     * <p>数据库读取并设置华为短信
     * @author :Wind
    */
    public static void huawei(){
        HuaweiConfig huaweiConfig = SmsUtil.jsonForObject(select.get(SupplierType.HUAWEI.getName()), HuaweiConfig.class);
        SmsUtil.copyBean(huaweiConfig, SupplierFactory.getHuaweiConfig());
    }

    /**
     *  jingdong
     * <p>数据库读取并设置京东短信
     * @author :Wind
    */
    public static void jingdong(){
        JdCloudConfig jdCloudConfig = SmsUtil.jsonForObject(select.get(SupplierType.JD_CLOUD.getName()), JdCloudConfig.class);
        SmsUtil.copyBean(jdCloudConfig,SupplierFactory.getJdCloudConfig());
    }

    /**
     *  tencent
     * <p>数据库读取并设置腾讯短信
     * @author :Wind
    */
    public static void tencent(){
        TencentConfig tencentConfig = SmsUtil.jsonForObject(select.get(SupplierType.TENCENT.getName()), TencentConfig.class);
        SmsUtil.copyBean(tencentConfig, SupplierFactory.getTencentConfig());
    }

    /**
     *  uniSms
     * <p>数据库读取并设置合一短信配置
     * @author :Wind
    */
    public static void uniSms(){
        UniConfig uniConfig = SmsUtil.jsonForObject(select.get(SupplierType.UNI_SMS.getName()), UniConfig.class);
        SmsUtil.copyBean(uniConfig,SupplierFactory.getUniConfig());
    }

    /**
     *  yunPian
     * <p>数据库读取并设置云片短信
     * @author :Wind
    */
    public static void yunPian(){
        YunpianConfig yunpianConfig = SmsUtil.jsonForObject(select.get(SupplierType.YUNPIAN.getName()), YunpianConfig.class);
        SmsUtil.copyBean(yunpianConfig,SupplierFactory.getYunpianConfig());
    }

    /**
     *  cloopen
     * <p>数据库读取并设置荣联云短信
     * @author :Wind
    */
    public static void cloopen(){
        CloopenConfig cloopenConfig = SmsUtil.jsonForObject(select.get(SupplierType.CLOOPEN.getName()), CloopenConfig.class);
        SmsUtil.copyBean(cloopenConfig,SupplierFactory.getCloopenConfig());
    }

}
