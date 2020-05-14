package com.jzkj.gyxg.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jzkj.gyxg.util.StringUtil;

public class PayUtil {
    public static JSONObject getResult(String str) {
        if(StringUtil.isNull(str)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject body = jsonObject.getJSONObject("response").getJSONObject("body");
        if ("200".equals(body.get("result_code") + "")) {
            JSONObject biz_response = body.getJSONObject("biz_response");
            if ("200".equals(biz_response.get("result_code") + "")) {
                JSONObject data = biz_response.getJSONObject("data");
                data.put("result_code", "200");
                return data;
            } else {
                return biz_response;
            }
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        String str = "{\"signature\":\"N0h3ix3cL5SYn47JQXRCVVAL4X35itGtQCESz61OuowohUNattxUKTGQFIbiZQKa9VJkCn+AhRK4qCs4Lmivpuwi7dVcIJvtzGHKWPl9a/1f3oF8+ashNBEW0XLKjXYYUbHri7gzniY6CFvhJVF++mDBJshD1ICvqc1hz5/evMy7mb2/+JR+hJPQnPhFbGabTXKoJanjFZT7TBJGX8bdhOn35Pak8OZ23LtMM6VTqHsa0P/SeJM39cY1zKOx5ShM8+7Q+B95FF9KJ3CKcAXenlNPeVhu4hh0DAkBwRis7MHY3s+D6hu8t88SdMy5B5MoYHpyu0mOx7MI5WwawMxCvg==\",\"response\":{\"head\":{\"appid\":\"28lpm0000002\",\"reserve\":\"{}\",\"version\":\"2.0.0\",\"response_time\":\"2020-01-03T18:30:46+08:00\"},\"body\":{\"result_code\":\"200\",\"biz_response\":{\"result_code\":\"200\",\"data\":{\"brand_code\":\"999888\",\"store_sn\":\"Gyxg\",\"workstation_sn\":\"0\",\"check_sn\":\"202001031604001\",\"order_sn\":\"200103999888000031\",\"order_status\":4,\"sales_time\":\"2020-01-03T16:04:43+08:00\",\"amount\":1,\"currency\":\"156\",\"subject\":\"买单\",\"description\":\"古渝雄关\",\"operator\":\"001\",\"customer\":\"001\",\"industry_code\":2,\"pos_info\":\"半夜鸡叫\",\"items\":[],\"tenders\":[{\"tender_type\":3,\"sub_tender_type\":301,\"sub_tender_desc\":\"微信支付\",\"amount\":1,\"collected_amount\":1,\"paid_amount\":1,\"tender_sn\":\"620010300000056705\",\"pay_time\":\"2020-01-03T16:06:56+08:00\",\"pay_status\":3}]}}}}}";
        System.out.println(getResult(str));
        str = "{\"signature\":\"POSJc/YujHcVFkg7PLPcmlmJIOifJZSWIanDfAXrBmLrAnuDlUMgxvdZVT7on+mq5gsuDa1zqklcX5f9NA4Z3NRMZzmAQP8HL/9r+FSOBbtYszw4st9LUinYspAVW1Pw6n25pw221FyEhH12ClsXat3IuyfJdJctuzF4rRbak6xbllcDOv7wuh41SdCx6XypMyIjS1nj/iC9KZWpXBxEnUuqXGHns2CSJAMYMpzv30Bul8OngSt5Xa5NtjeHRHyPBJo+6BppB6EEdpfSIs9YIQNcQ1NRFz/m5phERplL+C0RktZo4wfgmCAT3if7rqQVYBn+rUOirG4xLEmoYEvCVA==\",\"response\":{\"head\":{\"appid\":\"28lpm0000002\",\"reserve\":\"{}\",\"version\":\"2.0.0\",\"response_time\":\"2020-01-03T17:53:51+08:00\"},\"body\":{\"result_code\":\"200\",\"biz_response\":{\"result_code\":\"10003\",\"error_code\":\"ACQ.ORIGIN_TENDER_ERROR\",\"error_message\":\"原始订单未完成或流水不存在\"}}}}";
        System.out.println(getResult(str));
    }
}
