package com.hainu.controller.device;

import com.hainu.system.config.mqtt.MqttPushClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class DeviceListController {
    @Autowired
    private MqttPushClient mqttPushClient;


    @GetMapping("test")
    public String test(){
        mqttPushClient.subscribe("status",0);
        return "2131";
    }
}
