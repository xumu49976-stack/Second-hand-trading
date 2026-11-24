package com.campus.trade;

import java.math.BigDecimal;

public class User {
    private long id;
    private String username;
    private String passwordhash;
    private String nickname;

    /** 仅供从数据库重建对象，业务代码请用 3 参构造 */
    public User(long id, String username, String password, String nickname) {
        this.setId(id);
        if( !this.setUsername(username) ){
            throw new IllegalArgumentException("用户名不能为空！");
        }
        if( !this.setNickname(nickname) ){
            throw new IllegalArgumentException("昵称不能为空！");
        }
        if( !this.setPasswordhash(password) ){
            throw new IllegalArgumentException("密码不能为空！");
        }
    }
    //实例化构造
    public User(String username, String password, String nickname) {
        this(0L, username, password, nickname);   // 0 表示"还没入库"
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public boolean setUsername(String name) {
        if(name == null || name.isEmpty()){
            return false;
        }else {
            this.username = name;
            return true;
        }
    }

    public String getPasswordhash() {
        return passwordhash;
    }
    public boolean setPasswordhash(String password) {
        if(password == null || password.isEmpty()){
            return false;
        }else {
            this.passwordhash = password;
            return true;
        }
    }

    public String getNickname() {
        return nickname;
    }
    public boolean setNickname(String name) {
        if(name == null || name.isEmpty()){
            return false;
        }else {
            this.nickname = name;
            return true;
        }
    }

    @Override
    public boolean equals(Object o){
        if(this == o){ return true; }
        if(o == null || getClass() != o.getClass()){ return false; }
        User u = (User) o;

        if (id != 0 && u.id != 0) {        // 都入库了 → 按主键
            return id == u.id;
        }
        return false;                      // 有一个没入库 → 只有同一引用才相等
    }

    /** 约定**：对象入库后才可以当集合的 key（最省事，靠自律）*/
    @Override
    public int hashCode() {
        // 已入库按主键算，未入库按内存地址算
        return id != 0 ? Long.hashCode(id) : System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password_hash=" + "*****" +
                ", nickname=" + nickname +
                '}';
    }




}
