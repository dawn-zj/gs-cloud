package com.gs.common.exception;

public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private int errNum;
    private String errMsg;

    public BaseException() {
        super();
    }

    public BaseException(String errMsg) {
        super(errMsg);
        this.errMsg = errMsg;
    }

    public int getErrNum() {
        return errNum;
    }

    public void setErrNum(int errNum) {
        this.errNum = errNum;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
