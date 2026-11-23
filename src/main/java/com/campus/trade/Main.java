package com.campus.trade;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 程序入口（对应路线 V0.1 · 验收）
 *
 * ── 验收标准 ─────────────────────────────────────────────
 * 1. 创建至少两个不同的商品对象
 * 2. 展示它们的信息
 * 3. 测试正常修改库存
 * 4. 测试非法输入（价格为负、库存为负）
 *
 * 暂时不做数据库、不做网页、不写菜单。
 *
 * ── 提醒 ────────────────────────────────────────────────
 * 第 4 条容易被糊弄过去：如果你在构造方法里"拦住"了非法价格，
 * 那 main 里那句 new Product("坏商品", -5, 1) 会直接抛异常、程序中断。
 * 这正是你要观察的现象——但要想清楚：这算"处理好"了，还是算"崩了"？
 * 两种答案都可能有道理，取决于你想让程序在这种情况下怎么表现。
 * 先做出来，再想这个。
 */
public class Main {
    public static void main(String[] args) throws IOException {

        ProductManager productManager = new ProductManager();
        List<Product> productList = new ArrayList<>();

        //查库展示
        try {
            productList = productManager.findAll();
            System.out.println(productList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //查询
        try {
            List<Product> products = productManager.searchByName("米雪");
            System.out.println(products);
        }catch (SQLException e){
            e.printStackTrace();
        }

        //增加
        long keyid = 0;
        try {
            Product product = new Product("鸡腿", new BigDecimal("5.00"), 66);
            if( productManager.addProduct(product) ){
                System.out.println("商品增加成功！");
                keyid = product.getId();
            } else {
                System.out.println("商品增加失败！！");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        //修改库存
        try {
            if( productManager.updateStock(keyid, 666) ){
                System.out.println("商品库存修改成功！");
            } else {
                System.out.println("商品库存修改失败！！");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        //删除
        try {
            if( productManager.deleteById(keyid) ){
                System.out.println("商品删除成功！");
            } else {
                System.out.println("商品删除失败！！");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        //查库展示
        try {
            productList = productManager.findAll();
            System.out.println(productList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
