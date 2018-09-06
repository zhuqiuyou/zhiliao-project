package com.cn.thinkx.ecom.you163.api.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.cn.thinkx.ecom.you163.api.vo.order.OrderSkuVO;
import com.cn.thinkx.ecom.you163.api.vo.order.OrderVO;

public final class YXOpenApiUtils {
    private static Logger logger = LoggerFactory.getLogger(YXOpenApiUtils.class);

    public static OrderVO createOrderVO(String skuId, String price) {
        BigDecimal parsePrice = new BigDecimal(price);
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(UUID.randomUUID().toString());
        orderVO.setSubmitTime(DateUtils.parseDateToString(new Date(), null));
        orderVO.setPayTime(DateUtils.parseDateToString(new Date(), null));
        //可省略
        orderVO.setBuyerAccount("openApiDemo@163.com");
        orderVO.setReceiverName("openApiDemo");
        //可省略
        orderVO.setReceiverMobile("13612345678");
        orderVO.setReceiverPhone("13612345678");
        orderVO.setReceiverProvinceName("浙江省");
        orderVO.setReceiverCityName("杭州市");
        orderVO.setReceiverDistrictName("滨江区");
        orderVO.setReceiverAddressDetail("网商路599号Demo勿发");
        orderVO.setRealPrice(parsePrice);
        orderVO.setExpFee(new BigDecimal("00.00"));
        orderVO.setPayMethod("支付宝SDK");
        //可省略
//        orderVO.setInvoiceTitle("网易严选");
        //可省略
//        orderVO.setInvoiceAmount(parsePrice);
        List<OrderSkuVO> orderSkuVOS = new ArrayList<>();
        orderVO.setOrderSkus(orderSkuVOS);

        OrderSkuVO orderSkuVO = new OrderSkuVO();
        orderSkuVO.setSkuId(skuId);
        orderSkuVO.setProductName("demo下单商品");
        orderSkuVO.setSaleCount(1);
        orderSkuVO.setOriginPrice(parsePrice);
        orderSkuVO.setSubtotalAmount(parsePrice);
        //可省略
        orderSkuVO.setCouponTotalAmount(new BigDecimal("0.00"));
        //可省略
        orderSkuVO.setActivityTotalAmount(new BigDecimal("0.00"));
        orderSkuVOS.add(orderSkuVO);

        if (logger.isInfoEnabled()) {
            logger.info(".createOrderVO() order={}", JSON.toJSONString(orderVO));
        }

        return orderVO;
    }

    /**
     * 组装参数，包括sign值的生成
     * @param appKey appKey
     * @param appSecret appSecret
     * @param timestamp timestamp
     * @param method method
     * @param paramMap paramMap
     * @return treeMap
     */
    public static TreeMap<String, String> packageParams(String appKey, String appSecret, String timestamp, String method,
                                                  Map<String, String> paramMap) {
        //将请求参数按名称排序
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("appKey", appKey);
        treeMap.put("method", method);
        treeMap.put("timestamp", timestamp);
        if (null != paramMap) {
            treeMap.putAll(paramMap);
        }

        //遍历treeMap，将参数值进行拼接
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = treeMap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            sb.append(key).append("=");
            sb.append(treeMap.get(key));
        }

        //参数值拼接的字符串收尾添加appSecret值
        String waitSignStr = appSecret + sb.toString() + appSecret;

        //获取MD5加密后的字符串
        String sign = ParseMD5.parseStrToMd5U32(waitSignStr);

        if (logger.isInfoEnabled()) {
            logger.info(".getSign() param={} sign={}", treeMap, sign);
        }

        treeMap.put("sign", sign);

        return treeMap;
    }
    
    
    /**
     * 严选回调 获取sign值的生成
     * @param appKey appKey
     * @param appSecret appSecret
     * @param timestamp timestamp
     * @param method method
     * @param paramMap paramMap
     * @return String 
     */
    public static String getSign(String appKey, String appSecret, String timestamp, String method,
                                                  Map<String, String> paramMap) {
        //将请求参数按名称排序
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("appKey", appKey);
        treeMap.put("method", method);
        treeMap.put("timestamp", timestamp);
        if (null != paramMap) {
            treeMap.putAll(paramMap);
        }

        //遍历treeMap，将参数值进行拼接
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = treeMap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            sb.append(key).append("=");
            sb.append(treeMap.get(key));
        }

        //参数值拼接的字符串收尾添加appSecret值
        String waitSignStr = appSecret + sb.toString() + appSecret;

        //获取MD5加密后的字符串
        String sign = ParseMD5.parseStrToMd5U32(waitSignStr);

        if (logger.isInfoEnabled()) {
            logger.info("getSign() param={},return sign={}", treeMap, sign);
        }
        return sign;
    }
    
    public static void main(String[] args) {
    	 Map<String, String> data = new HashMap<>();
         data.put("skuCheck", "{\"id\":\"asd12321312\",\"operateTime\":1521450151,\"skuChecks\":[{\"skuId\":1087002,\"count\":30}]}");
         getSign("83cf7fb5d68543568372c9a1c2f8e46d","a2281a55c76c4dc6a9531d81ba4f1fc1","2018-04-25 10:00:00","yanxuan.notification.inventory.count.check",data);
    }

}
