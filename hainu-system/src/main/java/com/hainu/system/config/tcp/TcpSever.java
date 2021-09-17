package com.hainu.system.config.tcp;

import cn.hutool.core.util.HexUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hainu.system.entity.DeviceCurrent;
import com.hainu.system.service.DeviceCurrentService;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
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


            while (true) {
                String name = this.getName();
                System.out.println(name);
                //接收客户端的消息并打印
                // System.out.println(socket);
                // System.out.println(socket.getInetAddress());
                byte[] bytes = new byte[512];


                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();

                int length = inputStream.read(bytes);

                System.out.println(length);
                String info = HexUtil.encodeHexStr(bytes);
                while (true) {
                    int begin = 0;
                    String str = info.substring(begin, begin + 2);
                    if (str.equals("fe")) {
                        begin += 2;
                        str = info.substring(begin, begin + 2);
                        if (str.equals("81")) {
                            begin = store(info, begin + 2, socket.getInetAddress().getHostAddress());
                        }

                        //##########校验###########
                        // str = info.substring(begin, begin + 2);
                        //######################

                        begin += 2;
                        if (!info.substring(begin, begin + 2).equals("fd")) {
                            outputStream.write("data error".getBytes());
                            break;
                        } else {
                            outputStream.write("OK".getBytes());
                            break;
                        }

                    } else {
                        break;
                    }

                }


                //
                // String string = new String(bytes, 0, length);
                // System.out.println(string);
                // System.out.println(string.equals("12"));


                //向客户端发送消息


                // System.out.println("OK");
                // List<DeviceCurrent> list = deviceCurrentService.list();
                // System.out.println(list);

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


    public int store(String info, int begin, String wsName) {
        int dataLength = Integer.parseInt(info.substring(begin, begin + 2));
        for (int i = 0; i < dataLength / 3; i++) {

            begin += 2;
            String node = info.substring(begin, begin + 2);
            begin += 2;
            String sensorName = info.substring(begin, begin + 2);
            begin += 2;
            Integer sensorValueTemp = HexUtil.hexToInt(info.substring(begin, begin + 2));
            Double sensorValue = sensorValueTemp > 99 ? sensorValueTemp.doubleValue() / 10 : sensorValueTemp.doubleValue();


            UpdateWrapper<DeviceCurrent> deviceUpdate = new UpdateWrapper<>();
            DeviceCurrent deviceCurrent = new DeviceCurrent();

            deviceUpdate.eq("node", node);

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
            switchValuetemp= (double) -1;
        }
        if (switchValuetemp == 25.5||switchValuetemp==255) {
            switchValuetemp= 1.0;
        }
        int switchValue=switchValuetemp.intValue();

        if (switchValue == 1||switchValue == 0) {
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

    // public boolean sendMessage(String message){
    //     try {
    //         socket.getOutputStream().write(message.getBytes());
    //         return true;
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         return false;
    //     }
    // }
}
