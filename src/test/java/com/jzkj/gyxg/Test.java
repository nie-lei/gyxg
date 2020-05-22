package com.jzkj.gyxg;

import cn.hutool.core.util.NumberUtil;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Test {
    public static void main(String[] args) throws UnknownHostException {
//        System.out.println(System.currentTimeMillis());
        double fz = 16.9;
        double fm = 4.2;
//        double result = NumberUtil.round(NumberUtil.div(fz,fm),2).doubleValue();
//        result = NumberUtil.div(193.2,25.3);
//        System.out.println(result);
//        String[] a = "".split(",");
//        System.out.println(a[0]);
//
//
//        Double f = 45.4;
//        f = f * -1;
//        System.out.println(f);
//        System.out.println(f * -1);
//
//        f = NumberUtil.sub(fz, fm);
//        System.out.println("sub:" + f);
//        f = NumberUtil.mul(fz, fm);
//        System.out.println("mul:" + f);
//        f = NumberUtil.div(fz, fm);
//        System.out.println("div:" + f);
//        String str = "123123123";
//        System.out.println(str.substring(0, str.length() -1));

        InetAddress[] ia = InetAddress.getAllByName("183.230.133.205");
        for (InetAddress inetAddress: ia) {
            System.out.println(inetAddress.getCanonicalHostName());
            System.out.println("亲测有效"+what(inetAddress.getCanonicalHostName()));
        }
    }

    public static String what(String ip) {
        NetworkInterface ne = null;
        try {
            ne = NetworkInterface.getByInetAddress(InetAddress.getByName(ip));
            if (ne != null) {

                byte[] mac = ne.getHardwareAddress();
                StringBuffer sb = new StringBuffer("");
                for (int i = 0; i < mac.length; i++) {
                    if (i != 0) {
                        sb.append("-");
                    }
                    //字节转换为整数
                    int temp = mac[i] & 0xff;
                    String str = Integer.toHexString(temp);
                    System.out.println("每8位:" + str);
                    if (str.length() == 1) {
                        sb.append("0" + str);
                    } else {
                        sb.append(str);
                    }
                }
                return sb.toString().toUpperCase();
            }
            return "";
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "";
    }

}
