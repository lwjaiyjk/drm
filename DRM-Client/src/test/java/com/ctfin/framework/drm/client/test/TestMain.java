/**
 * TestMain.java
 * author: yujiakui
 * 2017年9月19日
 * 下午4:01:11
 */
package com.ctfin.framework.drm.client.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author yujiakui
 *
 *         下午4:01:11
 *
 */
public class TestMain {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		System.setProperty("applicationName", "yjk");
		System.setProperty("zk.server.addr", "192.168.1.202:2181");
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
				"com.ctfin.framework.drm.client", "com.ctfin.framework.drm.client.test");

		/*DrmRequestParam drmRequestParam = new DrmRequestParam();
		drmRequestParam.setClassFullName("com.ctfin.framework.drm.test.DrmWebInfoCndResource");
		drmRequestParam.setFieldName("testDrmValue");
		drmRequestParam.setDrmValue("yjk--------xxxxxx");
		DrmResourceParseFactory drmResourceParseFactory = annotationConfigApplicationContext
				.getBean(DrmResourceParseFactory.class);
		DrmRequestResult result = drmResourceParseFactory.resetFieldValue(drmRequestParam);
		if (!result.isSuccess()) {
			System.out.println(result.getErrorMsg());
		}

		*/

		while (true) {
			Thread.sleep(10);
		}

	}

}
