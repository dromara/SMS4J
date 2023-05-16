package org.dromara.sms4j.core;

import org.dromara.sms4j.aliyun.config.AlibabaConfig;
import org.dromara.sms4j.cloopen.config.CloopenConfig;
import org.dromara.sms4j.comm.enumerate.SupplierType;
import org.dromara.sms4j.comm.utils.JDBCTool;
import org.dromara.sms4j.comm.utils.SmsUtil;
import org.dromara.sms4j.core.config.SupplierFactory;
import org.dromara.sms4j.ctyun.config.CtyunConfig;
import org.dromara.sms4j.emay.config.EmayConfig;
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

    /**
     *  readSqlConfig
     * <p>读取数据库配置信息
     * @author :Wind
    */
    public static void readSqlConfig(){
        select = JDBCTool.selectConfig();
    }

    /**
     *  refreshSqlConfig
     * <p>读取并刷新数据库配置
     * @author :Wind
    */
    public static void refreshSqlConfig(){
        readSqlConfig();
        alibaba();
        huawei();
        jingdong();
        tencent();
        uniSms();
        yunPian();
        cloopen();
        emay();
        ctyun();
    }

    public SupplierSqlConfig() {
        refreshSqlConfig();
    }

    public static SupplierSqlConfig newSupplierSqlConfig(){
        return new SupplierSqlConfig();
    }

    /**
     *  alibaba
     * <p>数据库读取并设置阿里云短信
     * @author :Wind
    */
    public static void alibaba(){
        AlibabaConfig alibabaConfig = SmsUtil.jsonForObject(select.get(SupplierType.ALIBABA.getName()), AlibabaConfig.class);
       SupplierFactory.setAlibabaConfig(alibabaConfig);
    }

    /**
     *  huawei
     * <p>数据库读取并设置华为短信
     * @author :Wind
    */
    public static void huawei(){
        HuaweiConfig huaweiConfig = SmsUtil.jsonForObject(select.get(SupplierType.HUAWEI.getName()), HuaweiConfig.class);
        SupplierFactory.setHuaweiConfig(huaweiConfig);
    }

    /**
     *  jingdong
     * <p>数据库读取并设置京东短信
     * @author :Wind
    */
    public static void jingdong(){
        JdCloudConfig jdCloudConfig = SmsUtil.jsonForObject(select.get(SupplierType.JD_CLOUD.getName()), JdCloudConfig.class);
        SupplierFactory.setJdCloudConfig(jdCloudConfig);
    }

    /**
     *  tencent
     * <p>数据库读取并设置腾讯短信
     * @author :Wind
    */
    public static void tencent(){
        TencentConfig tencentConfig = SmsUtil.jsonForObject(select.get(SupplierType.TENCENT.getName()), TencentConfig.class);
        SupplierFactory.setTencentConfig(tencentConfig);
    }

    /**
     *  uniSms
     * <p>数据库读取并设置合一短信配置
     * @author :Wind
    */
    public static void uniSms(){
        UniConfig uniConfig = SmsUtil.jsonForObject(select.get(SupplierType.UNI_SMS.getName()), UniConfig.class);
        SupplierFactory.setUniConfig(uniConfig);
    }

    /**
     *  yunPian
     * <p>数据库读取并设置云片短信
     * @author :Wind
    */
    public static void yunPian(){
        YunpianConfig yunpianConfig = SmsUtil.jsonForObject(select.get(SupplierType.YUNPIAN.getName()), YunpianConfig.class);
        SupplierFactory.setYunpianConfig(yunpianConfig);
    }

    /**
     *  cloopen
     * <p>数据库读取并设置容联云短信
     * @author :Wind
    */
    public static void cloopen(){
        CloopenConfig cloopenConfig = SmsUtil.jsonForObject(select.get(SupplierType.CLOOPEN.getName()), CloopenConfig.class);
        SupplierFactory.setCloopenConfig(cloopenConfig);
    }

    /**
     * emay
     * <p>数据库读取并设置亿美软通短信
     */
    public static void emay() {
        EmayConfig emayConfig = SmsUtil.jsonForObject(select.get(SupplierType.EMAY.getName()), EmayConfig.class);
        SupplierFactory.setEmayConfig(emayConfig);
    }

    /**
     *  ctyun
     * <p>数据库读取并设置天翼云短信
     * @author :Wind
    */
    public static void ctyun(){
        CtyunConfig ctyunConfig = SmsUtil.jsonForObject(select.get(SupplierType.CTYUN.getName()), CtyunConfig.class);
        SupplierFactory.setCtyunConfig(ctyunConfig);
    }
}
