package com.hainu.test;

import cn.hutool.core.util.HexUtil;
import com.hainu.system.config.netty.UdpNetty;

/**
 * @author frank
 * @date 2020-01-04
 * @email:345690014@qq.com
 */
public class Test {

    public static void main(String[] args)
    {

            // String btSign = "2bLKdfCQZHZFcTiM7TlbbXTy9rTVvNKu";
            // String url = "http://192.168.2.219:8888/system?action=GetNetWork";
            // String timestamp = (System.currentTimeMillis()+"");
            // String md5Sign = SecureUtil.md5(btSign);
            // String token = SecureUtil.md5(timestamp + md5Sign);
            // String json = "request_time="+timestamp+"&request_token="+token;
            // String responseText = HttpRequest.post(url)
            //         .body(json)
            //         .execute().body();
            // System.out.println(responseText);

        // Integer i=1;
        // int b=0;
        // Optional<Integer> integer = Optional.ofNullable(i)
        //         .map((w) -> {
        //             return w+1;
        //         });
        // System.out.println(integer.orElseGet(()->null));
        //
        // DeviceController deviceController = new DeviceController();
        // deviceController.saveTopic("/dev/dat");

        //
        // System.out.println(LocalDateTime.now());
        // QueryDeviceDto queryDevice;
        // queryDevice = new QueryDeviceDto(null,null,null,null,null,null);
        // System.out.println(queryDevice);

        // LocalDateTime now = LocalDateTime.of(2021,9,15,18,43,25);
        // System.out.println(now);
        // LocalDateTime now2 = LocalDateTime.now();
        // Duration between = Duration.between(now, now2);
        //
        // System.out.println(between.toSeconds());
    // String json="{\"id\":1}";
    //     JSON parse = JSONUtil.parse(json);
    //     boolean empty = ObjectUtils.isEmpty(parse.getByPath("id"));
    //     System.out.println(empty);

        // String string="ff";
        //
        // int bytes = ;
        // System.out.println(bytes);
        try {
            new UdpNetty().run();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @org.junit.jupiter.api.Test
    void test(){
        String s = HexUtil.encodeHexStr(new byte[]{
                (byte) 0xff
        });
        System.out.println(s);
    }






}
