package com.hainu.system.config.niotcp;

import cn.hutool.core.util.HexUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hainu.system.entity.DeviceCurrent;
import com.hainu.system.service.DeviceCurrentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.util.ByteUtils;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.config.niotcp
 * @Date：2021/9/24 11:39
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
public class NioTcp implements CommandLineRunner {
    @Autowired
    private DeviceCurrentService deviceCurrentService;

    public static Map<String, SocketChannel> tcpClient=new HashMap<>();


    @Override
    public void run(String... args) throws Exception {

        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(9999));
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            selector.select();
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();

                if (key.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(256);
                    sc.register(selector,SelectionKey.OP_READ,buffer);
                }else if (key.isReadable()) {
                    try {
                        SocketChannel channel = (SocketChannel) key.channel();
                        InetSocketAddress remoteAddress = (InetSocketAddress) channel.getRemoteAddress();
                        tcpClient.put(remoteAddress.getAddress().getHostAddress(), channel);
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        int read = channel.read(buffer);
                        if (read==-1) {
                            key.cancel();
                        }else {

                            buffer.flip();

                            byte[] bytes = ByteUtils.getBytes(buffer);

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
                                    channel.write(ByteBuffer.wrap(new byte[]{
                                            (byte) 0xfe, 0x01, 0x01, (byte) 0xff, (byte) 0x99, (byte) 0xfd
                                    }));
                                } else {
                                    channel.write(ByteBuffer.wrap(new byte[]{
                                            (byte) 0xfe, 0x01, 0x01, 0x00, (byte) 0x99, (byte) 0xfd
                                    }) );


                                    store(info, dataBegin, dataLength, remoteAddress.getAddress().getHostAddress());
                                }

                            } else {
                                channel.write(ByteBuffer.wrap(new byte[]{
                                        (byte) 0xfe, 0x01, 0x01, (byte) 0xff, (byte) 0x99, (byte) 0xfd
                                }) );

                            }
                            System.out.println(remoteAddress.getPort() + ":" + Charset.defaultCharset().decode(buffer));


                        }

                    }catch (Exception e) {

                    }
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
                // deviceCurrent.setDeviceSmoke(switchValue);
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
                // deviceCurrent.setDeviceGuard(switchValue);
            }
        }


        return deviceCurrent;
    }


}


