/**
 * DrmService.java
 * author: yujiakui
 * 2017年12月27日
 * 上午9:19:52
 */
package com.ctfin.framework.drm.admin.service;

import java.util.List;

import com.ctfin.framework.drm.admin.model.DrmConfRequestParam;

/**
 * @author yujiakui
 *
 *         上午9:19:52
 *
 *
 *         drm推送服务
 */
public interface DrmService {

	/**
	 * drm推送配置请求参数
	 *
	 * @param drmConfRequestParam
	 * @return
	 */
	public boolean push(DrmConfRequestParam drmConfRequestParam);

	/**
	 * 删除对应的drm 配置，如下线对应的drm 配置，则可以通过这个删除
	 *
	 * @param drmConfRequestParam
	 *            会删除特定机器或者所有的all对应的zk路径，即是会删除/CTFIN/DRM/应用程序名称/all/类名/属性名
	 *            或者/CTFIN/DRM/应用程序名称/特定机器/类名/属性名，注意在删除all的同时会连带删除对应的特定机器
	 *            即是清除路径/CTFIN/DRM/应用程序名称/all/类名/属性名/persist或者nopersist
	 *            如果类下面仅仅只有一个属性，且这个属性是要删除的，则会清除路径到类
	 *            删除的时候，如果字段为空，则从类开始删除，如果类为空，则冲机器开始删除
	 * @return
	 */
	public boolean delete(DrmConfRequestParam drmConfRequestParam);

	/**
	 * 获得所有的drm推送列表
	 *
	 * @return
	 */
	public List<DrmConfRequestParam> listAllDrmInfo();
}
