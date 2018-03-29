/**
 * ZkMonitorEnum.java
 * author: yujiakui
 * 2017年12月27日
 * 上午10:23:46
 */
package com.ctfin.framework.drm.admin.model;

/**
 * @author yujiakui
 *
 *         上午10:23:46
 *
 *         监控枚举
 */
public enum ZkMonitorEnum {

	/** 子节点变化 */
	CHILD_CHANGE("CHILD_CHANGE", "子节点变化", "CHILD_CHANGE", "子节点变化"),

	/** 数据变化 */
	DATA_CHANGE("DATA_CHANGE", "数据变化", "DATA_CHANGE", "数据变化"),

	/** 数据删除 */
	DATA_DEL("DATA_DEL", "数据删除", "DATA_DEL", "数据删除");

	private String code;

	private String chineseName;

	private String englishName;

	private String desc;

	/**
	 * @param key
	 * @param chineseName
	 * @param englishName
	 * @param memo
	 */
	private ZkMonitorEnum(String code, String chineseName, String englishName, String desc) {
		this.code = code;
		this.chineseName = chineseName;
		this.englishName = englishName;
		this.desc = desc;
	}

	/**
	 * 根据code查询对应的枚举类型
	 *
	 * @param code
	 * @return
	 */
	public static ZkMonitorEnum getByCode(String code) {
		for (ZkMonitorEnum ele : values()) {
			if (ele.getCode().equals(code)) {
				return ele;
			}
		}
		return null;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the chineseName
	 */
	public String getChineseName() {
		return chineseName;
	}

	/**
	 * @param chineseName
	 *            the chineseName to set
	 */
	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	/**
	 * @return the englishName
	 */
	public String getEnglishName() {
		return englishName;
	}

	/**
	 * @param englishName
	 *            the englishName to set
	 */
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

}
