package com.example.needtodo;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class User extends LitePalSupport {

    long id;
    private boolean online;
    private List<ThingsList> thingsListList = new ArrayList<ThingsList>();
    private String name;
    private String account;
    private String sign;
    private String headImage_File;
    private String password;
    public String getHeadImage_File() {
        return headImage_File;
    }
    public void setHeadImage_File(String headImage_File) {
        this.headImage_File = headImage_File;
    }
    public String getSign() {
        return sign;
    }
    public void setSign(String sign) {
        this.sign = sign;
    }
    public boolean isOnline() {
        return online;
    }
    public void setOnline(boolean online) {
        this.online = online;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public List<ThingsList> getThingsListList() {
        return thingsListList;
    }
    public void setThingsListList(List<ThingsList> thingsListList) {
        this.thingsListList = thingsListList;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString() {
        return "User{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
