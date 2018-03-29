/**
 * DrmRequestParam.java
 * author: yujiakui
 * 2017年9月19日
 * 上午9:54:35
 */
package com.ctfin.framework.drm.client;

import java.io.Serializable;

/**
 * @author yujiakui
 *
 *         上午9:54:35
 *
 *         drm推送的请求参数
 */
public class DrmRequestParam implements Cloneable, Serializable {

	/** serial id */
	private static final long serialVersionUID = -2940277243382576657L;

	/** 类名 */
	private String classFullName;

	/** 域名 */
	private String fieldName;

	/** drm推送的值 */
	private Object drmValue;

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("DrmRequestParam{");
		stringBuilder.append("classFullName=").append(classFullName).append(",");
		stringBuilder.append("fieldName=").append(fieldName).append(",");
		stringBuilder.append("drmValue=").append(drmValue).append("}");
		return stringBuilder.toString();
	}

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldName
	 *            the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return the drmValue
	 */
	public Object getDrmValue() {
		return drmValue;
	}

	/**
	 * @param drmValue
	 *            the drmValue to set
	 */
	public void setDrmValue(Object drmValue) {
		this.drmValue = drmValue;
	}

	/**
	 * @return the classFullName
	 */
	public String getClassFullName() {
		return classFullName;
	}

	/**
	 * @param classFullName
	 *            the classFullName to set
	 */
	public void setClassFullName(String classFullName) {
		this.classFullName = classFullName;
	}

}
