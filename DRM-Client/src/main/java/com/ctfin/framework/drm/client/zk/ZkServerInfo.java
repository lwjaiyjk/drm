/**
 * ZkServerInfo.java
 * author: yujiakui
 * 2017年12月28日
 * 上午8:53:27
 */
package com.ctfin.framework.drm.client.zk;

import java.io.Serializable;

import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ctfin.framework.drm.client.DrmResourceManager;
import com.ctfin.framework.drm.client.annotation.DrmClassResource;
import com.ctfin.framework.drm.client.annotation.DrmFieldResource;
import com.ctfin.framework.drm.client.utils.LogUtils;

/**
 * @author yujiakui
 *
 *         上午8:53:27
 *
 *         zookeeper服务器相关信息
 */
@Component
@DrmClassResource
public class ZkServerInfo implements Serializable, DrmResourceManager {

	/** logger */
	private final static Logger LOGGER = LoggerFactory.getLogger(ZkServerInfo.class);

	/** serial id */
	private static final long serialVersionUID = 8836672595066519698L;

	/** 服务器地址信息 192.168.1.202:218 可能是一个列表，逗号分隔 */
	@Value("${zk.server.addr}")
	@DrmFieldResource
	private String serverAddrInfo;

	@Autowired
	private ZkBindMonitor zkBindMonitor;

	@Autowired
	private ZkClientFetch zkClientFetch;

	/** 变更前的服务器地址信息 */
	private String befServerAddrInfo = serverAddrInfo;

	/*
	 * @see com.ctfin.framework.drm.client.DrmResourceManager#beforeUpdate()
	 */
	@Override
	public void beforeUpdate() {
		befServerAddrInfo = serverAddrInfo;
		LogUtils.info(LOGGER, "zk 服务器信息befServerAddrInfo={0}", befServerAddrInfo);
	}

	/*
	 * @see com.ctfin.framework.drm.client.DrmResourceManager#afterUpdate()
	 */
	@Override
	public void afterUpdate() {
		LogUtils.info(LOGGER, "zk 服务器信息afterServerAddrInfo={0}", serverAddrInfo);
		if (!StringUtils.equals(serverAddrInfo, befServerAddrInfo)) {
			ZkClient oldZkClient = zkClientFetch.getZkClient();
			try {
				zkClientFetch.createZkClient(serverAddrInfo);
				zkBindMonitor.bindMonitorAndStartInit();
				oldZkClient.close();
			} catch (Throwable throwable) {
				LogUtils.warn(LOGGER,
						"修改zk服务器地址失败befServerAddrInfo={0},serverAddrInfo={1},excepMsg={2}",
						befServerAddrInfo, serverAddrInfo, throwable.getMessage());
				zkClientFetch.setZkClient(oldZkClient);
			}

		}
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
