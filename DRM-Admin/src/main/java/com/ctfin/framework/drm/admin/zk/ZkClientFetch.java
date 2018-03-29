/**
 * ZkClientFetch.java
 * author: yujiakui
 * 2017年12月27日
 * 上午10:09:21
 */
package com.ctfin.framework.drm.admin.zk;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author yujiakui
 *
 *         上午10:09:21
 *
 *         zookeeper的客户端获取器
 */
@Component
public class ZkClientFetch implements InitializingBean {
	/** 服务器地址信息 */
	/** 服务器地址信息 192.168.1.202:2181 */
	@Value("${zk.server.addr}")
	private String serverAddrInfo;

	/** zookeeper 客户端 */
	private ZkClient zkClient;

	/** session 超时 */
	private final static int SESSION_TIMEOUT = 3000;

	/** 链接 超时 */
	private final static int CONNECTION_TIMEOUT = 3000;

	/*
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		zkClient = new ZkClient(serverAddrInfo.trim(), SESSION_TIMEOUT, CONNECTION_TIMEOUT,
				new MyZkSerializer());
	}

	/**
	 * @return the zkClient
	 */
	public ZkClient getZkClient() {
		return zkClient;
	}

	/**
	 * @param zkClient
	 *            the zkClient to set
	 */
	public void setZkClient(ZkClient zkClient) {
		this.zkClient = zkClient;
	}

	/**
	 * @return the serverAddrInfo
	 */
	public String getServerAddrInfo() {
		return serverAddrInfo;
	}

	/**
	 * @param serverAddrInfo
	 *            the serverAddrInfo to set
	 */
	public void setServerAddrInfo(String serverAddrInfo) {
		this.serverAddrInfo = serverAddrInfo;
	}

}
