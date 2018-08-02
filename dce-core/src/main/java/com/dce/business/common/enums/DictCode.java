package com.dce.business.common.enums;

public enum DictCode {
    /**
     * 报单会员等级
     */
    BaoDanFei("BaoDanFei"),
    /**
     * 美元点转人民币比例
     */
    Point2RMB("Point2RMB"),
    /**
     * 人民币转美元点比例
     */
    RMB2Point("RMB2Point"),
    /**
     * 当日DEC对人民币价格
     */
    DEC2RMB("DEC2RMB"),
    /**
     * 交易费率
     */
    TRANS_RATE("TRANS_RATE"),
    /**
     * 美元兑人民币汇率
     */
    ExRate("ExRate"),
    /**
     * 客户级别
     */
    KHJB("KHJB"),
    /**
     * 以太坊手续费
     */
    Gas("Gas"),
    /**
     * 美元点N:现持仓 1
     */
    MYDBXCC("MYDBXCC"),
    /**
     * 以太坊手续费最大限额
     */
    GasLimit("GasLimit"),
    BaoDanZengSong("BaoDanZengSong"), LiangPeng("LiangPeng"), LiangPengFengDing("LiangPengFengDing"), LiangPengRate("LiangPengRate"), ZhiTui(
            "ZhiTui"), HuZhuJiaQuan("HuZhuJiaQuan"), LingDao("LingDao"), JiaJin("JiaJin");

    private String code;

    DictCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
