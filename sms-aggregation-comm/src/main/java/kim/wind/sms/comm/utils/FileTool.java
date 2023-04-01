package kim.wind.sms.comm.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileTool {

    private FileTool(){}

    /**
     *  getPath
     * <p>获取用户工作目录
     * @author :Wind
    */
    public static String getPath(){
      return System.getProperty("user.dir");
    }

    /**
     *  createFile
     * <p>创建文件
     * @param path 要创建的文件路径
     * @return 文件创建成功或存在则为true
     * @author :Wind
    */
    public static boolean createFile(String path){
        File file = new File(path);
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        return file.exists();
    }

    /**
     *  writeFile
     * <p>写入文件
     * @param isAppend 是否追加写入，true追加写入，false覆盖写入
     * @author :Wind
    */
    public static void writeFile(File file,String content,boolean isAppend){
        Writer writer = null;
        try {
            writer = new FileWriter(file,isAppend);
            writer.write(content);
        }catch (IOException e){
            throw new RuntimeException(e);
        }finally {
            try {
                writer.close();
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    /**
     *  readFile
     * <p>读取文件为字符串
     * @param path 文件路径
     * @author :Wind
    */
    public static String readFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
    }
}
