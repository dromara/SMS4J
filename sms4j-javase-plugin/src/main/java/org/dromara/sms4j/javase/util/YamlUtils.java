package org.dromara.sms4j.javase.util;

import cn.hutool.core.util.StrUtil;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;

public class YamlUtils {

    /**
     * 构建Yaml实例
     * @param cls 类
     * @return Yaml实例
     */
    public static Yaml buildYamlInstance(Class<?> cls){
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setEnumCaseSensitive(false);
        Constructor c = new Constructor(cls, loaderOptions);
        c.setPropertyUtils(new PropertyUtils() {
            @Override
            public Property getProperty(Class<?> type, String name) {
                name = StrUtil.toCamelCase(name, '-');
                //忽略yaml中无法在类中找到属性的字段
                setSkipMissingProperties(true);
                return super.getProperty(type, name);
            }
        });
        c.setEnumCaseSensitive(false);
        return new Yaml(c);
    }

    /**
     * 读取配置内容
     * @param content 内容
     * @param cls 类
     * @return 读取内容
     */
    public static <T> T toBean(String content, Class<T> cls) {
        Yaml yaml = YamlUtils.buildYamlInstance(cls);
        return yaml.loadAs(content, cls);
    }

}
