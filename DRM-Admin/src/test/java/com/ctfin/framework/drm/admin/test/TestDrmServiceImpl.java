/**
 * TestDrmServiceImpl.java
 * author: yujiakui
 * 2017年12月27日
 * 上午11:37:44
 */
package com.ctfin.framework.drm.admin.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ctfin.framework.drm.admin.model.DrmConfRequestParam;
import com.ctfin.framework.drm.admin.service.DrmService;

/**
 * @author yujiakui
 *
 *         上午11:37:44
 *
 */
public class TestDrmServiceImpl {

	public static void main(String[] args) throws InterruptedException {
		System.setProperty("zk.server.addr", "192.168.1.202:2181");
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
				"com.ctfin.framework.drm.admin", "com.ctfin.framework.drm.admin.test");
		DrmConfRequestParam drmConfRequestParam = new DrmConfRequestParam();
		drmConfRequestParam.setApplicationName("kccf");
		drmConfRequestParam.setClassName("com.kccf.pc.controller.IndexController");
		drmConfRequestParam.setDrmValue(73);
		drmConfRequestParam.setFieldName("operateNoticesCount");
		drmConfRequestParam.setPersistenceFlag(true);
		drmConfRequestParam.setAllDrmFlag(true);
		// drmConfRequestParam.setDrmRequestUrl(Lists.newArrayList("all"));
		DrmService drmService = annotationConfigApplicationContext.getBean(DrmService.class);
		// drmService.push(drmConfRequestParam);
		drmService.push(drmConfRequestParam);

		Thread.sleep(200);
	}
}
