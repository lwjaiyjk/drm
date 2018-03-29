/**
 * TestDrmDelete.java
 * author: yujiakui
 * 2018年1月4日
 * 下午3:13:15
 */
package com.ctfin.framework.drm.admin.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ctfin.framework.drm.admin.model.DrmConfRequestParam;
import com.ctfin.framework.drm.admin.service.DrmService;
import com.google.common.collect.Lists;

/**
 * @author yujiakui
 *
 *         下午3:13:15
 *
 */
public class TestDrmDelete {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		System.setProperty("zk.server.addr", "192.168.1.202:2181");
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
				"com.ctfin.framework.drm.admin", "com.ctfin.framework.drm.admin.test");
		DrmConfRequestParam drmConfRequestParam = new DrmConfRequestParam();
		drmConfRequestParam.setApplicationName("yjk");
		drmConfRequestParam
				.setClassName("com.ctfin.framework.drm.client.test.DrmWebInfoCndResource");
		drmConfRequestParam.setFieldName("testDrmValue");
		drmConfRequestParam.setAllDrmFlag(true);
		drmConfRequestParam.setDrmRequestUrl(Lists.newArrayList("172.16.255.123", "127.0.0.1"));
		DrmService drmService = annotationConfigApplicationContext.getBean(DrmService.class);
		// drmService.push(drmConfRequestParam);
		drmService.delete(drmConfRequestParam);

		Thread.sleep(200);
	}

}
