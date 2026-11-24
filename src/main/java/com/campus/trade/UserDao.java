package com.campus.trade;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class UserDao {
    // 声明为 static，全类共享
    private static String url;
    private static String mysqlName;
    private static String password;

    // 静态代码块，在类加载时执行一次
    static {
        Properties props = new Properties();
        // 使用类加载器去 resources 目录下读取文件
        try (InputStream is = UserDao.class.getClassLoader().getResourceAsStream("jdbc.properties")) {
            if (is == null) {
                System.out.println("严重错误：找不到 jdbc.properties 文件！");
            }
            props.load(is);

            // 赋值给静态变量
            url = props.getProperty("jdbc.url");
            mysqlName = props.getProperty("jdbc.username");
            password = props.getProperty("jdbc.password");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //按用户名查找对应用户
    User findByUsername(String username) throws SQLException {
        String sql = "select * from users where username = ?";
        try(Connection connection = DriverManager.getConnection(url, mysqlName, password);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {
            preparedStatement.setString(1, username);
            try(ResultSet resultSet = preparedStatement.executeQuery();){
                if(resultSet.next()){
                    long id = resultSet.getLong("id");
                    String userName = resultSet.getString("username");
                    String password = resultSet.getString("password_hash");
                    String nickname = resultSet.getString("nickname");
                    User user = new User(id, userName, password, nickname);
                    return user;
                }
            }
        }

        return null;
    }


    //按用户名查看是否存在
    boolean existsByUsername(String username) throws SQLException {
        String sql = "select count(*) from users where username = ?";
        int count = 0;
        try(Connection connection = DriverManager.getConnection(url, mysqlName, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {
            preparedStatement.setString(1, username);
            try(ResultSet rs = preparedStatement.executeQuery();)
            {
                if(rs.next()){
                    count = rs.getInt(1);
                }
            }
        }

        return count > 0;
    }
}
