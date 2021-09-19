package com.hainu.system.config.tcp;

import cn.hutool.core.util.HexUtil;
import cn.hutool.log.StaticLog;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hainu.system.entity.DeviceCurrent;
import com.hainu.system.service.DeviceCurrentService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.config.tcp
 * @Date：2021/9/16 18:45
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */

public class TcpSever extends Thread {


    private DeviceCurrentService deviceCurrentService;


    private Socket socket;
    InputStream inputStream;
    OutputStream outputStream;

    public TcpSever() {

    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setDeviceCurrentService(DeviceCurrentService deviceCurrentService) {
        this.deviceCurrentService = deviceCurrentService;
    }


    @Override
    public void run() {

        try {

            String hostAddress = socket.getInetAddress().getHostAddress();
            StaticLog.info(hostAddress + ":" + socket.getPort() + "已连接");
            byte[] bytes = new byte[512];
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();

            //设备一连接后显示在线状态，若60秒后未发送消息则显示离线状态
            UpdateWrapper<DeviceCurrent> deviceUpdate = new UpdateWrapper<>();
            DeviceCurrent deviceCurrent = new DeviceCurrent();
            deviceCurrent.setWsName(hostAddress);
            deviceUpdate.eq("ws_name",hostAddress);
            deviceCurrent.setUpdateTime(LocalDateTime.now());
            if (!deviceCurrentService.update(deviceCurrent, deviceUpdate)) {
                deviceCurrentService.save(deviceCurrent);
            }


            while (true) {


                //获取原始数据以及数据转换
                int length = inputStream.read(bytes);
                if (length == -1) {
                    StaticLog.warn(hostAddress + ":" + socket.getPort() + "连接断开");
                    TcpConnect.socketClient.remove(hostAddress);
                    break;
                }
                String info = HexUtil.encodeHexStr(bytes);

                //数据处理
                int begin = 0;
                String str = info.substring(begin, begin + 2);
                if (str.equals("fe")) {
                    begin += 2;
                    str = info.substring(begin, begin + 2);
                    int dataBegin=0;
                    int dataLength=0;
                    if (str.equals("81")) {
                        begin += 2;
                         dataLength = HexUtil.hexToInt(info.substring(begin, begin + 2));
                         dataBegin=begin;
                        begin=begin+2+dataLength*2;
                    }

                    //##########校验###########

                    // str = ;

                    if (!info.substring(begin, begin + 2).equals("99")) {
                        StringBuffer sb=new StringBuffer(info);
                        sb.replace(begin+2,begin + 4,"fa");
                        info=sb.toString();
                    }
                    //######################

                    begin += 2;
                    if (!info.substring(begin, begin + 2).equals("fd")) {
                        outputStream.write(new byte[]{
                                (byte) 0xfe, 0x01, 0x01, (byte) 0xff, (byte) 0x99, (byte) 0xfd
                        });
                    } else {
                        outputStream.write(new byte[]{
                                (byte) 0xfe, 0x01, 0x01, 0x00, (byte) 0x99, (byte) 0xfd
                        });
                        store(info, dataBegin , dataLength, hostAddress);
                    }

                } else {
                    outputStream.write(new byte[]{
                            (byte) 0xfe, 0x01, 0x01, (byte) 0xff, (byte) 0x99, (byte) 0xfd
                    });
                }
            }


        } catch (Exception e) {
            System.out.println("客户端主动断开连接了");

            //e.printStackTrace();
        }
        //操作结束，关闭socket
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("关闭连接出现异常");
            e.printStackTrace();
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
    public int store(String info, int begin,int dataLength, String wsName) {
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

            deviceUpdate.eq("ws_name",wsName);
            deviceUpdate.eq("node", node).or().eq("node","");


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
