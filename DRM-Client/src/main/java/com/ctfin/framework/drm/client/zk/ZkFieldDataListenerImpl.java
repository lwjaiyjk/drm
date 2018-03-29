/**
 * ZkFieldDataListenerImpl.java
 * author: yujiakui
 * 2018年1月3日
 * 下午5:19:47
 */
package com.ctfin.framework.drm.client.zk;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ctfin.framework.drm.client.DrmRequestParam;
import com.ctfin.framework.drm.client.DrmResourceParseFactory;
import com.ctfin.framework.drm.client.model.ZkPathConstants;
import com.ctfin.framework.drm.client.utils.LogUtils;

/**
 * @author yujiakui
 *
 *         下午5:19:47
 *
 */
@Component
public class ZkFieldDataListenerImpl implements IZkDataListener {

	/** logger */
	private final static Logger LOGGER = LoggerFactory.getLogger(ZkFieldDataListenerImpl.class);

	@Autowired
	private ZkClientFetch zkClientFetch;

	@Autowired
	private DrmResourceParseFactory drmResourceParseFactory;

	/* (non-Javadoc)
	 * @see org.I0Itec.zkclient.IZkDataListener#handleDataChange(java.lang.String, java.lang.Object)
	 */
	@Override
	public void handleDataChange(String dataPath, Object data) throws Exception {
		LogUtils.info(LOGGER, "handleDataChange dataPath={0},data={1}", dataPath, data);
		// 获取路径上的类名，属性名和对应的属性只，然后进行drm推送
		int pathSepNum = StringUtils.countMatches(dataPath, ZkPathConstants.PATH_SEP);
		if (ZkPathConstants.FIELD_PATH_SEG_NUM != pathSepNum) {
			LogUtils.warn(LOGGER, "监听数据变更的路径分隔符{0}的数量{1}不等于规定的值{2}", ZkPathConstants.PATH_SEP,
					pathSepNum, ZkPathConstants.FIELD_PATH_SEG_NUM);
			return;
		}

		// 从路径中截取对应的类名和属性名
		parseAndresetFieldValue(dataPath);
	}

	/**
	 * 解析对应的路径获得对应的类名和属性名，然后将对应的值推送
	 *
	 * @param dataPath
	 */
	private void parseAndresetFieldValue(String dataPath) {
		int index = StringUtils.lastIndexOf(dataPath, ZkPathConstants.PATH_SEP);
		String fieldName = StringUtils.substring(dataPath, index + 1);
		String parentDataPath = StringUtils.substring(dataPath, 0, index);
		index = StringUtils.lastIndexOf(parentDataPath, ZkPathConstants.PATH_SEP);
		String className = StringUtils.substring(parentDataPath, index + 1);
		ZkClient zkClient = zkClientFetch.getZkClient();
		Object fieldContent = zkClient.readData(dataPath);
		DrmRequestParam drmRequestParam = new DrmRequestParam();
		drmRequestParam.setClassFullName(className);
		drmRequestParam.setFieldName(fieldName);
		drmRequestParam.setDrmValue(fieldContent);

		// drm推送值
		drmResourceParseFactory.resetFieldValue(drmRequestParam);
	}

	/* (non-Javadoc)
	 * @see org.I0Itec.zkclient.IZkDataListener#handleDataDeleted(java.lang.String)
	 */
	@Override
	public void handleDataDeleted(String dataPath) throws Exception {
		LogUtils.info(LOGGER, "handleDataDeleted dataPath={0}", dataPath);
	}

}
