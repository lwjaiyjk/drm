/**
 * DrmConfRequestParam.java
 * author: yujiakui
 * 2017年9月19日
 * 下午1:46:42
 */
package com.ctfin.framework.drm.admin.model;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author yujiakui
 *
 *         下午1:46:42
 *
 *         drm配置请求参数
 */
public class DrmConfRequestParam implements Cloneable, Serializable {

	/** serial id */
	private static final long serialVersionUID = -4391448172258037621L;

	/** drm请求对应的url地址:如果推送所有的则对应的url列表中存放的就是all */
	private List<String> drmRequestUrl;

	/** 持久化标记 */
	private boolean persistenceFlag;

	/** 应用程序的名称 */
	private String applicationName;

	/** 类名 */
	private String className;

	/** 字段名称 */
	private String fieldName;

	/** drm推送的值 */
	private Object drmValue;

	/** 是否所有都进行推送 */
	private boolean allDrmFlag;

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("DrmConfRequestParam{");
		stringBuilder.append("drmRequestUrl=[").append(drmRequestUrl).append("],");
		stringBuilder.append("persistenceFlag=").append(persistenceFlag).append(",");
		stringBuilder.append("applicationName=").append(applicationName).append(",");
		stringBuilder.append("className=").append(className).append(",");
		stringBuilder.append("fieldName=").append(fieldName).append(",");
		stringBuilder.append("allDrmFlag=").append(allDrmFlag).append(",");
		stringBuilder.append("drmValue=").append(drmValue);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}

	@Override
	public DrmConfRequestParam clone() throws CloneNotSupportedException {
		DrmConfRequestParam drmConfRequestParam = (DrmConfRequestParam) super.clone();
		drmConfRequestParam.setApplicationName(applicationName);
		drmConfRequestParam.setClassName(className);
		drmConfRequestParam.setDrmRequestUrl(Lists.newArrayList(drmRequestUrl));
		drmConfRequestParam.setDrmValue(drmValue);
		drmConfRequestParam.setFieldName(fieldName);
		drmConfRequestParam.setPersistenceFlag(persistenceFlag);
		drmConfRequestParam.setAllDrmFlag(allDrmFlag);
		return drmConfRequestParam;
	}

	/**
	 * @return the persistenceFlag
	 */
	public boolean isPersistenceFlag() {
		return persistenceFlag;
	}

	/**
	 * @param persistenceFlag
	 *            the persistenceFlag to set
	 */
	public void setPersistenceFlag(boolean persistenceFlag) {
		this.persistenceFlag = persistenceFlag;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
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
	 * @return the drmRequestUrl
	 */
	public List<String> getDrmRequestUrl() {
		return drmRequestUrl;
	}

	/**
	 * @param drmRequestUrl
	 *            the drmRequestUrl to set
	 */
	public void setDrmRequestUrl(List<String> drmRequestUrl) {
		this.drmRequestUrl = drmRequestUrl;
	}

	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @param applicationName
	 *            the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	/**
	 * @return the allDrmFlag
	 */
	public boolean isAllDrmFlag() {
		return allDrmFlag;
	}

	/**
	 * @param allDrmFlag
	 *            the allDrmFlag to set
	 */
	public void setAllDrmFlag(boolean allDrmFlag) {
		this.allDrmFlag = allDrmFlag;
	}

}
