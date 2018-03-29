/**
 * DrmConfRequestParamPersistImpl.java
 * author: yujiakui
 * 2017年12月27日
 * 下午4:40:18
 */
package com.ctfin.framework.drm.admin.test;

import org.springframework.stereotype.Component;

import com.ctfin.framework.drm.admin.DrmConfRequestParamPersist;
import com.ctfin.framework.drm.admin.model.DrmConfRequestParam;

/**
 * @author yujiakui
 *
 *         下午4:40:18
 *
 */
@Component
public class DrmConfRequestParamPersistImpl implements DrmConfRequestParamPersist {

	/* (non-Javadoc)
	 * @see com.ctfin.framework.drm.server.DrmConfRequestParamPersist#save(com.ctfin.framework.drm.server.model.DrmConfRequestParam)
	 */
	@Override
	public void save(DrmConfRequestParam drmConfRequestParam) {
		System.out.println(drmConfRequestParam);
	}

}
