package com.campus.trade;

/**
 * 商品（对应路线 V0.1 · 任务 1）
 *
 * ── 你要写的 ─────────────────────────────────────────────
 * 1. 三个属性：name（名称）、price（价格）、stock（库存），用 private 修饰
 * 2. 构造方法：创建商品时初始化这三个属性
 * 3. 展示方法：把一件商品的完整信息打印到控制台
 * 4. 改库存的方法：提供一个合理的方式调整库存
 * 5. 基本校验：价格不允许为负，库存不允许为负
 *
 * ── 动手之前，先回答这三个问题（路线第 10 节给的）──────────
 * 1. 哪些数据应该属于一个商品对象？
 * 2. 构造方法和普通方法有什么区别？
 * 3. 创建对象之后，怎样调用它的方法？
 *
 * ── 两个我没有替你决定的问题 ────────────────────────────
 * · 属性加了 private 之后，外部还能不能直接 p.name = "x"？如果不能，你要补什么？
 * · 校验放在构造方法里、放在 set 方法里、还是两处都放？这三种选择的后果各是什么？
 *
 * 先别急着敲。把你的想法告诉我，我只指出问题，不给答案。
 */
public class Product {
    private String name;
    private double price;
    private int stock;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }
    public boolean setPrice(double price) {
        if(price < 0){
            System.out.println("商品创建失败，价格不可为负");
            return false;
        }else{
            this.price = price;
            return true;
        }
    }

    public int getStock() {
        return stock;
    }
    public boolean setStock(int stock) {
        if(stock < 0){
            System.out.println("商品创建失败，库存不可为负");
            return false;
        }else{
            this.stock = stock;
            return true;
        }
    }

    public Product(String name, double price, int stock) {
        this.name = name;
        this.setPrice(price);
        this.setStock(stock);
    }

    public void showProduct(){
        System.out.println("商品具体信息：");
        System.out.println("名称："+this.name);
        System.out.println("价格："+this.price);
        System.out.println("库存："+this.stock);
    }

    public void stockChange(String symbol, int quantity){
        if(quantity <= 0){
            System.out.println("要改变的数量异常");
            return;
        }

        if(symbol == "+"){
            this.stock += quantity;
        }else if(symbol == "-"){
            if(quantity > this.stock){
                System.out.println("库存量不足以拿出"+quantity+"个");
            }else {
                this.stock -= quantity;
            }
        }else{
            System.out.println("库存修改失败！");
        }
    }
}
