/**
 * DrmResult.java
 * author: yujiakui
 * 2017年9月19日
 * 上午10:06:07
 */
package com.ctfin.framework.drm.client;

import java.io.Serializable;

/**
 * @author yujiakui
 *
 *         上午10:06:07
 *
 *         drm推送请求结果
 */
public class DrmRequestResult implements Cloneable, Serializable {

	/** serial id */
	private static final long serialVersionUID = -9163822003977459918L;

	/** 成功标记 */
	private boolean success = true;

	/** 失败信息 */
	private String errorMsg;

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success
	 *            the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the errorMsg
	 */
	public String getErrorMsg() {
		return errorMsg;
	}

	/**
	 * @param errorMsg
	 *            the errorMsg to set
	 */
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
