package com.dce.business.common.alipay.util;

/**
 * 支付宝公用参数
 * 
 * @author Administrator
 *
 */
public class AlipayConfig {

	public static String APPID = "2018080960938624";
	
	// 商户支付宝用户号
	public static String seller_id = "2088231284681737";

	// 私钥 pkcs8格式
	public static String RSA_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCE9kmVzYU1T2OJJoDuKMG3InjLm9DMO88nYpXlz/CkNNg7KfCesVyyyLb1sMHIP6rHk4kCUeTe7HTHfM4BtVP3LX28BT5n5o3ovQA6JhZeDlETZZu24UpWlp3Ejiy6XefmfQu+FSWuqNCoq+9BYrKqpSeNj8NBeumpg01cHoV9cmwIMcovAnDUgnpnbGr8VQrx+eRijMsMQNpt0U6jCnGVKuyx1UjetMQJOZgMCrISGrsLjRwC86SU8BhDKe5aEUNHsre8u5TB/9Xg1wwjT9PKypnPkiSs9fUymyyrbpEpEDSrCYQyslsze+Q/CSSL3uGkVsiz9+Ztn1xVnkFIgnvdAgMBAAECggEAbtTxj7qKMF7lxHxg+qTknOhSMr7GdIfibJ0kADWuDOKz39Vc6ptDfxAKWpClZ2uSytuzWEi1dBE8YtEo04m33RGBfooMdAVeA0QQfWgkOvDKKlYHT5FKKxVPm3k71XnThXB0qlTsXUQA8qinPiSsc8xJRRoyE1IGljOJ96DFe0QCzdmf23Diemx7B8Z8cI7/T3zu2Dl49fCO2ewVzruLmwMtmJeo9wEie4LQClOn2dHHoK7jhR8Z3UoiNprL/TBMeieNhvec3i1yikq1uzzaD2C5NsPcBBfjAjJq34d68p5l9KdLrvJZFugjDVRFZ1dSfswuc8L1JmL5fNRTXEtJDQKBgQDB5UzV1sWLFBkUqzS9KLlpJWwjvy51ainzLjG6vw6k3t6lkzFB72FE62IzuWnrC133QaXY8or5lWvRQS6tI9lJhonTWC5VrqhM0zFu9DkuU1D6kCS67KI0i+XuCH+1w5nDS8qHUantJzTqBM16lqSOamr+CLJexmsMDwQsw28uywKBgQCvjKnfb0uDk0BdAPOckTjIalXfiujzOziin0ltvc3Hr5kHjZWShxPVNSTtKs+qkdtkM/enN3EOZiXxVPy/MJWk4HL1VeIn1oAGayc/PHNdgBWmR4ZUlYnPwkxOqJ7Qs7quElOI90EpCdbigjcwm2emfnZYmB4LdYMDcnqumQRC9wKBgQCb0yj9zd3yWNOTPchjoN/pNa8Dx3iR2iEek2hZ2YPuUkKCtJlokPYOClFgmkkaIYeB15tuj/XKgSAmCol1khLlOR943yxVpckR/MSEk/eEWGcDB2bNZrExI6FEvEXOkQT44+c++w1iTxkCXZu5Qmm7HqEfyWN4MWQES+kiBGpNIwKBgGB57K23q4d++INkT32/LbavAcydudJavJBa++7Mvfe9IFvPSuFBTPwCfe29dm1mRW03RiZmyMJCDx4nN9QW7lazZ1o/Pc/pCWj9oI8XOMus5bkBW+jGWfuM64TbFJTcAX9C4RfH0s6r8qV/LLeQ4JDWtVGRIAnqJftSAexMedQXAoGAA09df48i30DYdO688alSQ2Hy2Ylahn1ayJq4FbaPMkT5nyFIXn1BnMPVN9g2LueEbcXXT34tfyyWic/aOqrruFRw6dt7+7N+WD04E7ZmAMp/xlfcP/c5uOCYgkbO4d+/Mk+io1TE6Eet7TGsGSmMhSTIg1NLwuILt16PZ6UKz4Q=";

	// 支付宝公钥
	public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApKVKnMg5/5V/T4HVP+ecji6l3SoKyeBM4HbDsIvPDAPeh2v5nCihl8x2f+/00pkxmzu51tfIrguJ7H9euTT1blzRwpws7NTg01GrD34mWGSFcL4Q61wLX6dCYP23DxJ1UbaouZ59shL6i6gYJe43+mS3pbdVJtDn0T/7+EckxHq7tENhqeTK6vn1S+d7rA19FEPxt/EVla3sU1VOUylX/2doLhALTkAFtjeCFEtafekXfTA/YGMXDr20ebmQ5GWleRtKva8tgRger/DVL6vcVfJDecuaSndCrTbhqsopEYIxfcBA07z9o9+wtFgqdxQAGxTkd8ZW1tUdGZxzAcXHCQIDAQAB";

	// 请求支付宝的网关地址
	public static String URL = "https://openapi.alipay.com/gateway.do";

	// 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://47.106.123.116:8080/dce-app/order/notify_url.do";
	
	// 返回格式
	public static String FORMAT = "JSON";

	// 编码
	public static String CHARSET = "UTF-8";

	// 签名方式
	public static String SIGNTYPE = "RSA2";
	
	//交易的超时时间	取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）
	public static String timeoutExpress = "30m";
	
}
