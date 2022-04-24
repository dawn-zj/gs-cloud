package com.gs.common.exception;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 */

public class NetGSRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int errNum;
	private String errMsg;

	public NetGSRuntimeException() {
		super();
	}

	public NetGSRuntimeException(String msg) {
		super(msg);
		this.errMsg = msg;
	}

	public NetGSRuntimeException(String msg, Throwable e) {
		super(msg, e);
		this.errMsg = msg;
	}

	public NetGSRuntimeException(int errNum, String msg) {
		super(msg);
		this.errNum = errNum;
		this.errMsg = msg;
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

	public String getMessage() {
		return errMsg;
	}

}