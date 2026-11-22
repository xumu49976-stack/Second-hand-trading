package com.campus.trade;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ProductDao {
    // 声明为 static，全类共享
    private static String url;
    private static String mysqlName;
    private static String password;

    // 静态代码块，在类加载时执行一次
    static {
        Properties props = new Properties();
        // 使用类加载器去 resources 目录下读取文件
        try (InputStream is = ProductDao.class.getClassLoader().getResourceAsStream("jdbc.properties")) {
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

    //将数据库里所有商品封装到List里返回
    List<Product> findAll() throws SQLException {
        String sql = "select * from product";
        List<Product> productList = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(url, mysqlName, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    String name = resultSet.getString("name");
                    BigDecimal price = resultSet.getBigDecimal("price");
                    int stock = resultSet.getInt("stock");
                    Product product = new Product(name, price, stock);
                    productList.add(product);
                }
            }
        }

        return productList;
    }

    //根据id返回数据库里对应商品对象
    Product findById(long id) throws SQLException {
        String sql = "select * from product where id =?";
        try(Connection connection = DriverManager.getConnection(url, mysqlName, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {
            preparedStatement.setLong(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery();)
            {
                if(resultSet.next()){
                    String name = resultSet.getString("name");
                    BigDecimal price = resultSet.getBigDecimal("price");
                    int stock = resultSet.getInt("stock");
                    Product product = new Product(name, price, stock);
                    return product;
                }
            }
        }

        return null;
    }

    //根据商品名返回同名商品列表
    List<Product> findByName(String name) throws SQLException {
        List<Product> productList = new ArrayList<>();
        String sql = "select name, price, stock from product where name = ?";
        try(Connection connection = DriverManager.getConnection(url, mysqlName, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {
            preparedStatement.setString(1, name);
            try(ResultSet resultSet = preparedStatement.executeQuery();)
            {
                while (resultSet.next()){
                    Product product = new Product(resultSet.getString("name"), resultSet.getBigDecimal("price"), resultSet.getInt("stock"));
                    productList.add(product);
                }
            }
        }

        return productList;
    }

    //将传入的product插入到数据库里
    boolean insert(Product product) throws SQLException {
        String productNamename = product.getName();
        BigDecimal productPrice = product.getPrice();
        int productStock = product.getStock();
        int count = 0;
        String sql = "insert into product (name, price, stock) values (?, ?, ?)";
        try(Connection connection = DriverManager.getConnection(url, mysqlName, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {
            preparedStatement.setString(1, productNamename);
            preparedStatement.setBigDecimal(2, productPrice);
            preparedStatement.setInt(3, productStock);
            count = preparedStatement.executeUpdate();
        }

        return count > 0;
    }

    //根据传入商品ID更新数据库商品库存
    boolean updateStock(long id, int stock) throws SQLException {
        String sql = "update product set stock = ? where id = ?";
        int count = 0;
        try(Connection connection = DriverManager.getConnection(url, mysqlName, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {
            preparedStatement.setLong(2, id);
            preparedStatement.setInt(1, stock);
            count = preparedStatement.executeUpdate();
        }

        return count > 0;
    }

    //查询商品否存在
    boolean existsByName(String name) throws SQLException {
        String sql = "select count(*) from product where name = ?";
        int count = 0;
        try(Connection connection = DriverManager.getConnection(url, mysqlName, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {
            preparedStatement.setString(1, name);
            try(ResultSet rs = preparedStatement.executeQuery();)
            {
                if(rs.next()){
                    count = rs.getInt(1);
                }
            }
        }

        return count > 0;
    }

    //根据传入商品ID删除数据库商品
    boolean deleteById(long id) throws SQLException {
        String sql = "delete from product where id = ?";
        int count = 0;
        try(Connection connection = DriverManager.getConnection(url, mysqlName, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {
            preparedStatement.setLong(1, id);
            count = preparedStatement.executeUpdate();
        }

        return count > 0;
    }

}


