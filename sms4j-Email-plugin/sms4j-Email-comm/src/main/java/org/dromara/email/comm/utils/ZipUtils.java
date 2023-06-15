package org.dromara.email.comm.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.http.HttpUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩包处理类
 *
 * @author Bleachtred
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZipUtils extends ZipUtil {
    private final static Integer TEMP_SIZE = 2048;

    /**
     * 压缩方法（支持 本地文件/目录 + oss网络路径 混合）
     * @param files 文件列表
     * @author Bleachtred
     */
    public static void zipFilePip(Map<String, String> files, OutputStream outputStream) {
        try(WritableByteChannel out = Channels.newChannel(outputStream)) {
            Pipe pipe = Pipe.open();
            //异步任务
            CompletableFuture.runAsync(() -> runTask(pipe, files));
            //获取读通道
            try (ReadableByteChannel readableByteChannel = pipe.source()) {
                ByteBuffer buffer = ByteBuffer.allocate(TEMP_SIZE);
                while (readableByteChannel.read(buffer) >= 0) {
                    buffer.flip();
                    out.write(buffer);
                    buffer.clear();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void runTask(Pipe pipe, Map<String, String> files) {
        try(ZipOutputStream zos = new ZipOutputStream(Channels.newOutputStream(pipe.sink()));
            WritableByteChannel out = Channels.newChannel(zos)) {
            for (Map.Entry<String, String> entry : files.entrySet()) {
                taskFunction(zos, out, entry.getKey(), entry.getValue());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 打包文件
     * @param zos 压缩包输出
     * @param out 缓冲区通道
     * @param fileName 文件名称
     * @param file 文件
     * @throws IOException IOException
     */
    private static void taskFunction(ZipOutputStream zos, WritableByteChannel out, String fileName, File file) throws IOException {
        // 是否为目录
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            fileName = StrUtil.isEmpty(fileName) ? file.getName() + "/" : fileName + "/";
            if (files == null || files.length == 0){
                return;
            }
            for (File child : files) {
                taskFunction(zos, out, fileName + child.getName(), child);
            }
        } else {
            fileName = StrUtil.isEmpty(fileName) ? file.getName() : fileName;
            zos.putNextEntry(new ZipEntry(fileName));
            try(FileInputStream fis = new FileInputStream(file.getAbsolutePath())){
                FileChannel fileChannel = fis.getChannel();
                fileChannel.transferTo(0, fileChannel.size(), out);
                fileChannel.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    private static void taskFunction(ZipOutputStream zos, WritableByteChannel out, String fileName, String file) throws IOException {
        // 网络文件
        if (file.startsWith("http")) {
            zos.putNextEntry(new ZipEntry(fileName));
            byte[] bytes = HttpUtil.downloadBytes(file);
            out.write(ByteBuffer.wrap(bytes));
        }else {
            taskFunction(zos, out, fileName, new File(file));
        }
    }
}
