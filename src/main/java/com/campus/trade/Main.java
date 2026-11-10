package com.campus.trade;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

        String path = "out/products.txt";
        if(new File(path).exists()){
            productManager.loadFromFile(path);
        }else{
            System.out.println("还没有存档，从空列表开始");
        }


        productManager.addIndexName();
        List<Product> searchProducts = productManager.searchByName("哇哈哈");
        for (Product searchProduct : searchProducts) {
            System.out.println(searchProduct.getName());
        }

        System.out.println("-------------------------------------");

        productManager.saveToFile(path);
        productManager.showAll();
    }
}
