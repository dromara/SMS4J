package org.dromara.email.comm.utils;

import org.dromara.email.comm.errors.MailException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * HtmlUtil
 * <p> Html相关工具
 *
 * @author :Wind
 * 2023/6/7  20:15
 **/
public final class HtmlUtil {

    private static final HtmlUtil htmlUtil = new HtmlUtil();

    private HtmlUtil() {
    }

    /**
     * readHtml
     * <p>从resource读取模板文件
     *
     * @param name 模板文件名
     * @author :Wind
     */
    public static List<String> readHtml(String name) throws MailException {
        try (InputStream is = HtmlUtil.class.getResourceAsStream("/template/" + name);) {
            return readHtml(is);
        } catch (IOException e) {
            throw new MailException(e);
        }
    }

    /**
     * readHtml
     * <p>从自定义路径读取模板文件
     *
     * @param file 自定义路径file
     * @author :Wind
     */
    public static List<String> readHtml(File file) throws MailException {
        try (InputStream ip = Files.newInputStream(file.toPath());) {
            return readHtml(ip);
        } catch (IOException e) {
            throw new MailException(e);
        }

    }

    /**
     * readHtml
     * <p>从输入流读取模板文件
     *
     * @param inputStream 输入流
     * @author :Wind
     */
    public static List<String> readHtml(InputStream inputStream) throws MailException {
        List<String> data = new ArrayList<>();
        if (Objects.isNull(inputStream)) {
            throw new MailException("The template could not be found!");
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line);
            }
        } catch (IOException e) {
            throw new MailException(e);
        }
        return data;
    }

    /**
     * replacePlaceholder
     * <p>将所包含占位符的字符串替换为固定值
     *
     * @param data      源数据
     * @param parameter key为占位符名称 value为占位符应替换的值
     * @author :Wind
     */
    public static List<String> replacePlaceholder(List<String> data, Map<String, String> parameter) {
        for (int i = 0; i < data.size(); i++) {
            for (Map.Entry<String, String> s : parameter.entrySet()) {
                String piece = piece(s.getKey());
                if (data.get(i).contains(piece)){
                    String replace = data.get(i).replace(piece, s.getValue());
                    data.set(i,replace);
                }
            }
        }
        return data;
    }

    /**
     * pieceHtml
     * <p>将数据拼合为html
     *
     * @param data 需要拼合的数据
     * @author :Wind
     */
    public static String pieceHtml(List<String> data) {
        StringBuilder sb = new StringBuilder();
        for (String datum : data) {
            sb.append(datum);
            sb.append("\r\n");
        }
        return sb.toString();
    }

    /**
     * piece
     * <p>将参数拼合为完整占位符
     *
     * @author :Wind
     */
    public static String piece(String parameter) {
        return "#{" + parameter + "}";
    }
}
