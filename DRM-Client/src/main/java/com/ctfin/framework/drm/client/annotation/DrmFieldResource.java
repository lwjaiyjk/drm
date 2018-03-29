/**
 * Resource.java
 * author: yujiakui
 * 2017年9月19日
 * 上午8:38:08
 */
package com.ctfin.framework.drm.client.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
/**
 * @author yujiakui
 *
 *         上午8:38:08
 *
 *         资源管理对应的资源：是一个标记接口
 */
public @interface DrmFieldResource {

}
