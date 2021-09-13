package com.hainu.system.config.mqtt;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Classname MtqqEntity
 * @Description mqtt相关配置信息
 */
@Component
@ConfigurationProperties("mqtt")
@Data
public class MqttConfig {
    @Autowired
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private MqttPushClient mqttPushClient;

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 连接地址
     */
    private String hostUrl;
    /**
     * 客户Id
     */

    private String clientId;
    /**
     * 默认连接话题
     */
    private String defaultTopic;
    /**
     * 超时时间
     */
    private int timeout;
    /**
     * 保持连接数
     */
    private int keepalive;



    @Bean
    public MqttPushClient getMqttPushClient() {
        mqttPushClient.connect(hostUrl, clientId, username, password, timeout, keepalive);
        // 订阅设备信息
        mqttPushClient.subscribe("device_info", 1);
        // 订阅设备状态
        mqttPushClient.subscribe("status", 1);
        // 订阅设备配置
        mqttPushClient.subscribe("setting", 1);
        // 订阅设备离线遗嘱
        mqttPushClient.subscribe("offline", 1);

        mqttPushClient.subscribe("/dev/test1",1);
        mqttPushClient.subscribe("/dev/test2",1);
        mqttPushClient.subscribe("/dev/test3",1);
        return mqttPushClient;
    }
}

