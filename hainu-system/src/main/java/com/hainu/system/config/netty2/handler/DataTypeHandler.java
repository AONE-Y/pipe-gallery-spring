package com.hainu.system.config.netty2.handler;

import cn.hutool.core.util.HexUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hainu.common.constant.DeviceConst;
import com.hainu.common.dto.DataAddrDto;
import com.hainu.system.entity.DeviceData;
import com.hainu.system.entity.DeviceInfo;
import com.hainu.system.entity.DeviceRes;
import com.hainu.system.service.DeviceInfoService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;

import java.util.HashMap;
import java.util.Map;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.config.netty.handle
 * @Date：2021/10/10 15:42
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
public class DataTypeHandler extends ChannelInboundHandlerAdapter {
    // @Autowired
    // private DeviceQueryService deviceQuerySer;
    //
    private DeviceInfoService deviceInfoService;
    public DataTypeHandler(DeviceInfoService deviceInfoService){
        this.deviceInfoService = deviceInfoService;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        DatagramPacket rec = (DatagramPacket) msg;
        String hostAddress = rec.sender().getAddress().getHostAddress();
        ByteBuf recBuf = rec.content();
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("dp", rec);
        byte type = recBuf.readByte();
        //舍弃长度字节，固定为11字节
        recBuf.readByte();

        byte[] bytes = new byte[2];
        recBuf.readBytes(bytes);
        String node = HexUtil.encodeHexStr(bytes);

        //舍弃一字节
        recBuf.readByte();

        byte sensorName = recBuf.readByte();
        byte valueByte1 = recBuf.readByte();
        byte valueByte2 = recBuf.readByte();
        String code = HexUtil.encodeHexStr(new byte[]{sensorName});
        String valueStr;
        if (valueByte1==(byte)0xff) {
            valueStr = HexUtil.encodeHexStr(new byte[]{valueByte2});
        } else {
            valueStr = HexUtil.encodeHexStr(new byte[]{valueByte1, valueByte2});
        }
        double value = HexUtil.hexToInt(valueStr);
        QueryWrapper<DeviceData> dataWrapper = new QueryWrapper<>();
        dataWrapper.eq(DeviceInfo.COL_NODE, node)
                .eq(DeviceInfo.COL_CODE, code);
        QueryWrapper<DeviceInfo> wapper = new QueryWrapper<>();
        wapper.eq(DeviceInfo.COL_NODE, node)
                .eq(DeviceInfo.COL_CODE, code);
        //这里可能出问题，一个节点加一个代码只能查询出一条数据
        DeviceInfo info = deviceInfoService.getOne(wapper);
        Integer calculate = info.getCalculate();
        if (type == (byte) 0x83||type == (byte) 0x84){
            DeviceData deviceData=DeviceData.builder()
                    .wsName(hostAddress).node(node)
                    .code(code).build();
            datastore(type, value, calculate, deviceData);
            objectMap.put("dd", deviceData);
            super.channelRead(ctx, objectMap);
        }else if (type == (byte) 0x81||type == (byte) 0x82) {
            DeviceRes deviceRes = DeviceRes.builder()
                    .wsName(hostAddress).node(node)
                    .code(code).build();;
            dataRes(type, value, calculate, deviceRes);
            objectMap.put("dr", deviceRes);
            super.channelRead(ctx, objectMap);
        }else {
            ctx.writeAndFlush(new DataAddrDto((byte) 0xff, rec.sender()));
        }
    }

    private void dataRes(byte type, double value, Integer calculate, DeviceRes deviceRes) {
        if (type == (byte) 0x81) {
            deviceRes.setCodeType("81");
            deviceRes.setCodeValue(value / calculate);
        }
        if (type == (byte) 0x82) {
            deviceRes.setCodeType("82");
            double switchStatus = value == DeviceConst.RES_ON ? 1 : 0;
            deviceRes.setCodeValue(switchStatus);
        }
    }

    private void datastore(byte type, double value, Integer calculate, DeviceData deviceData) {
        if (type == (byte) 0x83) {
            deviceData.setType(0);
            deviceData.setCodeValue(value / calculate);
        }
        if (type == (byte) 0x84) {
            deviceData.setType(1);
            double switchStatus = value == DeviceConst.DATE_ON ? 1 : 0;
            deviceData.setCodeValue(switchStatus);
        }
    }
}

