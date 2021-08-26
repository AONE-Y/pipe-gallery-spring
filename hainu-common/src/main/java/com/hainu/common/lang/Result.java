package com.hainu.common.lang;

import com.hainu.common.constant.ResponseConstant;

/**
 * @author ANONE
 */
public class Result<T> {

    private  int code;

    private  String msg;

    private T data;

    /**
     * 返回成功
     */
    public Result<T> success() {
        return success("操作成功！");
    }

    /**
     * 返回成功
     */
    public Result<T> success(String message) {
        return success(200, message);
    }

    /**
     * 返回成功
     */
    public Result<T> success(ResponseConstant constant) {
        return success(constant.getResult(), constant.getMsg());
    }

    /**
     * 返回成功
     */
    public Result<T> success(int code, String message) {
        this.setCode(code);
        this.setMsg(message);
        return this;
    }

    /**
     * 返回失败
     */
    public Result<T> error() {
        return error("操作失败！");
    }

    /**
     * 返回失败
     */
    public Result<T> error(String messag) {
        return error(500, messag);
    }

    /**
     * 返回失败
     */
    public Result<T> error(int code, String message) {
        return success(code, message);
    }

    /**
     * 返回信息
     */
    public Result<T> error(ResponseConstant constant) {
        return success(constant.getResult(), constant.getMsg());
    }
    /**
     * 放入object
     */
    public Result<T> put(T object) {
        this.setData(object);
        return this;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
