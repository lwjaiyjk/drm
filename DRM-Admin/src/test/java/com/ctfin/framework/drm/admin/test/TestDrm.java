/**
 * TestDrm.java
 * author: yujiakui
 * 2018年1月2日
 * 上午10:31:07
 */
package com.ctfin.framework.drm.admin.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ctfin.framework.drm.admin.model.DrmConfRequestParam;
import com.ctfin.framework.drm.admin.service.DrmService;
import com.google.common.collect.Lists;

/**
 * @author yujiakui
 *
 *         上午10:31:07
 *
 */
public class TestDrm {

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
		drmConfRequestParam.setClassName("com.ctfin.framework.throttle.drm.ThrottleDrmManager");
		drmConfRequestParam.setDrmValue(
				"com.ctfin.framework.throttle.test.EnableThrottingPolicyAnnotationSubTest#public void com.ctfin.framework.throttle.test.EnableThrottingPolicyAnnotationSubTest.test(java.lang.String,java.lang.String)#test1#second#15");
		drmConfRequestParam.setFieldName("drmModifyThrottleInfo");
		drmConfRequestParam.setPersistenceFlag(true);
		drmConfRequestParam.setAllDrmFlag(true);
		drmConfRequestParam.setDrmRequestUrl(Lists.newArrayList("172.16.255.123", "127.0.0.1"));
		DrmService drmService = annotationConfigApplicationContext.getBean(DrmService.class);
		// drmService.push(drmConfRequestParam);
		drmService.push(drmConfRequestParam);

		Thread.sleep(200);
	}

}
