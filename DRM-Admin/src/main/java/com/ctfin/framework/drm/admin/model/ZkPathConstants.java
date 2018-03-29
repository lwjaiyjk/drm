/**
 * ZkPathConstants.java
 * author: yujiakui
 * 2017年12月27日
 * 上午11:01:24
 */
package com.ctfin.framework.drm.admin.model;

/**
 * @author yujiakui
 *
 *         上午11:01:24
 *
 *         zookeeper路径常量
 */
public interface ZkPathConstants {

	/** 根目录 */
	String ROOT_PATH = "/CTFIN/DRM";

	/** 持久化 */
	String PERSIST = "persist";

	/** 非持久化 */
	String NO_PERSIST = "nopersist";

	/** 路径分割符号 */
	String PATH_SEP = "/";

	/** 对所有的机器都试用 */
	String ALL = "all";

	/** 所有应用程序的ip地址 */
	String IP = "ip";
}
