package com.hainu.system.shiro;

import org.apache.shiro.authc.AuthenticationToken;

public class JWTToken implements AuthenticationToken {

    private static final long serialVersionUID = -2819620437297382200L;

    /**
     * 密钥token
     * @return
     */
    private String token;

    public JWTToken(String token){
        this.token = token;
    }


    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
