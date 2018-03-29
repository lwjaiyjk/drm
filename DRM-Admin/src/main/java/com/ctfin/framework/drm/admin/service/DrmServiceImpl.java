/**
 * DrmServiceImpl.java
 * author: yujiakui
 * 2017年12月27日
 * 上午9:21:37
 */
package com.ctfin.framework.drm.admin.service;

import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.ctfin.framework.drm.admin.DrmConfRequestParamPersist;
import com.ctfin.framework.drm.admin.model.DrmConfRequestParam;
import com.ctfin.framework.drm.admin.model.ZkPathConstants;
import com.ctfin.framework.drm.admin.zk.ZkClientFetch;
import com.google.common.collect.Lists;

/**
 * @author yujiakui
 *
 *         上午9:21:37
 *
 *         推送请求服务
 */
@Component
public class DrmServiceImpl implements DrmService {

	@Autowired
	private ZkClientFetch zkClientFetch;

	@Autowired
	private DrmConfRequestParamPersist drmConfRequestParamPersist;

	/* (non-Javadoc)
	 * @see com.ctfin.framework.drm.server.service.DrmService#drmPush(com.ctfin.framework.drm.server.model.DrmConfRequestParam)
	 */
	@Override
	public boolean push(DrmConfRequestParam drmConfRequestParam) {
		List<String> reqUrls = drmConfRequestParam.getDrmRequestUrl();
		ZkClient zkClient = zkClientFetch.getZkClient();
		// 如果所有的都推送，则处理所有都推送的场景，对于推送是all的场景，还要覆盖推送所有的特定机器的
		handleAllMachineDrm(drmConfRequestParam, zkClient);

		if (!CollectionUtils.isEmpty(reqUrls)) {
			// 处理特定机器的推送，要注意对于推送all的情况，其会同时更新对应的机器field的值
			handleReqUrlDrm(drmConfRequestParam, reqUrls, zkClient);
		}

		// 保存推送的日志：记录对应的推送流水
		// 记录推送的日志--记录推送信息
		drmConfRequestParamPersist.save(drmConfRequestParam);

		return true;
	}

	/**
	 * 处理reqUrl对应的drm推送，如/CTFIN/DRM/特定机器/类名/属性名/persist或者nopersist
	 *
	 * @param drmConfRequestParam
	 * @param reqUrls
	 * @param zkClient
	 */
	private void handleReqUrlDrm(DrmConfRequestParam drmConfRequestParam, List<String> reqUrls,
			ZkClient zkClient) {
		for (String reqUrl : reqUrls) {
			String reqUrlFieldpath = StringUtils.join(
					Lists.newArrayList(ZkPathConstants.ROOT_PATH,
							drmConfRequestParam.getApplicationName(), reqUrl,
							drmConfRequestParam.getClassName(), drmConfRequestParam.getFieldName()),
					ZkPathConstants.PATH_SEP);
			String reqUrlPersistFieldPath = reqUrlFieldpath + ZkPathConstants.PATH_SEP
					+ ZkPathConstants.PERSIST;
			String reqUrlNoPersistFieldPath = reqUrlFieldpath + ZkPathConstants.PATH_SEP
					+ ZkPathConstants.NO_PERSIST;
			// 持久化场景
			if (drmConfRequestParam.isPersistenceFlag()) {
				if (zkClient.exists(reqUrlPersistFieldPath)) {
					zkClient.writeData(reqUrlFieldpath, drmConfRequestParam.getDrmValue());
				} else if (zkClient.exists(reqUrlNoPersistFieldPath)) {
					zkClient.createPersistent(reqUrlPersistFieldPath, true);
					zkClient.writeData(reqUrlFieldpath, drmConfRequestParam.getDrmValue());
					zkClient.delete(reqUrlNoPersistFieldPath);
				}
				zkClient.delete(reqUrlPersistFieldPath);
			} else {
				if (zkClient.exists(reqUrlNoPersistFieldPath)) {
					zkClient.writeData(reqUrlFieldpath, drmConfRequestParam.getDrmValue());
				} else if (zkClient.exists(reqUrlPersistFieldPath)) {
					zkClient.createPersistent(reqUrlNoPersistFieldPath, true);
					zkClient.writeData(reqUrlFieldpath, drmConfRequestParam.getDrmValue());
					zkClient.delete(reqUrlPersistFieldPath);
				}
			}
		}
	}

	/**
	 * 处理所有机器的drm推送：生成对应的路径：/CTFIN/DRM/应用程序名称/ALL/类名/属性名/persist或者nopersist
	 *
	 * @param drmConfRequestParam
	 * @param zkClient
	 */
	private void handleAllMachineDrm(DrmConfRequestParam drmConfRequestParam, ZkClient zkClient) {
		if (drmConfRequestParam.isAllDrmFlag()) {
			String zkAllFieldPath = StringUtils.join(
					Lists.newArrayList(ZkPathConstants.ROOT_PATH,
							drmConfRequestParam.getApplicationName(), ZkPathConstants.ALL,
							drmConfRequestParam.getClassName(), drmConfRequestParam.getFieldName()),
					ZkPathConstants.PATH_SEP);
			String zkAllPath = zkAllFieldPath + ZkPathConstants.PATH_SEP
					+ (drmConfRequestParam.isPersistenceFlag() ? ZkPathConstants.PERSIST
							: ZkPathConstants.NO_PERSIST);
			if (!zkClient.exists(zkAllPath)) {
				zkClient.createPersistent(zkAllPath);
			}
			zkClient.writeData(zkAllFieldPath, drmConfRequestParam.getDrmValue());
		}
	}

	/* (non-Javadoc)
	 * @see com.ctfin.framework.drm.admin.service.DrmService#delete(com.ctfin.framework.drm.admin.model.DrmConfRequestParam)
	 */
	@Override
	public boolean delete(DrmConfRequestParam drmConfRequestParam) {
		ZkClient zkClient = zkClientFetch.getZkClient();
		String zkApplicationPath = ZkPathConstants.ROOT_PATH + ZkPathConstants.PATH_SEP
				+ drmConfRequestParam.getApplicationName();
		// 先判断对应的路径是否存在
		if (drmConfRequestParam.isAllDrmFlag()) {
			deleteZkPath(zkClient, zkApplicationPath, ZkPathConstants.ALL,
					drmConfRequestParam.getClassName(), drmConfRequestParam.getFieldName());
			String zkAllPath = zkApplicationPath + ZkPathConstants.PATH_SEP + ZkPathConstants.ALL;
			String zkAppIpPath = zkAllPath + ZkPathConstants.PATH_SEP + ZkPathConstants.IP;
			int zkAllPathChildNum = zkClient.countChildren(zkAllPath);
			int zkAllIpPathChildNum = zkClient.countChildren(zkAppIpPath);
			if (1 == zkAllPathChildNum && 0 == zkAllIpPathChildNum) {
				// 删除路径：/CTFIN/DRM/应用程序名称/all/ip/
				deleteRecureZkPath(zkClient, zkAppIpPath, ZkPathConstants.ROOT_PATH);
			}
		}

		if (!CollectionUtils.isEmpty(drmConfRequestParam.getDrmRequestUrl())) {
			for (String reqUrl : drmConfRequestParam.getDrmRequestUrl()) {
				deleteZkPath(zkClient, zkApplicationPath, reqUrl,
						drmConfRequestParam.getClassName(), drmConfRequestParam.getFieldName());
			}
		}
		return true;
	}

	/**
	 * 递归删除zk对应的路径，一直删除到应用程序
	 *
	 * @param zkClient
	 * @param zkAppPath
	 * @param machineName
	 * @param className
	 * @param fieldName
	 */
	private void deleteZkPath(ZkClient zkClient, String zkAppPath, String machineName,
			String className, String fieldName) {
		String zkAllFieldPath = zkAppPath + ZkPathConstants.PATH_SEP + machineName;
		if (StringUtils.isNotBlank(className)) {
			zkAllFieldPath += (ZkPathConstants.PATH_SEP + className);
			if (StringUtils.isNotBlank(fieldName)) {
				zkAllFieldPath += (ZkPathConstants.PATH_SEP + fieldName);
			}
		}
		// 递归删除
		deleteRecureZkPath(zkClient, zkAllFieldPath, ZkPathConstants.ROOT_PATH);

	}

	/**
	 * 递归删除
	 *
	 * @param zkClient
	 * @param delPath
	 *            删除的路径
	 * @param stopPath
	 *            终止的路径
	 */
	private void deleteRecureZkPath(ZkClient zkClient, String delPath, String stopPath) {

		if (StringUtils.equals(delPath, stopPath) || !zkClient.exists(delPath)) {
			return;
		}
		zkClient.deleteRecursive(delPath);
		String parentPath = StringUtils.substringBeforeLast(delPath, ZkPathConstants.PATH_SEP);

		// 没有到达终止条件
		while (!StringUtils.equals(parentPath, stopPath)) {

			int childNum = zkClient.countChildren(parentPath);
			if (0 == childNum) {
				zkClient.delete(parentPath);
				parentPath = StringUtils.substringBeforeLast(parentPath, ZkPathConstants.PATH_SEP);
			} else {
				break;
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.ctfin.framework.drm.admin.service.DrmService#listAllDrmInfo()
	 */
	@Override
	public List<DrmConfRequestParam> listAllDrmInfo() {
		ZkClient zkClient = zkClientFetch.getZkClient();
		List<DrmConfRequestParam> drmConfRequestParams = Lists.newArrayList();
		List<String> applicationNames = zkClient.getChildren(ZkPathConstants.ROOT_PATH);

		for (String applicationName : applicationNames) {
			String zkAllPath = ZkPathConstants.ROOT_PATH + ZkPathConstants.PATH_SEP
					+ applicationName + ZkPathConstants.PATH_SEP + ZkPathConstants.ALL;
			List<String> zkAllChildNames = zkClient.getChildren(zkAllPath);
			List<String> ipAddrs = zkClient
					.getChildren(zkAllPath + ZkPathConstants.PATH_SEP + ZkPathConstants.IP);
			for (String className : zkAllChildNames) {
				if (StringUtils.equals(ZkPathConstants.IP, className)) {
					continue;
				}
				// 遍历获得属性
				String classNamePath = zkAllPath + ZkPathConstants.PATH_SEP + className;
				List<String> zkFieldNames = zkClient.getChildren(classNamePath);
				for (String fieldName : zkFieldNames) {
					DrmConfRequestParam drmConfRequestParam = new DrmConfRequestParam();
					drmConfRequestParam.setApplicationName(applicationName);
					drmConfRequestParam.setClassName(className);
					drmConfRequestParam.setFieldName(fieldName);
					drmConfRequestParam.setDrmRequestUrl(ipAddrs);
					drmConfRequestParams.add(drmConfRequestParam);
				}
			}
		}
		return drmConfRequestParams;
	}

}
