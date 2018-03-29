/**
 * TestDrmServerInfo.java
 * author: yujiakui
 * 2017年12月28日
 * 上午9:45:13
 */
package com.ctfin.framework.drm.admin.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ctfin.framework.drm.admin.model.DrmConfRequestParam;
import com.ctfin.framework.drm.admin.service.DrmService;
import com.google.common.collect.Lists;

/**
 * @author yujiakui
 *
 *         上午9:45:13
 *
 */
public class TestDrmServerInfo {

	public static void main(String[] args) throws InterruptedException {
		System.setProperty("zk.server.addr", "192.168.1.202:2181");
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
				"com.ctfin.framework.drm.server", "com.ctfin.framework.drm.test.server");
		DrmConfRequestParam drmConfRequestParam = new DrmConfRequestParam();
		drmConfRequestParam.setApplicationName("yjk");
		drmConfRequestParam.setClassName("com.ctfin.framework.drm.client.zk.ZkServerInfo");
		drmConfRequestParam.setDrmValue("192.168.1.202:2181");
		drmConfRequestParam.setFieldName("serverAddrInfo");
		drmConfRequestParam.setPersistenceFlag(true);
		drmConfRequestParam.setDrmRequestUrl(Lists.newArrayList("all"));
		DrmService drmService = annotationConfigApplicationContext.getBean(DrmService.class);
		// drmService.push(drmConfRequestParam);
		drmService.push(drmConfRequestParam);

		Thread.sleep(200);
	}
}
