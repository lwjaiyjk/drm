/**
 * ZkBindMonitor.java
 * author: yujiakui
 * 2018年1月3日
 * 下午4:36:46
 */
package com.ctfin.framework.drm.client.zk;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ctfin.framework.drm.client.DrmRequestParam;
import com.ctfin.framework.drm.client.DrmResourceParseFactory;
import com.ctfin.framework.drm.client.model.ZkPathConstants;
import com.ctfin.framework.drm.client.utils.LogUtils;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

/**
 * @author yujiakui
 *
 *         下午4:36:46
 *
 *         zk 绑定监控
 */
@Component
public class ZkBindMonitor implements InitializingBean {

	/** logger */
	private final static Logger LOGGER = LoggerFactory.getLogger(ZkBindMonitor.class);

	@Autowired
	private ZkClientFetch zkClientFetch;

	@Autowired
	private ZkFieldDataListenerImpl zkFieldDataListenerImpl;

	@Autowired
	private DrmResourceParseFactory drmResourceParseFactory;

	@Value("${applicationName}")
	private String applicationName;

	/*
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		// 解析drm注解
		drmResourceParseFactory.parseDrmAnnotation();
		// 绑定监听和启动初始化
		bindMonitorAndStartInit();
	}

	/**
	 * 绑定监控和启动初始化
	 */
	public void bindMonitorAndStartInit() {
		/*	// 获取spring上下文的应用程序名称，如果不为空则设置对应的应用程序名为spring应用程序名称
			if (StringUtils.isNotEmpty(applicationContext.getApplicationName())) {
				applicationName = applicationContext.getApplicationName();
			}*/
		if (StringUtils.isEmpty(applicationName)) {
			throw new RuntimeException("DRM 要求对应的applicationName不能为空");
		}
		String zkApplicationPath = getZkApplicationPath();
		// 处理 ALL和特定 机器对应的路径：如果不存在则创建，如果存在则判断是否是持久化，如果不是则不进行处理
		handleMachinePath(zkApplicationPath);
	}

	/**
	 * 处理所有的机器对应的路径：如：/CTFIN/DRM/应用程序名称/all
	 *
	 * @param zkApplicationPath
	 */
	private void handleMachinePath(String zkApplicationPath) {

		// zk all对应的路径
		String zkAllPath = zkApplicationPath + ZkPathConstants.ALL + ZkPathConstants.PATH_SEP;

		// zkClient客户端
		ZkClient zkClient = zkClientFetch.getZkClient();
		String localHostIp = getLocalIp();

		// 创建路径: /CTFIN/DRM/应用程序/all/ip/localip
		createLocalIpPath(zkAllPath, zkClient, localHostIp);

		// 获取特定机器对应的zk 路径
		String zkIpPath = zkApplicationPath + localHostIp + ZkPathConstants.PATH_SEP;

		// 获取所有的需要drm推送的属性
		Table<String, String, Field> drmFieldTable = drmResourceParseFactory.getParseResultTable();
		for (Cell<String, String, Field> drmEle : drmFieldTable.cellSet()) {
			String className = drmEle.getRowKey();
			String fieldName = drmEle.getColumnKey();
			// 处理路径： /CTFIN/DRM/应用程序名称/all
			handleAllMachineField(zkAllPath, zkClient, className, fieldName);
			// 处理路径： /CTFIN/DRM/应用程序名称/特定机器
			handleIpMachineField(zkIpPath, zkClient, className, fieldName);
		}

	}

	/**
	 * 创建路径: /CTFIN/DRM/应用程序/all/ip/localip
	 *
	 * @param zkAllPath
	 * @param zkClient
	 * @param localHostIp
	 */
	private void createLocalIpPath(String zkAllPath, ZkClient zkClient, String localHostIp) {
		String zkAllIpPath = zkAllPath + ZkPathConstants.IP;
		if (!zkClient.exists(zkAllIpPath)) {
			zkClient.createPersistent(zkAllIpPath, true);
		}
		String curIpTempNode = zkAllIpPath + ZkPathConstants.PATH_SEP + localHostIp;
		if (!zkClient.exists(curIpTempNode)) {
			zkClient.createEphemeral(curIpTempNode);
		}
	}

	/**
	 * 处理特定机器的字段
	 *
	 * @param zkIpPath
	 * @param zkClient
	 * @param className
	 * @param fieldName
	 */
	private void handleIpMachineField(String zkIpPath, ZkClient zkClient, String className,
			String fieldName) {
		String zkIpFieldPath = zkIpPath + className + ZkPathConstants.PATH_SEP + fieldName;
		if (zkClient.exists(zkIpFieldPath)) {
			zkClient.subscribeDataChanges(zkIpFieldPath, zkFieldDataListenerImpl);
		}
		String zkIpPersistFieldPath = zkIpFieldPath + ZkPathConstants.PATH_SEP
				+ ZkPathConstants.PERSIST;
		if (zkClient.exists(zkIpPersistFieldPath)) {
			// 持久化路径存在
			Object drmPersistFieldValue = zkClient.readData(zkIpPersistFieldPath);
			handleDrmValue(className, fieldName, drmPersistFieldValue);
		}
	}

	/**
	 * 处理所有机器路径的属性 ： /CTFIN/DRM/应用程序名称/all
	 *
	 * @param zkAllPath
	 * @param zkClient
	 * @param className
	 * @param fieldName
	 */
	private void handleAllMachineField(String zkAllPath, ZkClient zkClient, String className,
			String fieldName) {
		String zkAllFieldPath = zkAllPath + className + ZkPathConstants.PATH_SEP + fieldName;
		if (zkClient.exists(zkAllFieldPath)) {

			String zkPersistPath = zkAllFieldPath + ZkPathConstants.PATH_SEP
					+ ZkPathConstants.PERSIST;
			if (zkClient.exists(zkPersistPath)) {
				// 处理持久化操作---在后台推送的时候，如果推送的所有的机器对应的属性，则同时会修改特定机器对应的属性，
				// 但是对于特定机器的推送，则不会修改对应的所有机器对应的属性，也就是说特定机器对应的属性要优先于所有机器
				Object drmPersistFieldValue = zkClient.readData(zkAllFieldPath);
				handleDrmValue(className, fieldName, drmPersistFieldValue);
			}
		} else {
			// 仅仅创建路径，不进行值得设置
			zkClient.createPersistent(zkAllFieldPath, true);
		}
		// 对于属性仅仅监听对应的值得变化
		zkClient.subscribeDataChanges(zkAllFieldPath, zkFieldDataListenerImpl);
	}

	/**
	 * 处理drm推送的值
	 *
	 * @param className
	 * @param fieldName
	 * @param drmValue
	 */
	private void handleDrmValue(String className, String fieldName, Object drmValue) {
		DrmRequestParam drmRequestParam = new DrmRequestParam();
		drmRequestParam.setClassFullName(className);
		drmRequestParam.setFieldName(fieldName);
		drmRequestParam.setDrmValue(drmValue);
		drmResourceParseFactory.resetFieldValue(drmRequestParam);
	}

	/**
	 * 获取zk对应的应用程序地址路径 例如： /CTFIN/DRM/应用程序名称/
	 *
	 * @return
	 */
	private String getZkApplicationPath() {
		StringBuilder stringBuilder = new StringBuilder(ZkPathConstants.ROOT_PATH);
		stringBuilder.append(ZkPathConstants.PATH_SEP);
		stringBuilder.append(applicationName);
		stringBuilder.append(ZkPathConstants.PATH_SEP);
		return stringBuilder.toString();
	}

	/**
	 * 获取本机对应的ip地址
	 *
	 * @return
	 */
	private String getLocalIp() {
		// 获取当前机器的ip地址：
		String ipAddr = "ERROR_IP";
		try {
			InetAddress localAddress = InetAddress.getLocalHost();
			ipAddr = localAddress.getHostAddress();
		} catch (UnknownHostException e) {
			LogUtils.error(LOGGER, "获取当前机器对应的ip地址失败errorMsg={0}", e.getMessage());
		}
		return ipAddr;
	}

}
