/**
 * DrmConfRequestParamPersist.java
 * author: yujiakui
 * 2017年9月19日
 * 下午2:06:11
 */
package com.ctfin.framework.drm.admin;

import com.ctfin.framework.drm.admin.model.DrmConfRequestParam;

/**
 * @author yujiakui
 *
 *         下午2:06:11
 *
 *         drm配置请求参数持久化
 */
public interface DrmConfRequestParamPersist {

	/**
	 * 保存drm配置对应的请求参数
	 *
	 * @param drmConfRequestParam
	 */
	public void save(DrmConfRequestParam drmConfRequestParam);

}
