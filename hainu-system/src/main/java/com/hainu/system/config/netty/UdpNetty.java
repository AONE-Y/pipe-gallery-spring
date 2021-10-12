package com.hainu.system.config.netty;

import com.hainu.system.config.netty.handle.*;
import com.hainu.system.service.DeviceCurrentService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.config.netty
 * @Date：2021/10/10 11:16
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@Component
@Order(1)
public class UdpNetty implements CommandLineRunner {
    @Autowired
    private DeviceCurrentService deviceCurrentService;
    @Override
    public void run(String... args) throws Exception {
        new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioDatagramChannel.class)
                .handler(new ChannelInitializer<NioDatagramChannel>() {

                    @Override
                    protected void initChannel(NioDatagramChannel ch)  {

                        ch.pipeline().addLast("responseBase",new ResponseHandler());
                        //校验发送过来的数据帧是否以0xfe开头
                        ch.pipeline().addLast("title", new HeaderHandler());
                        //校验数据上传类型
                        ch.pipeline().addLast("dataType",new DataTypeHandler());
                        //校验帧
                        ch.pipeline().addLast("checkoutNum",new CheckoutNumHandler());
                        //数据帧尾，并且保存数据
                        ch.pipeline().addLast("lastAndStore",new TailAndStoreHandler(deviceCurrentService));
                    }
                }).bind(9999);
    }
}
