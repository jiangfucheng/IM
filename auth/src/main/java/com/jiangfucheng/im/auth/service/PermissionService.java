package com.jiangfucheng.im.auth.service;

import com.jiangfucheng.im.auth.bo.PermissionResourceBo;
import com.jiangfucheng.im.auth.po.PermissionPo;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/10
 * Time: 10:31
 *
 * @author jiangfucheng
 */

public interface PermissionService {

	/**
	 * 创建权限
	 *
	 * @param permissionPo
	 */
	void createPermission(PermissionPo permissionPo);

	/**
	 * 创建权限
	 *
	 * @param permissionCode
	 */
	void createPermission(String permissionCode);

	/**
	 * 删除权限
	 *
	 * @param permissionId
	 */
	void deletePermission(Long permissionId);

	/**
	 * 更新权限
	 *
	 * @param permissionPo
	 */
	void updatePermission(PermissionPo permissionPo);

	/**
	 * 根据权限id查询权限信息
	 *
	 * @param id 权限id
	 * @return 权限码
	 */
	String hasPermission(Long id);

	/**
	 * 查询用户的所有权限
	 *
	 * @param userId 用户id
	 * @return 权限码列表
	 */
	List<String> queryPermissionWithUser(Long userId);

	/**
	 * 查询用户是否有某一个权限
	 *
	 * @param userId       用户id
	 * @param permissionId 权限id
	 */
	boolean hasPermission(Long userId, Long permissionId);

	/**
	 * 查询用户是否有某一个权限
	 *
	 * @param userId         用户id
	 * @param permissionCode 权限码
	 */
	boolean hasPermission(Long userId, String permissionCode);

	/**
	 * 列出所有权限列表
	 */
	List<String> queryPermissions();

	/**
	 * 授予用户权限
	 *
	 * @param userId         用户id
	 * @param permissionCode 权限码
	 */
	void awardPermission(Long userId, String permissionCode);

	/**
	 * 撤销用户权限
	 *
	 * @param userId       用户id
	 * @param permissionId 权限id
	 */
	void revokePermission(Long userId, Long permissionId);

	/**
	 * 撤销某个用户某个资源的所有的权限
	 */
	void revokePermissionByResource(PermissionResourceBo permissionResourceBo);


	/**
	 * 撤销用户某个权限
	 *
	 * @param userId         用户id
	 * @param permissionCode 权限码
	 */
	void revokePermissionByPermissionCode(Long userId, String permissionCode);

	/**
	 * 撤销用户某个权限
	 *
	 * @param userId       用户id
	 * @param permissionId 权限id
	 */
	void revokePermissionByPermissionId(Long userId, Long permissionId);

}
