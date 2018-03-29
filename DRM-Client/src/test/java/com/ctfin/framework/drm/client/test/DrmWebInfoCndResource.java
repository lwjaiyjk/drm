/**
 * DrmWebInfoCndResource.java
 * author: yujiakui
 * 2017年9月19日
 * 下午3:59:34
 */
package com.ctfin.framework.drm.client.test;

import org.springframework.stereotype.Component;

import com.ctfin.framework.drm.client.annotation.DrmClassResource;
import com.ctfin.framework.drm.client.annotation.DrmFieldResource;

/**
 * @author yujiakui
 *
 *         下午3:59:34
 *
 */
@Component
@DrmClassResource
public class DrmWebInfoCndResource /*implements DrmResourceManager*/ {

	@DrmFieldResource
	private String testDrmValue = "helloworld";

	/*
	 * @see com.ctfin.framework.drm.client.DrmResourceManager#beforeUpdate()
	 */
	/*@Override
	public void beforeUpdate() {
		System.out.println("============beforeUpdate===============");
		System.out.println(testDrmValue);
	}*/

	/*
	 * @see com.ctfin.framework.drm.client.DrmResourceManager#afterUpdate()
	 */
	/*@Override
	public void afterUpdate() {
		System.out.println("--------------afterUpdate------------------");
		System.out.println(testDrmValue);
	}
	*/
}
