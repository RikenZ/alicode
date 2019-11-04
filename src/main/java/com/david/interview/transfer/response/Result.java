package com.david.interview.transfer.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 4692958320047342711L;

    /**
     * 返回true表示业务受理成功,返回false表示业务受理失败
     */
    private boolean success;

    /**
     * 需要返回的数据对象,如转账时返回的是Transfer对象
     */
    private T data;

    /**
     * 业务受理失败的原因
     */
    private String errorMessage;

    public Result(T result) {
        this.success = true;
        this.data = result;
    }

    public Result(RuntimeException e) {
        this.success = false;
        this.errorMessage = e.getMessage();
    }
}
