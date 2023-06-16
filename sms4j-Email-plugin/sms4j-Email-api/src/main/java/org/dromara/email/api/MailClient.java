package org.dromara.email.api;

import java.io.InputStream;
import java.lang.String;
import java.util.List;
import java.util.Map;

public interface MailClient {

    /**
     *  sendMail
     * <p> 发送纯文本邮件
     * @param mailAddress 收件人地址
     * @param title 邮件标题
     * @param body 邮件正文
     * @author :Wind
    */
    void sendMail(String mailAddress, String title ,String body);

    /**
     *  sendMail
     * <p> 群体发送纯文本邮件
     * @param mailAddress 收件人地址,添加多个
     * @param title 邮件标题
     * @param body 邮件正文
     * @author :Wind
    */
    void sendMail(List<String> mailAddress ,String title ,String body);

    /**
     *  sendEmail
     * <p>发送带有附件的文本邮件
     * @param mailAddress 收件人地址
     * @param title 邮件标题
     * @param body 邮件正文
     * @param files 附件，可添加多个
     * @author :Wind
    */
    void sendEmail(String mailAddress, String title, String body,Map<String,String> files);

    /**
     *  sendEmail
     * <p>发送带有附件的文本邮件
     * @param mailAddress 收件人地址 多个收件人地址请按英文','字符隔开
     * @param title 邮件标题
     * @param body 邮件正文
     * @param zipName 压缩包名称 比如 附件.zip
     * @param files 附件，可添加多个
     * @author :Wind
     */
    void sendEmail(String mailAddress, String title, String body, String zipName, Map<String,String> files);

    /**
     *  sendEmail
     * <p>群体发送带有附件的文本邮件
     * @param mailAddress 收件人地址，添加多个
     * @param title 邮件标题
     * @param body 邮件正文
     * @param files 附件，可添加多个
     * @author :Wind
     */
    void sendEmail(List<String> mailAddress, String title, String body, Map<String,String> files);

    /**
     *  sendEmail
     * <p>单人发送带有附件的文本邮件,同时可以携带密送人和抄送人
     * <p>需要注意的是，密送人和抄送人也不能存在于黑名单内，否则也会被过滤
     * @param mailAddress 收件人地址，添加多个
     * @param title 邮件标题
     * @param body 邮件正文
     * @param files 附件，可添加多个
     * @author :Wind
     */
    void sendEmail(String mailAddress, String title, String body,List<String> cc,List<String> bcc,Map<String,String> files);

    /**
     *  sendEmail
     * <p>群体发送带有附件的文本邮件,同时可以携带密送人和抄送人
     * <p>需要注意的是，密送人和抄送人也不能存在于黑名单内，否则也会被过滤
     * @param mailAddress 收件人地址，添加多个
     * @param title 邮件标题
     * @param body 邮件正文
     * @param files 附件，可添加多个
     * @author :Wind
    */
    void sendEmail(List<String> mailAddress, String title, String body,List<String> cc,List<String> bcc,Map<String,String> files);

    /**
     *  sendHtml
     * <p> 读取模板发送html邮件，无正文
     * <p> 将默认读取resources/template下的html文件，第三个参数为html的名称，需携带尾缀
     * @param mailAddress 收件人地址
     * @param title 邮件标题
     * @param htmlName 邮件正文
     * @param parameter key为模板的变量名称 无需携带大括号  value为模板变量所对应的值
     * @author :Wind
    */
    void sendHtml(String mailAddress, String title , String htmlName, Map<String,String> parameter);

    /**
     *  sendHtml
     * <p> 读取模板发送html邮件，无正文
     * <p> 将默认读取resources/template下的html文件，第三个参数为html的名称，需携带尾缀
     * @param mailAddress 收件人地址，添加多个
     * @param title 邮件标题
     * @param htmlName 邮件正文
     * @param parameter key为模板的变量名称 无需携带大括号  value为模板变量所对应的值
     * @author :Wind
     */
    void sendHtml(List<String> mailAddress, String title , String htmlName, Map<String,String> parameter);

    /**
     *  sendHtml
     * <p> 读取模板发送html邮件,无正文
     * <p> 将默认读取resources/template下的html文件，第三个参数为html的名称，需携带尾缀
     * <p> 用户可以自己编写一个实体类，并实现Parameter接口，编写get和set方法，这样一来字段的名称则为模板变量名称，对象的值则为模板变量的值
     * @param mailAddress 收件人地址
     * @param title 邮件标题
     * @param htmlName 邮件模板名称
     * @param parameter 实体
     * @author :Wind
     */
    void sendHtml(String mailAddress, String title , String htmlName, Parameter parameter);

    /**
     *  sendHtml
     * <p> 读取模板发送html邮件,无正文
     * <p> 将默认读取resources/template下的html文件，第三个参数为html的名称，需携带尾缀
     * <p> 用户可以自己编写一个实体类，并实现Parameter接口，编写get和set方法，这样一来字段的名称则为模板变量名称，对象的值则为模板变量的值
     * @param mailAddress 收件人地址，添加多个
     * @param title 邮件标题
     * @param htmlName 邮件模板名称
     * @param parameter 实体
     * @author :Wind
     */
    void sendHtml(List<String> mailAddress, String title , String htmlName, Parameter parameter);

    /**
     *  sendHtml
     * <p> 读取模板发送html邮件,无正文，带附件
     * <p> 将默认读取resources/template下的html文件，第三个参数为html的名称，需携带尾缀
     * @param mailAddress 收件人地址
     * @param title 邮件标题
     * @param htmlName 邮件模板名称
     * @param parameter 实体
     * @param files 附件，可添加多个
     * @author :Wind
     */
    void sendHtml(String mailAddress, String title , String htmlName,Map<String,String> parameter,Map<String,String> files);

    /**
     *  sendHtml
     * <p> 读取模板发送html邮件,无正文，带附件
     * <p> 将默认读取resources/template下的html文件，第三个参数为html的名称，需携带尾缀
     * @param mailAddress 收件人地址，添加多个
     * @param title 邮件标题
     * @param htmlName 邮件模板名称
     * @param parameter 实体
     * @param files 附件，可添加多个
     * @author :Wind
     */
    void sendHtml(List<String> mailAddress, String title , String htmlName,Map<String,String> parameter,Map<String,String> files);

    /**
     *  sendHtml
     * <p> 读取模板发送html邮件,无正文，带附件
     * <p> 将默认读取resources/template下的html文件，第三个参数为html的名称，需携带尾缀
     * <p> 用户可以自己编写一个实体类，并实现Parameter接口，编写get和set方法，这样一来字段的名称则为模板变量名称，对象的值则为模板变量的值
     * @param mailAddress 收件人地址
     * @param title 邮件标题
     * @param htmlName 邮件模板名称
     * @param parameter 实体
     * @param files 附件，可添加多个
     * @author :Wind
     */
    void sendHtml(String mailAddress, String title , String htmlName,Parameter parameter,Map<String,String> files);

    /**
     *  sendHtml
     * <p> 读取模板发送html邮件,无正文，带附件
     * <p> 将默认读取resources/template下的html文件，第三个参数为html的名称，需携带尾缀
     * <p> 用户可以自己编写一个实体类，并实现Parameter接口，编写get和set方法，这样一来字段的名称则为模板变量名称，对象的值则为模板变量的值
     * @param mailAddress 收件人地址，添加多个
     * @param title 邮件标题
     * @param htmlName 邮件模板名称
     * @param parameter 实体
     * @param files 附件，可添加多个
     * @author :Wind
     */
    void sendHtml(List<String> mailAddress, String title , String htmlName,Parameter parameter,Map<String,String> files);

    /**
     *  sendHtml
     * <p> 读取模板发送html邮件,并携带正文
     * <p> 将默认读取resources/template下的html文件，第四个参数为html的名称，需携带尾缀
     * @param mailAddress 收件人地址
     * @param title 邮件标题
     * @param body 邮件文本正文
     * @param htmlName 邮件正文
     * @param parameter key为模板的变量名称 无需携带大括号  value为模板变量所对应的值
     * @author :Wind
     */
    void sendHtml(String mailAddress, String title ,String body, String htmlName, Map<String,String> parameter);

    /**
     *  sendHtml
     * <p> 读取模板发送html邮件,并携带正文
     * <p> 将默认读取resources/template下的html文件，第四个参数为html的名称，需携带尾缀
     * @param mailAddress 收件人地址，添加多个
     * @param title 邮件标题
     * @param body 邮件文本正文
     * @param htmlName 邮件正文
     * @param parameter key为模板的变量名称 无需携带大括号  value为模板变量所对应的值
     * @author :Wind
     */
    void sendHtml(List<String> mailAddress, String title ,String body, String htmlName, Map<String,String> parameter);

    /**
     *  sendHtml
     * <p> 读取模板发送html邮件,并携带正文
     * <p> 将默认读取resources/template下的html文件，第四个参数为html的名称，需携带尾缀
     * <p> 用户可以自己编写一个实体类，并实现Parameter接口，编写get和set方法，这样一来字段的名称则为模板变量名称，对象的值则为模板变量的值
     * @param mailAddress 收件人地址
     * @param title 邮件标题
     * @param body 邮件文本正文
     * @param htmlName 邮件正文
     * @param parameter 实体
     * @author :Wind
     */
    void sendHtml(String mailAddress, String title ,String body, String htmlName, Parameter parameter);

    /**
     *  sendHtml
     * <p> 读取模板发送html邮件,并携带正文
     * <p> 将默认读取resources/template下的html文件，第四个参数为html的名称，需携带尾缀
     * <p> 用户可以自己编写一个实体类，并实现Parameter接口，编写get和set方法，这样一来字段的名称则为模板变量名称，对象的值则为模板变量的值
     * @param mailAddress 收件人地址，添加多个
     * @param title 邮件标题
     * @param body 邮件文本正文
     * @param htmlName 邮件正文
     * @param parameter 实体
     * @author :Wind
     */
    void sendHtml(List<String> mailAddress, String title ,String body, String htmlName, Parameter parameter);

    /**
     *  sendHtml
     * <p> 读取模板发送html邮件,并携带正文和附件
     * <p> 将默认读取resources/template下的html文件，第四个参数为html的名称，需携带尾缀
     * @param mailAddress 收件人地址
     * @param title 邮件标题
     * @param body 邮件文本正文
     * @param htmlName 邮件正文
     * @param parameter key为模板的变量名称 无需携带大括号  value为模板变量所对应的值
     * @param files 附件，可添加多个 key 为文件名，value为文件的路径
     * @author :Wind
     */
    void sendHtml(String mailAddress, String title ,String body, String htmlName, Map<String,String> parameter,Map<String,String> files);

    /**
     *  sendHtml
     * <p> 读取模板发送html邮件,并携带正文和附件
     * <p> 将默认读取resources/template下的html文件，第四个参数为html的名称，需携带尾缀
     * @param mailAddress 收件人地址 多个收件人地址请按英文','字符隔开
     * @param title 邮件标题
     * @param body 邮件文本正文 可为空
     * @param htmlName 邮件正文
     * @param parameter key为模板的变量名称 无需携带大括号  value为模板变量所对应的值
     * @param zipName 压缩包名称 比如 附件.zip
     * @param files 附件，可添加多个 key 为文件名，value为文件的路径
     * @author :bleachtred
     */
    void sendHtml(String mailAddress, String title, String body, String htmlName, Map<String,String> parameter, String zipName, Map<String,String> files);

    /**
     *  sendHtml
     * <p> 读取模板发送html邮件,并携带正文和附件
     * <p> 将默认读取resources/template下的html文件，第四个参数为html的名称，需携带尾缀
     * @param mailAddress 收件人地址 多个收件人地址请按英文','字符隔开
     * @param title 邮件标题
     * @param body 邮件文本正文 可为空
     * @param htmlName 邮件正文
     * @param parameter 字段名称为变量名称，字段值为变量值
     * @param zipName 压缩包名称 比如 附件.zip
     * @param files 附件，可添加多个 key 为文件名，value为文件的路径
     * @author :Wind
     */
    void sendHtml(String mailAddress, String title, String body, String htmlName, Parameter parameter, String zipName, Map<String,String> files);

    /**
     *  sendHtml
     * <p> 读取模板发送html邮件,并携带正文和附件
     * <p> 将默认读取resources/template下的html文件，第四个参数为html的名称，需携带尾缀
     * @param mailAddress 收件人地址，添加多个
     * @param title 邮件标题
     * @param body 邮件文本正文
     * @param htmlName 邮件正文
     * @param parameter key为模板的变量名称 无需携带大括号  value为模板变量所对应的值
     * @param files 附件，可添加多个 key 为文件名，value为文件的路径
     * @author :Wind
     */
    void sendHtml(List<String> mailAddress, String title ,String body, String htmlName, Map<String,String> parameter,Map<String,String> files);

    /**
     *  sendHtml
     * <p> 读取模板发送html邮件,并携带正文和附件
     * <p> 将默认读取resources/template下的html文件，第四个参数为html的名称，需携带尾缀
     * <p> 用户可以自己编写一个实体类，并实现Parameter接口，编写get和set方法，这样一来字段的名称则为模板变量名称，对象的值则为模板变量的值
     * @param mailAddress 收件人地址
     * @param title 邮件标题
     * @param body 邮件文本正文
     * @param htmlName 邮件正文
     * @param parameter 字段名称为变量名称，字段值为变量值
     * @param files 附件，可添加多个 key 为文件名，value为文件的路径
     * @author :Wind
     */
    void sendHtml(String mailAddress, String title ,String body, String htmlName, Parameter parameter,Map<String,String> files);

    /**
     *  sendHtml
     * <p> 读取模板发送html邮件,并携带正文和附件
     * <p> 将默认读取resources/template下的html文件，第四个参数为html的名称，需携带尾缀
     * <p> 用户可以自己编写一个实体类，并实现Parameter接口，编写get和set方法，这样一来字段的名称则为模板变量名称，对象的值则为模板变量的值
     * @param mailAddress 收件人地址，添加多个
     * @param title 邮件标题
     * @param body 邮件文本正文
     * @param htmlName 邮件正文
     * @param parameter 字段名称为变量名称，字段值为变量值
     * @param files 附件，可添加多个 key 为文件名，value为文件的路径
     * @author :Wind
     */
    void sendHtml(List<String> mailAddress, String title ,String body, String htmlName, Parameter parameter,Map<String,String> files);

    /**
     *  sendHtml
     * <p> 读取模板发送html邮件,并携带正文和附件
     * <p> 从用户给定的输入流获取html模板文件
     * <p> 用户可以自己编写一个实体类，并实现Parameter接口，编写get和set方法，这样一来字段的名称则为模板变量名称，对象的值则为模板变量的值
     * @param mailAddress 收件人地址，添加多个
     * @param title 邮件标题
     * @param body 邮件文本正文
     * @param html html模板的输入流，这个流可以来自任何来源，例如网络请求，或是本地文件，或者对象存储等
     * @param parameter key为模板的变量名称 无需携带大括号  value为模板变量所对应的值
     * @param files 附件，可添加多个 key 为文件名，value为文件的路径
     * @author :Wind
     */
    void sendHtml(List<String> mailAddress, String title , String body, InputStream html, Map<String, String> parameter, Map<String,String> files);

    /**
     *  sendHtml
     * <p> 读取模板发送html邮件,并携带正文和附件
     * <p> 从用户给定的输入流获取html模板文件
     * <p> 用户可以自己编写一个实体类，并实现Parameter接口，编写get和set方法，这样一来字段的名称则为模板变量名称，对象的值则为模板变量的值
     * @param mailAddress 收件人地址，添加多个
     * @param title 邮件标题
     * @param body 邮件文本正文
     * @param html html模板的输入流,这个流可以来自任何来源，例如网络请求，或是本地文件，或者对象存储等
     * @param parameter 字段名称为变量名称，字段值为变量值
     * @param files 附件，可添加多个 key 为文件名，value为文件的路径
     * @author :Wind
     */
    void sendHtml(List<String> mailAddress, String title ,String body, InputStream html, Parameter parameter,Map<String,String> files);
}
