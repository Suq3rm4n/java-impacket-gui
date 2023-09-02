package utils;

import java.util.ArrayList;

public class Kinds_Coder {
    private ArrayList<String> Coderlist;

    /**
     * 构造器初始化数据
     */
    public Kinds_Coder() {
        this.coders();
    }

    public ArrayList<String> coders() {
        Coderlist = new ArrayList<>();
        Coderlist.add("UTF-8");
        Coderlist.add("GBK");
        Coderlist.add("UTF-16");
        return Coderlist;
    }

    public ArrayList<String> getKindList() {
        return Coderlist;
    }

}
