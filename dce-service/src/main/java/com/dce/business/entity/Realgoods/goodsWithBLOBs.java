package com.dce.business.entity.Realgoods;

public class goodsWithBLOBs extends goods {
    private String goodsspec;

    private String goodsdesc;

    public String getGoodsspec() {
        return goodsspec;
    }

    public void setGoodsspec(String goodsspec) {
        this.goodsspec = goodsspec == null ? null : goodsspec.trim();
    }

    public String getGoodsdesc() {
        return goodsdesc;
    }

    public void setGoodsdesc(String goodsdesc) {
        this.goodsdesc = goodsdesc == null ? null : goodsdesc.trim();
    }
}