package com.hainu.test;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;

/**
 * @author frank
 * @date 2020-01-04
 * @email:345690014@qq.com
 */
public class Test {

    public static void main(String[] args)
    {

            String btSign = "2bLKdfCQZHZFcTiM7TlbbXTy9rTVvNKu";
            String url = "http://192.168.2.219:8888/system?action=GetNetWork";
            String timestamp = (System.currentTimeMillis()+"");
            String md5Sign = SecureUtil.md5(btSign);
            String token = SecureUtil.md5(timestamp + md5Sign);
            String json = "request_time="+timestamp+"&request_token="+token;
            String responseText = HttpRequest.post(url)
                    .body(json)
                    .execute().body();
            System.out.println(responseText);

    }



}
