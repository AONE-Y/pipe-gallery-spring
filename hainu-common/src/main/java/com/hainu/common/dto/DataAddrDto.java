package com.hainu.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.InetSocketAddress;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.common.dto
 * @Date：2021/10/10 12:10
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@Data
@AllArgsConstructor
public class DataAddrDto {
    private Object data;
    private InetSocketAddress addr;
}
