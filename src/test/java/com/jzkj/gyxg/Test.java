package com.jzkj.gyxg;

import cn.hutool.core.util.NumberUtil;

public class Test {
    public static void main(String[] args){
        System.out.println(System.currentTimeMillis());
        double fz = 16.9;
        double fm = 4.2;
        double result = NumberUtil.round(NumberUtil.div(fz,fm),2).doubleValue();
        result = NumberUtil.div(193.2,25.3);
        System.out.println(result);
        String[] a = "".split(",");
        System.out.println(a[0]);
    }
}
