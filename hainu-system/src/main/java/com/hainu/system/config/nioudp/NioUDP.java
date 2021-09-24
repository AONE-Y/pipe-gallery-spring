package com.hainu.system.config.nioudp;

import cn.hutool.core.util.HexUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hainu.system.entity.DeviceCurrent;
import com.hainu.system.service.DeviceCurrentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.util.ByteUtils;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.config.nioudp
 * @Date：2021/9/23 21:06
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@Component
public class NioUDP implements CommandLineRunner {

    @Autowired
    private DeviceCurrentService deviceCurrentService;

    public static Map<String,DatagramChannel> udpClient=new HashMap<>();

    @Override
    public void run(String... args) throws Exception {
        DatagramChannel channel = DatagramChannel.open();
        channel.configureBlocking(false);
        channel.socket().bind(new InetSocketAddress(9999));

        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_READ);
        ByteBuffer buf = ByteBuffer.allocate(48);

        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();


                DatagramChannel channel1 = (DatagramChannel) next.channel();
                channel1.configureBlocking(false);
                buf.clear();
                InetSocketAddress address = (InetSocketAddress) channel1.receive(buf);
                udpClient.put(address.getHostName(), channel1);
                try {

                    buf.flip();

                    byte[] bytes = ByteUtils.getBytes(buf);

                    String info = HexUtil.encodeHexStr(bytes);

                    //数据处理
                    int begin = 0;
                    String str = info.substring(begin, begin + 2);
                    if (str.equals("fe")) {
                        begin += 2;
                        str = info.substring(begin, begin + 2);
                        int dataBegin = 0;
                        int dataLength = 0;
                        if (str.equals("81")) {
                            begin += 2;
                            dataLength = HexUtil.hexToInt(info.substring(begin, begin + 2));
                            dataBegin = begin;
                            begin = begin + 2 + dataLength * 2;
                        }

                        //##########校验###########

                        // str = ;

                        if (!info.substring(begin, begin + 2).equals("99")) {
                            StringBuffer sb = new StringBuffer(info);
                            sb.replace(begin + 2, begin + 4, "fa");
                            info = sb.toString();
                        }
                        //######################

                        begin += 2;
                        if (!info.substring(begin, begin + 2).equals("fd")) {
                            channel1.send(ByteBuffer.wrap(new byte[]{
                                    (byte) 0xfe, 0x01, 0x01, (byte) 0xff, (byte) 0x99, (byte) 0xfd
                            }), address);
                        } else {
                            channel1.send(ByteBuffer.wrap(new byte[]{
                                    (byte) 0xfe, 0x01, 0x01, 0x00, (byte) 0x99, (byte) 0xfd
                            }), address);


                            store(info, dataBegin, dataLength, address.getHostName());
                        }

                    } else {
                        channel1.send(ByteBuffer.wrap(new byte[]{
                                (byte) 0xfe, 0x01, 0x01, (byte) 0xff, (byte) 0x99, (byte) 0xfd
                        }), address);

                    }
                    System.out.println(address.getPort() + ":" + Charset.defaultCharset().decode(buf));
                } catch (Exception e) {
                    channel1.send(ByteBuffer.wrap(new byte[]{
                            (byte) 0xfe, 0x01, 0x01, (byte) 0xff, (byte) 0x99, (byte) 0xfd
                    }), address);
                }

            }
        }
    }

    /**
     * @param info   数据
     * @param begin  数据读取位置
     * @param wsName 仓端名
     * @return @return int 下一次读取字节的位置
     * @description: 存储数据到数据库
     * @author： ANONE
     * @date： 2021/09/18
     */
    public int store(String info, int begin, int dataLength, String wsName) {
        for (int i = 0; i < dataLength / 3; i++) {

            begin += 2;
            String node = info.substring(begin, begin + 2);
            begin += 2;
            String sensorName = info.substring(begin, begin + 2);
            begin += 2;
            int sensorValueTemp = HexUtil.hexToInt(info.substring(begin, begin + 2));
            Double sensorValue = sensorValueTemp > 99 ? (double) sensorValueTemp / 10 : (double) sensorValueTemp;


            UpdateWrapper<DeviceCurrent> deviceUpdate = new UpdateWrapper<>();
            DeviceCurrent deviceCurrent = new DeviceCurrent();

            deviceUpdate.eq("ws_name", wsName);
            deviceUpdate.eq("node", node).or().eq("node", "");


            deviceCurrent.setWsName(wsName);
            deviceCurrent.setNode(node);
            deviceCurrent.setUpdateTime(LocalDateTime.now());
            deviceCurrent = setSensor(deviceCurrent, sensorName, sensorValue);
            if (!deviceCurrentService.update(deviceCurrent, deviceUpdate)) {
                deviceCurrentService.save(deviceCurrent);
            }
        }
        return begin + 2;
    }

    /**
     * @param deviceCurrent 当前设备
     * @param sensorName    传感器的名字
     * @param sensorValue   传感器值
     * @return @return {@link DeviceCurrent }
     * @description: 设置传感器的数据
     * @author： ANONE
     * @date： 2021/09/18
     */
    public DeviceCurrent setSensor(DeviceCurrent deviceCurrent, String sensorName
            , Double sensorValue) {


        if (sensorName.equals("01")) {
            deviceCurrent.setDeviceTemp(sensorValue);
        }
        if (sensorName.equals("02")) {
            deviceCurrent.setDeviceHumi(sensorValue);
        }
        if (sensorName.equals("03")) {
            deviceCurrent.setDeviceLlv(sensorValue);
        }
        if (sensorName.equals("04")) {
            deviceCurrent.setDeviceGas(sensorValue);
        }
        if (sensorName.equals("05")) {
            deviceCurrent.setDeviceO2(sensorValue);
        }


        Double switchValuetemp = sensorValue;
        if (switchValuetemp == 1) {
            switchValuetemp = (double) -1;
        }
        if (switchValuetemp == 25.5 || switchValuetemp == 255) {
            switchValuetemp = 1.0;
        }
        int switchValue = switchValuetemp.intValue();

        if (switchValue == 1 || switchValue == 0) {
            if (sensorName.equals("80")) {
                deviceCurrent.setDeviceSmoke(switchValue);
            }
            if (sensorName.equals("81")) {
                deviceCurrent.setDeviceLighting(switchValue);
            }
            if (sensorName.equals("82")) {
                deviceCurrent.setDeviceWaterpump(switchValue);
            }
            if (sensorName.equals("83")) {
                deviceCurrent.setDeviceFan(switchValue);
            }
            if (sensorName.equals("84")) {
                deviceCurrent.setDeviceInfra(switchValue);
            }
            if (sensorName.equals("85")) {
                deviceCurrent.setDeviceGuard(switchValue);
            }
        }


        return deviceCurrent;
    }


}
