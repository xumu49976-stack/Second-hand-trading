package com.campus.trade;

import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.*;

/**
 * 商品管理器（对应路线 V0.1 · 任务 2）
 *
 * ── 你要写的 ─────────────────────────────────────────────
 * 把「新增、展示、查询、删除」从 main 里搬到这里，
 * 让 Main 只负责组织流程，ProductManager 负责管理商品。
 *
 * ── 但这里有个路线里没写清的地方，得先定 ──────────────────
 * 「管理多个商品」必须先有个地方把它们存起来。ArrayList 要第 5–8 周才学。
 * 所以现在有两条路：
 *
 *   A. 先用数组顶着：Product[] + 一个 count 字段记录真实数量，
 *      学完集合之后把数组换成 ArrayList。你现在已经会数组了，这条路走得通。
 *
 *   B. 这一步先不存多个：ProductManager 只写「针对单个商品」的方法，
 *      等学完集合再补上「存多个商品」的能力。V0.1 的验收其实只要求
 *      在 main 里创建两个商品并展示，暂时不强制要管理器存一堆。
 *
 * 选 A 还是 B？告诉我理由，我们一起看哪个更适合你现在的水平。
 * 我不替你选，因为这两条路要练的东西不一样。
 */
public class ProductManager {
    private ArrayList<Product> products = new ArrayList<>();
    private Map<String, List<Product>> indexByName = new HashMap<>();


    public void addProduct(Product p){
        products.add(p);
        addIndexName(p);
    }
    public void showAll(){
        for (Product p : products) {
            if(p == null){ continue; }
            p.showProduct();
        }
    }

    public Product findByName(String name){
        for (Product p : products){
            if(name.equals(p.getName())){
                return p;
            }
        }
        return null;
    }

    public boolean removeByName(String name){
        for(Product p : products){
            if(name.equals(p.getName())){
                removeIndexName(p);
                products.remove(p);
                return true;
            }
        }
        return false;
    }
    //将products里的商品写入文件 （一次性全部保存）
    public void saveToFile(String path) throws IOException {
        File file = new File(path);

        //加父目录兜底
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) { parent.mkdirs(); }

        //按行写入商品数据
        FileWriter fileWriter = new FileWriter(file, false);
        //文件路径不存在时：FileWriter 会自动建文件
        fileWriter.write("名称\t\t"+ "价格\t\t"+ "库存\n");
        String name;
        double price = 0;
        int stock = 0;
        for (Product product : products ){
            name = product.getName();
            price = product.getPrice();
            stock = product.getStock();
            fileWriter.write(name+ "\t\t"+ price+ "\t\t"+ stock+ "\n");
        }
        fileWriter.close();
    }

    //将文件里的商品读取到products
    public void loadFromFile(String path) throws IOException {
        File file = new File(path);
        //抛出文件路径不存在的异常
        if (!file.exists()) {
            throw new NoSuchFileException("找不到该文件：" + path);
        }

        // 1. 使用 BufferedReader 按行读取，效率高且方便
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;

            // 2. 清空当前列表（避免重复加载导致数据翻倍）
            products.clear();

            while ((line = br.readLine()) != null) {
                // 3. 跳过第一行的表头（标题行）
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                // 4. 跳过空行
                if (line.trim().isEmpty()) {
                    continue;
                }

                // 5. 按制表符切割。注意：saveToFile 里写的是 "\t\t"（两个制表符）
                // 使用 "\\t+" 可以匹配一个或多个制表符，防止因空格数量对不上导致解析失败
                String[] parts = line.split("\\t+");

                // 确保切割出来的数据有 3 个部分（名称、价格、库存）
                if (parts.length >= 3) {
                    String name = parts[0].trim();
                    double price = Double.parseDouble(parts[1].trim());
                    int stock = Integer.parseInt(parts[2].trim());

                    // 6. 用写好的 Product 构造方法创建对象，并添加到列表
                    // 注意：构造方法里会对价格和库存进行校验，如果文件里存有非法数据会抛异常
                    Product p = new Product(name, price, stock);
                    products.add(p);
                }
            }
        }
    }

    public boolean exists(String name){
        for ( Product product : products ) {
            if( product.getName().equals(name) ){ return true; }
        }
        return false;
    }



    //把products里同名product放入同一个Arrylist
    public void addIndexName(){
        //清空indexByName，每次调用即重建indexByName
        indexByName.clear();

        String name;
        for (Product product : products) {
            name = product.getName();
            //indexByName里没有就新建list反之就添加
            if( !(indexByName.containsKey(name)) ){
                //先创建name键所对应的vale
                List<Product> productList = new ArrayList<>();

                //添加第一个名叫name的商品
                productList.add(product);
                indexByName.put(name, productList);
            }else {
                indexByName.get(name).add(product);
            }

            //上述精简：indexByName.computeIfAbsent(name, k -> new ArrayList<>()).add(product);
        }
    }
    //重载方法给addProduct等调用以同步indexByName
    private void addIndexName(Product product){
        String name = product.getName();
        if( !(indexByName.containsKey(name)) ){
            //先创建name键所对应的vale
            List<Product> productList = new ArrayList<>();

            //添加第一个名叫name的商品
            productList.add(product);
            indexByName.put(name, productList);
        }else {
            indexByName.get(name).add(product);
        }
    }

    //removeByName调用以同步indexByName
    private void removeIndexName(Product product){
        String name = product.getName();
        List<Product> productsList = indexByName.get(name);
        if( productsList == null ){
            return;
        }
        productsList.remove(product);
        // 如果删完这个 key 对应的 list 空了，就把整个键值对删掉
        if(productsList.isEmpty()){
            indexByName.remove(name);
        }
    }



    //通过名称返回 同名商品list
    public List<Product> searchByName(String name){
        return indexByName.getOrDefault(name, Collections.emptyList());
    }

}
