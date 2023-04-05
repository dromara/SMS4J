package kim.wind.sms.sql.utils;

import cn.hutool.core.util.StrUtil;
import kim.wind.sms.sql.config.SmsSqlConfig;
import kim.wind.sms.sql.err.SmsSqlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * JDBCTool
 * <p> 数据库相关工具
 *
 * @author :Wind
 * 2023/4/4  22:34
 **/
public class JDBCTool {

    private final SmsSqlConfig config;

    public static final String SELECT = "select {},{} from {} where {} = {}";

    private static final String ERR_CONFIG = "One configuration was expected, but {} was found. Please check your database configuration";


    public JDBCTool(SmsSqlConfig config) {
        if (config == null){
            throw new SmsSqlException("The configuration file failed to be loaded. Procedure");
        }
        this.config = config;
        if (StringUtils.isEmpty(this.config.getDatabaseName())) {
            throw new SmsSqlException("You did not specify a database driver");
        }
    }

    /**
     * 获取链接
     */
    public Connection getConn() {
        Connection connection;
        try {
            String url = config.getUrl() + "/" + config.getDatabaseName();
            connection = DriverManager.getConnection(url, config.getUsername(), config.getPassword());
        } catch (SQLException e) {
            throw new SmsSqlException(e.getMessage());
        }
        return connection;
    }

    /**
     *  select
     * <p>查询封装
     * @param sql 要查询的sql语句
     * @author :Wind
    */
    public Map<String,String> select(String sql) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Map<String,String> map = new Hashtable<>();
        try {
            conn = getConn();
            preparedStatement = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setFetchSize(1000);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String data = resultSet.getString(config.getConfigName());
                String supplier = resultSet.getString(config.getSupplierFieldName());
                map.put(supplier,data);
            }
            if (map.size() != 1){
                throw new SmsSqlException(StrUtil.format(ERR_CONFIG,String.valueOf(map.size())));
            }
        } catch (SQLException e) {
            throw new SmsSqlException(e.getMessage());
        } finally {
            close(conn, preparedStatement, resultSet);
        }
        return map;
    }

    /**
     * close
     * <p>关闭链接方法
     *
     * @author :Wind
     */
    private void close(Connection conn, PreparedStatement stmt, ResultSet rs) {
        // 关闭连接
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new SmsSqlException(e.getMessage());
            }
        }
        // 关闭 statement
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                throw new SmsSqlException(e.getMessage());
            }
        }
        // 关闭结果集
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new SmsSqlException(e.getMessage());
            }
        }
    }

    public Map<String,String> select(){
        String format = StrUtil.format(SELECT,
                config.getConfigName(),
                config.getSupplierFieldName(),
                config.getDatabaseName(),
                config.getStartName(),
                config.getIsStart());
        return select(format);
    }

}
