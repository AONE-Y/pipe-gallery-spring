package com.hainu.system.config.tcp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.config.tcp
 * @Date：2021/9/16 18:44
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@Component
public class TcpConnect implements CommandLineRunner {

    //监听端口
    private static final int PORT = 8083;


    @Override
    public void run(String... args) throws Exception {
        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            //建立服务器的Socket，并设定一个监听的端口PORT
            serverSocket = new ServerSocket(PORT);
            //由于需要进行循环监听，因此获取消息的操作应放在一个while大循环中
            while(true){
                try {
                    //建立跟客户端的连接
                    socket = serverSocket.accept();
                } catch (Exception e) {
                    System.out.println("建立与客户端的连接出现异常");
                    e.printStackTrace();
                }
                TcpSever thread = new TcpSever();
                thread.setSocket(socket);
                thread.start();
            }
        } catch (Exception e) {
            System.out.println("端口被占用");
            e.printStackTrace();
        }
        finally {
            serverSocket.close();
        }
    }
}
