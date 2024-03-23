package org.dromara.oa.comm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图文消息，一个图文消息支持1到8条图文
 * @author dongfeng
 * 2024-03-23 19:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeTalkRequestArticle {
    // 标题，不超过128个字节，超过会自动截断
    private String title;

    // 描述，不超过512个字节，超过会自动截断
    private String description;

    // 点击后跳转的链接。
    private String url;

    // 图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图 1068*455，小图150*150。
    private String picUrl;
}
