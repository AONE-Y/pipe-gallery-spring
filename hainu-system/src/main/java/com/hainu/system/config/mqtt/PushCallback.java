/******************************************************************************
 * 作者：kerwincui
 * 时间：2021-06-08
 * 邮箱：164770707@qq.com
 * 源码地址：https://gitee.com/kerwincui/wumei-smart
 * author: kerwincui
 * create: 2021-06-08
 * email：164770707@qq.com
 * source:https://github.com/kerwincui/wumei-smart
 ******************************************************************************/
package com.hainu.system.config.mqtt;


import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hainu.system.entity.DeviceCurrent;
import com.hainu.system.entity.DeviceLog;
import com.hainu.system.service.DeviceCurrentService;
import com.hainu.system.service.DeviceLogService;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @Classname PushCallback
 * @Description 消费监听类
 */
@Component
public class PushCallback implements MqttCallbackExtended {
    private static final Logger logger = LoggerFactory.getLogger(MqttPushClient.class);

    @Autowired
    private MqttConfig mqttConfig;

    @Autowired
    private MqttPushClient mqttPushClient;

    @Autowired
    private DeviceCurrentService dcs;
    @Autowired
    private DeviceLogService dls;

    private static MqttClient client;

    @Override
    public void connectionLost(Throwable throwable) {
        // 连接丢失后，一般在这里面进行重连
        logger.info("连接断开，可以做重连");
        if (client == null || !client.isConnected()) {
            mqttConfig.getMqttPushClient();
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
//        // subscribe后得到的消息会执行到这里面
        logger.info("接收消息主题 : " + topic);
        logger.info("接收消息Qos : " + mqttMessage.getQos());
        logger.info("接收消息内容 : " + new String(mqttMessage.getPayload()));

//        if(topic.equals("device_info")){
//            //添加设备信息
//            IotDevice device = JSON.parseObject(new String(mqttMessage.getPayload()), IotDevice.class);
//            IotDevice deviceEntity=iotDeviceService.selectIotDeviceByNum(device.getDeviceNum());
//            if(deviceEntity!=null){
//                device.setDeviceId(deviceEntity.getDeviceId());
//                iotDeviceService.updateIotDevice(device);
//            }else {
//                IotCategory categoryEntity=iotCategoryService.selectIotCategoryById(device.getCategoryId());
//                if(device.getDeviceName()==null || device.getDeviceNum().length()==0) {
//                    Random rand = new Random(); //随机生成两位数
//                    device.setDeviceName(categoryEntity.getCategoryName()+(rand.nextInt(90) + 10));
//                }
//                iotDeviceService.insertIotDevice(device);
//            }
//            //获取设备状态(消息内容不能为空，硬件获取不到数据报错)
//            mqttPushClient.publish(1,false,"status/get/"+device.getDeviceNum(),"wumei.live");
//            //获取设备配置
//            mqttPushClient.publish(1,false,"setting/get/"+device.getDeviceNum(),"wumei.live");
//        }else if(topic.equals("status")){
//            IotDeviceStatus deviceStatus = JSON.parseObject(new String(mqttMessage.getPayload()), IotDeviceStatus.class);
//            IotDevice device=iotDeviceService.selectIotDeviceByNum(deviceStatus.getDeviceNum());
//            //添加设备状态
//            deviceStatus.setDeviceId(device.getDeviceId());
//            iotDeviceStatusService.insertIotDeviceStatus(deviceStatus);
//        }else if(topic.equals("setting")){
//            IotDeviceSet deviceSet = JSON.parseObject(new String(mqttMessage.getPayload()), IotDeviceSet.class);
//            // 智能配网时需要获取IP、地址和设备用户
//            IotDevice device=iotDeviceService.selectIotDeviceByNum(deviceSet.getDeviceNum());
//            if(deviceSet.getIsSmartConfig()==1){
//                final String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
//                deviceSet.setNetworkIp(ip);
//                deviceSet.setNetworkAddress( AddressUtils.getRealAddressByIP(ip));
//                //更新设备用户
//                device.setOwnerId(deviceSet.getOwnerId());
//                iotDeviceService.updateIotDevice(device);
//            }
//            //添加设备配置
//            deviceSet.setDeviceId(device.getDeviceId());
//            iotDeviceSetService.insertIotDeviceSet(deviceSet);
//        }else if(topic.equals("offline")){
//            //离线遗嘱
//            IotDeviceStatus deviceStatus = JSON.parseObject(new String(mqttMessage.getPayload()), IotDeviceStatus.class);
//            IotDeviceStatus deviceStatusEntity=iotDeviceStatusService.selectIotDeviceStatusByDeviceNum(deviceStatus.getDeviceNum());
//            //设备状态为离线
//            if(deviceStatusEntity!=null) {
//                deviceStatusEntity.setIsOnline(0);
//                iotDeviceStatusService.insertIotDeviceStatus(deviceStatusEntity);
//            }
//        }

        String[] unIncludeTopic={"device_info","setting","status","offline"};
        if (!Arrays.asList(unIncludeTopic).contains(topic)) {
            String[] topicSplit = topic.split("/");
            String deviceName= topicSplit[topicSplit.length - 1];

            DeviceCurrent deviceCurrent = new DeviceCurrent();
            DeviceLog deviceLog = new DeviceLog();
            try {
                JSON deviceInfo = JSONUtil.parse(new String(mqttMessage.getPayload()));

                //设备当前状态
                Integer temp = deviceInfo.getByPath("temp", Integer.class);
                Integer humi = deviceInfo.getByPath("humi", Integer.class);
                Integer llv = deviceInfo.getByPath("llv", Integer.class);
                Integer gas = deviceInfo.getByPath("gas", Integer.class);
                Integer o2 = deviceInfo.getByPath("O2", Integer.class);
                Integer smoke = deviceInfo.getByPath("smoke", Integer.class);
                Integer lighting = deviceInfo.getByPath("lighting", Integer.class);
                Integer waterpump = deviceInfo.getByPath("waterpump", Integer.class);
                Integer fan = deviceInfo.getByPath("fan", Integer.class);
                Integer infra = deviceInfo.getByPath("infra", Integer.class);
                Integer guard = deviceInfo.getByPath("guard", Integer.class);

                deviceCurrent.setDeviceName(deviceName);
                deviceCurrent.setDeviceTemp(temp);
                deviceCurrent.setDeviceHumi(humi);
                deviceCurrent.setDeviceLlv(llv);
                deviceCurrent.setDeviceGas(gas);
                deviceCurrent.setDeviceO2(o2);
                deviceCurrent.setDeviceSmoke(smoke);
                deviceCurrent.setDeviceLighting(lighting);
                deviceCurrent.setDeviceWaterpump(waterpump);
                deviceCurrent.setDeviceFan(fan);
                deviceCurrent.setDeviceInfra(infra);
                deviceCurrent.setDeviceGuard(guard);
                UpdateWrapper<DeviceCurrent> deviceUpdate = new UpdateWrapper<>();
                deviceUpdate.eq("device_name", deviceName);
                if (!dcs.update(deviceCurrent, deviceUpdate)) {
                    dcs.save(deviceCurrent);
                }

                //存储为历史记录
                deviceLog.setDeviceName(deviceName);
                deviceLog.setDeviceTemp(temp);
                deviceLog.setDeviceHumi(humi);
                deviceLog.setDeviceLlv(llv);
                deviceLog.setDeviceGas(gas);
                deviceLog.setDeviceO2(o2);
                deviceLog.setDeviceSmoke(smoke);
                deviceLog.setDeviceLighting(lighting);
                deviceLog.setDeviceWaterpump(waterpump);
                deviceLog.setDeviceFan(fan);
                deviceLog.setDeviceInfra(infra);
                deviceLog.setDeviceGuard(guard);
                deviceLog.setCreateTime(LocalDateTime.now());

                dls.save(deviceLog);

            }catch (Exception ignored) {

            }



        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        logger.info("deliveryComplete---------" + iMqttDeliveryToken.isComplete());
    }

    @Override
    public void connectComplete(boolean b, String s) {
        logger.info("连接成功 : " + s);
    }
}
