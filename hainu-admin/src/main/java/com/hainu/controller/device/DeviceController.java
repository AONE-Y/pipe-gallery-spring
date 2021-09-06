package com.hainu.controller.device;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.hainu.common.lang.Result;
import com.hainu.system.config.mqtt.MqttPushClient;
import com.hainu.system.dao.DeviceCurrentMapper;
import com.hainu.system.dao.DeviceListMapper;
import com.hainu.system.entity.DeviceCurrent;
import com.hainu.system.entity.DeviceList;
import com.hainu.system.service.DeviceCurrentService;
import com.hainu.system.service.DeviceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.controller.device
 * @Date：2021/9/3 10:25
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@RestController
@RequestMapping("/device")
public class DeviceController {
    @Autowired
    private MqttPushClient mqttPushClient;

    @Value("${info.baseEmqUrl}")
    private String baseEmqUrl;

    @Autowired
    private DeviceListService dls;
    @Autowired
    private DeviceListMapper dlm;
    @Autowired
    private DeviceCurrentService dcs;
    @Autowired
    private DeviceCurrentMapper dcm;

    /**
     * @param
     * @return @return {@link Result<?> }
     * @description: 订阅主题
     * @author： ANONE
     * @date： 2021/09/05
     */
    @GetMapping("reSubTopic")
    public Result<?> reSubTopic() {


        String unSubInfo = HttpRequest.get(baseEmqUrl + "/routes")
                .basicAuth("admin", "public")
                .execute().body();
        List<JSON> unSubdata = JSONUtil.parseObj(unSubInfo).getByPath("data", ArrayList.class);

        unSubdata.stream()
                .map((e) -> e
                        .getByPath("topic").toString())
                .forEach((o) -> mqttPushClient.unSubscribe(o));
        dlm.truncateData();
        dcm.truncateData();


        String subInfo = HttpRequest.get(baseEmqUrl + "/routes")
                .basicAuth("admin", "public")
                .execute().body();
        List<JSON> subData = JSONUtil.parseObj(subInfo).getByPath("data", ArrayList.class);

        subData.stream()
                .map((e) -> e
                        .getByPath("topic").toString())
                .forEach((o) -> {
                    mqttPushClient.subscribe(o, 1);
                    saveTopic(o);
                });

        return new Result<>().success();
    }


    /**
     * @param topic 主题
     * @return
     * @description: 保存主题
     * @author： ANONE
     * @date： 2021/09/05
     */
    public void saveTopic(String topic) {
        DeviceList deviceList = new DeviceList();
        deviceList.setDeviceTopic(topic);
        String[] topicSplit = topic.split("/");
        String deviceName= topicSplit[topicSplit.length - 1];

        DeviceCurrent deviceCurrent = new DeviceCurrent();

        if (topicSplit.length>1) {
            deviceCurrent.setDeviceName(deviceName);
            dcs.save(deviceCurrent);
        }

        deviceList.setDeviceName(deviceName);

        dls.save(deviceList);

    }




}
