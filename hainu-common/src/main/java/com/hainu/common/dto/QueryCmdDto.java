package com.hainu.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.common.dto
 * @Date：2021/9/18 21:48
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryCmdDto {

    private String wsName;

    private String node;

    private List<String> options;
}
