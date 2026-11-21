package com.campus.trade;

import java.sql.SQLException;
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

    ProductDao productDao = new ProductDao();


    //将商品添加入数据库
    public boolean addProduct(Product p) throws SQLException {
        return productDao.insert(p);
    }
    //从数据库里拿到包含所有商品的List类
    public List<Product> findAll() throws SQLException {
        return productDao.findAll();
    }

    //根据ID更新库存
    public boolean updateStock(long id, int stock) throws SQLException {
        if(stock < 0){
            System.out.println("库存不能为负！");
            return false;
        }
        return productDao.updateStock(id, stock);
    }

    //根据ID删除商品
    public boolean deleteById(long id) throws SQLException {
        return productDao.deleteById(id);
    }

    //按名称查看是否存在
    public boolean exists(String name) throws SQLException {
        return productDao.existsByName(name);
    }

    //通过名称返回 同名商品list
    public List<Product> searchByName(String name) throws SQLException {
        return productDao.findByName(name);
    }

}
