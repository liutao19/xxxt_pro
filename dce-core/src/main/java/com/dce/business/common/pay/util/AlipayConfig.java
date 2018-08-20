package com.dce.business.common.pay.util;

/**
 * 支付宝公用参数
 * 
 * @author Administrator
 *
 */
public class AlipayConfig {

	public static String APPID = "2018080960938624";
	
	// 合作身份者ID
	public static String partner = "2088231284681737";

	// 私钥 pkcs8格式
	public static String RSA_PRIVATE_KEY = "";

	// 支付宝公钥
	public static String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	// 请求支付宝的网关地址
	public static String URL = "https://openapi.alipay.com/gateway.do";

	// 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://www.xxx.com/alipay/notify_url.do";

	// 编码
	public static String CHARSET = "UTF-8";

	// 签名方式
	public static String SIGNTYPE = "RSA2";
	
}
