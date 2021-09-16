package com.hainu.system.config.tcp;

import com.hainu.system.entity.DeviceCurrent;
import com.hainu.system.service.DeviceCurrentService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

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
// @Component
public class TcpSever extends Thread{


     DeviceCurrentService deviceCurrentService;

    private Socket socket ;
    InputStream inputStream;
    OutputStream outputStream;
    public TcpSever(){

    }
    public  void setSocket(Socket socket){
        this.socket=socket;
    }
    public void setDeviceCurrentService(DeviceCurrentService deviceCurrentService){this.deviceCurrentService = deviceCurrentService;}
    public Socket getSocket(){
        return this.socket;
    }
    @Override
    public void run(){

        try {
            while (true){
                String name = this.getName();
                System.out.println(name);
                //接收客户端的消息并打印
                // System.out.println(socket);
                System.out.println(socket.getInetAddress());
                inputStream=socket.getInputStream();
                byte[] bytes = new byte[1024];
                int length=inputStream.read(bytes);

                String string = new String(bytes,0,length);
                System.out.println(string);
                System.out.println(string.equals("12"));

                //向客户端发送消息
                outputStream = socket.getOutputStream();
                outputStream.write("OK".getBytes());

                // System.out.println("OK");
                List<DeviceCurrent> list = deviceCurrentService.list();
                System.out.println(list);

            }
        } catch (Exception e) {
            System.out.println("客户端主动断开连接了");
            //e.printStackTrace();
        }
        //操作结束，关闭socket
        try{
            socket.close();
        }catch(IOException e){
            System.out.println("关闭连接出现异常");
            e.printStackTrace();
        }
    }

    public boolean sendMessage(String message){
        try {
            socket.getOutputStream().write(message.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
