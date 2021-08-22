package com.hainu.system.shiro;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountProfile implements Serializable {

    private String id;

    private String username;

    private String name;

    private int role;

}
