package com.ruifenglb.www.bean;

public class GoldTipBean {


    /**
     * msg : 最低提现20元
     * info : 当前可提现金额5000元，提现比例10金币=1元
     */

    private String msg;
    private String info;
    private String can_money;


    public String getCan_money() {
        return can_money;
    }

    public void setCan_money(String can_money) {
        this.can_money = can_money;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
