/**
 * TestDrmList.java
 * author: yujiakui
 * 2018年1月8日
 * 下午3:26:25
 */
package com.ctfin.framework.drm.admin.test;

import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ctfin.framework.drm.admin.model.DrmConfRequestParam;
import com.ctfin.framework.drm.admin.service.DrmService;

/**
 * @author yujiakui
 *
 *         下午3:26:25
 *
 */
public class TestDrmList {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		System.setProperty("zk.server.addr", "192.168.1.202:2181");
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
				"com.ctfin.framework.drm.admin", "com.ctfin.framework.drm.admin.test");
		DrmService drmService = annotationConfigApplicationContext.getBean(DrmService.class);
		// drmService.push(drmConfRequestParam);
		List<DrmConfRequestParam> drmConfRequestParams = drmService.listAllDrmInfo();
		System.out.println(drmConfRequestParams);

		Thread.sleep(200);
	}

}
