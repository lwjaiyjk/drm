/**
 * DrmResource.java
 * author: yujiakui
 * 2017年9月19日
 * 上午8:36:36
 */
package com.ctfin.framework.drm.client.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
/**
 * @author yujiakui
 *
 *         上午8:36:36
 *
 *         资源管理的注解
 *
 */
public @interface DrmClassResource {

}
