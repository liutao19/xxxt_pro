package com.dce.business.entity.Realgoods;

import java.math.BigDecimal;
import java.util.Date;

public class goods {
    private Integer goodsid;

    private String goodssn;

    private String title;

    private String goodsimg;

    private Integer goodsthums;

    private Integer brandid;

    private Integer shopid;

    private BigDecimal marketprice;

    private BigDecimal shopprice;

    private Integer goodsstock;

    private Integer salecount;

    private Byte isbook;

    private Integer bookquantity;

    private Integer warnstock;

    private String goodsunit;

    private Byte issale;

    private Byte isbest;

    private Byte ishot;

    private Byte isrecomm;

    private Byte isnew;

    private Byte isadminbest;

    private Byte isadminrecom;

    private String recommdesc;

    private Integer cid1;

    private Integer cid2;

    private Integer cid3;

    private Integer shopcatid1;

    private Integer shopcatid2;

    private Byte isshoprecomm;

    private Byte isindexrecomm;

    private Byte isactivityrecomm;

    private Byte isinnerrecomm;

    private Byte status;

    private Date saletime;

    private Integer attrcatid;

    private String goodskeywords;

    private Byte goodsflag;

    private String statusremarks;

    private Date createtime;

    public Integer getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
    }

    public String getGoodssn() {
        return goodssn;
    }

    public void setGoodssn(String goodssn) {
        this.goodssn = goodssn == null ? null : goodssn.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getGoodsimg() {
        return goodsimg;
    }

    public void setGoodsimg(String goodsimg) {
        this.goodsimg = goodsimg == null ? null : goodsimg.trim();
    }

    public Integer getGoodsthums() {
        return goodsthums;
    }

    public void setGoodsthums(Integer goodsthums) {
        this.goodsthums = goodsthums;
    }

    public Integer getBrandid() {
        return brandid;
    }

    public void setBrandid(Integer brandid) {
        this.brandid = brandid;
    }

    public Integer getShopid() {
        return shopid;
    }

    public void setShopid(Integer shopid) {
        this.shopid = shopid;
    }

    public BigDecimal getMarketprice() {
        return marketprice;
    }

    public void setMarketprice(BigDecimal marketprice) {
        this.marketprice = marketprice;
    }

    public BigDecimal getShopprice() {
        return shopprice;
    }

    public void setShopprice(BigDecimal shopprice) {
        this.shopprice = shopprice;
    }

    public Integer getGoodsstock() {
        return goodsstock;
    }

    public void setGoodsstock(Integer goodsstock) {
        this.goodsstock = goodsstock;
    }

    public Integer getSalecount() {
        return salecount;
    }

    public void setSalecount(Integer salecount) {
        this.salecount = salecount;
    }

    public Byte getIsbook() {
        return isbook;
    }

    public void setIsbook(Byte isbook) {
        this.isbook = isbook;
    }

    public Integer getBookquantity() {
        return bookquantity;
    }

    public void setBookquantity(Integer bookquantity) {
        this.bookquantity = bookquantity;
    }

    public Integer getWarnstock() {
        return warnstock;
    }

    public void setWarnstock(Integer warnstock) {
        this.warnstock = warnstock;
    }

    public String getGoodsunit() {
        return goodsunit;
    }

    public void setGoodsunit(String goodsunit) {
        this.goodsunit = goodsunit == null ? null : goodsunit.trim();
    }

    public Byte getIssale() {
        return issale;
    }

    public void setIssale(Byte issale) {
        this.issale = issale;
    }

    public Byte getIsbest() {
        return isbest;
    }

    public void setIsbest(Byte isbest) {
        this.isbest = isbest;
    }

    public Byte getIshot() {
        return ishot;
    }

    public void setIshot(Byte ishot) {
        this.ishot = ishot;
    }

    public Byte getIsrecomm() {
        return isrecomm;
    }

    public void setIsrecomm(Byte isrecomm) {
        this.isrecomm = isrecomm;
    }

    public Byte getIsnew() {
        return isnew;
    }

    public void setIsnew(Byte isnew) {
        this.isnew = isnew;
    }

    public Byte getIsadminbest() {
        return isadminbest;
    }

    public void setIsadminbest(Byte isadminbest) {
        this.isadminbest = isadminbest;
    }

    public Byte getIsadminrecom() {
        return isadminrecom;
    }

    public void setIsadminrecom(Byte isadminrecom) {
        this.isadminrecom = isadminrecom;
    }

    public String getRecommdesc() {
        return recommdesc;
    }

    public void setRecommdesc(String recommdesc) {
        this.recommdesc = recommdesc == null ? null : recommdesc.trim();
    }

    public Integer getCid1() {
        return cid1;
    }

    public void setCid1(Integer cid1) {
        this.cid1 = cid1;
    }

    public Integer getCid2() {
        return cid2;
    }

    public void setCid2(Integer cid2) {
        this.cid2 = cid2;
    }

    public Integer getCid3() {
        return cid3;
    }

    public void setCid3(Integer cid3) {
        this.cid3 = cid3;
    }

    public Integer getShopcatid1() {
        return shopcatid1;
    }

    public void setShopcatid1(Integer shopcatid1) {
        this.shopcatid1 = shopcatid1;
    }

    public Integer getShopcatid2() {
        return shopcatid2;
    }

    public void setShopcatid2(Integer shopcatid2) {
        this.shopcatid2 = shopcatid2;
    }

    public Byte getIsshoprecomm() {
        return isshoprecomm;
    }

    public void setIsshoprecomm(Byte isshoprecomm) {
        this.isshoprecomm = isshoprecomm;
    }

    public Byte getIsindexrecomm() {
        return isindexrecomm;
    }

    public void setIsindexrecomm(Byte isindexrecomm) {
        this.isindexrecomm = isindexrecomm;
    }

    public Byte getIsactivityrecomm() {
        return isactivityrecomm;
    }

    public void setIsactivityrecomm(Byte isactivityrecomm) {
        this.isactivityrecomm = isactivityrecomm;
    }

    public Byte getIsinnerrecomm() {
        return isinnerrecomm;
    }

    public void setIsinnerrecomm(Byte isinnerrecomm) {
        this.isinnerrecomm = isinnerrecomm;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getSaletime() {
        return saletime;
    }

    public void setSaletime(Date saletime) {
        this.saletime = saletime;
    }

    public Integer getAttrcatid() {
        return attrcatid;
    }

    public void setAttrcatid(Integer attrcatid) {
        this.attrcatid = attrcatid;
    }

    public String getGoodskeywords() {
        return goodskeywords;
    }

    public void setGoodskeywords(String goodskeywords) {
        this.goodskeywords = goodskeywords == null ? null : goodskeywords.trim();
    }

    public Byte getGoodsflag() {
        return goodsflag;
    }

    public void setGoodsflag(Byte goodsflag) {
        this.goodsflag = goodsflag;
    }

    public String getStatusremarks() {
        return statusremarks;
    }

    public void setStatusremarks(String statusremarks) {
        this.statusremarks = statusremarks == null ? null : statusremarks.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}