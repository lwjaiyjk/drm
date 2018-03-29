/**
 * DrmResourceParseFactory.java
 * author: yujiakui
 * 2017年9月19日
 * 上午8:46:00
 */
package com.ctfin.framework.drm.client;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.ctfin.framework.drm.client.annotation.DrmClassResource;
import com.ctfin.framework.drm.client.annotation.DrmFieldResource;
import com.ctfin.framework.drm.client.utils.LogUtils;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * @author yujiakui
 *
 *         上午8:46:00
 *
 *         自定义注解link @DrmResource 和link @Resource 解析工厂类
 */
@Component
public class DrmResourceParseFactory implements ApplicationContextAware {

	/** logger */
	private final static Logger LOGGER = LoggerFactory.getLogger(DrmResourceParseFactory.class);

	/** spring上下文 */
	private ApplicationContext applicationContext;

	/** 解析结果table，其中rowKey=类全名，columnKey=属性名,value=属性的method */
	private Table<String, String, Field> parseResultTable = HashBasedTable.create();

	/*
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * 重置bean对应的field值
	 *
	 * @param param
	 * @return
	 */
	public DrmRequestResult resetFieldValue(DrmRequestParam param) {

		// 1. 根据ClassName获取对应的Class对象
		try {
			Class<?> classObj = getClassByName(param.getClassFullName());
			Object beanObj = getBeanObjByClass(classObj);
			Object targetBeanObj = getTarget(beanObj);

			// 2. 根据className + fieldName获得对应的Field对象,这个实际上也校验了对应的类是否实现
			setDrmFieldValue(param, targetBeanObj);
		} catch (RuntimeException exception) {
			return assembleErrorResult(exception.getMessage(), exception);
		}
		return new DrmRequestResult();
	}

	/**
	 * 设置fieldDrmValue
	 *
	 * @param param
	 * @param beanObj
	 */
	private void setDrmFieldValue(DrmRequestParam param, Object beanObj) {
		Field field = parseResultTable.get(param.getClassFullName(), param.getFieldName());
		if (field == null) {
			throw new RuntimeException(MessageFormat.format("类名{0}和属性名{1}对应的field不存在",
					param.getClassFullName(), param.getFieldName()));
		}

		DrmResourceManager drmResourceManager = null;
		if (beanObj instanceof DrmResourceManager) {
			drmResourceManager = (DrmResourceManager) beanObj;
		}

		try {
			field.setAccessible(true);
			Object fieldContentObj = field.get(beanObj);
			if (null != fieldContentObj && fieldContentObj.equals(param.getDrmValue())) {
				LogUtils.debug(LOGGER, "推送对应的值和原来的值一样不进行推送处理param={0},fieldName={1}", param,
						field.getName());
				return;
			}
			// 回调
			if (null != drmResourceManager) {
				drmResourceManager.beforeUpdate();
			}

			field.set(beanObj, param.getDrmValue());
			LogUtils.info(LOGGER,
					"DRM推送之前对应的值param={0},fieldName={1},befFieldValue={2},aftFieldValue={3}", param,
					field.getName(), fieldContentObj, param.getDrmValue());
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(MessageFormat.format(
					"field={0}设置值对应的非法参数异常beanObj={1},param={2}", field, beanObj, param), e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(MessageFormat.format(
					"field={0}设置值对应的非法访问异常beanObj={1},param={2}", field, beanObj, param), e);
		}
		if (null != drmResourceManager) {
			drmResourceManager.afterUpdate();
		}

	}

	/**
	 * 根据classObj获得对应的BeanObj
	 *
	 * @param classObj
	 * @return
	 */
	private Object getBeanObjByClass(Class<?> classObj) {
		Object beanObj = null;
		try {
			beanObj = applicationContext.getBean(classObj);
		} catch (NoUniqueBeanDefinitionException noUniqueBeanDefinitionException) {
			throw new RuntimeException(
					MessageFormat.format("drm请求推送参数对应的类{0}类型的bean存在不止一个", classObj.getName()),
					noUniqueBeanDefinitionException);
		} catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {
			throw new RuntimeException(
					MessageFormat.format("drm请求推送参数对应的类{0}类型的bean不存在", classObj.getName()),
					noSuchBeanDefinitionException);
		}
		return beanObj;
	}

	/**
	 * 根据类名获得对应的class对象
	 *
	 * @param className
	 * @return
	 */
	private Class<?> getClassByName(String className) {
		Class<?> classObj = null;
		try {
			classObj = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(MessageFormat.format("drm请求推送参数对应的类{0}不存在", className), e);
		}
		return classObj;
	}

	/**
	 * 组装drm失败的组装结果
	 *
	 * @param errorMsg
	 * @param exception
	 * @return
	 */
	private DrmRequestResult assembleErrorResult(String errorMsg, Exception exception) {
		DrmRequestResult result = new DrmRequestResult();
		result.setSuccess(false);
		String errorMsg1 = MessageFormat.format("{0},exception={1}", errorMsg,
				exception.toString());
		result.setErrorMsg(errorMsg1);
		return result;
	}

	/**
	 * 校验注解类: 判断这样类是否有@Resource和是否实现了接口DrmResourceManager
	 *
	 * @param annotationClassMap
	 */
	private void parseValidateAnnotationClass(Map<String, Object> annotationClassMap) {

		parseResultTable.clear();
		for (Map.Entry<String, Object> entryEle : annotationClassMap.entrySet()) {
			String beanName = entryEle.getKey();
			Object beanObj = entryEle.getValue();

			// 判断这个对象是否实现了接口DrmResourceManager---->不需要实现接口
			/*if (!(beanObj instanceof DrmResourceManager)) {
				String errorMsg = MessageFormat.format(
						"beanName={0},beanObj={1}标记了@DrmClassResource,但是没有实现接口DrmResourceManager",
						beanName, beanObj);
				throw new RuntimeException(errorMsg);
			}*/
			Table<String, String, Field> tempParseResultTable = parseValidateFieldResourceAnnotation(
					beanName, beanObj);
			parseResultTable.putAll(tempParseResultTable);
		}
	}

	/**
	 * @param beanName
	 * @param beanObj
	 */
	private Table<String, String, Field> parseValidateFieldResourceAnnotation(String beanName,
			Object beanObj) {

		Table<String, String, Field> tempParseResultTable = HashBasedTable.create();

		// 获取对应的目标对象
		Object targetBeanObj = getTarget(beanObj);
		// 获取所有定义的属性
		Field[] declaredFields = targetBeanObj.getClass().getDeclaredFields();

		boolean existDrmFieldFlag = false;
		// 遍历所有的属性，判断是否有属性标记了注解@Resource
		for (Field field : declaredFields) {
			DrmFieldResource drmFieldResource = field.getAnnotation(DrmFieldResource.class);
			if (null == drmFieldResource) {
				continue;
			} else {
				// 说明存在
				existDrmFieldFlag = true;
				tempParseResultTable.put(targetBeanObj.getClass().getName(), field.getName(),
						field);
			}
		}

		if (!existDrmFieldFlag) {
			String errorMsg = MessageFormat.format(
					"beanName={0},beanObj={1}标记了@DrmClassResource,但是没有field有注解@DrmFieldResource",
					beanName, targetBeanObj);
			throw new RuntimeException(errorMsg);
		}

		return tempParseResultTable;
	}

	/**
	 * 获取对应的对象，如果是代理则获取对应的targetObj
	 *
	 * @param beanInstance
	 * @return
	 */
	private Object getTarget(Object beanInstance) {
		if (!AopUtils.isAopProxy(beanInstance)) {
			return beanInstance;
		} else if (AopUtils.isCglibProxy(beanInstance)) {
			try {
				Field h = beanInstance.getClass().getDeclaredField("CGLIB$CALLBACK_0");
				h.setAccessible(true);
				Object dynamicAdvisedInterceptor = h.get(beanInstance);

				Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
				advised.setAccessible(true);

				Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor))
						.getTargetSource().getTarget();
				return target;
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return null;

	}

	/**
	 * @return the parseResultTable
	 */
	public Table<String, String, Field> getParseResultTable() {
		return parseResultTable;
	}

	/**
	 * 解析对应的drm 注解
	 */
	public void parseDrmAnnotation() {
		// 初始化动作:1.获取所有注解@DrmResource的类
		Map<String, Object> annotationClassMap = applicationContext
				.getBeansWithAnnotation(DrmClassResource.class);
		// 2.校验注解@DrmResource的类，
		parseValidateAnnotationClass(annotationClassMap);
	}

}
