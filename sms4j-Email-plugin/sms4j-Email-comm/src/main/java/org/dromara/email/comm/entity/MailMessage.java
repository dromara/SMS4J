package org.dromara.email.comm.entity;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import lombok.Builder;
import lombok.Getter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class MailMessage {

    /**
     * 收件人地址
     */
    private String mailAddress;

    /**
     * 邮件主题
     */
    private String title;

    /**
     * 文字正文
     */
    private String body;

    /**
     * html模板文件路径（resources目录下的路径）
     */
    private String htmlPath;

    /**
     * html模板文件的输入流，可来自任意可读取位置
     */
    private InputStream htmlInputStream;

    /**
     * html 模板参数
     */
    private Map<String, String> htmlValues;

    /**
     * zip文件名称
     */
    private String zipName;

    /**
     * 抄送人
     */
    private List<String> cc;

    /**
     * 密送人
     */
    private List<String> bcc;

    /**
     * 附件
     */
    private Map<String, String> files;

    public List<String> getMailAddressList() {
        return Convert.toList(String.class, mailAddress);
    }
}
