package com.hainu.controller.device;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.hainu.common.lang.Result;
import com.hainu.system.config.mqtt.MqttPushClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.ArrayList;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.controller.device
 * @Date：2021/9/3 10:25
 * @Author：yy188
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: yy188
 */
@RestController
@RequestMapping("/device")
public class DeviceController {
    @Autowired
    private MqttPushClient mqttPushClient;

    @Value("${info.baseEmqUrl}")
    private String baseEmqUrl;

    @GetMapping("subTopic")
    public Result<?> subTopic(){

        String emqRoutersInfo = HttpRequest.get(baseEmqUrl+"/routes")
                .basicAuth("admin", "public")
                .execute().body();
        List<JSON> data = JSONUtil.parseObj(emqRoutersInfo).getByPath("data", ArrayList.class);

        data.stream()
                .map((e)->e
                        .getByPath("topic"))
                .forEach((o)->mqttPushClient.subscribe((String) o,1));

        // mqttPushClient.subscribe("status",0);
        return new Result<>().success();
    }


    @GetMapping("unSubTopic")
    public Result<?> unSubTopic(){
        String emqRoutersInfo = HttpRequest.get(baseEmqUrl+"/routes")
                .basicAuth("admin", "public")
                .execute().body();
        List<JSON> data = JSONUtil.parseObj(emqRoutersInfo).getByPath("data", ArrayList.class);

        data.stream()
                .map((e)->e
                        .getByPath("topic").toString())
                .forEach((o)->mqttPushClient.unSubscribe((String) o));
        return new Result<>().success();
    }
}
